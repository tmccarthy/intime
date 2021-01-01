package au.id.tmm.intime.scalacheck.chooseimpls

import java.time.{Instant, ZoneId}

import org.scalacheck.Gen
import org.scalacheck.Gen.Choose

import scala.collection.Searching

/**
 * A `Choose` that returns `java.time.ZoneRegion` values.
 *
 * Note that the type parameter is `ZoneId` because the `ZoneRegion` subtype is package-private.
 */
private[scalacheck] class ZoneRegionChoose private (at: Instant, allZoneRegions: Array[ZoneId]) extends Choose[ZoneId] {

  private val (offsets, zoneRegions) =
    allZoneRegions
      .map(zoneRegion => (zoneRegion.getRules.getOffset(at), zoneRegion))
      .sortBy { case (offset, zoneId) => (offset, zoneId.getId) }
      .unzip

  override def choose(minZone: ZoneId, maxZone: ZoneId): Gen[ZoneId] = {
    val minOffset = minZone.getRules.getOffset(at)
    val maxOffset = maxZone.getRules.getOffset(at)

    val sliceStart = offsets.search(minOffset) match {
      case Searching.Found(foundIndex) => foundIndex
      case Searching.InsertionPoint(insertionPoint) => (insertionPoint + 1) min offsets.length
    }

    val sliceEnd   = offsets.search(maxOffset) match {
      case Searching.Found(foundIndex) => foundIndex + 1
      case Searching.InsertionPoint(insertionPoint) => insertionPoint
    }

    if (sliceStart == sliceEnd) Gen.fail else Gen.oneOf(zoneRegions.view.slice(sliceStart, sliceEnd))
  }
}

object ZoneRegionChoose {
  class Factory private () {
    private val allZoneRegions =
      ZoneId.getAvailableZoneIds
        .toArray(new Array[String](_))
        .map(ZoneId.of)
        .sortBy(zoneRegion => zoneRegion.getRules.getOffset(Instant.EPOCH))

    def zoneRegionChooseAsAt(instant: Instant): ZoneRegionChoose = new ZoneRegionChoose(instant, allZoneRegions)
  }

  object Factory {
    def apply(): Factory = new Factory
  }

  def asAt(instant: Instant): ZoneRegionChoose = Factory().zoneRegionChooseAsAt(instant)
}
