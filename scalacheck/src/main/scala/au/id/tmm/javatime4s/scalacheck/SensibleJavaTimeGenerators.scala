package au.id.tmm.javatime4s.scalacheck

import java.time._
import java.time.temporal.TemporalAccessor

import au.id.tmm.javatime4s.scalacheck.ChooseInstances._
import org.scalacheck.Gen
import org.scalacheck.Gen.Choose

/**
  * Provides generators for java.time classes within "sensible" ranges.
  *
  * This means (roughly):
  * <ul>
  *   <li>Representations of a point on the time line are between 1900 and 2100</li>
  *   <li>Representations of a period of time are within -100 years and 100 years</li>
  *   <li>Offsets are in quarter-hour increments</li>
  * </ul>
  *
  * Generators for temporal fields where these constraints are trivial or meaningless (eg days of
  * the week) are not provided here. `Arbitrary` generators should be used instead.
  */
trait SensibleJavaTimeGenerators {

  private[scalacheck] val MIN_SENSIBLE: ZonedDateTime =
    ZonedDateTime.of(LocalDate.of(1900, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC)
  private[scalacheck] val MAX_SENSIBLE: ZonedDateTime =
    ZonedDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC)

  private def genBetweenMinMax[A : Choose](temporalQuery: TemporalAccessor => A) =
    implicitly[Choose[A]].choose(temporalQuery(MIN_SENSIBLE), temporalQuery(MAX_SENSIBLE))

  val genSensibleInstant: Gen[Instant]               = genBetweenMinMax(Instant.from)
  val genSensibleYear: Gen[Year]                     = genBetweenMinMax(Year.from)
  val genSensibleYearMonth: Gen[YearMonth]           = genBetweenMinMax(YearMonth.from)
  val genSensibleLocalDate: Gen[LocalDate]           = genBetweenMinMax(LocalDate.from)
  val genSensibleLocalDateTime: Gen[LocalDateTime]   = genBetweenMinMax(LocalDateTime.from)
  val genSensibleOffsetDateTime: Gen[OffsetDateTime] = genBetweenMinMax(OffsetDateTime.from)
  val genSensibleZonedDateTime: Gen[ZonedDateTime]   = genBetweenMinMax(ZonedDateTime.from)

  val genSensibleZoneOffset: Gen[ZoneOffset] =
    for {
      hours <- Gen.choose(-18, 18)
      minutes <- hours match {
        case -18 | 18 => Gen.const(0)
        case _        => Gen.oneOf(0, 15, 30, 45).map(_ * hours.sign)
      }
    } yield ZoneOffset.ofHoursMinutes(hours, minutes)

  val genSensiblePeriod: Gen[Period] = for {
    years  <- Gen.choose(0, 99)
    months <- Gen.choose(0, 11)
    days   <- Gen.choose(0, 31)
  } yield Period.of(years, months, days)

  val genSensibleDuration: Gen[Duration] = chooseDuration.choose(
    min = Duration.ofDays(365 * 100).negated,
    max = Duration.ofDays(365 * 100),
  )

}

object SensibleJavaTimeGenerators extends SensibleJavaTimeGenerators
