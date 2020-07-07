package au.id.tmm.intime.cats.instances

import java.time.MonthDay

import au.id.tmm.intime.cats.instances.monthDay._
import au.id.tmm.intime.scalacheck._
import cats.kernel.laws.discipline.{HashTests, OrderTests}
import cats.tests.CatsSuite

class MonthDayInstancesSpec extends CatsSuite {

  checkAll("monthDay", OrderTests[MonthDay].order)
  checkAll("monthDay", HashTests[MonthDay].hash)

}
