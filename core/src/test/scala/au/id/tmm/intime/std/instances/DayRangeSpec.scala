package au.id.tmm.intime.std.instances

import java.nio.file.{Files, Path}
import java.time.{LocalDate, Month, Period}

import au.id.tmm.intime.std.syntax.localDate._
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
import scala.collection.immutable.ArraySeq
import scala.io.Source

class DayRangeSpec extends AnyFlatSpec with ParallelTestExecution with TableDrivenPropertyChecks {

  behavior of "day range computation"

  private def table =
    Table(
      ("period", "manuallyComputedDuration"),
      ManuallyComputedPeriodDurations.read().toSeq: _*,
    )

  "day range computation" should "match with manually computed values" in forEvery(table) {
    (period, manuallyComputedDayRange) =>
      assert(DayRange.fromPeriod(period) === manuallyComputedDayRange)
  }

}

/**
  * Manually computes the duration of a set of periods, and writes the expected values to the resource at
  * `au.id.tmm.intime.std.instances.manuallyComputedPeriodDurations.tsv`
  */
object ManuallyComputedPeriodDurations extends IOApp {

  private val longestPeriodToTestInYears: Int = 2000

  private final case class DateRange(start: LocalDate, end: LocalDate) {
    val durationInDays: Long = end.toEpochDay - start.toEpochDay
  }

  private final case class ResourceRow(
    period: Period,
    dayRange: DayRange,
    exampleMinDateRange: DateRange,
    exampleMaxDateRange: DateRange,
  )

  private object ResourceRow {
    def toRaw(resourceRow: ResourceRow): String =
      ArraySeq(
        resourceRow.period.toString,
        resourceRow.dayRange.min.toString,
        resourceRow.dayRange.max.toString,
        resourceRow.exampleMinDateRange.start.toString,
        resourceRow.exampleMinDateRange.end.toString,
        resourceRow.exampleMaxDateRange.start.toString,
        resourceRow.exampleMaxDateRange.end.toString,
      ).mkString("\t")

    def fromRaw(row: String): ResourceRow =
      ArraySeq.unsafeWrapArray(row.split('\t')) match {
        case ArraySeq(
              period,
              dayRangeMin,
              dayRangeMax,
              exampleMinDateRangeStart,
              exampleMinDateRangeEnd,
              exampleMaxDateRangeStart,
              exampleMaxDateRangeEnd,
            ) =>
          ResourceRow(
            Period.parse(period),
            DayRange(
              dayRangeMin.toLong,
              dayRangeMax.toLong,
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

  def read(): Iterator[(Period, DayRange)] =
    Source
      .fromInputStream(getClass.getResourceAsStream("manuallyComputedPeriodDurations.tsv"), "UTF-8")
      .getLines()
      .map(ResourceRow.fromRaw)
      .map(r => r.period -> r.dayRange)

  override def run(args: List[String]): IO[ExitCode] =
    for {
      fsPath <- IO(
        Path.of("core/src/test/resources/au/id/tmm/intime/std/instances", "manuallyComputedPeriodDurations.tsv"),
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
                DayRange(
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
      .to(ArraySeq.untagged)
      .map { resourceRows =>
        val lines: ArraySeq[String] =
          resourceRows
            .sortBy(r => (r.period.getYears, r.period.getMonths))
            .map(ResourceRow.toRaw)

        Files.write(path, lines.asJava)
      }

  private val periodsToVerify: Stream[Pure, Period] =
    Stream.range(0, 12).map(Period.ofMonths) ++ Stream.range(1, longestPeriodToTestInYears).map(Period.ofYears)

  private val consideredRange: DateRange = DateRange(
    start = LocalDate.of(1, Month.JANUARY, 1),
    end = LocalDate.of(1, Month.JANUARY, 1) + Period.ofYears(longestPeriodToTestInYears) + Period.ofYears(20),
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
