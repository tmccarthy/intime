package au.id.tmm.intime.cats.instances

import java.time.ZoneOffset

import au.id.tmm.intime.cats.instances.zoneOffset._
import au.id.tmm.intime.scalacheck._
import cats.kernel.laws.discipline.{HashTests, OrderTests}
import cats.tests.CatsSuite

class ZoneOffsetInstancesSpec extends CatsSuite {

  checkAll("zoneOffset", OrderTests[ZoneOffset].order)
  checkAll("zoneOffset", HashTests[ZoneOffset].hash)

}
