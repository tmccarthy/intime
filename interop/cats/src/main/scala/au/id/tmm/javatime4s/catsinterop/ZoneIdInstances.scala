package au.id.tmm.javatime4s.catsinterop

import java.time.ZoneId

import cats.{Hash, Show}

trait ZoneIdInstances {
  implicit val catsKernelStdHashForZoneId: Hash[ZoneId] = new ZoneIdHash
  implicit val catsKernelStdShowForDayOfWeek: Show[ZoneId] = Show.fromToString
}

class ZoneIdHash extends Hash[ZoneId] {
  override def hash(x: ZoneId): Int = x.hashCode()
  override def eqv(x: ZoneId, y: ZoneId): Boolean = x == y
}

