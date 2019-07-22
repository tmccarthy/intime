package au.id.tmm.intime.cats

import java.time.Duration

import au.id.tmm.intime.scalacheck._
import cats.kernel.laws.discipline.{CommutativeGroupTests, HashTests, OrderTests}
import cats.tests.CatsSuite

class DurationInstancesSpec extends CatsSuite {

  checkAll("duration", CommutativeGroupTests[Duration].commutativeGroup)
  checkAll("duration", OrderTests[Duration].order)
  checkAll("duration", HashTests[Duration].hash)

}
