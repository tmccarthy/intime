package au.id.tmm.intime.cats

import java.time.Period

import au.id.tmm.intime.std.instances.period._
import cats.kernel.{CommutativeGroup, PartialOrder}
import cats.{Hash, Show}

trait PeriodInstances {
  implicit val catsKernelStdHashForPeriod: Hash[Period] with PartialOrder[Period] = new PeriodHash
  implicit val catsKernelStdGroupForPeriod: CommutativeGroup[Period]              = new PeriodGroup
  implicit val catsKernelStdShowForPeriod: Show[Period]                           = Show.fromToString
}

class PeriodHash extends Hash[Period] with PartialOrder[Period] {
  override def hash(x: Period): Int               = x.hashCode()
  override def eqv(x: Period, y: Period): Boolean = intimePartialOrderingForJavaTimePeriod.equiv(x, y)
  override def partialCompare(x: Period, y: Period): Double =
    intimePartialOrderingForJavaTimePeriod.tryCompare(x, y) match {
      case Some(value) => value.toDouble
      case None        => Double.NaN
    }
}

class PeriodGroup extends CommutativeGroup[Period] {
  override def inverse(a: Period): Period            = a.negated()
  override def empty: Period                         = Period.ZERO
  override def combine(x: Period, y: Period): Period = x plus y
}
