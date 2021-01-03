package au.id.tmm.intime.std.extras

import java.time.{Duration => JDuration}

import au.id.tmm.intime.scalacheck.arbitraryDuration
import au.id.tmm.intime.std.NANOS_PER_SECOND
import au.id.tmm.intime.std.extras.ScalaConcurrentDurationConversions._
import au.id.tmm.intime.std.extras.ScalaConcurrentDurationConversionsSpec._
import au.id.tmm.intime.std.extras.TestingGens._
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatest.Assertion
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.concurrent.duration.{NANOSECONDS, Duration => SDuration, FiniteDuration => SFiniteDuration}

class ScalaConcurrentDurationConversionsSpec extends AnyFlatSpec with ScalaCheckDrivenPropertyChecks {

  "Converting an SDuration to a JDuration" should "obey the round trip law" in
    forAll(arbitrary[SDuration].filter(notMinimumDurationOnJava8)) { sDuration: SDuration =>
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

  // https://github.com/tmccarthy/intime/issues/16
  it should "obey the round trip law for the minimum SDuration" in {
    if (!isJava8) {
      val jDuration = sDurationToJDuration(MINIMUM_S_DURATION).getOrElse(fail)

      val roundTripSDuration = jDurationToSDuration(jDuration).getOrElse(fail)

      assert(
        roundTripSDuration === MINIMUM_S_DURATION,
        s"SDuration: $MINIMUM_S_DURATION\nJDuration: $jDuration\nRound trip SDuration: $roundTripSDuration",
      )
    }
  }

  it should "fail for an infinite SDuration" in forAll(genInfiniteSDuration) { sInfiniteDuration: SDuration.Infinite =>
    assert(sDurationToJDuration(sInfiniteDuration).left.map(_.sDuration) === Left(sInfiniteDuration))
  }

  it should "be precise down to the nanosecond for a finite SDuration" in forAll { sFiniteDuration: SFiniteDuration =>
    val jDuration = sDurationToJDuration(sFiniteDuration).getOrElse(fail)

    assertSameNanos(jDuration, sFiniteDuration)
  }

  // https://github.com/tmccarthy/intime/issues/16
  it should "be precise down to the nanosecond for the minimum SDuration" in {
    val jDuration = sDurationToJDuration(MINIMUM_S_DURATION).getOrElse(fail)

    assertSameNanos(jDuration, MINIMUM_S_DURATION)
  }

  "The total conversion from an SDuration to a JDuration" should "convert finite SDurations to a matching SDuration" in
    forAll { sFiniteDuration: SFiniteDuration =>
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

  "Converting an SFiniteDuration to a JDuration" should "obey the round trip law" in
    forAll(arbitrary[SFiniteDuration].filter(notMinimumDurationOnJava8)) { sFiniteDuration: SFiniteDuration =>
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

  it should "fail if it exceeds the maximum SDuration" in forAll(genLargeJavaDuration) { largeJDuration =>
    assert(jDurationToSDuration(largeJDuration).left.map(_.jDuration) === Left(largeJDuration))
  }

  it should "be precise down to the nanosecond if it fits in an SDuration" in forAll(genSmallJavaDuration) {
    smallJDuration =>
      assertSameNanos(smallJDuration, jDurationToSDuration(smallJDuration).getOrElse(fail))
  }

  "The total conversion from a JDuration to an SDuration" should "convert small JDurations to a matching SFiniteDuration" in forAll(
    genSmallJavaDuration,
  ) { smallJDuration =>
    assertSameNanos(smallJDuration, jDurationToSDurationTotal(smallJDuration))
  }

  it should "convert large positive JDurations a positive infinite SDuration" in forAll(genLargeJavaPositiveDuration) {
    largePositiveJDuration =>
      assert(jDurationToSDurationTotal(largePositiveJDuration) === SDuration.Inf)
  }

  it should "convert large negative JDurations to a negative infinite SDuration" in forAll(
    genLargeJavaNegativeDuration,
  ) { largeNegativeJDuration =>
    assert(jDurationToSDurationTotal(largeNegativeJDuration) === SDuration.MinusInf)
  }

  "The JDuration syntax" should "compile" in {
    import au.id.tmm.intime.std.syntax.duration._

    val jDuration: JDuration = JDuration.ZERO

    assert(jDuration.toScalaConcurrent === Right(SDuration.Zero))
    assert(jDuration.toScalaConcurrentTotal === SDuration.Zero)
    assert(jDuration.toScalaConcurrentUnsafe === SDuration.Zero)
  }

  "The SDuration syntax" should "compile for a finite duration" in {
    import au.id.tmm.intime.std.syntax.duration._

    val sFiniteDuration: SFiniteDuration = SDuration.Zero

    assert(sFiniteDuration.toJava === JDuration.ZERO)
    assert(sFiniteDuration.toJavaTotal === JDuration.ZERO)
    assert(sFiniteDuration.toJavaUnsafe === JDuration.ZERO)
  }

  it should "compile for a possibly infinite duration" in {
    import au.id.tmm.intime.std.syntax.duration._

    val sDuration: SDuration = SDuration.Zero

    assert(sDuration.toJava === Right(JDuration.ZERO))
    assert(sDuration.toJavaTotal === JDuration.ZERO)
    assert(sDuration.toJavaUnsafe === JDuration.ZERO)
  }

}

private object ScalaConcurrentDurationConversionsSpec {

  val MINIMUM_S_DURATION: SDuration = SDuration(-9223372036854775807L, NANOSECONDS)

  val genInfiniteSDuration: Gen[SDuration.Infinite] =
    Gen.oneOf(SDuration.Undefined, SDuration.Inf, SDuration.MinusInf)

  def assertSameNanos(jDuration: JDuration, sDuration: SDuration): Assertion = {
    if (jDuration.isNegative && sDuration.lt(SDuration.Zero)) return assertSameNanos(jDuration.negated, -sDuration)

    import org.scalatest.Assertions._

    val sDurationNanos   = sDuration.toNanos % NANOS_PER_SECOND
    val sDurationSeconds = sDuration.toNanos / NANOS_PER_SECOND

    assert(jDuration.getSeconds === sDurationSeconds)
    assert(jDuration.getNano === sDurationNanos)
  }

  /**
    * There is a bug in Java 8 where `JDuration.toNanos` throws when handling the minimum `SDuration`. We use this
    * predicate to ignore these cases in tests.
    */
  def notMinimumDurationOnJava8: SDuration => Boolean =
    if (isJava8) { sDuration =>
      sDuration != MINIMUM_S_DURATION
    } else { _ =>
      true
    }

  def isJava8: Boolean = System.getProperty("java.specification.version") == "1.8"

}
