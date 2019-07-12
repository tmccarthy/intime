package au.id.tmm.javatime4s.cats

import java.time.LocalTime

import au.id.tmm.javatime4s.scalacheck._
import cats.kernel.laws.discipline.{HashTests, OrderTests}
import cats.tests.CatsSuite

class LocalTimeInstancesSpec extends CatsSuite {

  checkAll("localTime", OrderTests[LocalTime].order)
  checkAll("localTime", HashTests[LocalTime].hash)

}
