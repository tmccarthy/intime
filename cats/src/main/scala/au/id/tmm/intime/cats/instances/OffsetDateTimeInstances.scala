package au.id.tmm.intime.cats.instances

import java.time.OffsetDateTime

import cats.{Hash, Order, Show}

trait OffsetDateTimeInstances {
  implicit val intimeOrderForOffsetDateTime: Order[OffsetDateTime] with Hash[OffsetDateTime] =
    new OffsetDateTimeOrder
  implicit val intimeShowForOffsetDateTime: Show[OffsetDateTime] = Show.fromToString
}

class OffsetDateTimeOrder extends Order[OffsetDateTime] with Hash[OffsetDateTime] {
  override def compare(x: OffsetDateTime, y: OffsetDateTime): Int = x compareTo y
  override def hash(x: OffsetDateTime): Int                       = x.hashCode()
}
