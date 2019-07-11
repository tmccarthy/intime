package au.id.tmm.javatime4s.cats

import java.time.Duration

import au.id.tmm.javatime4s.scalacheck._
import cats.kernel.laws.discipline.CommutativeGroupTests
import cats.tests.CatsSuite

class DurationInstancesSpec extends CatsSuite {

  checkAll("duration", CommutativeGroupTests[Duration].commutativeGroup)

}
