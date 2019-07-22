package au.id.tmm.intime.cats

import java.time.ZonedDateTime

import au.id.tmm.intime.scalacheck._
import cats.kernel.laws.discipline.{HashTests, OrderTests}
import cats.tests.CatsSuite

class ZonedDateTimeInstancesSpec extends CatsSuite {

  checkAll("zonedDateTime", OrderTests[ZonedDateTime].order)
  checkAll("zonedDateTime", HashTests[ZonedDateTime].hash)

}
