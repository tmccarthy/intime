package au.id.tmm.intime.cats.instances

import java.time.LocalDate

import au.id.tmm.intime.cats.instances.localDate._
import au.id.tmm.intime.scalacheck._
import cats.kernel.laws.discipline.{HashTests, OrderTests}
import cats.tests.CatsSuite

class LocalDateInstancesSpec extends CatsSuite {

  checkAll("localDate", OrderTests[LocalDate].order)
  checkAll("localDate", HashTests[LocalDate].hash)

}
