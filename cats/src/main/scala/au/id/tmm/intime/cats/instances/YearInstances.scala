package au.id.tmm.intime.cats.instances

import java.time.Year

import cats.{Hash, Order, Show}

trait YearInstances {
  implicit val intimeOrderForYear: Order[Year] with Hash[Year] = new YearOrder
  implicit val intimeShowForYear: Show[Year]                   = Show.fromToString
}

class YearOrder extends Order[Year] with Hash[Year] {
  override def compare(x: Year, y: Year): Int = x compareTo y
  override def hash(x: Year): Int             = x.hashCode()
}
