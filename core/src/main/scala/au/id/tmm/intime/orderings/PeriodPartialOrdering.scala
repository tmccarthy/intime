package au.id.tmm.intime.orderings

import java.time.Period

/**
  * A `PartialOrdering` for `Period`, which allows comparison between periods that are unambiguously
  * ordered.
  *
  * Because the number of days per month (or year) varies, it is impossible to define a total
  * ordering for `Period`. A period of 30 days may or may not be longer than one of one month,
  * depending on they day the period starts. Some periods, though, can be compared. Two months are
  * longer than one month, 32 days will always be longer than one month, etc. This partial ordering
  * captures these unambiguous cases while not providing orderings in ambiguous ones.
  */
object PeriodPartialOrdering extends PartialOrdering[Period] {

  override def tryCompare(x: Period, y: Period): Option[Int] = {
    val xNormalised = x.normalized()
    val yNormalised = y.normalized()

    if (xNormalised == yNormalised) {
      Some(0)
    } else if (xNormalised.getDays == yNormalised.getDays) {
      Some(
        Ordering
          .Tuple2[Int, Int]
          .compare(
            (xNormalised.getYears, xNormalised.getMonths),
            (yNormalised.getYears, yNormalised.getMonths),
          ),
      )
    } else if ((xNormalised.getYears == yNormalised.getYears) && (xNormalised.getMonths == yNormalised.getMonths)) {
      Some(
        Ordering[Int].compare(xNormalised.getDays, yNormalised.getDays),
      )
    } else {
      DayRange.partialOrdering.tryCompare(
        DayRange.fromPeriod(xNormalised),
        DayRange.fromPeriod(yNormalised),
      )
    }
  }

  override def lteq(x: Period, y: Period): Boolean = tryCompare(x, y).exists(_ <= 0)

}

private[intime] final case class DayRange(min: Long, max: Long) {
  def +(that: DayRange): DayRange = DayRange(this.min + that.min, this.max + that.max)

  def -(that: DayRange): DayRange = DayRange(this.min - that.min, this.max - that.max)
  def *(scalar: Int): DayRange    = DayRange(this.min * scalar, this.max * scalar)
}

private[intime] object DayRange {
  def apply(min: Long, max: Long): DayRange = new DayRange(min min max, min max max)

  def fromPeriod(period: Period): DayRange =
    DayRange(period.getDays, period.getDays) +
      lengthOfNumMonths(period.getMonths) +
      lengthOfNumYears(period.getYears)

  private def lengthOfNumMonths(numMonths: Int): DayRange = numMonths match {
    case negative if negative < 0 => lengthOfNumMonths(-negative) * -1
    case 0                        => DayRange(0, 0)
    // Shortest is List(FEBRUARY) with 28 days
    // Longest is List(JANUARY) with 31 days
    case 1 => DayRange(28, 31)
    // Shortest is List(FEBRUARY, MARCH) with 59 days
    // Longest is List(JULY, AUGUST) with 62 days
    case 2 => DayRange(59, 62)
    // Shortest is List(FEBRUARY, MARCH, APRIL) with 89 days
    // Longest is List(MARCH, APRIL, MAY) with 92 days
    case 3 => DayRange(89, 92)
    // Shortest is List(NOVEMBER, DECEMBER, JANUARY, FEBRUARY) with 120 days
    // Longest is List(MAY, JUNE, JULY, AUGUST) with 123 days
    case 4 => DayRange(120, 123)
    // Shortest is List(FEBRUARY, MARCH, APRIL, MAY, JUNE) with 150 days
    // Longest is List(MARCH, APRIL, MAY, JUNE, JULY) with 153 days
    case 5 => DayRange(150, 153)
    // Shortest is List(NOVEMBER, DECEMBER, JANUARY, FEBRUARY, MARCH, APRIL) with 181 days
    // Longest is List(MARCH, APRIL, MAY, JUNE, JULY, AUGUST) with 184 days
    case 6 => DayRange(181, 184)
    // Shortest is List(DECEMBER, JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE) with 212 days
    // Longest is List(JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER, JANUARY) with 215 days
    case 7 => DayRange(212, 215)
    // Shortest is List(NOVEMBER, DECEMBER, JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE) with 242 days
    // Longest is List(MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER) with 245 days
    case 8 => DayRange(242, 245)
    // Shortest is List(NOVEMBER, DECEMBER, JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY) with 273 days
    // Longest is List(MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER, JANUARY) with 276 days
    case 9 => DayRange(273, 276)
    // Shortest is List(SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER, JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE) with 303 days
    // Longest is List(MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER) with 306 days
    case 10 => DayRange(303, 306)
    // Shortest is List(NOVEMBER, DECEMBER, JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER) with 334 days
    // Longest is List(MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER, JANUARY) with 337 days
    case 11 => DayRange(334, 337)
  }

  private def lengthOfNumYears(numYears: Int): DayRange = numYears match {
    case negative if negative < 0 => lengthOfNumYears(-negative) * -1
    case numYears                 => DayRange(365 * numYears, 365 * numYears) + numLeapDaysIn(numYears)
  }

  private def numLeapDaysIn(numYears: Int): DayRange = DayRange(
    min = minNumLeapDaysIn(numYears),
    max = maxNumLeapDaysIn(numYears),
  )

  private def minNumLeapDaysIn(numYears: Int): Int = numYears match {
    case 0            => 0
    case n if n < 8   => 0
    case n if n < 104 => (n - 4) / 4
    case n if n < 204 => (n - 4) / 4 - 1
    case n if n < 304 => (n - 4) / 4 - 2
    case n if n < 400 => (n - 4) / 4 - 2
    case n            => (97 * (n / 400)) + minNumLeapDaysIn(n % 400)
  }

  private def maxNumLeapDaysIn(numYears: Int): Int = numYears match {
    case 0            => 0
    case n if n < 4   => 1
    case n if n < 197 => (n - 1) / 4 + 1
    case n if n < 297 => (n - 1) / 4
    case n if n < 397 => (n - 1) / 4 - 1
    case n if n < 400 => 97
    case n            => (97 * (n / 400)) + maxNumLeapDaysIn(n % 400)
  }

  implicit val partialOrdering: PartialOrdering[DayRange] = new PartialOrdering[DayRange] {
    override def tryCompare(x: DayRange, y: DayRange): Option[Int] =
      if ((x.min == x.max) && (y.min == y.max) && (x.min == y.min)) {
        Some(0)
      } else if (x.max < y.min) {
        Some(-1)
      } else if (x.min > y.max) {
        Some(1)
      } else {
        None
      }

    override def lteq(x: DayRange, y: DayRange): Boolean = tryCompare(x, y).exists(_ <= 0)
  }
}
