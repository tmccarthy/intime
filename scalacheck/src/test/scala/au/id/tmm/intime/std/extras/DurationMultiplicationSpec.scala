package au.id.tmm.intime.std.extras

import java.time.Duration

import au.id.tmm.intime.scalacheck.arbitraryDuration
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class DurationMultiplicationSpec extends AnyFlatSpec with ScalaCheckDrivenPropertyChecks {

  "multiplying a duration by a double" should "return the duration if the multiplicand is 1" in forAll { d: Duration =>
    assert(DurationMultiplication.multiply(d, 1) === d)
  }

  it should "return the negation if the multiplicand is -1" in forAll { d: Duration =>
    assert(DurationMultiplication.multiply(d, -1) === d.negated)
  }

  it should "return the zero duration if the mulitplicand is 0" in forAll { d: Duration =>
    assert(DurationMultiplication.multiply(d, 0) === Duration.ZERO)
  }

  it should "work for a (1 second) * 2" in {
    assert(DurationMultiplication.multiply(Duration.ofSeconds(1), 2) === Duration.ofSeconds(2))
  }

  it should "round down" in {
    assert(DurationMultiplication.multiply(Duration.ofNanos(1), 0.5d) === Duration.ZERO)
  }

  it can "overflow" in {
    intercept[ArithmeticException](DurationMultiplication.multiply(Duration.ofSeconds(Long.MaxValue), 2))
  }

}
