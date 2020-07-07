package au.id.tmm.intime.std.syntax

import java.time.Period
import java.time.temporal.TemporalAmount
import au.id.tmm.intime.std.instances.period._

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
