package au.id.tmm.javatime4s.cats

import java.time.DayOfWeek

import au.id.tmm.javatime4s.scalacheck._
import cats.kernel.laws.discipline.{HashTests, OrderTests}
import cats.tests.CatsSuite

class DayOfWeekInstancesSpec extends CatsSuite {

  checkAll("dayOfWeek", OrderTests[DayOfWeek].order)
  checkAll("dayOfWeek", HashTests[DayOfWeek].hash)

}
