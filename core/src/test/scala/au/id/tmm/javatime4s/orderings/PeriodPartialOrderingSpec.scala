package au.id.tmm.javatime4s.orderings

import java.time.Month._
import java.time.Period

import org.scalatest.FlatSpec

class PeriodPartialOrderingSpec extends FlatSpec {

  behavior of "the partial ordering for period"

  it should "compare two periods in terms of years" in
    assert(PeriodPartialOrdering.lt(Period.ofYears(5), Period.ofYears(10)))

  it should "compare two equivalent periods in terms of years" in
    assert(PeriodPartialOrdering.equiv(Period.ofYears(5), Period.ofYears(5)))

  it should "compare two periods in terms of months" in
    assert(PeriodPartialOrdering.lt(Period.ofMonths(5), Period.ofMonths(10)))

  it should "compare two equivalent periods in terms of months" in
    assert(PeriodPartialOrdering.equiv(Period.ofMonths(5), Period.ofMonths(5)))

  it should "compare two periods in terms of days" in
    assert(PeriodPartialOrdering.lt(Period.ofDays(5), Period.ofDays(10)))

  it should "compare two equivalent periods in terms of days" in
    assert(PeriodPartialOrdering.equiv(Period.ofDays(5), Period.ofDays(5)))

  dayRangeComparisonAssertion(Period.ofMonths(1))(
    alwaysGreaterThanNumDays = FEBRUARY.minLength() - 1,
    alwaysLessThanNumDays = JANUARY.maxLength() + 1,
  )

  dayRangeComparisonAssertion(Period.ofMonths(2))(
    alwaysGreaterThanNumDays = List(FEBRUARY, MARCH).map(_.minLength).sum - 1,
    alwaysLessThanNumDays = List(JULY, AUGUST).map(_.maxLength).sum + 1,
  )

  dayRangeComparisonAssertion(Period.ofMonths(3))(
    alwaysGreaterThanNumDays = List(FEBRUARY, MARCH, APRIL).map(_.minLength).sum - 1,
    alwaysLessThanNumDays = List(MARCH, APRIL, MAY).map(_.maxLength).sum + 1,
  )

  dayRangeComparisonAssertion(Period.ofMonths(4))(
    alwaysGreaterThanNumDays = List(NOVEMBER, DECEMBER, JANUARY, FEBRUARY).map(_.minLength).sum - 1,
    alwaysLessThanNumDays = List(MAY, JUNE, JULY, AUGUST).map(_.maxLength).sum + 1,
  )

  dayRangeComparisonAssertion(Period.ofMonths(5))(
    alwaysGreaterThanNumDays = List(FEBRUARY, MARCH, APRIL, MAY, JUNE).map(_.minLength).sum - 1,
    alwaysLessThanNumDays = List(MARCH, APRIL, MAY, JUNE, JULY).map(_.maxLength).sum + 1,
  )

  dayRangeComparisonAssertion(Period.ofMonths(6))(
    alwaysGreaterThanNumDays = List(NOVEMBER, DECEMBER, JANUARY, FEBRUARY, MARCH, APRIL).map(_.minLength).sum - 1,
    alwaysLessThanNumDays = List(MARCH, APRIL, MAY, JUNE, JULY, AUGUST).map(_.maxLength).sum + 1,
  )

  dayRangeComparisonAssertion(Period.ofMonths(7))(
    alwaysGreaterThanNumDays = List(DECEMBER, JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE).map(_.minLength).sum - 1,
    alwaysLessThanNumDays = List(JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER, JANUARY)
      .map(_.maxLength)
      .sum + 1,
  )

  dayRangeComparisonAssertion(Period.ofMonths(8))(
    alwaysGreaterThanNumDays = List(NOVEMBER, DECEMBER, JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE)
      .map(_.minLength)
      .sum - 1,
    alwaysLessThanNumDays = List(MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER).map(_.maxLength).sum + 1,
  )

  dayRangeComparisonAssertion(Period.ofMonths(9))(
    alwaysGreaterThanNumDays = List(NOVEMBER, DECEMBER, JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY)
      .map(_.minLength)
      .sum - 1,
    alwaysLessThanNumDays = List(MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER, JANUARY)
      .map(_.maxLength)
      .sum + 1,
  )

