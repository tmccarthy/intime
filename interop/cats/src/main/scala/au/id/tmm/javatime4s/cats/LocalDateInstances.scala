package au.id.tmm.javatime4s.cats

import java.time.LocalDate

import cats.{Hash, Order, Show}

trait LocalDateInstances {
  implicit val catsKernelStdOrderForLocalDate: Order[LocalDate] with Hash[LocalDate] = new LocalDateOrder
  implicit val catsKernelStdShowForLocalDate: Show[LocalDate]                        = Show.fromToString
}

class LocalDateOrder extends Order[LocalDate] with Hash[LocalDate] {
  override def compare(x: LocalDate, y: LocalDate): Int = x compareTo y
  override def hash(x: LocalDate): Int                  = x.hashCode()
}
