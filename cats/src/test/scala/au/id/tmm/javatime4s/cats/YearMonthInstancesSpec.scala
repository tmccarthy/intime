package au.id.tmm.javatime4s.cats

import java.time.YearMonth

import au.id.tmm.javatime4s.scalacheck._
import cats.kernel.laws.discipline.{HashTests, OrderTests}
import cats.tests.CatsSuite

class YearMonthInstancesSpec extends CatsSuite {

  checkAll("yearMonth", OrderTests[YearMonth].order)
  checkAll("yearMonth", HashTests[YearMonth].hash)

}
