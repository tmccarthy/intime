package au.id.tmm.intime

import java.nio.file.{Files, Path, Paths}
import java.time.{LocalDate, Month, Period}

import au.id.tmm.intime.std.syntax.localDate._
import au.id.tmm.intime.std.syntax.period._
import cats.effect.{ExitCode, IO, IOApp}
import cats.instances.long.catsKernelStdOrderForLong
import cats.kernel.Order
import cats.syntax.flatMap._
import com.github.ghik.silencer.silent
import fs2._
import org.scalatest.ParallelTestExecution
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.prop.TableDrivenPropertyChecks

import scala.collection.JavaConverters.asJavaIterableConverter
import scala.collection.immutable.Vector
import scala.io.Source

class PeriodLengthSpec extends AnyFlatSpec with ParallelTestExecution with TableDrivenPropertyChecks {

  private def table =
    Table(
      ("period", "manuallyComputedDuration"),
      ManuallyComputedPeriodDurations.read().toSeq: _*,
    )

  "period length computation" should "match with manually computed values" in forEvery(table) {
    (period, manuallyComputedPeriodLength) =>
      assert(PeriodLength.of(period) === manuallyComputedPeriodLength)
  }

}

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
  ): Stream[Pure, DateRange] =
    Stream
      .iterate(eligibleRange.start)(d => d.plusDays(1))
      .map(startDate => DateRange(startDate, startDate.plus(period)))
      .takeWhile(dateRange => dateRange.end <= eligibleRange.end)

  @silent("deprecated")
  def write(path: Path): IO[Unit] =
    periodsToVerify
      .lift[IO]
      .balanceAvailable
      .take(Runtime.getRuntime.availableProcessors())
      .map { periodsToCompute: Stream[IO, Period] =>
        periodsToCompute
          .evalMap(computeMinMaxDateRanges)
          .map {
            case (period, (minLengthDateRange, maxLengthDateRange)) =>
              ResourceRow(
                period,
                PeriodLength(
                  minLengthDateRange.durationInDays,
                  maxLengthDateRange.durationInDays,
                ),
                minLengthDateRange,
                maxLengthDateRange,
              )
          }
      }
      .parJoinUnbounded
      .compile
      .to(Vector)
      .map { resourceRows =>
        val lines: Vector[String] =
          resourceRows
            .sortBy(r => (r.period.getYears, r.period.getMonths))
            .map(ResourceRow.toRaw)
            .+:(ResourceRow.headerRow)

        Files.write(path, lines.asJava)
      }

  private val periodsToVerify: Stream[Pure, Period] = {
    Stream.iterate(Period.ZERO)(p => p.plusMonths(1))
      .map(_.normalized)
      .takeWhile(p => p <= longestPeriodToTest)
  }

  private val consideredRange: DateRange = DateRange(
    start = LocalDate.of(1, Month.JANUARY, 1),
    end = LocalDate.of(1, Month.JANUARY, 1) + (longestPeriodToTest * 4),
  )

  private def computeMinMaxDateRanges(period: Period): IO[(Period, (DateRange, DateRange))] =
    startAndEndDatesMatching(consideredRange, period)
      .minAndMax(Order.by((d: DateRange) => d.durationInDays))
      .lift[IO]
      .compile
      .lastOrError
      .map(period -> _)
      .flatTap {
        case (period, _) => IO(println(s"Computed for period $period on thread ${Thread.currentThread().getId}"))
      }

  private implicit class StreamOps[F[_], O](stream: Stream[F, O]) {
    def minAndMax(implicit order: Order[O]): Stream[F, (O, O)] =
      stream
        .map(o => (o, o))
        .reduce[(O, O)] {
          case ((minSoFar, maxSoFar), (o, _)) =>
            (
              order.min(minSoFar, o),
              order.max(maxSoFar, o),
            )
        }
  }
}
