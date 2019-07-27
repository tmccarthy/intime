package au.id.tmm.intime.cats

import java.time.ZoneId

import au.id.tmm.intime.scalacheck._
import cats.kernel.laws.discipline.HashTests
import cats.tests.CatsSuite

class ZoneIdInstancesSpec extends CatsSuite {

  checkAll("zoneId", HashTests[ZoneId].hash)

}
