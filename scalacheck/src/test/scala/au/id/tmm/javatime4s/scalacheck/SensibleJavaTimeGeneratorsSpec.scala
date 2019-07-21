package au.id.tmm.javatime4s.scalacheck

import java.time._

import au.id.tmm.javatime4s._
import org.scalatest.FlatSpec
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class SensibleJavaTimeGeneratorsSpec extends FlatSpec with ScalaCheckDrivenPropertyChecks {

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

  // TODO test for period
  // TODO test for duration

}
