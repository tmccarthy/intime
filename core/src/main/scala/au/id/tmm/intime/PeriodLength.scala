package au.id.tmm.intime

import java.time.Period

import scala.math.{abs, signum}

/**
  * Represents the range of possible lengths of a `java.time.Period` expressed as a range of days.
  *
  *  | `Period`              | `PeriodLength`               | Note                                                 |
  *  | --------------------- | ---------------------------- | ---------------------------------------------------- |
  *  | `Period.ofDays(1)`    | PeriodLength(1, 1)           | Simple case                                          |
  *  | `Period.ofMonths(1)`  | PeriodLength(28, 31)         | Months can vary in length                            |
  *  | `Period.ofYears(1)`   | PeriodLength(365, 366)       | May be a leap year                                   |
  *  | `Period.ofYears(400)` | PeriodLength(146097, 146097) | Every 400 year period will have exactly 97 leap days |
  */
final case class PeriodLength(
  minLengthInDays: Long,
  maxLengthInDays: Long,
) {

  private def +(that: PeriodLength): PeriodLength =
    PeriodLength(
      minLengthInDays = this.minLengthInDays + that.minLengthInDays,
      maxLengthInDays = this.maxLengthInDays + that.maxLengthInDays,
    )

  def *(scalar: Int): PeriodLength =
    PeriodLength(
      minLengthInDays = this.minLengthInDays * scalar,
      maxLengthInDays = this.maxLengthInDays * scalar,
    )

  def variation: Long = abs(maxLengthInDays - minLengthInDays)

}

object PeriodLength {

  /**
    * Constructs a `PeriodLength`, silently swapping the parameters if their the minimum is greater than the maximum.
    */
  def apply(
    minLengthInDays: Long,
    maxLengthInDays: Long,
  ): PeriodLength =
    new PeriodLength(
      minLengthInDays = minLengthInDays min maxLengthInDays,
      maxLengthInDays = minLengthInDays max maxLengthInDays,
    )

  /**
    * Returns a `PeriodLength` with both the minimum and maximum length set to the given value.
    */
  def always(lengthInDays: Long): PeriodLength = new PeriodLength(lengthInDays, lengthInDays)

  // TODO this is broken for negative
  def of(period: Period): PeriodLength = {
    val normalisedPeriod: Period = period.normalized

    val periodLength = PeriodLength(normalisedPeriod.getDays, normalisedPeriod.getDays) +
      lengthOfNumMonths(normalisedPeriod.getMonths) +
      lengthOfNumYears(normalisedPeriod.getYears)

    val requiresAdjustment =
      (normalisedPeriod.getMonths != 0) &&
        ((abs(normalisedPeriod.getYears)      % 100 > 4) || (abs(normalisedPeriod.getYears) % 400 == 303)) &&
        ((abs(normalisedPeriod.getYears) + 1) % 4 == 0)

    if (requiresAdjustment)
      (signum(period.getYears), signum(period.getMonths)) match {
        case (-1, -1) =>
          PeriodLength(
            minLengthInDays = periodLength.minLengthInDays,
            maxLengthInDays = periodLength.maxLengthInDays - 1,
          )
        case (-1, 1) =>
          PeriodLength(
            minLengthInDays = periodLength.minLengthInDays,
            maxLengthInDays = periodLength.maxLengthInDays - 1,
          )
        case (1, -1) =>
          PeriodLength(
            minLengthInDays = periodLength.minLengthInDays + 1,
            maxLengthInDays = periodLength.maxLengthInDays,
          )
        case (1, 1) =>
          PeriodLength(
            minLengthInDays = periodLength.minLengthInDays + 1,
            maxLengthInDays = periodLength.maxLengthInDays,
          )
        case _ => throw new AssertionError()
      }
    else
      periodLength
  }

