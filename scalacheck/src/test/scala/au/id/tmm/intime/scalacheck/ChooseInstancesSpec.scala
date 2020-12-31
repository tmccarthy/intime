package au.id.tmm.intime.scalacheck

import java.time._

import au.id.tmm.intime.std.instances.all._
import org.scalacheck.Arbitrary
import org.scalacheck.Gen.Choose
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.reflect.ClassTag

class ChooseInstancesSpec extends AnyFlatSpec with ScalaCheckDrivenPropertyChecks {

  testChooseRange[Duration](chooseDuration)
  testChooseRange[Instant](chooseInstant)
  testChooseRange[Year](chooseYear)
  testChooseRange[Month](chooseMonth)
  testChooseRange[YearMonth](chooseYearMonth)
  testChooseRange[MonthDay](chooseMonthDay)
  testChooseRange[LocalDate](chooseLocalDate)
  testChooseRange[LocalTime](chooseLocalTime)
  testChooseRange[LocalDateTime](chooseLocalDateTime)
  testChooseRange[ZoneOffset](chooseZoneOffset)
  testChooseRange[DayOfWeek](chooseDayOfWeek)
  testChooseRange[ZonedDateTime](chooseZonedDateTime, hasOffset = true)
  testChooseRange[OffsetDateTime](chooseOffsetDateTime, hasOffset = true)
  testChooseRange[OffsetTime](chooseOffsetTime, hasOffset = true)

  def testChooseRange[A : Arbitrary : Ordering : ClassTag](choose: Choose[A], hasOffset: Boolean = false): Unit = {
    val className = implicitly[ClassTag[A]].runtimeClass.getSimpleName

    s"the choose for $className" should "generate instances within the specified range" in {
      forAll { pair: (A, A) =>
        val min = Ordering[A].min(pair._1, pair._2)
        val max = Ordering[A].max(pair._1, pair._2)

        val generator = choose.choose(min, max)

        forAll(generator) { a: A =>
          assert(Ordering[A].lteq(min, a) && Ordering[A].lteq(a, max))
        }
      }
    }

    if (!hasOffset) {
      it should "generate a value if the min and max are equivalent" in {
        forAll { a1: A =>
          val generator = choose.choose(a1, a1)

          forAll(generator) { a2: A =>
            assert(Ordering[A].equiv(a1, a2))
          }
        }
      }
    }
  }

  // TODO https://github.com/tmccarthy/intime/issues/12
  behavior of "the choose for ZonedDateTime"
  ignore should "function correctly when values are close to one another" in {
    val min = Instant.EPOCH.atZone(ZoneOffset.UTC)
    val max = min.plusMinutes(1)

    forAll(chooseZonedDateTime.choose(min, max), minSuccessful(10000)) { genValue: ZonedDateTime =>
      assert(Ordering[ZonedDateTime].lteq(min, genValue) && Ordering[ZonedDateTime].lteq(genValue, max))
    }
  }

  "the choose for OffsetDateTime" should "function correctly when values are close to one another" in {
    val min = Instant.EPOCH.atOffset(ZoneOffset.UTC)
    val max = min.plusMinutes(1)

    forAll(chooseOffsetDateTime.choose(min, max), minSuccessful(10000)) { genValue: OffsetDateTime =>
      assert(Ordering[OffsetDateTime].lteq(min, genValue) && Ordering[OffsetDateTime].lteq(genValue, max))
    }
  }

  "the choose for OffsetTime" should "function correctly when values are close to one another" in {
    val min = OffsetTime.of(LocalTime.NOON, ZoneOffset.UTC)
    val max = min.plusMinutes(1)

    forAll(chooseOffsetTime.choose(min, max), minSuccessful(10000)) { genValue: OffsetTime =>
      assert(Ordering[OffsetTime].lteq(min, genValue) && Ordering[OffsetTime].lteq(genValue, max))
    }
  }
}
