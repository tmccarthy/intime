package au.id.tmm.intime.cats.instances

import java.time.ZoneOffset

import cats.{Hash, Order, Show}

trait ZoneOffsetInstances {
  implicit val intimeOrderForZoneOffset: Order[ZoneOffset] with Hash[ZoneOffset] = new ZoneOffsetOrder
  implicit val intimeShowForZoneOffset: Show[ZoneOffset]                         = Show.fromToString
}

class ZoneOffsetOrder extends Order[ZoneOffset] with Hash[ZoneOffset] {
  override def compare(x: ZoneOffset, y: ZoneOffset): Int = x compareTo y
  override def hash(x: ZoneOffset): Int                   = x.hashCode()
}
