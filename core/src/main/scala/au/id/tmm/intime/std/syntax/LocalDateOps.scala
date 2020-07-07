package au.id.tmm.intime.std.syntax

import java.time.LocalDate
import java.time.temporal.TemporalAmount
import au.id.tmm.intime.std.instances.localDate._

final class LocalDateOps private (localDate: LocalDate) extends OrderingOps[LocalDate](localDate) {
  def +(temporalAmount: TemporalAmount): LocalDate = localDate.plus(temporalAmount)
  def -(temporalAmount: TemporalAmount): LocalDate = localDate.minus(temporalAmount)
}

object LocalDateOps {
  trait ToLocalDateOps {
    implicit def toLocalDateOps(localDate: LocalDate): LocalDateOps = new LocalDateOps(localDate)
  }
}
