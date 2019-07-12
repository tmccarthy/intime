package au.id.tmm.javatime4s.cats

import java.time.DayOfWeek

import cats.{Hash, Order, Show}

trait DayOfWeekInstances {
  implicit val catsKernelStdOrderForDayOfWeek: Order[DayOfWeek] with Hash[DayOfWeek] = new DayOfWeekOrder
  implicit val catsKernelStdShowForDayOfWeek: Show[DayOfWeek]                        = Show.fromToString
}

class DayOfWeekOrder extends Order[DayOfWeek] with Hash[DayOfWeek] {
  override def compare(x: DayOfWeek, y: DayOfWeek): Int = x compareTo y
  override def hash(x: DayOfWeek): Int                  = x.hashCode()
}
