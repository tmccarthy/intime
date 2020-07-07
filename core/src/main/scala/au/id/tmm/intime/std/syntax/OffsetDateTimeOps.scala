package au.id.tmm.intime.std.syntax

import java.time.OffsetDateTime
import java.time.temporal.TemporalAmount
import au.id.tmm.intime.std.instances.offsetDateTime._

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
