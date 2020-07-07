package au.id.tmm.intime.std.syntax

import java.time._
import java.time.temporal.TemporalAmount

import au.id.tmm.intime.std.instances.all._

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
  * Those classes for which there is an ordering defined in `au.id.tmm.intime.instances.Orderings`
  * also have the comparison operators `<`, `<=`, `>`, `>=`, `equiv`, `min` and `max` defined via
  * `OrderingOps`.
  */
object Syntaxes {

  final class DurationOps private (duration: Duration) extends OrderingOps[Duration](duration) {
    def +(temporalAmount: Duration): Duration = duration.plus(temporalAmount)
    def -(temporalAmount: Duration): Duration = duration.minus(temporalAmount)
    def /(divisor: Long): Duration            = duration.dividedBy(divisor)
    def *(multiplicand: Long): Duration       = duration.multipliedBy(multiplicand)
    def unary_- : Duration                    = duration.negated()
  }

  object DurationOps {
    trait ToDurationOps {
      implicit def toDurationOps(duration: Duration): DurationOps = new DurationOps(duration)
    }
  }

  final class InstantOps private (instant: Instant) extends OrderingOps[Instant](instant) {
    def +(temporalAmount: TemporalAmount): Instant = instant.plus(temporalAmount)
    def -(temporalAmount: TemporalAmount): Instant = instant.minus(temporalAmount)
  }

  object InstantOps {
    trait ToInstantOps {
      implicit def toInstantOps(instant: Instant): InstantOps = new InstantOps(instant)
    }
  }

  final class LocalDateOps private (localDate: LocalDate) extends OrderingOps[LocalDate](localDate) {
    def +(temporalAmount: TemporalAmount): LocalDate = localDate.plus(temporalAmount)
    def -(temporalAmount: TemporalAmount): LocalDate = localDate.minus(temporalAmount)
  }

  object LocalDateOps {
    trait ToLocalDateOps {
      implicit def toLocalDateOps(localDate: LocalDate): LocalDateOps = new LocalDateOps(localDate)
    }
  }

  final class LocalDateTimeOps private (localDateTime: LocalDateTime)
      extends OrderingOps[LocalDateTime](localDateTime) {
    def +(temporalAmount: TemporalAmount): LocalDateTime = localDateTime.plus(temporalAmount)
    def -(temporalAmount: TemporalAmount): LocalDateTime = localDateTime.minus(temporalAmount)
  }

  object LocalDateTimeOps {
    trait ToLocalDateTimeOps {
      implicit def toLocalDateTimeOps(localDateTime: LocalDateTime): LocalDateTimeOps =
        new LocalDateTimeOps(localDateTime)
    }
  }

  final class LocalTimeOps private (localTime: LocalTime) extends OrderingOps[LocalTime](localTime) {
    def +(temporalAmount: TemporalAmount): LocalTime = localTime.plus(temporalAmount)
    def -(temporalAmount: TemporalAmount): LocalTime = localTime.minus(temporalAmount)
  }

  object LocalTimeOps {
    trait ToLocalTimeOps {
      implicit def toLocalTimeOps(localTime: LocalTime): LocalTimeOps = new LocalTimeOps(localTime)
    }
  }

  final class OffsetDateTimeOps private (offsetDateTime: OffsetDateTime)
      extends OrderingOps[OffsetDateTime](offsetDateTime) {
    def +(temporalAmount: TemporalAmount): OffsetDateTime = offsetDateTime.plus(temporalAmount)
    def -(temporalAmount: TemporalAmount): OffsetDateTime = offsetDateTime.minus(temporalAmount)
  }

  object OffsetDateTimeOps {
    trait ToOffsetDateTimeOps {
      implicit def toOffsetDateTimeOps(offsetDateTime: OffsetDateTime): OffsetDateTimeOps =
        new OffsetDateTimeOps(offsetDateTime)
    }
  }

  final class OffsetTimeOps private (offsetTime: OffsetTime) extends OrderingOps[OffsetTime](offsetTime) {
    def +(temporalAmount: TemporalAmount): OffsetTime = offsetTime.plus(temporalAmount)
    def -(temporalAmount: TemporalAmount): OffsetTime = offsetTime.minus(temporalAmount)
  }

