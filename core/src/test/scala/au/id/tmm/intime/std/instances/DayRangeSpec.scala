package au.id.tmm.intime.std.instances

import java.time.{Month, Period, Year}

import org.scalatest.ParallelTestExecution
import org.scalatest.flatspec.AnyFlatSpec

class DayRangeSpec extends AnyFlatSpec with ParallelTestExecution {

  behavior of "the day range for a period of months"

  it should "be 0 days for a period of 0 months" in {
    assert(DayRange.fromPeriod(Period.ofMonths(0)) === DayRange(0, 0))
  }

  it should "match up with manually computed values" in {
    val months = Vector.fill(2)((1 to 12).map(Month.of)).flatten

    (1 to 12).foreach { numMonths =>
      val windows = months.sliding(numMonths).toVector

      val minNumDays = windows.map(_.map(_.minLength).sum).min
      val maxNumDays = windows.map(_.map(_.maxLength).sum).max

      val expectedDayRange = DayRange(minNumDays, maxNumDays)

      val actualDayRange = DayRange.fromPeriod(Period.ofMonths(numMonths).normalized())

      assert(actualDayRange.min === expectedDayRange.min)
      assert(actualDayRange.max === expectedDayRange.max)
    }
  }

  behavior of "the day range for a period of years"

  private def windowsOfNYears(n: Int): Iterator[IndexedSeq[Year]] =
    n match {
      case 0 => Iterator.single(Vector.empty[Year])
      case _ => (-1 to 2020).map(Year.of).sliding(n)
    }

  (0 to 900).foreach { numYears =>
    it should s"match up with the manually computed value for a period of $numYears years" in {
      val manuallyComputedDayRange: DayRange = DayRange(
        min = windowsOfNYears(numYears).map(_.map(_.length).sum).min,
        max = windowsOfNYears(numYears).map(_.map(_.length).sum).max,
      )

      val dayRangeWithNoLeapYears: DayRange = DayRange(365 * numYears, 365 * numYears)

      val manuallyComputedNumLeapDays: DayRange = manuallyComputedDayRange - dayRangeWithNoLeapYears

      val actualNumLeapDays: DayRange = DayRange.fromPeriod(Period.ofYears(numYears)) - dayRangeWithNoLeapYears

      assert(actualNumLeapDays === manuallyComputedNumLeapDays)
    }
  }

}
