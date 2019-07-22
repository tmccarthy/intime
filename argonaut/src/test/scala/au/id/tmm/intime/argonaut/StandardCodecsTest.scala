package au.id.tmm.intime.argonaut

import java.time._

import argonaut.{DecodeJson, DecodeResult, EncodeJson}
import au.id.tmm.intime.scalacheck._
import org.scalacheck.Arbitrary
import org.scalatest.FlatSpec
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class StandardCodecsTest extends FlatSpec with ScalaCheckDrivenPropertyChecks {

  implicit val config: PropertyCheckConfiguration = PropertyCheckConfiguration(
    minSuccessful = 50000,
  )

  behavior of "The codec for Instant"

  testsForCodec[Instant]()

  behavior of "The codec for Year"

  testsForCodec[Year]()

  behavior of "The codec for Month"

  testsForCodec[Month]()

  behavior of "The codec for YearMonth"

  testsForCodec[YearMonth]()

  behavior of "The codec for LocalDate"

  testsForCodec[LocalDate]()

  behavior of "The codec for LocalTime"

  testsForCodec[LocalTime]()

  behavior of "The codec for LocalDateTime"

  testsForCodec[LocalDateTime]()

  behavior of "The codec for MonthDay"

  testsForCodec[MonthDay]()

  behavior of "The codec for ZoneOffset"

  testsForCodec[ZoneOffset]()

  behavior of "The codec for OffsetDateTime"

  testsForCodec[OffsetDateTime]()

  behavior of "The codec for OffsetTime"

  testsForCodec[OffsetTime]()

  behavior of "The codec for ZonedDateTime"

  // This test fails in Java 8 due to a bug in java.time.ZonedDateTime
  it should "encode and decode a zoned date time in the GMT0 timezone" in {
    val zonedDateTime = ZonedDateTime.of(
      LocalDate.of(508590951, 6, 8),
      LocalTime.of(23, 43, 29, 887768879),
      ZoneId.of("GMT0"),
    )

    assertEncodeDecodeIsIdentity(zonedDateTime)
  }

  testsForCodec[ZonedDateTime]()

  behavior of "The codec for DayOfWeek"

  testsForCodec[DayOfWeek]()

  behavior of "The codec for Duration"

  // This test fails in Java 8 due to a bug in java.time.Duration
  it should "encode and decode a negative duration of less than one second" in {
    val period = Duration.ofSeconds(0, -174786001)

    assertEncodeDecodeIsIdentity(period)
  }

  // This test fails in Java 8 due to a bug in java.time.Duration
  it should "encode and decode a negative duration of more than one second" in {
    val period = Duration.ofSeconds(-1, -174786001)

    assertEncodeDecodeIsIdentity(period)
  }

  testsForCodec[Duration]()

  behavior of "The codec for ZoneId"

  testsForCodec[ZoneId]()

  behavior of "The codec for Period"

  testsForCodec[Period]()

  private def testsForCodec[A : EncodeJson : DecodeJson : Arbitrary](): Unit =
    it should "form an identity when encoding and then decoding" in forAll { a: A =>
      assertEncodeDecodeIsIdentity(a)
    }

  def assertEncodeDecodeIsIdentity[A : EncodeJson : DecodeJson](a: A): Unit = {
    val encoded = implicitly[EncodeJson[A]].encode(a)

    val decoded = implicitly[DecodeJson[A]].decodeJson(encoded)

    assert(decoded === DecodeResult.ok(a))
  }

}
