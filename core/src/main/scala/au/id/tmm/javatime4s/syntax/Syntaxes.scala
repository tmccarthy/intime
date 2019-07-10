package au.id.tmm.javatime4s.syntax

import java.time._
import java.time.temporal.TemporalAmount

import au.id.tmm.javatime4s.instances._

trait Syntaxes {

  implicit class DurationSyntax(duration: Duration) extends durationOrdering.OrderingOps(duration) {
    def +(temporalAmount: Duration): Duration = duration.plus(temporalAmount)
    def -(temporalAmount: Duration): Duration = duration.minus(temporalAmount)
    def /(divisor: Long): Duration = duration.dividedBy(divisor)
    def *(multiplicand: Long): Duration = duration.multipliedBy(multiplicand)
    def unary_- : Duration = duration.negated()
  }

  implicit class InstantSyntax(instant: Instant) extends instantOrdering.OrderingOps(instant) {
    def +(temporalAmount: TemporalAmount): Instant = instant.plus(temporalAmount)
    def -(temporalAmount: TemporalAmount): Instant = instant.minus(temporalAmount)
  }

  implicit class LocalDateSyntax(localDate: LocalDate) extends localDateOrdering.OrderingOps(localDate) {
    def +(temporalAmount: TemporalAmount): LocalDate = localDate.plus(temporalAmount)
    def -(temporalAmount: TemporalAmount): LocalDate = localDate.minus(temporalAmount)
  }

  implicit class LocalDateTimeSyntax(localDateTime: LocalDateTime) extends localDateTimeOrdering.OrderingOps(localDateTime) {
    def +(temporalAmount: TemporalAmount): LocalDateTime = localDateTime.plus(temporalAmount)
    def -(temporalAmount: TemporalAmount): LocalDateTime = localDateTime.minus(temporalAmount)
  }

  implicit class LocalTimeSyntax(localTime: LocalTime) extends localTimeOrdering.OrderingOps(localTime) {
    def +(temporalAmount: TemporalAmount): LocalTime = localTime.plus(temporalAmount)
    def -(temporalAmount: TemporalAmount): LocalTime = localTime.minus(temporalAmount)
  }

  implicit class OffsetDateTimeSyntax(offsetDateTime: OffsetDateTime) extends offsetDateTimeOrdering.OrderingOps(offsetDateTime) {
    def +(temporalAmount: TemporalAmount): OffsetDateTime = offsetDateTime.plus(temporalAmount)
    def -(temporalAmount: TemporalAmount): OffsetDateTime = offsetDateTime.minus(temporalAmount)
  }

  implicit class OffsetTimeSyntax(offsetTime: OffsetTime) extends offsetTimeOrdering.OrderingOps(offsetTime) {
    def +(temporalAmount: TemporalAmount): OffsetTime = offsetTime.plus(temporalAmount)
    def -(temporalAmount: TemporalAmount): OffsetTime = offsetTime.minus(temporalAmount)
  }

  implicit class PeriodSyntax(period: Period) {
    def +(temporalAmount: TemporalAmount): Period = period.plus(temporalAmount)
    def -(temporalAmount: TemporalAmount): Period = period.minus(temporalAmount)
    def *(scalar: Int): Period = period.multipliedBy(scalar)
    def unary_- : Period = period.negated()
  }

  implicit class YearSyntax(year: Year) extends yearOrdering.OrderingOps(year) {
    def +(temporalAmount: TemporalAmount): Year = year.plus(temporalAmount)
    def -(temporalAmount: TemporalAmount): Year = year.minus(temporalAmount)
  }

  implicit class YearMonthSyntax(yearMonth: YearMonth) extends yearMonthOrdering.OrderingOps(yearMonth) {
    def +(temporalAmount: TemporalAmount): YearMonth = yearMonth.plus(temporalAmount)
    def -(temporalAmount: TemporalAmount): YearMonth = yearMonth.minus(temporalAmount)
  }

  implicit class ZonedDateTimeSyntax(zonedDateTime: ZonedDateTime) extends zonedDateTimeOrdering.OrderingOps(zonedDateTime) {
    def +(temporalAmount: TemporalAmount): ZonedDateTime = zonedDateTime.plus(temporalAmount)
    def -(temporalAmount: TemporalAmount): ZonedDateTime = zonedDateTime.minus(temporalAmount)
  }

}
