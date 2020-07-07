package au.id.tmm.intime.std.syntax

import java.time.Instant
import java.time.temporal.TemporalAmount
import au.id.tmm.intime.std.instances.instant._

final class InstantOps private (instant: Instant) extends OrderingOps[Instant](instant) {
  def +(temporalAmount: TemporalAmount): Instant = instant.plus(temporalAmount)
  def -(temporalAmount: TemporalAmount): Instant = instant.minus(temporalAmount)
}

object InstantOps {
  trait ToInstantOps {
    implicit def toInstantOps(instant: Instant): InstantOps = new InstantOps(instant)
  }
}
