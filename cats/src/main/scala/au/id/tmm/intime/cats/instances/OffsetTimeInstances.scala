package au.id.tmm.intime.cats.instances

import java.time.OffsetTime

import cats.{Hash, Order, Show}

trait OffsetTimeInstances {
  implicit val intimeOrderForOffsetTime: Order[OffsetTime] with Hash[OffsetTime] = new OffsetTimeOrder
  implicit val intimeShowForOffsetTime: Show[OffsetTime]                         = Show.fromToString
}

class OffsetTimeOrder extends Order[OffsetTime] with Hash[OffsetTime] {
  override def compare(x: OffsetTime, y: OffsetTime): Int = x compareTo y
  override def hash(x: OffsetTime): Int                   = x.hashCode()
}
