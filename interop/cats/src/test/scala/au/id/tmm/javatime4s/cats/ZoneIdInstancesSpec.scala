package au.id.tmm.javatime4s.cats

import java.time.ZoneId

import au.id.tmm.javatime4s.scalacheck._
import cats.kernel.laws.discipline.HashTests
import cats.tests.CatsSuite

class ZoneIdInstancesSpec extends CatsSuite {

  checkAll("zoneId", HashTests[ZoneId].hash)

}
