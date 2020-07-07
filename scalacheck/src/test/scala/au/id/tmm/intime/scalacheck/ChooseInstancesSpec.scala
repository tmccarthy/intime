package au.id.tmm.intime.scalacheck

import java.time._

import au.id.tmm.intime.orderings._
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

}
