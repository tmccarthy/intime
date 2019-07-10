package au.id.tmm.javatime4s.catsinterop

import java.time.OffsetTime

import cats.{Hash, Order, Show}

trait OffsetTimeInstances {
  implicit val catsKernelStdOrderForOffsetTime: Order[OffsetTime] with Hash[OffsetTime] = new OffsetTimeOrder
  implicit val catsKernelStdShowForDayOfWeek: Show[OffsetTime] = Show.fromToString
}

class OffsetTimeOrder extends Order[OffsetTime] with Hash[OffsetTime] {
  override def compare(x: OffsetTime, y: OffsetTime): Int = x compareTo y
  override def hash(x: OffsetTime): Int = x.hashCode()
}

