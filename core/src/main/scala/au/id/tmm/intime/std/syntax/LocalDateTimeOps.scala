package au.id.tmm.intime.std.syntax

import java.time.LocalDateTime
import java.time.temporal.TemporalAmount
import au.id.tmm.intime.std.instances.localDateTime._

final class LocalDateTimeOps private (localDateTime: LocalDateTime) extends OrderingOps[LocalDateTime](localDateTime) {
  def +(temporalAmount: TemporalAmount): LocalDateTime = localDateTime.plus(temporalAmount)
  def -(temporalAmount: TemporalAmount): LocalDateTime = localDateTime.minus(temporalAmount)
}

object LocalDateTimeOps {
  trait ToLocalDateTimeOps {
    implicit def toLocalDateTimeOps(localDateTime: LocalDateTime): LocalDateTimeOps =
      new LocalDateTimeOps(localDateTime)
  }
}
