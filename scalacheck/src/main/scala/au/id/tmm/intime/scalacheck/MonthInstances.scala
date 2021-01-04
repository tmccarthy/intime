package au.id.tmm.intime.scalacheck

import java.time.Month

import org.scalacheck.Gen

private[scalacheck] trait MonthInstances {

  val genMonth: Gen[Month] = Gen.oneOf(Month.values.toIndexedSeq)

}
