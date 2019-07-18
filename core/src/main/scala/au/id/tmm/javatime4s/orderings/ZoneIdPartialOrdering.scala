package au.id.tmm.javatime4s.orderings

import java.time.{ZoneId, ZoneOffset}

/**
  * Provides a `PartialOrdering` for `ZoneId` which provides comparisons between zones if:
  * <ul>
  *   <li>they both represent a fixed offset (ie they can be normalied to a `ZoneOffset`), or</li>
  *   <li>they represent the same zone</li>
  * </ul>
  */
object ZoneIdPartialOrdering extends PartialOrdering[ZoneId] {
  override def tryCompare(x: ZoneId, y: ZoneId): Option[Int] =
    (x.normalized(), y.normalized()) match {
      case (x: ZoneOffset, y: ZoneOffset)     => Some(x compareTo y)
      case (x, y) if x == y                   => Some(0)
      case (x, y) if x.getRules == y.getRules => Some(0)
      case _                                  => None
    }

  override def lteq(x: ZoneId, y: ZoneId): Boolean = {
    val compared = tryCompare(x, y)

    compared.exists(_ <= 0)
  }
}