  dayRangeComparisonAssertion(Period.ofMonths(10))(
    alwaysGreaterThanNumDays = List(SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER, JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE)
      .map(_.minLength)
      .sum - 1,
    alwaysLessThanNumDays = List(MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER)
      .map(_.maxLength)
      .sum + 1,
  )

  dayRangeComparisonAssertion(Period.ofMonths(11))(
    alwaysGreaterThanNumDays = List(
      NOVEMBER,
      DECEMBER,
      JANUARY,
      FEBRUARY,
      MARCH,
      APRIL,
      MAY,
      JUNE,
      JULY,
      AUGUST,
      SEPTEMBER,
    ).map(_.minLength).sum - 1,
    alwaysLessThanNumDays = List(MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER, JANUARY)
      .map(_.maxLength)
      .sum + 1,
  )

  dayRangeComparisonAssertion(Period.ofMonths(12))(
    alwaysGreaterThanNumDays = List(
      DECEMBER,
      JANUARY,
      FEBRUARY,
      MARCH,
      APRIL,
      MAY,
      JUNE,
      JULY,
      AUGUST,
      SEPTEMBER,
      OCTOBER,
      NOVEMBER,
    ).map(_.minLength).sum - 1,
    alwaysLessThanNumDays = List(
      JANUARY,
      FEBRUARY,
      MARCH,
      APRIL,
      MAY,
      JUNE,
      JULY,
      AUGUST,
      SEPTEMBER,
      OCTOBER,
      NOVEMBER,
      DECEMBER,
    ).map(_.maxLength).sum + 1,
  )

  it should "mark a year as more than 11 months" in
    assert(PeriodPartialOrdering.gt(Period.ofYears(1), Period.ofMonths(11)))

  it should "mark a year as equivalent to 12 months" in
    assert(PeriodPartialOrdering.equiv(Period.ofYears(1), Period.ofMonths(12)))

  it should "mark a year as less than 13 months" in
    assert(PeriodPartialOrdering.lt(Period.ofYears(1), Period.ofMonths(13)))

  it should "mark 2 years and 1 month as equivalent to 1 year and 13 months" in
    assert(PeriodPartialOrdering.equiv(Period.of(2, 1, 0), Period.of(1, 13, 0)))

  dayRangeComparisonAssertion(Period.ofYears(1))(
    alwaysGreaterThanNumDays = 364,
    alwaysLessThanNumDays = 367,
  )

  it should "mark negative 2 years and 1 month as equivalent to negative 1 year and 13 months" in
    assert(PeriodPartialOrdering.equiv(Period.of(-2, -1, 0), Period.of(-1, -13, 0)))

  dayRangeComparisonAssertion(Period.ofMonths(-1))(
    alwaysGreaterThanNumDays = -JANUARY.maxLength() - 1,
    alwaysLessThanNumDays = -FEBRUARY.minLength() + 1,
  )

  dayRangeComparisonAssertion(Period.ofYears(-1))(
    alwaysGreaterThanNumDays = -367,
    alwaysLessThanNumDays = -364,
  )

  def dayRangeComparisonAssertion(
    period: Period,
  )(
    alwaysGreaterThanNumDays: Int,
    alwaysLessThanNumDays: Int,
  ): Unit = {
    it should s"mark $period as greater than $alwaysGreaterThanNumDays days" in
      assert(PeriodPartialOrdering.gt(period, Period.ofDays(alwaysGreaterThanNumDays)))

    val uncomparableToNumDays = Range.inclusive(alwaysGreaterThanNumDays + 1, alwaysLessThanNumDays - 1)

    uncomparableToNumDays.foreach { numDays =>
      it should s"fail to compare $period to $numDays days" in
        assert(PeriodPartialOrdering.tryCompare(period, Period.ofDays(numDays)) === None)
    }

    it should s"mark $period as less than $alwaysLessThanNumDays days" in
      assert(PeriodPartialOrdering.lt(period, Period.ofDays(alwaysLessThanNumDays)))
  }

}
