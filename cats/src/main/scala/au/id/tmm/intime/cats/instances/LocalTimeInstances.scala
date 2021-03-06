package au.id.tmm.intime.cats.instances

import java.time.LocalTime

import cats.{Hash, Order, Show}

trait LocalTimeInstances {
  implicit val intimeOrderForLocalTime: Order[LocalTime] with Hash[LocalTime] = new LocalTimeOrder
  implicit val intimeShowForLocalTime: Show[LocalTime]                        = Show.fromToString
}

class LocalTimeOrder extends Order[LocalTime] with Hash[LocalTime] {
  override def compare(x: LocalTime, y: LocalTime): Int = x compareTo y
  override def hash(x: LocalTime): Int                  = x.hashCode()
}
