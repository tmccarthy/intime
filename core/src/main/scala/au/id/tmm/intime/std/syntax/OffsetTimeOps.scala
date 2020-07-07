package au.id.tmm.intime.std.syntax

import java.time.OffsetTime
import java.time.temporal.TemporalAmount
import au.id.tmm.intime.std.instances.offsetTime._

final class OffsetTimeOps private (offsetTime: OffsetTime) extends OrderingOps[OffsetTime](offsetTime) {
  def +(temporalAmount: TemporalAmount): OffsetTime = offsetTime.plus(temporalAmount)
  def -(temporalAmount: TemporalAmount): OffsetTime = offsetTime.minus(temporalAmount)
}

object OffsetTimeOps {
  trait ToOffsetTimeOps {
    implicit def toOffsetTimeOps(offsetTime: OffsetTime): OffsetTimeOps = new OffsetTimeOps(offsetTime)
  }
}
