package au.id.tmm.intime.std.extras

import java.time.{Duration => JDuration}

import au.id.tmm.intime.scalacheck.{arbitraryDuration, chooseDuration}
import au.id.tmm.intime.std.extras.ScalaConcurrentDurationConversions._
import au.id.tmm.intime.std.extras.ScalaConcurrentDurationConversionsSpec._
import org.scalacheck.Gen
import org.scalatest.Assertion
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.concurrent.duration.{Duration => SDuration, FiniteDuration => SFiniteDuration}

class ScalaConcurrentDurationConversionsSpec extends AnyFlatSpec with ScalaCheckDrivenPropertyChecks {

  "Converting an SDuration to a JDuration" should "obey the round trip law" in forAll { sDuration: SDuration =>
    val jDurationOrError = sDurationToJDuration(sDuration)

    jDurationOrError match {
      case Right(jDuration) => {
        val resultSDuration = jDurationToSDuration(jDuration)
          .getOrElse(fail("Duration exceeded bounds on way back in round trip"))

        assert(resultSDuration === sDuration)
      }
      case Left(e: Errors.ScalaConcurrentDurationIsInfinite) =>
        assert(e.sDuration === sDuration, "A finite SDuration could not be converted to a JDuration")
    }
  }

  it should "fail for an infinite SDuration" in forAll(genInfiniteDuration) { sInfiniteDuration: SDuration.Infinite =>
    assert(sDurationToJDuration(sInfiniteDuration).left.map(_.sDuration) === Left(sInfiniteDuration))
  }

  it should "return Some for a finite SDuration" in forAll { sFiniteDuration: SFiniteDuration =>
    val jDuration = sDurationToJDuration(sFiniteDuration).getOrElse(fail)

    assertSameNanos(jDuration, sFiniteDuration)
  }

  "The total conversion from an SDuration to a JDuration" should "convert finite SDurations to a matching SDuration" in forAll {
    sFiniteDuration: SFiniteDuration =>
      assertSameNanos(sDurationToJDurationTotal(sFiniteDuration), sFiniteDuration)
  }

  it should "convert a positive infinite SDuration to the maximum JDuration" in {
    assert(sDurationToJDurationTotal(SDuration.Inf) === JDuration.ofSeconds(Long.MaxValue, NANOS_PER_SECOND - 1))
  }

  it should "convert an undefined SDuration to the maximum JDuration" in {
    assert(sDurationToJDurationTotal(SDuration.Undefined) === JDuration.ofSeconds(Long.MaxValue, NANOS_PER_SECOND - 1))
  }

  it should "convert a negative infinite SDuration to the minimum JDuration" in {
    assert(sDurationToJDurationTotal(SDuration.MinusInf) === JDuration.ofSeconds(Long.MinValue, 0))
  }

  "Converting an SFiniteDuration to a JDuration" should "obey the round trip law" in forAll {
    sFiniteDuration: SFiniteDuration =>
      assert(jDurationToSDuration(sFiniteDurationToJDuration(sFiniteDuration)) === Right(sFiniteDuration))
  }

  it should "be precise down to the nanosecond" in forAll { sFiniteDuration: SFiniteDuration =>
    assertSameNanos(sFiniteDurationToJDuration(sFiniteDuration), sFiniteDuration)
  }

  "Converting a JDuration to an SDuration" should "obey the round trip law" in forAll { jDuration: JDuration =>
    jDurationToSDuration(jDuration).map(sFiniteDurationToJDuration).foreach { resultJDuration =>
      assert(resultJDuration === jDuration)
    }
  }

  it should "fail if it exceeds the maximum SDuration" in forAll(genLargeJDuration) { largeJDuration =>
    assert(jDurationToSDuration(largeJDuration).left.map(_.jDuration) === Left(largeJDuration))
  }

  it should "be precise down to the nanosecond if it fits in an SDuration" in forAll(genSmallJDuration) {
    smallJDuration =>
      assertSameNanos(smallJDuration, jDurationToSDuration(smallJDuration).getOrElse(fail))
  }

  "The total conversion from a JDuration to an SDuration" should "convert small JDurations to a matching SFiniteDuration" in forAll(
    genSmallJDuration,
  ) { smallJDuration =>
    assertSameNanos(smallJDuration, jDurationToSDurationTotal(smallJDuration))
  }

  it should "convert large positive JDurations a positive infinite SDuration" in forAll(genLargePositiveJDuration) {
    largePositiveJDuration =>
      assert(jDurationToSDurationTotal(largePositiveJDuration) === SDuration.Inf)
  }

  it should "convert large negative JDurations to a negative infinite SDuration" in forAll(genLargeNegativeJDuration) {
    largeNegativeJDuration =>
      assert(jDurationToSDurationTotal(largeNegativeJDuration) === SDuration.MinusInf)
  }

}

object ScalaConcurrentDurationConversionsSpec {

  private val LARGEST_S_DURATION_AS_J_DURATION: JDuration =
    JDuration.ofSeconds(Long.MaxValue / NANOS_PER_SECOND, Long.MaxValue % NANOS_PER_SECOND)

  private val genSmallJDuration: Gen[JDuration] =
    Gen.choose[JDuration](LARGEST_S_DURATION_AS_J_DURATION.negated, LARGEST_S_DURATION_AS_J_DURATION)

  private val genLargePositiveJDuration: Gen[JDuration] =
    Gen.choose[JDuration](LARGEST_S_DURATION_AS_J_DURATION, JDuration.ofSeconds(Long.MaxValue, NANOS_PER_SECOND - 1))

  private val genLargeNegativeJDuration: Gen[JDuration] =
    Gen.choose[JDuration](JDuration.ofSeconds(Long.MinValue, 0), LARGEST_S_DURATION_AS_J_DURATION.negated)

  private val genLargeJDuration: Gen[JDuration] = Gen.oneOf(genLargePositiveJDuration, genLargeNegativeJDuration)

  private val genInfiniteDuration: Gen[SDuration.Infinite] =
    Gen.oneOf(SDuration.Undefined, SDuration.Inf, SDuration.MinusInf)

  private def assertSameNanos(jDuration: JDuration, sDuration: SDuration): Assertion = {
    if (jDuration.isNegative && sDuration.lt(SDuration.Zero)) return assertSameNanos(jDuration.negated, -sDuration)

    import org.scalatest.Assertions._

    val sDurationNanos   = sDuration.toNanos % NANOS_PER_SECOND
    val sDurationSeconds = sDuration.toNanos / NANOS_PER_SECOND

    assert(jDuration.getSeconds === sDurationSeconds)
    assert(jDuration.getNano === sDurationNanos)
  }

}
