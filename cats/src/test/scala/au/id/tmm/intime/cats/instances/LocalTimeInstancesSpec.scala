package au.id.tmm.intime.cats.instances

import java.time.LocalTime

import au.id.tmm.intime.cats.instances.localTime._
import au.id.tmm.intime.scalacheck._
import cats.kernel.laws.discipline.{HashTests, OrderTests}
import cats.tests.CatsSuite

class LocalTimeInstancesSpec extends CatsSuite {

  checkAll("localTime", OrderTests[LocalTime].order)
  checkAll("localTime", HashTests[LocalTime].hash)

}
