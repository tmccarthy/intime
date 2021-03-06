package au.id.tmm.intime.cats.instances

import java.time.Month

import au.id.tmm.intime.cats.instances.month._
import au.id.tmm.intime.scalacheck._
import cats.kernel.laws.discipline.{HashTests, OrderTests}
import cats.tests.CatsSuite

class MonthInstancesSpec extends CatsSuite {

  checkAll("month", OrderTests[Month].order)
  checkAll("month", HashTests[Month].hash)

}
