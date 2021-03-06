package au.id.tmm.intime.cats.instances

import java.time.Duration

import cats.kernel.CommutativeGroup
import cats.{Hash, Order, Show}

trait DurationInstances {
  implicit val intimeOrderForJavaTimeDuration: Order[Duration] with Hash[Duration] = new DurationOrder
  implicit val intimeGroupForJavaTimeDuration: CommutativeGroup[Duration]          = new DurationGroup
  implicit val intimeShowForJavaTimeDuration: Show[Duration]                       = Show.fromToString
}

class DurationOrder extends Order[Duration] with Hash[Duration] {
  override def compare(x: Duration, y: Duration): Int = x compareTo y
  override def hash(x: Duration): Int                 = x.hashCode()
}

class DurationGroup extends CommutativeGroup[Duration] {
  override def inverse(a: Duration): Duration              = a.negated()
  override def empty: Duration                             = Duration.ZERO
  override def combine(x: Duration, y: Duration): Duration = x plus y
}
