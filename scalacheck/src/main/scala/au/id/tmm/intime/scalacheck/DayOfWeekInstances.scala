package au.id.tmm.intime.scalacheck

import java.time.DayOfWeek

import au.id.tmm.intime.scalacheck.ShrinkUtils.shrinkEnum
import org.scalacheck.Gen.Choose
import org.scalacheck.{Arbitrary, Cogen, Gen, Shrink}

private[scalacheck] trait DayOfWeekInstances {

  val intimeGenForDayOfWeek: Gen[DayOfWeek] = Gen.oneOf(
    DayOfWeek.MONDAY,
    DayOfWeek.TUESDAY,
    DayOfWeek.WEDNESDAY,
    DayOfWeek.THURSDAY,
    DayOfWeek.FRIDAY,
    DayOfWeek.SATURDAY,
    DayOfWeek.SUNDAY,
  )

  implicit val intimeArbitraryForDayOfWeek: Arbitrary[DayOfWeek] = Arbitrary(intimeGenForDayOfWeek)

  implicit val intimeChooseForDayOfWeek: Choose[DayOfWeek] =
    Choose.xmap(DayOfWeek.of, _.getValue)

  implicit val intimeCogenForDayOfWeek: Cogen[DayOfWeek] =
    Cogen[Int].contramap(d => d.getValue)

  implicit val intimeShrinkForDayOfWeek: Shrink[DayOfWeek] = shrinkEnum(DayOfWeek.values)

}
