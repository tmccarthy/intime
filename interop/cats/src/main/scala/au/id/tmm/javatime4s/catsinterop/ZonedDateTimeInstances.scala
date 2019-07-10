package au.id.tmm.javatime4s.catsinterop

import java.time.ZonedDateTime

import cats.{Hash, Order, Show}

trait ZonedDateTimeInstances {
  implicit val catsKernelStdOrderForZonedDateTime: Order[ZonedDateTime] with Hash[ZonedDateTime] = new ZonedDateTimeOrder
  implicit val catsKernelStdShowForDayOfWeek: Show[ZonedDateTime] = Show.fromToString
}

class ZonedDateTimeOrder extends Order[ZonedDateTime] with Hash[ZonedDateTime] {
  override def compare(x: ZonedDateTime, y: ZonedDateTime): Int = x compareTo y
  override def hash(x: ZonedDateTime): Int = x.hashCode()
}

