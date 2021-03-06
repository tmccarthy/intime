package au.id.tmm.intime.cats.instances

import java.time.LocalDateTime

import cats.{Hash, Order, Show}

trait LocalDateTimeInstances {
  implicit val intimeOrderForLocalDateTime: Order[LocalDateTime] with Hash[LocalDateTime] =
    new LocalDateTimeOrder
  implicit val intimeShowForLocalDateTime: Show[LocalDateTime] = Show.fromToString
}

class LocalDateTimeOrder extends Order[LocalDateTime] with Hash[LocalDateTime] {
  override def compare(x: LocalDateTime, y: LocalDateTime): Int = x compareTo y
  override def hash(x: LocalDateTime): Int                      = x.hashCode()
}
