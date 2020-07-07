package au.id.tmm.intime.cats.instances

import java.time.MonthDay

import cats.{Hash, Order, Show}

trait MonthDayInstances {
  implicit val intimeOrderForMonthDay: Order[MonthDay] with Hash[MonthDay] = new MonthDayOrder
  implicit val intimeShowForMonthDay: Show[MonthDay]                       = Show.fromToString
}

class MonthDayOrder extends Order[MonthDay] with Hash[MonthDay] {
  override def compare(x: MonthDay, y: MonthDay): Int = x compareTo y
  override def hash(x: MonthDay): Int                 = x.hashCode()
}
