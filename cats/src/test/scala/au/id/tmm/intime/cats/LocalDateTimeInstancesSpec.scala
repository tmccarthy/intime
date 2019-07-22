package au.id.tmm.intime.cats

import java.time.LocalDateTime

import au.id.tmm.intime.scalacheck._
import cats.kernel.laws.discipline.{HashTests, OrderTests}
import cats.tests.CatsSuite

class LocalDateTimeInstancesSpec extends CatsSuite {

  checkAll("localDateTime", OrderTests[LocalDateTime].order)
  checkAll("localDateTime", HashTests[LocalDateTime].hash)

}
