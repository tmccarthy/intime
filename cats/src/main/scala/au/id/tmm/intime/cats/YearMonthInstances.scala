package au.id.tmm.intime.cats

import java.time.YearMonth

import cats.{Hash, Order, Show}

trait YearMonthInstances {
  implicit val catsKernelStdOrderForYearMonth: Order[YearMonth] with Hash[YearMonth] = new YearMonthOrder
  implicit val catsKernelStdShowForYearMonth: Show[YearMonth]                        = Show.fromToString
}

class YearMonthOrder extends Order[YearMonth] with Hash[YearMonth] {
  override def compare(x: YearMonth, y: YearMonth): Int = x compareTo y
  override def hash(x: YearMonth): Int                  = x.hashCode()
}