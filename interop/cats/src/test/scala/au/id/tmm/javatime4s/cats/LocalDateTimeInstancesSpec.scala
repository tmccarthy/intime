package au.id.tmm.javatime4s.cats

import java.time.LocalDateTime

import au.id.tmm.javatime4s.scalacheck._
import cats.kernel.laws.discipline.{HashTests, OrderTests}
import cats.tests.CatsSuite

class LocalDateTimeInstancesSpec extends CatsSuite {

  checkAll("localDateTime", OrderTests[LocalDateTime].order)
  checkAll("localDateTime", HashTests[LocalDateTime].hash)

}
