package au.id.tmm.javatime4s.cats

import java.time.ZoneId

import au.id.tmm.javatime4s.orderings.ZoneIdPartialOrdering
import cats.{Hash, PartialOrder, Show}

trait ZoneIdInstances {
  implicit val catsKernelStdHashForZoneId: Hash[ZoneId] with PartialOrder[ZoneId] = new ZoneIdHash
  implicit val catsKernelStdShowForZoneId: Show[ZoneId] = Show.fromToString
}

class ZoneIdHash extends Hash[ZoneId] with PartialOrder[ZoneId] {
  override def hash(x: ZoneId): Int               = x.hashCode()
  override def eqv(x: ZoneId, y: ZoneId): Boolean = x == y

  override def partialCompare(x: ZoneId, y: ZoneId): Double =
    ZoneIdPartialOrdering.tryCompare(x, y) match {
      case Some(value) => value.toDouble
      case None => Double.NaN
    }
}
