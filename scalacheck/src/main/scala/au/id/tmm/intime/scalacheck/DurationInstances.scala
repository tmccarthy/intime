package au.id.tmm.intime.scalacheck

import java.time.Duration
import java.time.temporal.ChronoField.NANO_OF_SECOND

import au.id.tmm.intime.scalacheck.ChooseUtils.combineChooses
import au.id.tmm.intime.scalacheck.ShrinkUtils.approachZero
import au.id.tmm.intime.scalacheck.TemporalGenUtils.genField
import au.id.tmm.intime.std.implicits.toDurationOps
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen.Choose
import org.scalacheck.{Arbitrary, Cogen, Gen, Shrink}

private[scalacheck] trait DurationInstances {

  val intimeGenForDuration: Gen[Duration] =
    for {
      seconds        <- arbitrary[Int] // This should probably be a Long, but it causes overflows
      nanoAdjustment <- genField(NANO_OF_SECOND)
    } yield Duration.ofSeconds(seconds, nanoAdjustment)

  implicit val intimeArbitraryForDuration: Arbitrary[Duration] = Arbitrary(intimeGenForDuration)

  implicit val durationCogen: Cogen[Duration] =
    Cogen[(Long, Int)].contramap(d => (d.getSeconds, d.getNano))

  implicit val intimeChooseForDuration: Choose[Duration] =
    combineChooses[Long, Long, Duration](
      _.getSeconds,
      _.getNano,
      Duration.ofSeconds,
      _ => 0L,
      _ => NANO_OF_SECOND.range().getMaximum,
    )

  implicit val intimeShrinkForDuration: Shrink[Duration] = approachZero(
    zero = Duration.ZERO,
  )(
    add = _ + _,
    divide = _ / _,
    negate = -_,
    isSmall = _ < Duration.ofMillis(500),
  )

}
