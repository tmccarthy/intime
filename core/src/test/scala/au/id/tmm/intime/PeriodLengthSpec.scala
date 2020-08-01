package au.id.tmm.intime

import org.scalatest.ParallelTestExecution
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.prop.TableDrivenPropertyChecks

class PeriodLengthSpec extends AnyFlatSpec with ParallelTestExecution with TableDrivenPropertyChecks {

  private def table =
    Table(
      ("period", "manuallyComputedDuration"),
      ManuallyComputedPeriodDurations.read().toSeq: _*,
    )

  "period length computation" should "match with manually computed values" in forEvery(table) {
    (period, manuallyComputedPeriodLength) =>
      assert(PeriodLength.of(period) === manuallyComputedPeriodLength)
  }

}
