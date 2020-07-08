package au.id.tmm.intime.std.instances

import org.scalatest.ParallelTestExecution
import org.scalatest.flatspec.AnyFlatSpec

class DayRangeSpec extends AnyFlatSpec with ParallelTestExecution {

  behavior of "day range computation"

  ManuallyComputedPeriodDurations.read().foreach {
    case (period, manuallyComputedDayRange) =>
      it should s"be ${manuallyComputedDayRange.min} to ${manuallyComputedDayRange.max} days for a period of ${period.getYears} years, ${period.getMonths} months" in {
        assert(DayRange.fromPeriod(period) === manuallyComputedDayRange)
      }
  }

}
