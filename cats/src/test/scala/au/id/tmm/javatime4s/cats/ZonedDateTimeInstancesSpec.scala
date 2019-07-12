package au.id.tmm.javatime4s.cats

import java.time.ZonedDateTime

import au.id.tmm.javatime4s.scalacheck._
import cats.kernel.laws.discipline.{HashTests, OrderTests}
import cats.tests.CatsSuite

class ZonedDateTimeInstancesSpec extends CatsSuite {

  checkAll("zonedDateTime", OrderTests[ZonedDateTime].order)
  checkAll("zonedDateTime", HashTests[ZonedDateTime].hash)

}
