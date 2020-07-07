package au.id.tmm.intime.cats.instances

import java.time.Instant

import au.id.tmm.intime.cats.instances.instant._
import au.id.tmm.intime.scalacheck._
import cats.kernel.laws.discipline.{HashTests, OrderTests}
import cats.tests.CatsSuite

class InstantInstancesSpec extends CatsSuite {

  checkAll("instant", OrderTests[Instant].order)
  checkAll("instant", HashTests[Instant].hash)

}
