package au.id.tmm.intime.scalacheck

import java.time._

import org.scalacheck.Shrink
import org.scalatest.FlatSpec

class ShrinkInstancesSpec extends FlatSpec {

  behavior of "the shrink for Duration"

  behavior of "the shrink for Instant"

  behavior of "the shrink for Year"

  testShrink(Year.of(2019))(
    Year.of(1994),
    Year.of(1946),
    Year.of(1982),
    Year.of(1958),
    Year.of(1976),
    Year.of(1964),
    Year.of(1973),
    Year.of(1967),
    Year.of(1971),
    Year.of(1969),
    Year.of(1970),
  )

  behavior of "the shrink for Month"

  testShrink(Month.DECEMBER)(
    Month.NOVEMBER,
    Month.OCTOBER,
    Month.SEPTEMBER,
    Month.AUGUST,
    Month.JULY,
    Month.JUNE,
    Month.MAY,
    Month.APRIL,
    Month.MARCH,
    Month.FEBRUARY,
    Month.JANUARY,
  )

  testShrink(Month.MARCH)(
    Month.FEBRUARY,
    Month.JANUARY,
  )

  testShrink(Month.JANUARY)()

  behavior of "the shrink for YearMonth"

  behavior of "the shrink for LocalDate"

  behavior of "the shrink for LocalTime"

  behavior of "the shrink for LocalDateTime"

  behavior of "the shrink for MonthDay"

  behavior of "the shrink for ZoneOffset"

  behavior of "the shrink for OffsetDateTime"

  behavior of "the shrink for OffsetTime"

  behavior of "the shrink for ZonedDateTime"

  behavior of "the shrink for DayOfWeek"

  testShrink(DayOfWeek.SUNDAY)(
    DayOfWeek.SATURDAY,
    DayOfWeek.FRIDAY,
    DayOfWeek.THURSDAY,
    DayOfWeek.WEDNESDAY,
    DayOfWeek.TUESDAY,
    DayOfWeek.MONDAY,
  )

  testShrink(DayOfWeek.FRIDAY)(
    DayOfWeek.THURSDAY,
    DayOfWeek.WEDNESDAY,
    DayOfWeek.TUESDAY,
    DayOfWeek.MONDAY,
  )

  testShrink(DayOfWeek.MONDAY)(
    )

  behavior of "the shrink for Period"

  private def testShrink[A : Shrink](a: A)(expectedShrunkSeq: A*): Unit = {
    val expectedShrunk = expectedShrunkSeq.toVector

    it should s"shrink $a to $expectedShrunk" in {
      assert(implicitly[Shrink[A]].shrink(a).take(50).toVector === expectedShrunk)
    }
  }

}
