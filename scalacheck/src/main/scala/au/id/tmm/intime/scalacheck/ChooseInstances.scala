package au.id.tmm.intime.scalacheck

import java.time._
import java.time.temporal.ChronoField._
import java.time.temporal.{ChronoField, TemporalAccessor}

import au.id.tmm.intime.scalacheck.chooseimpls.ZoneRegionChoose
import au.id.tmm.intime.std.implicits.all._
import org.scalacheck.Gen
import org.scalacheck.Gen.Choose

trait ChooseInstances {

  private val NANOS_PER_SECOND = ChronoField.NANO_OF_SECOND.range().getMaximum + 1

  implicit val chooseDuration: Choose[Duration] =
    combineChooses[Long, Long, Duration](
      _.getSeconds,
      _.getNano,
      Duration.ofSeconds,
      _ => 0L,
      _ => NANO_OF_SECOND.range().getMaximum,
    )

  implicit val chooseInstant: Choose[Instant] =
    makeChronoFieldRangeChoose(INSTANT_SECONDS, NANO_OF_SECOND) {
      case second :: nano :: Nil => Instant.ofEpochSecond(second, nano)
    }

  implicit val chooseYear: Choose[Year] =
    Choose.xmap[Int, Year](Year.of, _.getValue)

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

  implicit val chooseDayOfWeek: Choose[DayOfWeek] =
    Choose.xmap(DayOfWeek.of, _.getValue)

  implicit def chooseZonedDateTime: Choose[ZonedDateTime] =
    (min, max) => {
      val zoneRegionChooseFactory = ZoneRegionChoose.Factory()

      for {
        instant <- Gen.choose[Instant](
          min.toInstant,
          max.toInstant,
        )
        zone <-
          zoneRegionChooseFactory
            .zoneRegionChooseAsAt(instant)
            .reverse
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
    (min, max) => {
      def epochNanos(offsetTime: OffsetTime): Long =
        offsetTime.toLocalTime.toNanoOfDay -
          offsetTime.getOffset.getTotalSeconds.toLong * NANOS_PER_SECOND

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

        offset    = ZoneOffset.ofTotalSeconds(offsetComponent.getSeconds.toInt)
        localTime = LocalTime.ofNanoOfDay(localTimeComponent.toNanos)

      } yield OffsetTime.of(localTime, offset)
    }

  private def combineChooses[T1, T2, O](
    extractT1: O => T1,
    extractT2: O => T2,
    combine: (T1, T2) => O,
    t2Floor: T1 => T2,
    t2Ceil: T1 => T2,
  )(implicit
    t1Choose: Choose[T1],
    t1Ordering: Ordering[T1],
    t2Choose: Choose[T2],
  ): Choose[O] =
    (minO, maxO) => {
      val minT1 = extractT1(minO)
      val minT2 = extractT1(maxO)

      for {
        t1 <- t1Choose.choose(minT1, minT2)

        t2 <- t2Choose.choose(
          min = if (t1Ordering.lteq(t1, minT1)) extractT2(minO) else t2Floor(t1),
          max = if (t1Ordering.gteq(t1, minT2)) extractT2(maxO) else t2Ceil(t1),
        )
      } yield combine(t1, t2)
    }

  private def makeChronoFieldRangeChoose[A <: TemporalAccessor](
    fields: ChronoField*,
  )(
    makeA: PartialFunction[List[Long], A],
  ) =
    new Choose[A] {
      override def choose(min: A, max: A): Gen[A] = generateFieldValues(min, max).map(makeA)

      private def generateFieldValues(
        min: A,
        max: A,
        generatorForPreviousFields: Gen[List[Long]] = Gen.const(Nil),
        fieldsAlreadyGenerated: List[ChronoField] = Nil,
        fieldsRemaining: List[ChronoField] = fields.toList,
      ): Gen[List[Long]] =
        fieldsRemaining match {
          case Nil => generatorForPreviousFields
          case thisField :: fieldsRemaining =>
            generatorForPreviousFields.flatMap { valuesForPreviousFields =>
              val valuesWithHandledFields: List[(Long, ChronoField)] =
                valuesForPreviousFields zip fieldsAlreadyGenerated

              val isAtMinBoundary = valuesWithHandledFields.forall {
                case (generatedValue, field) =>
                  generatedValue <= field.getFrom(min)
              }

              val isAtMaxBoundary = valuesWithHandledFields.forall {
                case (generatedValue, field) =>
                  generatedValue >= field.getFrom(max)
              }

              val generatorForThisFieldValue = Gen.choose[Long](
                min = if (isAtMinBoundary) thisField.getFrom(min) else thisField.range().getMinimum,
                max = if (isAtMaxBoundary) thisField.getFrom(max) else thisField.range().getMaximum,
              )

              generateFieldValues(
                min,
                max,
                generatorForThisFieldValue.map(valuesForPreviousFields :+ _),
                fieldsAlreadyGenerated :+ thisField,
                fieldsRemaining,
              )
            }

        }
    }

}

object ChooseInstances extends ChooseInstances
