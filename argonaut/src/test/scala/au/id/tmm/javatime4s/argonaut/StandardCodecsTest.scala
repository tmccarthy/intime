package au.id.tmm.javatime4s.argonaut

import java.time._

import argonaut.{DecodeJson, DecodeResult, EncodeJson}
import au.id.tmm.javatime4s.scalacheck._
import org.scalacheck.Arbitrary
import org.scalatest.FlatSpec
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.reflect.ClassTag

class StandardCodecsTest extends FlatSpec with ScalaCheckDrivenPropertyChecks {

  implicit val config: PropertyCheckConfiguration = PropertyCheckConfiguration(
    minSuccessful = 200000,
  )

  testsForCodec[Instant]()
  testsForCodec[Year]()
  testsForCodec[Month]()
  testsForCodec[YearMonth]()
  testsForCodec[LocalDate]()
  testsForCodec[LocalTime]()
  testsForCodec[LocalDateTime]()
  testsForCodec[MonthDay]()
  testsForCodec[ZoneOffset]()
  testsForCodec[OffsetDateTime]()
  testsForCodec[OffsetTime]()
  testsForCodec[ZonedDateTime]()
  testsForCodec[DayOfWeek]()
  testsForCodec[Duration]()
  testsForCodec[ZoneId]()
  testsForCodec[Period]()

  def testsForCodec[A : EncodeJson : DecodeJson : Arbitrary : ClassTag](): Unit = {

    val className = implicitly[ClassTag[A]].runtimeClass.getSimpleName

    behavior of s"The codec for $className"

    it should "invert the encoder and decoder" in forAll { a: A =>
      val encoded = implicitly[EncodeJson[A]].encode(a)

      val decoded = implicitly[DecodeJson[A]].decodeJson(encoded)

      assert(decoded === DecodeResult.ok(a))
    }
  }

  // TODO add dedicated test for ZonedDateTime +508590951-06-08T23:53:29.887768879Z[GMT0]
  // TODO add dedicated test for PT-0.174786001S / PT0.174786001S

}
