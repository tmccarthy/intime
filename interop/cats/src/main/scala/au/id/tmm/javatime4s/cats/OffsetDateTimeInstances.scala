package au.id.tmm.javatime4s.cats

import java.time.OffsetDateTime

import cats.{Hash, Order, Show}

trait OffsetDateTimeInstances {
  implicit val catsKernelStdOrderForOffsetDateTime: Order[OffsetDateTime] with Hash[OffsetDateTime] =
    new OffsetDateTimeOrder
  implicit val catsKernelStdShowForOffsetDateTime: Show[OffsetDateTime] = Show.fromToString
}

class OffsetDateTimeOrder extends Order[OffsetDateTime] with Hash[OffsetDateTime] {
  override def compare(x: OffsetDateTime, y: OffsetDateTime): Int = x compareTo y
  override def hash(x: OffsetDateTime): Int                       = x.hashCode()
}
