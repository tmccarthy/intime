package au.id.tmm.javatime4s.scalacheck

import java.time._
import java.time.temporal.ChronoField._

import au.id.tmm.javatime4s.scalacheck.TemporalGenUtils._
import org.scalacheck.Arbitrary._
import org.scalacheck.{Arbitrary, Gen}

trait ArbitraryInstances {

  private val minInstantSeconds = -31557014167219200L
  private val maxInstantSeconds = 31556889864403199L

  private val genDuration: Gen[Duration] =
    for {
      seconds        <- arbitrary[Int] // This should probably be a Long, but it causes overflows
      nanoAdjustment <- genField(NANO_OF_SECOND)
    } yield Duration.ofSeconds(seconds, nanoAdjustment)

  private val genInstant: Gen[Instant] =
    for {
      seconds        <- Gen.choose(minInstantSeconds, maxInstantSeconds)
      nanoAdjustment <- genField(NANO_OF_SECOND)
    } yield Instant.ofEpochSecond(seconds, nanoAdjustment)

  private val genYear: Gen[Year] = genIntField(YEAR).map(Year.of)

  private val genMonth: Gen[Month] = Gen.oneOf(Month.values.toIndexedSeq)

  private val genYearMonth: Gen[YearMonth] =
    for {
      year  <- genYear
      month <- genMonth
    } yield YearMonth.of(year.getValue, month)

  private val genLocalDate: Gen[LocalDate] =
    for {
      yearMonth <- genYearMonth
      day       <- genIntFieldAt(DAY_OF_MONTH, yearMonth.atDay(1))
    } yield LocalDate.of(yearMonth.getYear, yearMonth.getMonth, day)

  private val genLocalTime: Gen[LocalTime] =
    for {
      hour         <- genIntField(HOUR_OF_DAY)
      minute       <- genIntField(MINUTE_OF_HOUR)
      second       <- genIntField(SECOND_OF_MINUTE)
      nanoOfSecond <- genIntField(NANO_OF_SECOND)
    } yield LocalTime.of(hour, minute, second, nanoOfSecond)

  private val genLocalDateTime: Gen[LocalDateTime] =
    for {
      localTime <- genLocalTime
      localDate <- genLocalDate
    } yield LocalDateTime.of(localDate, localTime)

  private val genMonthDay: Gen[MonthDay] =
    for {
      month <- genMonth
      day   <- Gen.choose(1, month.maxLength())
    } yield MonthDay.of(month, day)

  private val genZoneOffset: Gen[ZoneOffset] =
    genIntField(OFFSET_SECONDS).map(ZoneOffset.ofTotalSeconds)

  private val genZoneId: Gen[ZoneId] = {
    val genNamedZoneId = Gen.oneOf {
      // This weird way of converting from the java.util.Set to a Scala iterable is because there is
      // no way of using the Scala converters across both 2.12 and 2.13 without hitting either a
      // compiler error or a deprecation warning in one or the other version.
      ZoneId.getAvailableZoneIds.stream().toArray(new Array[String](_)).toIndexedSeq
    }.map(ZoneId.of)

    val genOffsetZoneId = genZoneOffset.map(_.normalized())

    Gen.oneOf(genNamedZoneId, genOffsetZoneId)
  }

  private val genOffsetDateTime: Gen[OffsetDateTime] =
    for {
      localDateTime <- genLocalDateTime
      offset        <- genZoneOffset
    } yield OffsetDateTime.of(localDateTime, offset)

  private val genOffsetTime: Gen[OffsetTime] =
    for {
      localTime <- genLocalTime
      offset    <- genZoneOffset
    } yield OffsetTime.of(localTime, offset)

  private val genZonedDateTime: Gen[ZonedDateTime] =
    for {
      localDateTime <- genLocalDateTime
      zoneId        <- genZoneId
    } yield ZonedDateTime.of(localDateTime, zoneId)

  private val genDayOfWeek: Gen[DayOfWeek] = Gen.oneOf(
    DayOfWeek.MONDAY,
    DayOfWeek.TUESDAY,
    DayOfWeek.WEDNESDAY,
    DayOfWeek.THURSDAY,
    DayOfWeek.FRIDAY,
    DayOfWeek.SATURDAY,
    DayOfWeek.SUNDAY,
  )

  private val genPeriod: Gen[Period] =
    for {
      // These should be arbitrary Int values, but if you do that all your tests will overflow,
      // so we use short instead
      years  <- arbitrary[Short]
      months <- arbitrary[Short]
      days   <- arbitrary[Short]
    } yield Period.of(years, months, days)

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

object ArbitraryInstances extends ArbitraryInstances
