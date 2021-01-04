package au.id.tmm.intime.scalacheck

import java.time._

import org.scalacheck.Cogen

trait CogenInstances {

  implicit val instantCogen: Cogen[Instant] =
    Cogen[(Long, Int)].contramap(i => (i.getEpochSecond, i.getNano))

  implicit val monthCogen: Cogen[Month] =
    Cogen[Int].contramap(_.getValue)

  implicit val yearMonthCogen: Cogen[YearMonth] =
    Cogen[(Int, Month)].contramap(ym => (ym.getYear, ym.getMonth))

  implicit val localDateCogen: Cogen[LocalDate] =
    Cogen[(Int, Int)].contramap(d => (d.getYear, d.getDayOfYear))

  implicit val localTimeCogen: Cogen[LocalTime] =
    Cogen[(Int, Int, Int, Int)].contramap(t => (t.getHour, t.getMinute, t.getSecond, t.getNano))

  implicit val localDateTimeCogen: Cogen[LocalDateTime] =
    Cogen[(LocalDate, LocalTime)].contramap(dt => (dt.toLocalDate, dt.toLocalTime))

  implicit val monthDayCogen: Cogen[MonthDay] =
    Cogen[(Month, Int)].contramap(md => (md.getMonth, md.getDayOfMonth))

  implicit val zoneOffsetCogen: Cogen[ZoneOffset] =
    Cogen[Int].contramap(zo => zo.getTotalSeconds)

  implicit val zoneIdCogen: Cogen[ZoneId] =
    Cogen[String].contramap(zi => zi.getId)

  implicit val offsetDateTimeCogen: Cogen[OffsetDateTime] =
    Cogen[(LocalDateTime, ZoneOffset)].contramap(dt => (dt.toLocalDateTime, dt.getOffset))

  implicit val offsetTimeCogen: Cogen[OffsetTime] =
    Cogen[(LocalTime, ZoneOffset)].contramap(ot => (ot.toLocalTime, ot.getOffset))

  implicit val zonedDateTimeCogen: Cogen[ZonedDateTime] =
    Cogen[(LocalDateTime, ZoneId)].contramap(dt => (dt.toLocalDateTime, dt.getZone))

  implicit val periodCogen: Cogen[Period] =
    Cogen[(Int, Int, Int)].contramap(p => (p.getYears, p.getMonths, p.getDays))

}

object CogenInstances extends CogenInstances
