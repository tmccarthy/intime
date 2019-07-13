package au.id.tmm.javatime4s.argonaut

import java.time._
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter._

import argonaut.CodecJson

trait StandardCodecs {

  implicit val instantCodec: CodecJson[Instant] =
    DateTimeFormatterCodecs.instantCodecFrom(ISO_INSTANT)

  implicit val yearCodec: CodecJson[Year] =
    DateTimeCodecUtils.makeCodec(_.getValue, Year.of)

  implicit val monthCodec: CodecJson[Month] =
    DateTimeCodecUtils.makeCodec(_.getValue, Month.of)

  implicit val yearMonthCodec: CodecJson[YearMonth] =
    DateTimeFormatterCodecs.yearMonthCodecFrom(DateTimeFormatter.ofPattern("uuuu-MM"))

  implicit val localDateCodec: CodecJson[LocalDate] =
    DateTimeFormatterCodecs.localDateCodecFrom(ISO_LOCAL_DATE)

  implicit val localTimeCodec: CodecJson[LocalTime] =
    DateTimeFormatterCodecs.localTimeCodecFrom(ISO_LOCAL_TIME)

  implicit val localDateTimeCodec: CodecJson[LocalDateTime] =
    DateTimeFormatterCodecs.localDateTimeCodecFrom(ISO_LOCAL_DATE_TIME)

  implicit val monthDayCodec: CodecJson[MonthDay] =
    DateTimeCodecUtils.makeCodec(_.toString, MonthDay.parse)

  implicit val zoneOffsetCodec: CodecJson[ZoneOffset] =
    DateTimeCodecUtils.makeCodec(_.toString, ZoneOffset.of)

  implicit val offsetDateTimeCodec: CodecJson[OffsetDateTime] =
    DateTimeFormatterCodecs.offsetDateTimeCodecFrom(ISO_OFFSET_DATE_TIME)

  implicit val offsetTimeCodec: CodecJson[OffsetTime] =
    DateTimeFormatterCodecs.offsetTimeCodecFrom(ISO_OFFSET_TIME)

  implicit val zonedDateTimeCodec: CodecJson[ZonedDateTime] =
    DateTimeFormatterCodecs.zonedDateTimeCodecFrom(ISO_ZONED_DATE_TIME)

  implicit val dayOfWeekCodec: CodecJson[DayOfWeek] =
    DateTimeCodecUtils.makeCodec(_.getValue, DayOfWeek.of)

  implicit val durationCodec: CodecJson[Duration] =
    DateTimeCodecUtils.makeCodec(_.toString, Duration.parse)

  implicit val zoneIdCodec: CodecJson[ZoneId] =
    DateTimeCodecUtils.makeCodec(_.toString, ZoneId.of)

  implicit val periodCodec: CodecJson[Period] =
    DateTimeCodecUtils.makeCodec(_.toString, Period.parse)

}

object StandardCodecs extends StandardCodecs
