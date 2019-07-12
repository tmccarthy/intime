package au.id.tmm.javatime4s.cats

import java.time.OffsetTime

import au.id.tmm.javatime4s.scalacheck._
import cats.kernel.laws.discipline.{HashTests, OrderTests}
import cats.tests.CatsSuite

class OffsetTimeInstancesSpec extends CatsSuite {

  checkAll("offsetTime", OrderTests[OffsetTime].order)
  checkAll("offsetTime", HashTests[OffsetTime].hash)

}
