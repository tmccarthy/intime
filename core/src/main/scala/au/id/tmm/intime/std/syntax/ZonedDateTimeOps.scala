package au.id.tmm.intime.std.syntax

import java.time.ZonedDateTime
import java.time.temporal.TemporalAmount
import au.id.tmm.intime.std.instances.zonedDateTime._

final class ZonedDateTimeOps private (zonedDateTime: ZonedDateTime) extends OrderingOps[ZonedDateTime](zonedDateTime) {
  def +(temporalAmount: TemporalAmount): ZonedDateTime = zonedDateTime.plus(temporalAmount)
  def -(temporalAmount: TemporalAmount): ZonedDateTime = zonedDateTime.minus(temporalAmount)
}

object ZonedDateTimeOps {
  trait ToZonedDateTimeOps {
    implicit def toZonedDateTimeOps(zonedDateTime: ZonedDateTime): ZonedDateTimeOps =
      new ZonedDateTimeOps(zonedDateTime)
  }
}
