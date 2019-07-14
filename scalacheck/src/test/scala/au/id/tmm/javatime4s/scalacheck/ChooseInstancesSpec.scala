package au.id.tmm.javatime4s.scalacheck

import java.time.{Duration, Instant}

import au.id.tmm.javatime4s.syntax._
import org.scalacheck.Gen
import org.scalatest.FlatSpec
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class ChooseInstancesSpec extends FlatSpec with ScalaCheckDrivenPropertyChecks {

  import ChooseInstances._

  "durations generated within a range" should "fall within the specified range" in {
    val minMaxGenerator: Gen[(Duration, Duration)] = for {
      minSeconds <- Gen.choose[Long](-10, 10)
      minNanos   <- Gen.choose[Long](0, 999999999)

      maxSeconds <- Gen.choose[Long](minSeconds, 10)
      maxNanos   <- Gen.choose[Long](if (maxSeconds == minSeconds) minNanos else 0, 999999999)

      minDuration = Duration.ofSeconds(minSeconds, minNanos)
      maxDuration = Duration.ofSeconds(maxSeconds, maxNanos)
    } yield (minDuration, maxDuration)

    forAll(minMaxGenerator) { case (minDuration, maxDuration) =>
      val generatorUnderTest: Gen[Duration] = Gen.choose(minDuration, maxDuration)

      forAll(generatorUnderTest) { d =>
        assert(d >= minDuration && d <= maxDuration)
      }
    }
  }

  "instants generated within a range" should "fall within the specified range" in {
    val minMaxGenerator: Gen[(Instant, Instant)] = for {
      minSeconds <- Gen.choose[Long](-10, 10)
      minNanos   <- Gen.choose[Long](0, 999999999)

      maxSeconds <- Gen.choose[Long](minSeconds, 10)
      maxNanos   <- Gen.choose[Long](if (maxSeconds == minSeconds) minNanos else 0, 999999999)

      minInstant = Instant.ofEpochSecond(minSeconds, minNanos)
      maxInstant = Instant.ofEpochSecond(maxSeconds, maxNanos)
    } yield (minInstant, maxInstant)

    forAll(minMaxGenerator) { case (minInstant, maxInstant) =>
      val generatorUnderTest: Gen[Instant] = Gen.choose(minInstant, maxInstant)

      forAll(generatorUnderTest) { d =>
        assert(d >= minInstant && d <= maxInstant)
      }
    }
  }

}
