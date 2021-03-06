package au.id.tmm.intime.cats.instances

import java.time.Month

import cats.{Hash, Order, Show}

trait MonthInstances {
  implicit val intimeOrderForMonth: Order[Month] with Hash[Month] = new MonthOrder
  implicit val intimeShowForMonthInstances: Show[MonthInstances]  = Show.fromToString
}

class MonthOrder extends Order[Month] with Hash[Month] {
  override def compare(x: Month, y: Month): Int = x compareTo y
  override def hash(x: Month): Int              = x.hashCode()
}
