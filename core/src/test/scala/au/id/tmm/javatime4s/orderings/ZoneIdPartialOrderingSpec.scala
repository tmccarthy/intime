package au.id.tmm.javatime4s.orderings

import java.time.{ZoneId, ZoneOffset}

import org.scalatest.FlatSpec

class ZoneIdPartialOrderingSpec extends FlatSpec {

  behavior of "the ZoneId partial ordering"

  it should "compare two equivalent offsets" in
    assert(ZoneIdPartialOrdering.equiv(ZoneOffset.ofHours(10), ZoneOffset.ofHours(10)))

  it should "compare two offsets that are equivalent once normalised" in
    assert(ZoneIdPartialOrdering.equiv(ZoneId.of("UTC"), ZoneOffset.ofHours(0)))

  it should "mark a 2 hour offset as less than a -1 hour offset" in
    assert(ZoneIdPartialOrdering.lt(ZoneOffset.ofHours(2), ZoneOffset.ofHours(-1)))

  it should "mark a zone without a constant offset as equivalent to itself" in
    assert(ZoneIdPartialOrdering.equiv(ZoneId.of("Australia/Melbourne"), ZoneId.of("Australia/Melbourne")))

  it should "mark two different zones without constant offsets but with the same rules as equivalent" in
    assert(ZoneIdPartialOrdering.equiv(ZoneId.of("Australia/Melbourne"), ZoneId.of("Australia/Victoria")))

  it should "fail to compare a zone without a constant offset to a fixed offset" in
    assert(ZoneIdPartialOrdering.tryCompare(ZoneId.of("Australia/Melbourne"), ZoneOffset.ofHours(10)) === None)

  it should "fail to compare two different zones without constant offsets" in
    assert(ZoneIdPartialOrdering.tryCompare(ZoneId.of("Australia/Melbourne"), ZoneId.of("Australia/Eucla")) === None)

}
