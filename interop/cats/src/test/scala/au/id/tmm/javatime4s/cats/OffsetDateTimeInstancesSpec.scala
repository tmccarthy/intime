package au.id.tmm.javatime4s.cats

import java.time.OffsetDateTime

import au.id.tmm.javatime4s.scalacheck._
import cats.kernel.laws.discipline.{HashTests, OrderTests}
import cats.tests.CatsSuite

class OffsetDateTimeInstancesSpec extends CatsSuite {

  checkAll("offsetDateTime", OrderTests[OffsetDateTime].order)
  checkAll("offsetDateTime", HashTests[OffsetDateTime].hash)

}
