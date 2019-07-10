package au.id.tmm.javatime4s.catsinterop

import java.time.MonthDay

import cats.{Hash, Order, Show}

trait MonthDayInstances {
  implicit val catsKernelStdOrderForMonthDay: Order[MonthDay] with Hash[MonthDay] = new MonthDayOrder
  implicit val catsKernelStdShowForDayOfWeek: Show[MonthDay] = Show.fromToString
}

class MonthDayOrder extends Order[MonthDay] with Hash[MonthDay] {
  override def compare(x: MonthDay, y: MonthDay): Int = x compareTo y
  override def hash(x: MonthDay): Int = x.hashCode()
}

