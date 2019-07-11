package au.id.tmm.javatime4s.cats

import java.time.Period

import cats.kernel.CommutativeGroup
import cats.{Hash, Show}

trait PeriodInstances {
  implicit val catsKernelStdHashForPeriod: Hash[Period]              = new PeriodHash
  implicit val catsKernelStdGroupForPeriod: CommutativeGroup[Period] = new PeriodGroup
  implicit val catsKernelStdShowForPeriod: Show[Period]              = Show.fromToString
}

class PeriodHash extends Hash[Period] {
  override def hash(x: Period): Int               = x.hashCode()
  override def eqv(x: Period, y: Period): Boolean = x == y
}

class PeriodGroup extends CommutativeGroup[Period] {
  override def inverse(a: Period): Period            = a.negated()
  override def empty: Period                         = Period.ZERO
  override def combine(x: Period, y: Period): Period = x plus y
}
