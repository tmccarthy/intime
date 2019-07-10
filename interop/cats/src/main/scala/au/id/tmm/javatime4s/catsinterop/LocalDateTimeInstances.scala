package au.id.tmm.javatime4s.catsinterop

import java.time.LocalDateTime

import cats.{Hash, Order, Show}

trait LocalDateTimeInstances {
  implicit val catsKernelStdOrderForLocalDateTime: Order[LocalDateTime] with Hash[LocalDateTime] = new LocalDateTimeOrder
  implicit val catsKernelStdShowForDayOfWeek: Show[LocalDateTime] = Show.fromToString
}

class LocalDateTimeOrder extends Order[LocalDateTime] with Hash[LocalDateTime] {
  override def compare(x: LocalDateTime, y: LocalDateTime): Int = x compareTo y
  override def hash(x: LocalDateTime): Int = x.hashCode()
}

