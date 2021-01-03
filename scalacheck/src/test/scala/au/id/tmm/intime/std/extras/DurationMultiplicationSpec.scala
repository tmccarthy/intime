package au.id.tmm.intime.std.extras

import java.time.Duration

import au.id.tmm.intime.scalacheck.arbitraryDuration
import au.id.tmm.intime.std.extras.TestingGens._
import au.id.tmm.intime.std.syntax.duration._
import org.scalacheck.{Gen, Prop}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.scalacheck.{Checkers, ScalaCheckDrivenPropertyChecks}

class DurationMultiplicationSpec extends AnyFlatSpec with ScalaCheckDrivenPropertyChecks with Checkers {

  "multiplying a duration by a double" should "return the duration if the multiplicand is 1" in forAll { d: Duration =>
    assert(DurationMultiplication.multiply(d, 1) === d)
  }

  it should "return the negation if the multiplicand is -1" in forAll { d: Duration =>
    assert(DurationMultiplication.multiply(d, -1) === d.negated)
  }

  it should "return the zero duration if the multiplicand is 0" in forAll { d: Duration =>
    assert(DurationMultiplication.multiply(d, 0) === Duration.ZERO)
  }

  it should "work for (1 second) * 2" in {
    assert(DurationMultiplication.multiply(Duration.ofSeconds(1), 2) === Duration.ofSeconds(2))
  }

  it should "round down" in {
    assert(DurationMultiplication.multiply(Duration.ofNanos(1), 0.5d) === Duration.ZERO)
  }

  it can "overflow" in {
    intercept[ArithmeticException](DurationMultiplication.multiply(Duration.ofSeconds(Long.MaxValue), 2))
  }

  it should "throw if the multiplicand is NaN" in {
    intercept[ArithmeticException](DurationMultiplication.multiply(Duration.ZERO, Double.NaN))
  }

  "dividing a duration by a double" should "return the duration if the divisor is 1" in forAll { d: Duration =>
    assert(DurationMultiplication.divide(d, 1) === d)
  }

  it should "return the negation if the divisor is -1" in forAll { d: Duration =>
    assert(DurationMultiplication.divide(d, -1) === d.negated)
  }

  it should "throw if the divisor is 0" in forAll { d: Duration =>
    intercept[ArithmeticException](DurationMultiplication.divide(d, 0))
  }

  it should "work for (1 second) / 2" in {
    assert(DurationMultiplication.divide(Duration.ofSeconds(1), 2) === Duration.ofMillis(500))
  }

  it should "round down" in {
    assert(DurationMultiplication.divide(Duration.ofNanos(1), 2d) === Duration.ZERO)
  }

  it can "overflow" in {
    intercept[ArithmeticException](DurationMultiplication.divide(Duration.ofSeconds(Long.MaxValue), 0.5d))
  }

  it should "throw if the divisor is NaN" in {
    intercept[ArithmeticException](DurationMultiplication.divide(Duration.ZERO, Double.NaN))
  }

  "multiplication and division" should "roughly honour the round trip law" in
    check(
      Prop.forAllNoShrink(genSmallJavaDuration, Gen.choose[Double](1e-3d, 1e7d)) { (duration, k) =>
        val multiplied        = DurationMultiplication.multiply(duration, k)
        val roundTripDuration = DurationMultiplication.divide(multiplied, k)

        Prop((roundTripDuration - duration).abs < (duration.abs * 1e-12d))
      },
      minSuccessful(100000),
    )

  "the duration multiplication syntax" should "compile" in {
    assert(Duration.ZERO.multipliedBy(1d) === Duration.ZERO)
    assert((Duration.ZERO * 1d) === Duration.ZERO)
  }

}
