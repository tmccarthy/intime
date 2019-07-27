package au.id.tmm.intime.cats

import java.time.ZoneId

import cats.{Hash, Show}

trait ZoneIdInstances {
  implicit val catsKernelStdHashForZoneId: Hash[ZoneId] = new ZoneIdHash
  implicit val catsKernelStdShowForZoneId: Show[ZoneId]                           = Show.fromToString
}

class ZoneIdHash extends Hash[ZoneId] {
  override def hash(x: ZoneId): Int               = x.hashCode()
  override def eqv(x: ZoneId, y: ZoneId): Boolean = x == y
}
