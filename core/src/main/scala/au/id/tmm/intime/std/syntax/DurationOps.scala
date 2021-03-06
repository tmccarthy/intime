package au.id.tmm.intime.std.syntax

import java.time.Duration

import au.id.tmm.intime.std.extras.DurationMultiplication.Syntax.DurationMultiplicationOps
import au.id.tmm.intime.std.extras.ScalaConcurrentDurationConversions
import au.id.tmm.intime.std.instances.duration._

import scala.concurrent.duration.{Duration => SDuration, FiniteDuration => SFiniteDuration}

final class DurationOps private (protected val duration: Duration)
    extends OrderingOps[Duration](duration)
    with DurationMultiplicationOps {
  def +(temporalAmount: Duration): Duration = duration.plus(temporalAmount)
  def -(temporalAmount: Duration): Duration = duration.minus(temporalAmount)
  def /(divisor: Long): Duration            = duration.dividedBy(divisor)
  def *(multiplicand: Long): Duration       = duration.multipliedBy(multiplicand)
  def unary_- : Duration                    = duration.negated()
}

object DurationOps {
  trait ToDurationOps extends LowPriorityToDurationOps {
    implicit def toDurationOps(duration: Duration): DurationOps = new DurationOps(duration)

    implicit def toDurationConversionOps(duration: Duration): ScalaConcurrentDurationConversions.Syntax.JDurationOps =
      new ScalaConcurrentDurationConversions.Syntax.JDurationOps(duration)

    implicit def toSFiniteDurationOps(
      finiteDuration: SFiniteDuration,
    ): ScalaConcurrentDurationConversions.Syntax.SFiniteDurationOps =
      new ScalaConcurrentDurationConversions.Syntax.SFiniteDurationOps(finiteDuration)
  }

  private[std] trait LowPriorityToDurationOps {
    implicit def toSDurationOps(sDuration: SDuration): ScalaConcurrentDurationConversions.Syntax.SDurationOps =
      new ScalaConcurrentDurationConversions.Syntax.SDurationOps(sDuration)
  }
}
