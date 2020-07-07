package au.id.tmm.intime.cats.instances

import java.time.OffsetDateTime

import au.id.tmm.intime.cats.instances.offsetDateTime._
import au.id.tmm.intime.scalacheck._
import cats.kernel.laws.discipline.{HashTests, OrderTests}
import cats.tests.CatsSuite

class OffsetDateTimeInstancesSpec extends CatsSuite {

  checkAll("offsetDateTime", OrderTests[OffsetDateTime].order)
  checkAll("offsetDateTime", HashTests[OffsetDateTime].hash)

}
