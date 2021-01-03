package au.id.tmm.intime.scalacheck

import java.time.temporal.ChronoField.YEAR
import java.time.{Instant, Year, ZoneOffset}

import au.id.tmm.intime.scalacheck.TemporalGenUtils.genIntField
import org.scalacheck.Gen.Choose
import org.scalacheck.{Arbitrary, Cogen, Gen, Shrink}

private[scalacheck] trait YearInstances {

  val intimeGenForYear: Gen[Year] = genIntField(YEAR).map(Year.of)

  implicit val intimeArbitraryForYear: Arbitrary[Year] = Arbitrary(intimeGenForYear)

  implicit val intimeCogenForYear: Cogen[Year] =
    Cogen[Int].contramap(_.getValue)

  implicit val intimeShrinkForYear: Shrink[Year] = {
    val epoch = Year.from(Instant.EPOCH.atZone(ZoneOffset.UTC))

    Shrink.xmap[Int, Year](epoch.plusYears(_), y => y.minusYears(epoch.getValue).getValue)
  }

  implicit val intimeChooseForYear: Choose[Year] =
    Choose.xmap[Int, Year](Year.of, _.getValue)

}
