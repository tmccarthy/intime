package au.id.tmm.intime.cats

import java.time.Month

import au.id.tmm.intime.scalacheck._
import cats.kernel.laws.discipline.{HashTests, OrderTests}
import cats.tests.CatsSuite

class MonthInstancesSpec extends CatsSuite {

  checkAll("month", OrderTests[Month].order)
  checkAll("month", HashTests[Month].hash)

}
