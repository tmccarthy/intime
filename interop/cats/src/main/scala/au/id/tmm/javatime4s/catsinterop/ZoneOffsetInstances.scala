package au.id.tmm.javatime4s.catsinterop

import java.time.ZoneOffset

import cats.{Hash, Order, Show}

trait ZoneOffsetInstances {
  implicit val catsKernelStdOrderForZoneOffset: Order[ZoneOffset] with Hash[ZoneOffset] = new ZoneOffsetOrder
  implicit val catsKernelStdShowForDayOfWeek: Show[ZoneOffset] = Show.fromToString
}

class ZoneOffsetOrder extends Order[ZoneOffset] with Hash[ZoneOffset] {
  override def compare(x: ZoneOffset, y: ZoneOffset): Int = x compareTo y
  override def hash(x: ZoneOffset): Int = x.hashCode()
}