  private def lengthOfNumMonths(numMonths: Int): PeriodLength =
    numMonths match {
      case negative if negative < 0 => lengthOfNumMonths(-negative) * -1
      case 0                        => PeriodLength.always(0)
      // Shortest is List(FEBRUARY) with 28 days
      // Longest is List(JANUARY) with 31 days
      case 1 => PeriodLength(28, 31)
      // Shortest is List(FEBRUARY, MARCH) with 59 days
      // Longest is List(JULY, AUGUST) with 62 days
      case 2 => PeriodLength(59, 62)
      // Shortest is List(FEBRUARY, MARCH, APRIL) with 89 days
      // Longest is List(MARCH, APRIL, MAY) with 92 days
      case 3 => PeriodLength(89, 92)
      // Shortest is List(NOVEMBER, DECEMBER, JANUARY, FEBRUARY) with 120 days
      // Longest is List(MAY, JUNE, JULY, AUGUST) with 123 days
      case 4 => PeriodLength(120, 123)
      // Shortest is List(FEBRUARY, MARCH, APRIL, MAY, JUNE) with 150 days
      // Longest is List(MARCH, APRIL, MAY, JUNE, JULY) with 153 days
      case 5 => PeriodLength(150, 153)
      // Shortest is List(NOVEMBER, DECEMBER, JANUARY, FEBRUARY, MARCH, APRIL) with 181 days
      // Longest is List(MARCH, APRIL, MAY, JUNE, JULY, AUGUST) with 184 days
      case 6 => PeriodLength(181, 184)
      // Shortest is List(DECEMBER, JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE) with 212 days
      // Longest is List(JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER, JANUARY) with 215 days
      case 7 => PeriodLength(212, 215)
      // Shortest is List(NOVEMBER, DECEMBER, JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE) with 242 days
      // Longest is List(MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER) with 245 days
      case 8 => PeriodLength(242, 245)
      // Shortest is List(NOVEMBER, DECEMBER, JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY) with 273 days
      // Longest is List(MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER, JANUARY) with 276 days
      case 9 => PeriodLength(273, 276)
      // Shortest is List(SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER, JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE) with 303 days
      // Longest is List(MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER) with 306 days
      case 10 => PeriodLength(303, 306)
      // Shortest is List(NOVEMBER, DECEMBER, JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER) with 334 days
      // Longest is List(MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER, JANUARY) with 337 days
      case 11 => PeriodLength(334, 337)
    }

  private def lengthOfNumYears(numYears: Int): PeriodLength =
    numYears match {
      case negative if negative < 0 => lengthOfNumYears(-negative) * -1
      case numYears                 => PeriodLength.always(365 * numYears) + numLeapDaysIn(numYears)
    }

  private def numLeapDaysIn(numYears: Int): PeriodLength =
    numYears match {
      case 0             => PeriodLength.always(0)
      case n if n >= 400 => (PeriodLength.always(97) * (n / 400)) + numLeapDaysIn(n % 400)
      case n =>
        PeriodLength(
          minLengthInDays = (n - 4) / 4 + (n match {
            case n if n < 104 => +0
            case n if n < 204 => -1
            case n if n < 400 => -2
          }),
          maxLengthInDays = (n - 1) / 4 + (n match {
            case n if n < 197 => +1
            case n if n < 297 => +0
            case n if n < 397 => -1
            case n if n < 400 => -2
          }),
        )
    }

  implicit val partialOrdering: PartialOrdering[PeriodLength] = new PartialOrdering[PeriodLength] {
    override def tryCompare(x: PeriodLength, y: PeriodLength): Option[Int] =
      if (
        (x.minLengthInDays == x.maxLengthInDays) && (y.minLengthInDays == y.maxLengthInDays) && (x.minLengthInDays == y.minLengthInDays)
      ) {
        Some(0)
      } else if (x.maxLengthInDays < y.minLengthInDays) {
        Some(-1)
      } else if (x.minLengthInDays > y.maxLengthInDays) {
        Some(1)
      } else {
        None
      }

    override def lteq(x: PeriodLength, y: PeriodLength): Boolean = tryCompare(x, y).exists(_ <= 0)
  }

}
