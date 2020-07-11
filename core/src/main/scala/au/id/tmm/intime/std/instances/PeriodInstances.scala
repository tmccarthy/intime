package au.id.tmm.intime.std.instances

import java.time.Period

import au.id.tmm.intime.PeriodLength

trait PeriodInstances {

  /**
    * A `PartialOrdering` for `Period`, which allows comparison between periods that are unambiguously
    * ordered.
    *
    * Because the number of days per month (or year) varies, it is impossible to define a total
    * ordering for `Period`. A period of 30 days may or may not be longer than one of one month,
    * depending on they day the period starts. Some periods, though, can be compared. Two months are
    * longer than one month, 32 days will always be longer than one month, etc. This partial ordering
    * captures these unambiguous cases while not providing orderings in ambiguous ones.
    */
  implicit val intimePartialOrderingForJavaTimePeriod: PartialOrdering[Period] = PeriodPartialOrdering

}

private object PeriodPartialOrdering extends PartialOrdering[Period] {

  override def tryCompare(x: Period, y: Period): Option[Int] = {
    val xNormalised = x.normalized()
    val yNormalised = y.normalized()

    if (xNormalised == yNormalised) {
      Some(0)
    } else if (xNormalised.getDays == yNormalised.getDays) {
      Some(
        Ordering
          .Tuple2[Int, Int]
          .compare(
            (xNormalised.getYears, xNormalised.getMonths),
            (yNormalised.getYears, yNormalised.getMonths),
          ),
      )
    } else if ((xNormalised.getYears == yNormalised.getYears) && (xNormalised.getMonths == yNormalised.getMonths)) {
      Some(
        Ordering[Int].compare(xNormalised.getDays, yNormalised.getDays),
      )
    } else {
      PeriodLength.partialOrdering.tryCompare(
        PeriodLength.of(xNormalised),
        PeriodLength.of(yNormalised),
      )
    }
  }

  override def lteq(x: Period, y: Period): Boolean = tryCompare(x, y).exists(_ <= 0)

}
