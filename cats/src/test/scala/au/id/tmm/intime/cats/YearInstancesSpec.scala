package au.id.tmm.intime.cats

import java.time.Year

import au.id.tmm.intime.scalacheck._
import cats.kernel.laws.discipline.{HashTests, OrderTests}
import cats.tests.CatsSuite

class YearInstancesSpec extends CatsSuite {

  checkAll("year", OrderTests[Year].order)
  checkAll("year", HashTests[Year].hash)

}
