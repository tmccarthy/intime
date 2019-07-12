package au.id.tmm.javatime4s.cats

import java.time.Instant

import au.id.tmm.javatime4s.scalacheck._
import cats.kernel.laws.discipline.{HashTests, OrderTests}
import cats.tests.CatsSuite

class InstantInstancesSpec extends CatsSuite {

  checkAll("instant", OrderTests[Instant].order)
  checkAll("instant", HashTests[Instant].hash)

}