  object OffsetTimeOps {
    trait ToOffsetTimeOps {
      implicit def toOffsetTimeOps(offsetTime: OffsetTime): OffsetTimeOps = new OffsetTimeOps(offsetTime)
    }
  }

  final class PeriodOps private (period: Period) extends PartialOrderingOps[Period](period) {
    def +(temporalAmount: TemporalAmount): Period = period.plus(temporalAmount)
    def -(temporalAmount: TemporalAmount): Period = period.minus(temporalAmount)
    def *(scalar: Int): Period                    = period.multipliedBy(scalar)
    def unary_- : Period                          = period.negated()
  }

  object PeriodOps {
    trait ToPeriodOps {
      implicit def toPeriodOps(period: Period): PeriodOps = new PeriodOps(period)
    }
  }

  final class YearOps private (year: Year) extends OrderingOps[Year](year) {
    def +(temporalAmount: TemporalAmount): Year = year.plus(temporalAmount)
    def -(temporalAmount: TemporalAmount): Year = year.minus(temporalAmount)
  }

  object YearOps {
    trait ToYearOps {
      implicit def toYearOps(year: Year): YearOps = new YearOps(year)
    }
  }

  final class YearMonthOps private (yearMonth: YearMonth) extends OrderingOps[YearMonth](yearMonth) {
    def +(temporalAmount: TemporalAmount): YearMonth = yearMonth.plus(temporalAmount)
    def -(temporalAmount: TemporalAmount): YearMonth = yearMonth.minus(temporalAmount)
  }

  object YearMonthOps {
    trait ToYearMonthOps {
      implicit def toYearMonthOps(yearMonth: YearMonth): YearMonthOps = new YearMonthOps(yearMonth)
    }
  }

  final class ZonedDateTimeOps private (zonedDateTime: ZonedDateTime)
      extends OrderingOps[ZonedDateTime](zonedDateTime) {
    def +(temporalAmount: TemporalAmount): ZonedDateTime = zonedDateTime.plus(temporalAmount)
    def -(temporalAmount: TemporalAmount): ZonedDateTime = zonedDateTime.minus(temporalAmount)
  }

  object ZonedDateTimeOps {
    trait ToZonedDateTimeOps {
      implicit def toZonedDateTimeOps(zonedDateTime: ZonedDateTime): ZonedDateTimeOps =
        new ZonedDateTimeOps(zonedDateTime)
    }
  }

  // This class is the equivalent of Ordering.Ops in 2.12 and Ordering.OrderingOps in 2.13. The
  // rename means that in order to cross compile we have to define one ourselves.
  abstract class OrderingOps[T] private[Syntaxes] (lhs: T)(implicit ordering: Ordering[T]) {
    def <(rhs: T): Boolean     = ordering.lt(lhs, rhs)
    def <=(rhs: T): Boolean    = ordering.lteq(lhs, rhs)
    def >(rhs: T): Boolean     = ordering.gt(lhs, rhs)
    def >=(rhs: T): Boolean    = ordering.gteq(lhs, rhs)
    def equiv(rhs: T): Boolean = ordering.equiv(lhs, rhs)
    def max(rhs: T): T         = ordering.max(lhs, rhs)
    def min(rhs: T): T         = ordering.min(lhs, rhs)
  }

  // This class is the equivalent of Ordering.Ops in 2.12 and Ordering.OrderingOps in 2.13. The
  // rename means that in order to cross compile we have to define one ourselves.
  abstract class PartialOrderingOps[T] private[Syntaxes] (lhs: T)(implicit partialOrdering: PartialOrdering[T]) {
    def <(rhs: T): Boolean     = partialOrdering.lt(lhs, rhs)
    def <=(rhs: T): Boolean    = partialOrdering.lteq(lhs, rhs)
    def >(rhs: T): Boolean     = partialOrdering.gt(lhs, rhs)
    def >=(rhs: T): Boolean    = partialOrdering.gteq(lhs, rhs)
    def equiv(rhs: T): Boolean = partialOrdering.equiv(lhs, rhs)
  }

}
