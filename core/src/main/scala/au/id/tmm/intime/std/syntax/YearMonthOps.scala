package au.id.tmm.intime.std.syntax

import java.time.YearMonth
import java.time.temporal.TemporalAmount
import au.id.tmm.intime.std.instances.yearMonth._

final class YearMonthOps private (yearMonth: YearMonth) extends OrderingOps[YearMonth](yearMonth) {
  def +(temporalAmount: TemporalAmount): YearMonth = yearMonth.plus(temporalAmount)
  def -(temporalAmount: TemporalAmount): YearMonth = yearMonth.minus(temporalAmount)
}

object YearMonthOps {
  trait ToYearMonthOps {
    implicit def toYearMonthOps(yearMonth: YearMonth): YearMonthOps = new YearMonthOps(yearMonth)
  }
}
