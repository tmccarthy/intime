package au.id.tmm.javatime4s.syntax

import java.time._
import java.time.temporal.TemporalAmount

import au.id.tmm.javatime4s.instances._
import au.id.tmm.javatime4s.syntax.Syntaxes.OrderingOps

import scala.math.Ordering

/**
  * Defines operator overloads for classes in the `java.time` package. These are always simple
  * aliases:
  * <ul>
  *   <li>`plus` is aliased to `+`</li>
  *   <li>`minus` is aliased to `-`</li>
  *   <li>`dividedBy` is aliased to `/`</li>
  *   <li>`multipliedBy` is aliased to `*`</li>
  *   <li>`negated` is aliased to `unary_-`</li>
  * </ul>
  *
  * Those classes for which there is an ordering defined in `au.id.tmm.javatime4s.instances.Orderings`
  * also have the comparison operators `<`, `<=`, `>`, `>=`, `equiv`, `min` and `max` defined via
  * `OrderingOps`.
  */
trait Syntaxes {

  implicit class DurationSyntax(duration: Duration) extends OrderingOps[Duration](duration) {
    def +(temporalAmount: Duration): Duration = duration.plus(temporalAmount)
    def -(temporalAmount: Duration): Duration = duration.minus(temporalAmount)
    def /(divisor: Long): Duration            = duration.dividedBy(divisor)
    def *(multiplicand: Long): Duration       = duration.multipliedBy(multiplicand)
    def unary_- : Duration                    = duration.negated()
  }

  implicit class InstantSyntax(instant: Instant) extends OrderingOps[Instant](instant) {
    def +(temporalAmount: TemporalAmount): Instant = instant.plus(temporalAmount)
    def -(temporalAmount: TemporalAmount): Instant = instant.minus(temporalAmount)
  }

  implicit class LocalDateSyntax(localDate: LocalDate) extends OrderingOps[LocalDate](localDate) {
    def +(temporalAmount: TemporalAmount): LocalDate = localDate.plus(temporalAmount)
    def -(temporalAmount: TemporalAmount): LocalDate = localDate.minus(temporalAmount)
  }

  implicit class LocalDateTimeSyntax(localDateTime: LocalDateTime) extends OrderingOps[LocalDateTime](localDateTime) {
    def +(temporalAmount: TemporalAmount): LocalDateTime = localDateTime.plus(temporalAmount)
    def -(temporalAmount: TemporalAmount): LocalDateTime = localDateTime.minus(temporalAmount)
  }

  implicit class LocalTimeSyntax(localTime: LocalTime) extends OrderingOps[LocalTime](localTime) {
    def +(temporalAmount: TemporalAmount): LocalTime = localTime.plus(temporalAmount)
    def -(temporalAmount: TemporalAmount): LocalTime = localTime.minus(temporalAmount)
  }

  implicit class OffsetDateTimeSyntax(offsetDateTime: OffsetDateTime)
      extends OrderingOps[OffsetDateTime](offsetDateTime) {
    def +(temporalAmount: TemporalAmount): OffsetDateTime = offsetDateTime.plus(temporalAmount)
    def -(temporalAmount: TemporalAmount): OffsetDateTime = offsetDateTime.minus(temporalAmount)
  }

  implicit class OffsetTimeSyntax(offsetTime: OffsetTime) extends OrderingOps[OffsetTime](offsetTime) {
    def +(temporalAmount: TemporalAmount): OffsetTime = offsetTime.plus(temporalAmount)
    def -(temporalAmount: TemporalAmount): OffsetTime = offsetTime.minus(temporalAmount)
  }

  implicit class PeriodSyntax(period: Period) {
    def +(temporalAmount: TemporalAmount): Period = period.plus(temporalAmount)
    def -(temporalAmount: TemporalAmount): Period = period.minus(temporalAmount)
    def *(scalar: Int): Period                    = period.multipliedBy(scalar)
    def unary_- : Period                          = period.negated()
  }

  implicit class YearSyntax(year: Year) extends OrderingOps[Year](year) {
    def +(temporalAmount: TemporalAmount): Year = year.plus(temporalAmount)
    def -(temporalAmount: TemporalAmount): Year = year.minus(temporalAmount)
  }

  implicit class YearMonthSyntax(yearMonth: YearMonth) extends OrderingOps[YearMonth](yearMonth) {
    def +(temporalAmount: TemporalAmount): YearMonth = yearMonth.plus(temporalAmount)
    def -(temporalAmount: TemporalAmount): YearMonth = yearMonth.minus(temporalAmount)
  }

  implicit class ZonedDateTimeSyntax(zonedDateTime: ZonedDateTime) extends OrderingOps[ZonedDateTime](zonedDateTime) {
    def +(temporalAmount: TemporalAmount): ZonedDateTime = zonedDateTime.plus(temporalAmount)
    def -(temporalAmount: TemporalAmount): ZonedDateTime = zonedDateTime.minus(temporalAmount)
  }

}

object Syntaxes {
  // This class is the equivalent of Ordering.Ops in 2.12 and Ordering.OrderingOps in 2.13. The
  // rename means that in order to cross compile we have to define one ourselves.
  abstract class OrderingOps[T](lhs: T)(implicit ordering: Ordering[T]) {
    def <(rhs: T): Boolean     = ordering.lt(lhs, rhs)
    def <=(rhs: T): Boolean    = ordering.lteq(lhs, rhs)
    def >(rhs: T): Boolean     = ordering.gt(lhs, rhs)
    def >=(rhs: T): Boolean    = ordering.gteq(lhs, rhs)
    def equiv(rhs: T): Boolean = ordering.equiv(lhs, rhs)
    def max(rhs: T): T         = ordering.max(lhs, rhs)
    def min(rhs: T): T         = ordering.min(lhs, rhs)
  }
}
