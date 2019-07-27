package au.id.tmm.intime.orderings

import java.time.{Month, Period, Year}

import org.scalatest.FlatSpec

class DayRangeSpec extends FlatSpec {

  behavior of "the day range for a period of months"

  it should "be 0 days for a period of 0 months" in {
    assert(DayRange.fromPeriod(Period.ofMonths(0)) === DayRange(0, 0))
  }

  {
    val months = Vector.fill(2)((1 to 12).map(Month.of)).flatten

    (1 to 12).foreach { numMonths =>
      val windows = months.sliding(numMonths).toVector

      val minNumDays = windows.map(_.map(_.minLength).sum).min
      val maxNumDays = windows.map(_.map(_.maxLength).sum).max

      val expectedDayRange = DayRange(minNumDays, maxNumDays)

      val actualDayRange = DayRange.fromPeriod(Period.ofMonths(numMonths).normalized())

      it should s"have at least ${expectedDayRange.min} days for a period of $numMonths months" in
        assert(actualDayRange.min === expectedDayRange.min)

      it should s"have at most ${expectedDayRange.max} days for a period of $numMonths months" in
        assert(actualDayRange.max === expectedDayRange.max)

    }
  }

  behavior of "the day range for a period of years"

  it should "be 0 days for a period of 0 years" in {
    assert(DayRange.fromPeriod(Period.ofYears(0)) === DayRange(0, 0))
  }

  {
    val years = (-1 to 2020).map(Year.of)

    (1 to 420).foreach { numYears =>
      val windows = years.sliding(numYears).toVector

      val minNumDays = windows.map(_.map(_.length).sum).min
      val maxNumDays = windows.map(_.map(_.length).sum).max

      def numLeapDaysGiven(numDays: DayRange): DayRange = numDays - DayRange(365 * numYears, 365 * numYears)

      val expectedNumLeapYears = numLeapDaysGiven(DayRange(minNumDays, maxNumDays))

      val actualNumLeapYears = numLeapDaysGiven(DayRange.fromPeriod(Period.ofYears(numYears)))

      it should s"have at least ${expectedNumLeapYears.min} leap days for a period of $numYears years" in
        assert(actualNumLeapYears.min === expectedNumLeapYears.min)

      it should s"have at most ${expectedNumLeapYears.max} leap days for a period of $numYears years" in
        assert(actualNumLeapYears.max === expectedNumLeapYears.max)
    }
  }

}
