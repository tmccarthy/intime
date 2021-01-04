package au.id.tmm.intime.cats.instances

import java.time.Duration

import au.id.tmm.intime.cats.instances.duration._
import au.id.tmm.intime.scalacheck.duration._
import cats.kernel.laws.discipline.{CommutativeGroupTests, HashTests, OrderTests}
import cats.tests.CatsSuite

class DurationInstancesSpec extends CatsSuite {

  checkAll("duration", CommutativeGroupTests[Duration].commutativeGroup)
  checkAll("duration", OrderTests[Duration].order)
  checkAll("duration", HashTests[Duration].hash)

}
