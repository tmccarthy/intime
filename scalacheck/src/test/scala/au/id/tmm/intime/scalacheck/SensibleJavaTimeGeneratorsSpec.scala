package au.id.tmm.intime.scalacheck

import java.time._

import au.id.tmm.intime._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class SensibleJavaTimeGeneratorsSpec extends AnyFlatSpec with ScalaCheckDrivenPropertyChecks {

  "the generator for a sensible Instant" should "produce values between 1900 and 2100" in {
    forAll(genSensibleInstant) { instant =>
      assert(MIN_SENSIBLE.toInstant <= instant && instant <= MAX_SENSIBLE.toInstant)
    }
  }

  "the generator for a sensible Year" should "produce values between 1900 and 2100" in {
    forAll(genSensibleYear) { year =>
      assert(Year.of(1900) <= year && year <= Year.of(2100))
    }
  }

  "the generator for a sensible YearMonth" should "produce values between 1900 and 2100" in {
    forAll(genSensibleYearMonth) { yearMonth =>
      assert(YearMonth.of(1900, 1) <= yearMonth && yearMonth <= YearMonth.of(2100, 1))
    }
  }

  "the generator for a sensible LocalDate" should "produce values between 1900 and 2100" in {
    forAll(genSensibleLocalDate) { localDate =>
      assert(LocalDate.of(1900, 1, 1) <= localDate && localDate <= LocalDate.of(2100, 1, 1))
    }
  }

  "the generator for a sensible LocalDateTime" should "produce values between 1900 and 2100" in {
    forAll(genSensibleLocalDateTime) { localDateTime =>
      assert(MIN_SENSIBLE.toLocalDateTime <= localDateTime && localDateTime <= MAX_SENSIBLE.toLocalDateTime)
    }
  }

  "the generator for a sensible OffsetDateTime" should "produce values between 1900 and 2100" in {
    forAll(genSensibleOffsetDateTime) { offsetDateTime =>
      assert(MIN_SENSIBLE.toOffsetDateTime <= offsetDateTime && offsetDateTime <= MAX_SENSIBLE.toOffsetDateTime)
    }
  }

  "the generator for a sensible ZonedDateTime" should "produce values between 1900 and 2100" in {
    forAll(genSensibleZonedDateTime) { zonedDateTime =>
      assert(MIN_SENSIBLE <= zonedDateTime && zonedDateTime <= MAX_SENSIBLE)
    }
  }

  "the generator for a sensible ZoneOffset" should "produce values with quarter-hour offsets" in {
    forAll(genSensibleZoneOffset) { zoneOffset =>
      assert(zoneOffset.getTotalSeconds % Duration.ofMinutes(15).getSeconds == 0)
    }
  }

  "the generator for a sensible period" should "produce values that between -100 years and 100 years" in {
    forAll(genSensiblePeriod) { period =>
      assert(Period.ofYears(-100) <= period && period <= Period.ofYears(100))
    }
  }

  it should "produce values where the sign of all components is the same" in {
    forAll(genSensiblePeriod) { period =>
      val periodComponents = Set(period.getYears, period.getMonths, period.getDays)

      assert(periodComponents.forall(_ >= 0) || periodComponents.forall(_ <= 0))
    }
  }

  "the generator for a sensible duration" should "produce values that between -100 years and 100 years" in {
    forAll(genSensibleDuration) { duration =>
      assert(Duration.ofDays(-100 * 365) <= duration && duration <= Duration.ofDays(100 * 365))
    }
  }

}
