package au.id.tmm.intime.std.syntax

import java.time.Duration
import au.id.tmm.intime.std.instances.duration._

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
