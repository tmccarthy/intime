package au.id.tmm.javatime4s.scalacheck

import java.time._

import org.scalacheck.Arbitrary

trait ArbitraryInstances {

  implicit val arbitraryDuration: Arbitrary[Duration]             = Arbitrary(genDuration)
  implicit val arbitraryInstant: Arbitrary[Instant]               = Arbitrary(genInstant)
  implicit val arbitraryYear: Arbitrary[Year]                     = Arbitrary(genYear)
  implicit val arbitraryMonth: Arbitrary[Month]                   = Arbitrary(genMonth)
  implicit val arbitraryYearMonth: Arbitrary[YearMonth]           = Arbitrary(genYearMonth)
  implicit val arbitraryLocalDate: Arbitrary[LocalDate]           = Arbitrary(genLocalDate)
  implicit val arbitraryLocalTime: Arbitrary[LocalTime]           = Arbitrary(genLocalTime)
  implicit val arbitraryLocalDateTime: Arbitrary[LocalDateTime]   = Arbitrary(genLocalDateTime)
  implicit val arbitraryMonthDay: Arbitrary[MonthDay]             = Arbitrary(genMonthDay)
  implicit val arbitraryZoneOffset: Arbitrary[ZoneOffset]         = Arbitrary(genZoneOffset)
  implicit val arbitraryZoneId: Arbitrary[ZoneId]                 = Arbitrary(genZoneId)
  implicit val arbitraryOffsetDateTime: Arbitrary[OffsetDateTime] = Arbitrary(genOffsetDateTime)
  implicit val arbitraryOffsetTime: Arbitrary[OffsetTime]         = Arbitrary(genOffsetTime)
  implicit val arbitraryZonedDateTime: Arbitrary[ZonedDateTime]   = Arbitrary(genZonedDateTime)
  implicit val arbitraryDayOfWeek: Arbitrary[DayOfWeek]           = Arbitrary(genDayOfWeek)
  implicit val arbitraryPeriod: Arbitrary[Period]                 = Arbitrary(genPeriod)

}
