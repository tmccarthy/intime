package au.id.tmm.intime.std.extras

import java.time.temporal.ChronoField
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
    val maybeJDuration: Option[JDuration] = sDurationToJDuration(sDuration)

    maybeJDuration match {
      case Some(jDuration) => {
        val resultSDuration = jDurationToSDuration(jDuration)
          .getOrElse(fail("Duration exceeded bounds on way back in round trip"))

        assert(resultSDuration === sDuration)
      }
      case None => assert(!sDuration.isFinite, "A finite SDuration could not be converted to a JDuration")
    }
  }

  it should "return None for an infinite SDuration" in {
    val genInfiniteDuration: Gen[SDuration.Infinite] = Gen.oneOf(SDuration.Undefined, SDuration.Inf, SDuration.MinusInf)

    forAll(genInfiniteDuration) { sInfiniteDuration: SDuration.Infinite =>
      assert(sDurationToJDuration(sInfiniteDuration) === None)
    }
  }

  it should "return Some for a finite SDuration" in forAll { sFiniteDuration: SFiniteDuration =>
    val jDuration = sDurationToJDuration(sFiniteDuration).getOrElse(fail)

    assertSameNanos(jDuration, sFiniteDuration)
  }

  "Converting an SFiniteDuration to a JDuration" should "obey the round trip law" in forAll { sFiniteDuration: SFiniteDuration =>
    assert(jDurationToSDuration(sFiniteDurationToJDuration(sFiniteDuration)) === sFiniteDuration)
  }

  it should "be precise down to the nanosecond" in forAll { sFiniteDuration: SFiniteDuration =>
    assertSameNanos(sFiniteDurationToJDuration(sFiniteDuration), sFiniteDuration)
  }

  "Converting a JDuration to an SDuration" should "obey the round trip law" in forAll { jDuration: JDuration =>
    jDurationToSDuration(jDuration).map(sFiniteDurationToJDuration).foreach { resultJDuration =>
      assert(resultJDuration === jDuration)
    }
  }

  it should "return None if it exceeds the maximum SDuration" in {
    val genLargePositiveJDuration = Gen.choose[JDuration](LARGEST_S_DURATION_AS_J_DURATION, JDuration.ofSeconds(Long.MaxValue, NANOS_PER_SECOND - 1))
    val genLargeNegativeJDuration = Gen.choose[JDuration](JDuration.ofSeconds(Long.MinValue, 0), LARGEST_S_DURATION_AS_J_DURATION.negated)

    val genLargeJDuration = Gen.oneOf(genLargePositiveJDuration, genLargeNegativeJDuration)

    forAll(genLargeJDuration) { largeJDuration =>
      assert(jDurationToSDuration(largeJDuration) === None)
    }
  }

  it should "be precise down to the nanosecond if it fits in an SDuration" in {
    val genSmallJDuration = Gen.choose[JDuration](LARGEST_S_DURATION_AS_J_DURATION.negated, LARGEST_S_DURATION_AS_J_DURATION)

    forAll(genSmallJDuration) { smallJDuration =>
      assertSameNanos(smallJDuration, jDurationToSDuration(smallJDuration).getOrElse(fail))
    }
  }

}

object ScalaConcurrentDurationConversionsSpec {

  private val NANOS_PER_SECOND: Long = ChronoField.NANO_OF_SECOND.range().getMaximum + 1

  private val LARGEST_S_DURATION_AS_J_DURATION: JDuration =
    JDuration.ofSeconds(Long.MaxValue / NANOS_PER_SECOND, Long.MaxValue % NANOS_PER_SECOND)

  private def assertSameNanos(jDuration: JDuration, sDuration: SDuration): Assertion = {
    if (jDuration.isNegative && sDuration.lt(SDuration.Zero)) return assertSameNanos(jDuration.negated, - sDuration)

    import org.scalatest.Assertions._

    val sDurationNanos = sDuration.toNanos % NANOS_PER_SECOND
    val sDurationSeconds = sDuration.toNanos / NANOS_PER_SECOND

    assert(jDuration.getSeconds === sDurationSeconds)
    assert(jDuration.getNano === sDurationNanos)
  }

}
