package au.id.tmm.javatime4s.cats

import java.time.Year

import au.id.tmm.javatime4s.scalacheck._
import cats.kernel.laws.discipline.{HashTests, OrderTests}
import cats.tests.CatsSuite

class YearInstancesSpec extends CatsSuite {

  checkAll("year", OrderTests[Year].order)
  checkAll("year", HashTests[Year].hash)

}
