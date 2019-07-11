package au.id.tmm.javatime4s.scalacheck

import java.time._
import java.time.temporal.ChronoField._
import java.time.temporal.{TemporalAccessor, TemporalField, ValueRange => TemporalValueRange}

import au.id.tmm.javatime4s.scalacheck.Generators._
import org.scalacheck.Arbitrary._
import org.scalacheck.Gen

import scala.collection.convert.AsScalaConverters

trait Generators {

  implicit val genDuration: Gen[Duration] =
    for {
      seconds        <- arbitrary[Long]
      nanoAdjustment <- genField(NANO_OF_SECOND)
    } yield Duration.ofSeconds(seconds, nanoAdjustment)

  implicit val genInstant: Gen[Instant] =
    for {
      seconds        <- Gen.choose(minInstantSeconds, maxInstantSeconds)
      nanoAdjustment <- genField(NANO_OF_SECOND)
    } yield Instant.ofEpochSecond(seconds, nanoAdjustment)

  implicit val genYear: Gen[Year] = genIntField(YEAR).map(Year.of)

  implicit val genMonth: Gen[Month] = Gen.oneOf(Month.values.toIndexedSeq)

  implicit val genYearMonth: Gen[YearMonth] =
    for {
      year  <- genYear
      month <- genMonth
    } yield YearMonth.of(year.getValue, month)

  implicit val genLocalDate: Gen[LocalDate] =
    for {
      yearMonth <- genYearMonth
      day       <- genIntFieldAt(DAY_OF_MONTH, yearMonth.atDay(1))
    } yield LocalDate.of(yearMonth.getYear, yearMonth.getMonth, day)

  implicit val genLocalTime: Gen[LocalTime] =
    for {
      hour         <- genIntField(HOUR_OF_DAY)
      minute       <- genIntField(MINUTE_OF_HOUR)
      second       <- genIntField(SECOND_OF_MINUTE)
      nanoOfSecond <- genIntField(NANO_OF_SECOND)
    } yield LocalTime.of(hour, minute, second, nanoOfSecond)

  implicit val genLocalDateTime: Gen[LocalDateTime] =
    for {
      localTime <- genLocalTime
      localDate <- genLocalDate
    } yield LocalDateTime.of(localDate, localTime)

  implicit val genMonthDay: Gen[MonthDay] =
    for {
      month <- genMonth
      day   <- Gen.choose(1, month.maxLength())
    } yield MonthDay.of(month, day)

  implicit val genZoneOffset: Gen[ZoneOffset] =
    genIntField(OFFSET_SECONDS).map(ZoneOffset.ofTotalSeconds)

  implicit val genZoneId: Gen[ZoneId] =
    Gen.oneOf(new AsScalaConverters {}.asScala(ZoneId.getAvailableZoneIds).toSeq).map(ZoneId.of)

  implicit val genOffsetDateTime: Gen[OffsetDateTime] =
    for {
      localDateTime <- genLocalDateTime
      offset        <- genZoneOffset
    } yield OffsetDateTime.of(localDateTime, offset)

  implicit val genOffsetTime: Gen[OffsetTime] =
    for {
      localTime <- genLocalTime
      offset    <- genZoneOffset
    } yield OffsetTime.of(localTime, offset)

  implicit val genZonedDateTime: Gen[ZonedDateTime] =
    for {
      localDateTime <- genLocalDateTime
      zoneId        <- genZoneId
    } yield ZonedDateTime.of(localDateTime, zoneId)

  implicit val genDayOfWeek: Gen[DayOfWeek] = Gen.oneOf(
    DayOfWeek.MONDAY,
    DayOfWeek.TUESDAY,
    DayOfWeek.WEDNESDAY,
    DayOfWeek.THURSDAY,
    DayOfWeek.FRIDAY,
    DayOfWeek.SATURDAY,
    DayOfWeek.SUNDAY
  )

  implicit val genPeriod: Gen[Period] =
    for {
      years  <- arbitrary[Int]
      months <- arbitrary[Int]
      days   <- arbitrary[Int]
    } yield Period.of(years, months, days)

}

object Generators {
  private val minInstantSeconds = -31557014167219200L
  private val maxInstantSeconds = 31556889864403199L

  private def genField(temporalField: TemporalField): Gen[Long] =
    genFrom(temporalField.range())

  private def genIntField(temporalField: TemporalField): Gen[Int] =
    genField(temporalField).map(_.toInt)

  private def genFieldAt(temporalField: TemporalField, temporal: TemporalAccessor): Gen[Long] =
    genFrom(temporalField.rangeRefinedBy(temporal))

  private def genIntFieldAt(temporalField: TemporalField, temporal: TemporalAccessor): Gen[Int] =
    genFieldAt(temporalField, temporal).map(_.toInt)

  private def genFrom(range: TemporalValueRange): Gen[Long] =
    Gen.choose(range.getMinimum, range.getMaximum)
}
