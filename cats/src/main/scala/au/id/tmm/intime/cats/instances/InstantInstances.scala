package au.id.tmm.intime.cats.instances

import java.time.Instant

import cats.{Hash, Order, Show}

trait InstantInstances {
  implicit val intimeOrderForInstant: Order[Instant] with Hash[Instant] = new InstantOrder
  implicit val intimeShowForInstant: Show[Instant]                      = Show.fromToString
}

class InstantOrder extends Order[Instant] with Hash[Instant] {
  override def compare(x: Instant, y: Instant): Int = x compareTo y
  override def hash(x: Instant): Int                = x.hashCode()
}
