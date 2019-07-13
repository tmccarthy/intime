package au.id.tmm.javatime4s.argonaut

import java.time._
import java.time.format.DateTimeFormatter
import java.time.temporal.{TemporalAccessor, TemporalQuery}

import argonaut._

object DateTimeFormatterCodecs {

  def codecFrom[A <: TemporalAccessor](
    formatter: DateTimeFormatter,
    temporalQuery: TemporalQuery[A],
  ): CodecJson[A] =
    DateTimeCodecUtils.makeCodec[String, A](formatter.format, formatter.parse(_, temporalQuery))

  def instantCodecFrom(formatter: DateTimeFormatter): CodecJson[Instant] =
    codecFrom(formatter, Instant.from(_))

  def yearCodecFrom(formatter: DateTimeFormatter): CodecJson[Year] =
    codecFrom(formatter, Year.from(_))

  def monthCodecFrom(formatter: DateTimeFormatter): CodecJson[Month] =
    codecFrom(formatter, Month.from(_))

  def yearMonthCodecFrom(formatter: DateTimeFormatter): CodecJson[YearMonth] =
    codecFrom(formatter, YearMonth.from(_))

  def localDateCodecFrom(formatter: DateTimeFormatter): CodecJson[LocalDate] =
    codecFrom(formatter, LocalDate.from(_))

  def localTimeCodecFrom(formatter: DateTimeFormatter): CodecJson[LocalTime] =
    codecFrom(formatter, LocalTime.from(_))

  def localDateTimeCodecFrom(formatter: DateTimeFormatter): CodecJson[LocalDateTime] =
    codecFrom(formatter, LocalDateTime.from(_))

  def monthDayCodecFrom(formatter: DateTimeFormatter): CodecJson[MonthDay] =
    codecFrom(formatter, MonthDay.from(_))

  def zoneOffsetCodecFrom(formatter: DateTimeFormatter): CodecJson[ZoneOffset] =
    codecFrom(formatter, ZoneOffset.from(_))

  def offsetDateTimeCodecFrom(formatter: DateTimeFormatter): CodecJson[OffsetDateTime] =
    codecFrom(formatter, OffsetDateTime.from(_))

  def offsetTimeCodecFrom(formatter: DateTimeFormatter): CodecJson[OffsetTime] =
    codecFrom(formatter, OffsetTime.from(_))

  def zonedDateTimeCodecFrom(formatter: DateTimeFormatter): CodecJson[ZonedDateTime] =
    codecFrom(formatter, ZonedDateTime.from(_))

  def dayOfWeekCodecFrom(formatter: DateTimeFormatter): CodecJson[DayOfWeek] =
    codecFrom(formatter, DayOfWeek.from(_))

}
