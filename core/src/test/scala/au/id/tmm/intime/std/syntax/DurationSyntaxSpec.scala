package au.id.tmm.intime.std.syntax

import java.time.Duration

import au.id.tmm.intime.std.syntax.duration._

import org.scalatest.flatspec.AnyFlatSpec

class DurationSyntaxSpec extends AnyFlatSpec {

  "a duration" can "be added to another" in {
    assert(Duration.ofDays(5) + Duration.ofHours(48) === Duration.ofDays(7))
  }

  it can "be subtracted from another" in {
    assert(Duration.ofDays(5) - Duration.ofHours(48) === Duration.ofDays(3))
  }

  it can "be divided by a long" in {
    assert(Duration.ofDays(4) / 3 === Duration.ofHours(32))
  }

  it can "be multiplied by a long" in {
    assert(Duration.ofDays(4) * 2 === Duration.ofDays(8))
  }

  it can "be negated" in {
    assert(-Duration.ofDays(3) === Duration.ofDays(-3))
  }

  it can "be compared using <" in {
    assert(Duration.ofDays(2) < Duration.ofDays(4))
  }

  it can "be compared using <=" in {
    assert(Duration.ofDays(2) <= Duration.ofDays(2))
  }

  it can "be compared using >" in {
    assert(Duration.ofDays(4) > Duration.ofDays(2))
  }

  it can "be compared using >=" in {
    assert(Duration.ofDays(2) >= Duration.ofDays(2))
  }

  it can "be compared using equiv" in {
    assert(Duration.ofDays(2) equiv Duration.ofDays(2))
  }

  it can "be compared using max" in {
    assert((Duration.ofDays(2) max Duration.ofDays(1)) === Duration.ofDays(2))
  }

  it can "be compared using min" in {
    assert((Duration.ofDays(2) min Duration.ofDays(1)) === Duration.ofDays(1))
  }

}
