package au.id.tmm.intime.scalacheck.chooseimpls

import java.time.{Instant, ZoneId}

import au.id.tmm.intime.scalacheck.chooseimpls.ZoneRegionChoose.allZoneRegions
import com.github.ghik.silencer.silent
import org.scalacheck.Gen
import org.scalacheck.Gen.Choose

import scala.collection.Searching

/**
  * A `Choose` that returns `java.time.ZoneRegion` values.
  *
 * Note that the type parameter is `ZoneId` because the `ZoneRegion` subtype is package-private.
  */
private[scalacheck] class ZoneRegionChoose private (at: Instant)(implicit zoneIdOrdering: Ordering[ZoneId])
    extends Choose[ZoneId] {

  private val zoneRegionsInOrder = allZoneRegions.sorted

  @silent("Unused import")
  private def zonesBetween(minZone: ZoneId, maxZone: ZoneId) = {
    import Searching.search

    val sliceStart = zoneRegionsInOrder.search(minZone) match {
      case Searching.Found(foundIndex)              => foundIndex
      case Searching.InsertionPoint(insertionPoint) => (insertionPoint + 1) min zoneRegionsInOrder.length
    }

    val sliceEnd = zoneRegionsInOrder.search(maxZone) match {
      case Searching.Found(foundIndex)              => foundIndex + 1
      case Searching.InsertionPoint(insertionPoint) => insertionPoint
    }

    zoneRegionsInOrder.view.slice(sliceStart, sliceEnd)
  }

  override def choose(minZone: ZoneId, maxZone: ZoneId): Gen[ZoneId] = {
    val candidateZones = zonesBetween(minZone, maxZone)

    if (candidateZones.isEmpty) Gen.fail else Gen.oneOf(candidateZones)
  }
}

object ZoneRegionChoose {
  @silent("deprecated")
  private lazy val allZoneRegions: Vector[ZoneId] = {
    import collection.JavaConverters._

    ZoneId.getAvailableZoneIds.asScala.toVector
      .map(ZoneId.of)
      .sortBy(zoneRegion => zoneRegion.getRules.getOffset(Instant.EPOCH))
  }

  def asAt(instant: Instant)(implicit zoneIdOrdering: Ordering[ZoneId]): ZoneRegionChoose =
    new ZoneRegionChoose(instant)
}
