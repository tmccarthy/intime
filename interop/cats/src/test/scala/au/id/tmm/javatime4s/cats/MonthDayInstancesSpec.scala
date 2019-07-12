package au.id.tmm.javatime4s.cats

import java.time.MonthDay

import au.id.tmm.javatime4s.scalacheck._
import cats.kernel.laws.discipline.{HashTests, OrderTests}
import cats.tests.CatsSuite

class MonthDayInstancesSpec extends CatsSuite {

  checkAll("monthDay", OrderTests[MonthDay].order)
  checkAll("monthDay", HashTests[MonthDay].hash)

}
