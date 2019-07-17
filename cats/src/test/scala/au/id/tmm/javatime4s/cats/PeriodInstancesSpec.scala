package au.id.tmm.javatime4s.cats

import java.time.Period

import au.id.tmm.javatime4s.scalacheck._
import cats.kernel.laws.discipline.{CommutativeGroupTests, HashTests, PartialOrderTests}
import cats.tests.CatsSuite

class PeriodInstancesSpec extends CatsSuite {

  checkAll("period", CommutativeGroupTests[Period].commutativeGroup)
  checkAll("period", PartialOrderTests[Period].partialOrder)
  checkAll("period", HashTests[Period].hash)

}
