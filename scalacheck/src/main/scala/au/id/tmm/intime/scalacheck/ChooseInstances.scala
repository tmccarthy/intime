package au.id.tmm.intime.scalacheck

import java.time._
import java.time.temporal.ChronoField._

import au.id.tmm.intime.scalacheck.ChooseUtils._
import au.id.tmm.intime.scalacheck.chooseimpls.ZoneRegionChoose
import au.id.tmm.intime.std.NANOS_PER_SECOND
import au.id.tmm.intime.std.implicits._
import org.scalacheck.Gen
import org.scalacheck.Gen.Choose

trait ChooseInstances {

  implicit val chooseInstant: Choose[Instant] =
    makeChronoFieldRangeChoose(INSTANT_SECONDS, NANO_OF_SECOND) {
      case second :: nano :: Nil => Instant.ofEpochSecond(second, nano)
    }

  implicit val chooseMonth: Choose[Month] =
    Choose.xmap[Int, Month](Month.of, _.getValue)

  implicit val chooseYearMonth: Choose[YearMonth] =
    makeChronoFieldRangeChoose(YEAR, MONTH_OF_YEAR) {
      case year :: monthOfYear :: Nil => YearMonth.of(year.toInt, monthOfYear.toInt)
    }

  implicit val chooseMonthDay: Choose[MonthDay] =
    combineChooses[Int, Int, MonthDay](
      _.getMonthValue,
      _.getDayOfMonth,
      MonthDay.of,
      _ => 1,
      month => Month.of(month).maxLength(),
    )

  implicit val chooseLocalDate: Choose[LocalDate] =
    combineChooses[YearMonth, Int, LocalDate](
      YearMonth.from,
      _.getDayOfMonth,
      (yearMonth, day) => LocalDate.of(yearMonth.getYear, yearMonth.getMonthValue, day),
      _ => 1,
      yearMonth => DAY_OF_MONTH.rangeRefinedBy(yearMonth.atDay(1)).getMaximum.toInt,
    )

  implicit val chooseLocalTime: Choose[LocalTime] =
    makeChronoFieldRangeChoose(HOUR_OF_DAY, MINUTE_OF_HOUR, SECOND_OF_MINUTE, NANO_OF_SECOND) {
      case hour :: minute :: second :: nano :: Nil => LocalTime.of(hour.toInt, minute.toInt, second.toInt, nano.toInt)
    }

  implicit val chooseLocalDateTime: Choose[LocalDateTime] =
    combineChooses[LocalDate, LocalTime, LocalDateTime](
      _.toLocalDate,
      _.toLocalTime,
      LocalDateTime.of,
      _ => LocalTime.MIN,
      _ => LocalTime.MAX,
    )

  // The ordering of ZoneOffset is unintuitive in that +10:00 is less than +09:00 (since it "comes
  // first" as the sun rises). Accordingly we have to reverse our mapping into the choose for long
  implicit val chooseZoneOffset: Choose[ZoneOffset] = (min, max) =>
    Gen.choose(max.getTotalSeconds, min.getTotalSeconds).map(totalSeconds => ZoneOffset.ofTotalSeconds(totalSeconds))

  implicit def chooseZonedDateTime: Choose[ZonedDateTime] =
    (min, max) => {
      for {
        instant <- Gen.choose[Instant](
          min.toInstant,
          max.toInstant,
        )

        // The ordering used for the zone component in `ZonedDateTime`'s compareTo is strange, hence this dedicated
        // ordering
        zoneIdOrdering: Ordering[ZoneId] = {
          implicit val reverseStringOrdering: Ordering[String] = Ordering.String.reverse

          Ordering.by { zoneId: ZoneId =>
            (zoneId.getRules.getOffset(instant), zoneId.getId)
          }.reverse
        }

        zone <-
          ZoneRegionChoose
            .asAt(instant)(zoneIdOrdering)
            .choose(
              if (Ordering[Instant].lteq(instant, min.toInstant)) min.getZone else ZoneOffset.MIN,
              if (Ordering[Instant].gteq(instant, max.toInstant)) max.getZone else ZoneOffset.MAX,
            )
      } yield instant.atZone(zone)
    }

  implicit def chooseOffsetDateTime: Choose[OffsetDateTime] = {
    // compareTo for OffsetDateTime unhelpfully reverses the ordering of the ZoneOffset component, so we have to reverse
    // the ordering of the Choose here

    def negate(zoneOffset: ZoneOffset): ZoneOffset = ZoneOffset.ofTotalSeconds(-zoneOffset.getTotalSeconds)

    combineChooses[Instant, ZoneOffset, OffsetDateTime](
      _.toInstant,
      _.getOffset,
      (instant, offset) => instant.atOffset(offset),
      _ => ZoneOffset.MIN,
      _ => ZoneOffset.MAX,
    )(
      t1Choose = implicitly,
      t1Ordering = implicitly,
      t2Choose = Choose.xmap[ZoneOffset, ZoneOffset](negate, negate),
    )
  }

  implicit def chooseOffsetTime: Choose[OffsetTime] =
    (min, max) =>
      {
        def epochNanos(offsetTime: OffsetTime): Long =
          offsetTime.toLocalTime.toNanoOfDay -
            Math.multiplyExact(offsetTime.getOffset.getTotalSeconds.toLong, NANOS_PER_SECOND)

        val minNanos = epochNanos(min)
        val maxNanos = epochNanos(max)

        for {
          epochDiffDuration <- Gen.choose[Long](minNanos, maxNanos).map(Duration.ofNanos)

          offsetSeconds <- Gen.choose[Long](
            (epochDiffDuration min Duration.ofHours(18)).negated().getSeconds,
            (Duration.ofDays(1).getSeconds - epochDiffDuration.getSeconds) min Duration.ofHours(18).getSeconds,
          )

          offsetComponent = Duration.ofSeconds(offsetSeconds)

          localTimeComponent = epochDiffDuration + offsetComponent

          offset = ZoneOffset.ofTotalSeconds(offsetComponent.getSeconds.toInt)
          localTime = LocalTime.ofNanoOfDay(
            (localTimeComponent.toNanos max NANO_OF_DAY.range.getMinimum) min NANO_OF_DAY.range.getMaximum,
          )

        } yield OffsetTime.of(localTime, offset)
      }.filter { offsetDateTime =>
        // TODO fix this properly https://github.com/tmccarthy/intime/issues/13
        offsetDateTime >= min && offsetDateTime <= max
      }

}

object ChooseInstances extends ChooseInstances
