package au.id.tmm.intime.cats

import java.time.OffsetTime

import au.id.tmm.intime.scalacheck._
import cats.kernel.laws.discipline.{HashTests, OrderTests}
import cats.tests.CatsSuite

class OffsetTimeInstancesSpec extends CatsSuite {

  checkAll("offsetTime", OrderTests[OffsetTime].order)
  checkAll("offsetTime", HashTests[OffsetTime].hash)

}
