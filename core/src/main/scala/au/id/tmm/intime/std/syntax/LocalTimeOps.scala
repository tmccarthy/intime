package au.id.tmm.intime.std.syntax

import java.time.LocalTime
import java.time.temporal.TemporalAmount
import au.id.tmm.intime.std.instances.localTime._

final class LocalTimeOps private (localTime: LocalTime) extends OrderingOps[LocalTime](localTime) {
  def +(temporalAmount: TemporalAmount): LocalTime = localTime.plus(temporalAmount)
  def -(temporalAmount: TemporalAmount): LocalTime = localTime.minus(temporalAmount)
}

object LocalTimeOps {
  trait ToLocalTimeOps {
    implicit def toLocalTimeOps(localTime: LocalTime): LocalTimeOps = new LocalTimeOps(localTime)
  }
}
