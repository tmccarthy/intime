package au.id.tmm.intime.std.instances

import java.nio.charset.Charset
import java.nio.file.{Files, Path}
import java.time.{Month, Period, Year}

import com.github.ghik.silencer.silent

import scala.collection.JavaConverters.asJavaIterableConverter
import scala.io.Source
import scala.util.matching.Regex

/**
  * Manually computes the duration of a set of periods, and writes the expected values to the resource at
  * `au.id.tmm.intime.std.instances.manuallyComputedPeriodDurations.tsv`
  */
object ManuallyComputedPeriodDurations {

  private def numMonthsToTest: Range = 0 to 11
  private def numYearsToTest: Range  = 1 to 3000

  def main(args: Array[String]): Unit =
    write(Path.of("core/src/test/resources/au/id/tmm/intime/std/instances", "manuallyComputedPeriodDurations.tsv"))

  def read(): Iterator[(Period, DayRange)] =
    Source
      .fromInputStream(getClass.getResourceAsStream("manuallyComputedPeriodDurations.tsv"), "UTF-8")
      .getLines()
      .map(readRow)

  @silent("deprecated")
  def write(path: Path): Unit = {
    val monthRows = numMonthsToTest.iterator.map { numMonths =>
      writeRow(Period.ofMonths(numMonths), manuallyComputeDurationOfNMonths(numMonths))
    }

    val yearRows = numYearsToTest.iterator.map { numYears =>
      writeRow(Period.ofYears(numYears), manuallyComputeDurationOfNYears(numYears))
    }

    Files.write(path, (monthRows concat yearRows).to(Iterable).asJava, Charset.forName("UTF-8"))
  }

  private def writeRow(period: Period, range: DayRange): String = {
    val normalisedPeriod = period.normalized
    s"${normalisedPeriod.getDays}\t${normalisedPeriod.getMonths}\t${normalisedPeriod.getYears}\t${range.min}\t${range.max}"
  }

  private val rowPattern: Regex = "(\\d+)\t(\\d+)\t(\\d+)\t(\\d+)\t(\\d+)".r

  private def readRow(string: String): (Period, DayRange) =
    string match {
      case rowPattern(days, months, years, rangeMin, rangeMax) =>
        Period.of(years.toInt, months.toInt, days.toInt) -> DayRange(rangeMin.toLong, rangeMax.toLong)
    }

  private def manuallyComputeDurationOfNYears(numYears: Int): DayRange =
    manuallyComputeDurationOfN[Year](
      n = numYears,
      length = y => DayRange(y.length),
      windowsOfLengthN = windowsOfNYears,
    )

  private def windowsOfNYears(n: Int): Iterator[IndexedSeq[Year]] =
    n match {
      case 0             => Iterator.single(Vector.empty[Year])
      case n if n < 4000 => (-1 to 4000).map(Year.of).sliding(n)
      case _             => throw new IllegalArgumentException(s"$n")
    }

  private def manuallyComputeDurationOfNMonths(numMonths: Int): DayRange =
    manuallyComputeDurationOfN[Month](
      n = numMonths,
      length = m => DayRange(m.minLength, m.maxLength),
      windowsOfLengthN = windowsOfNMonths,
    )

  private def windowsOfNMonths(n: Int): Iterator[IndexedSeq[Month]] =
    Range(0, 12).iterator.map { startingMonthIndex =>
      Range(0, n).map { monthOffset =>
        Month.of(((startingMonthIndex + monthOffset) % 12) + 1)
      }
    }

  private def manuallyComputeDurationOfN[A](
    n: Int,
    length: A => DayRange,
    windowsOfLengthN: Int => Iterator[IndexedSeq[A]],
  ): DayRange =
    reduce {
      windowsOfLengthN(n).map { windowOfLengthN: IndexedSeq[A] =>
        sum(windowOfLengthN.iterator.map(length))
      }
    }

  private def reduce(dayRanges: Iterator[DayRange]): DayRange =
    dayRanges.reduceOption[DayRange] {
      case (DayRange(minDurationSoFar, maxDurationSoFar), DayRange(min, max)) =>
        DayRange(
          min = minDurationSoFar min min,
          max = maxDurationSoFar max max,
        )
    }.getOrElse(DayRange(0))

  private def sum(dayRanges: Iterator[DayRange]): DayRange =
    dayRanges.reduceOption(_ + _).getOrElse(DayRange(0))

}
