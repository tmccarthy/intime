package au.id.tmm.javatime4s.cats

import java.time.LocalDate

import au.id.tmm.javatime4s.scalacheck._
import cats.kernel.laws.discipline.{HashTests, OrderTests}
import cats.tests.CatsSuite

class LocalDateInstancesSpec extends CatsSuite {

  checkAll("localDate", OrderTests[LocalDate].order)
  checkAll("localDate", HashTests[LocalDate].hash)

}
