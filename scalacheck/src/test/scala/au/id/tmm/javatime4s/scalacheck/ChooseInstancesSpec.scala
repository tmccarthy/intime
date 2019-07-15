package au.id.tmm.javatime4s.scalacheck

import java.time._

import au.id.tmm.javatime4s.instances._
import org.scalacheck.Arbitrary
import org.scalacheck.Gen.Choose
import org.scalatest.FlatSpec
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.reflect.ClassTag

class ChooseInstancesSpec extends FlatSpec with ScalaCheckDrivenPropertyChecks {

  import ChooseInstances._

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

  // TODO test for ZoneId
  // TODO test for Period

  def testChooseRange[A : Arbitrary : Ordering : ClassTag](choose: Choose[A]): Unit = {
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
  }

}
