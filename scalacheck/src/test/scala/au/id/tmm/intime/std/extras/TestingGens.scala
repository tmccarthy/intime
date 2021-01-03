package au.id.tmm.intime.std.extras

import java.time.Duration

import au.id.tmm.intime.scalacheck.chooseDuration
import au.id.tmm.intime.std.NANOS_PER_SECOND
import org.scalacheck.Gen

// TODO should these be in the main project?
private[extras] object TestingGens {

  val LARGEST_S_DURATION_AS_J_DURATION: Duration =
    Duration.ofSeconds(Long.MaxValue / NANOS_PER_SECOND, Long.MaxValue % NANOS_PER_SECOND)

  val genSmallJavaDuration: Gen[Duration] =
    Gen.choose[Duration](LARGEST_S_DURATION_AS_J_DURATION.negated, LARGEST_S_DURATION_AS_J_DURATION)

  val genLargeJavaPositiveDuration: Gen[Duration] =
    Gen.choose[Duration](LARGEST_S_DURATION_AS_J_DURATION, Duration.ofSeconds(Long.MaxValue, NANOS_PER_SECOND - 1))

  val genLargeJavaNegativeDuration: Gen[Duration] =
    Gen.choose[Duration](Duration.ofSeconds(Long.MinValue, 0), LARGEST_S_DURATION_AS_J_DURATION.negated)

  val genLargeJavaDuration: Gen[Duration] = Gen.oneOf(genLargeJavaPositiveDuration, genLargeJavaNegativeDuration)

}
