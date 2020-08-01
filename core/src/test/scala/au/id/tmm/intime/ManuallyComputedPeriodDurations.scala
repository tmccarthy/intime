package au.id.tmm.intime

import java.nio.file.{Files, Path, Paths}
import java.time.{LocalDate, Month, Period}

import au.id.tmm.intime.std.syntax.localDate._
import au.id.tmm.intime.std.syntax.period._
import cats.effect.{ExitCode, IO, IOApp}
import cats.instances.vector._
import cats.syntax.parallel._
import com.github.ghik.silencer.silent

import scala.collection.JavaConverters.asJavaIterableConverter
import scala.collection.immutable.Vector
import scala.collection.mutable
import scala.io.Source
import scala.util.Random

/**
  * Manually computes the duration of a set of periods, and writes the expected values to the resource at
  * `au.id.tmm.intime.manuallyComputedPeriodDurations.tsv`
  */
object ManuallyComputedPeriodDurations extends IOApp {

  private val longestPeriodToTest: Period = Period.ofYears(800)

  private final case class DateRange(start: LocalDate, end: LocalDate) {
    val durationInDays: Long = end.toEpochDay - start.toEpochDay
  }

  private final case class ResourceRow(
    period: Period,
    periodLength: PeriodLength,
    exampleMinDateRange: DateRange,
    exampleMaxDateRange: DateRange,
  )

  private object ResourceRow {
    def headerRow: String =
      Vector(
        "period",
        "min_length_days",
        "max_length_days",
        "min_length_example_start",
        "min_length_example_end",
        "max_length_example_start",
        "max_length_example_end",
      ).mkString("\t")

    def toRaw(resourceRow: ResourceRow): String =
      Vector(
        resourceRow.period.toString,
        resourceRow.periodLength.minLengthInDays.toString,
        resourceRow.periodLength.maxLengthInDays.toString,
        resourceRow.exampleMinDateRange.start.toString,
        resourceRow.exampleMinDateRange.end.toString,
        resourceRow.exampleMaxDateRange.start.toString,
        resourceRow.exampleMaxDateRange.end.toString,
      ).mkString("\t")

    def fromRaw(row: String): ResourceRow =
      row.split('\t').toVector match {
        case Vector(
              period,
              periodLengthMin,
              periodLengthMax,
              exampleMinDateRangeStart,
              exampleMinDateRangeEnd,
              exampleMaxDateRangeStart,
              exampleMaxDateRangeEnd,
            ) =>
          ResourceRow(
            Period.parse(period),
            PeriodLength(
              periodLengthMin.toLong,
              periodLengthMax.toLong,
            ),
            DateRange(
              LocalDate.parse(exampleMinDateRangeStart),
              LocalDate.parse(exampleMinDateRangeEnd),
            ),
            DateRange(
              LocalDate.parse(exampleMaxDateRangeStart),
              LocalDate.parse(exampleMaxDateRangeEnd),
            ),
          )
      }
  }

  def read(): Iterator[(Period, PeriodLength)] =
    Source
      .fromInputStream(getClass.getResourceAsStream("manuallyComputedPeriodDurations.tsv"), "UTF-8")
      .getLines()
      .drop(1)
      .map(ResourceRow.fromRaw)
      .map(r => r.period -> r.periodLength)

  override def run(args: List[String]): IO[ExitCode] =
    for {
      fsPath <- IO(
        Paths.get("core/src/test/resources/au/id/tmm/intime/manuallyComputedPeriodDurations.tsv"),
      )
      _ <- write(fsPath)
    } yield ExitCode.Success

  private def startAndEndDatesMatching(
    eligibleRange: DateRange,
    period: Period,
  ): Iterator[DateRange] =
    Iterator
      .iterate(eligibleRange.start)(d => d.plusDays(1))
      .map(startDate => DateRange(startDate, startDate.plus(period)))
      .takeWhile(dateRange => dateRange.end <= eligibleRange.end)

  @silent("deprecated")
  def write(path: Path): IO[Unit] = {
    val numProcessors = Runtime.getRuntime.availableProcessors

    val arraysPerProcessor: Vector[mutable.ArrayBuffer[Period]] =
      Vector.fill(numProcessors)(mutable.ArrayBuffer())

    periodsToVerify.sliding(numProcessors, numProcessors).foreach { periods =>
      (periods zip arraysPerProcessor).foreach {
        case (period, periods) => periods.append(period)
      }
    }

    arraysPerProcessor
      .parFlatTraverse { periods: mutable.ArrayBuffer[Period] =>
        IO {
          periods.map { period =>
            val (minLengthDateRange, maxLengthDateRange) = computeMinMaxDateRanges(period)

            ResourceRow(
              period,
              PeriodLength(
                minLengthDateRange.durationInDays,
                maxLengthDateRange.durationInDays,
              ),
              minLengthDateRange,
              maxLengthDateRange,
            )
          }.toVector
        }
      }
      .map { resourceRows =>
        val lines: Vector[String] =
          resourceRows
            .sortBy(r => (r.period.getYears, r.period.getMonths))
            .map(ResourceRow.toRaw)
            .+:(ResourceRow.headerRow)

        Files.write(path, lines.asJava)
      }
  }

  private val periodsToVerify: Iterator[Period] = {
    val everyPositiveMonthDurationUpToMax = Iterator
      .iterate(Period.ZERO)(p => p.plusMonths(1))
      .map(_.normalized)
      .takeWhile(p => p <= longestPeriodToTest)

    val randomPeriods = Iterator
      .continually(
        Period.of(
          Random.nextInt(800) - 400,
          Random.nextInt(23) - 12,
          Random.nextInt(800) - 400,
        ),
      )
      .take(1_000)

    randomPeriods ++ everyPositiveMonthDurationUpToMax
  }

  private val consideredRange: DateRange = DateRange(
    start = LocalDate.of(1, Month.JANUARY, 1),
    end = LocalDate.of(1, Month.JANUARY, 1) + (longestPeriodToTest * 4),
  )

  private def computeMinMaxDateRanges(period: Period): (DateRange, DateRange) = {
    val startAndEndDates: Iterator[DateRange] = startAndEndDatesMatching(consideredRange, period)

    var minDateRange: DateRange = null
    var maxDateRange: DateRange = null

    startAndEndDates.foreach { d =>
      if (minDateRange == null || d.durationInDays < minDateRange.durationInDays) minDateRange = d
      if (maxDateRange == null || d.durationInDays > maxDateRange.durationInDays) maxDateRange = d
    }

    println(s"Computed for period $period on thread ${Thread.currentThread().getId}")

    (minDateRange, maxDateRange)
  }

}
