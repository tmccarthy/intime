package au.id.tmm.javatime4s.cats

import java.time.ZoneOffset

import au.id.tmm.javatime4s.scalacheck._
import cats.kernel.laws.discipline.{HashTests, OrderTests}
import cats.tests.CatsSuite

class ZoneOffsetInstancesSpec extends CatsSuite {

  checkAll("zoneOffset", OrderTests[ZoneOffset].order)
  checkAll("zoneOffset", HashTests[ZoneOffset].hash)

}
