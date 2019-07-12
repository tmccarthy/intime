package au.id.tmm.javatime4s.cats

import java.time.Duration

import au.id.tmm.javatime4s.scalacheck._
import cats.kernel.laws.discipline.{CommutativeGroupTests, HashTests, OrderTests}
import cats.tests.CatsSuite

class DurationInstancesSpec extends CatsSuite {

  checkAll("duration", CommutativeGroupTests[Duration].commutativeGroup)
  checkAll("duration", OrderTests[Duration].order)
  checkAll("duration", HashTests[Duration].hash)

}
