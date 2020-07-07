package au.id.tmm.intime.std.syntax

import java.time.Year
import java.time.temporal.TemporalAmount
import au.id.tmm.intime.std.instances.year._

final class YearOps private (year: Year) extends OrderingOps[Year](year) {
  def +(temporalAmount: TemporalAmount): Year = year.plus(temporalAmount)
  def -(temporalAmount: TemporalAmount): Year = year.minus(temporalAmount)
}

object YearOps {
  trait ToYearOps {
    implicit def toYearOps(year: Year): YearOps = new YearOps(year)
  }
}
