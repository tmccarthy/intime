package au.id.tmm.intime.scalacheck.chooseimpls

import java.time.{Instant, ZoneId, ZoneOffset}

import au.id.tmm.intime.scalacheck.arbitraryZoneOffset
import org.scalacheck.Gen
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.math.Ordering.Implicits.infixOrderingOps

class ZoneRegionChooseSpec extends AnyFlatSpec with ScalaCheckDrivenPropertyChecks {

  private val testInstant: Instant = Instant.parse("2021-01-01T00:00:00Z")
  private val sut                  = ZoneRegionChoose.asAt(testInstant)

  "a zone region choose" should "only return zone regions" in {
    forAll(sut.choose(ZoneOffset.MAX, ZoneOffset.MIN)) { zoneId =>
      assert(zoneId.isInstanceOf[ZoneOffset] === false)
    }
  }

  it should "return zones whose offset is between the minimum and maximum, or be empty" in {
    forAll { (leftZoneOffset: ZoneOffset, rightZoneOffset: ZoneOffset) =>
      val minZoneOffset = leftZoneOffset min rightZoneOffset
      val maxZoneOffset = leftZoneOffset max rightZoneOffset

      val gen: Gen[ZoneId] = sut.choose(minZoneOffset, maxZoneOffset)

      if (gen.sample.isDefined) {
        forAll(gen) { zoneId =>
          assert(zoneId.getRules.getOffset(testInstant) >= minZoneOffset)
          assert(zoneId.getRules.getOffset(testInstant) <= maxZoneOffset)
        }
      }
    }
  }

}
