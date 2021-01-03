package au.id.tmm.intime.scalacheck

import au.id.tmm.intime.scalacheck.all._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class ArbitraryInstancesSpec extends AnyFlatSpec with ScalaCheckDrivenPropertyChecks {

  "arbitrary durations" should "be valid" in forAll(arbitraryDuration)(_ => succeed)
  "arbitrary instants" should "be valid" in forAll(arbitraryInstant)(_ => succeed)
  "arbitrary years" should "be valid" in forAll(intimeArbitraryForYear)(_ => succeed)
  "arbitrary months" should "be valid" in forAll(arbitraryMonth)(_ => succeed)
  "arbitrary yearMonths" should "be valid" in forAll(arbitraryYearMonth)(_ => succeed)
  "arbitrary localDates" should "be valid" in forAll(arbitraryLocalDate)(_ => succeed)
  "arbitrary localTimes" should "be valid" in forAll(arbitraryLocalTime)(_ => succeed)
  "arbitrary localDateTimes" should "be valid" in forAll(arbitraryLocalDateTime)(_ => succeed)
  "arbitrary monthDays" should "be valid" in forAll(arbitraryMonthDay)(_ => succeed)
  "arbitrary zoneOffsets" should "be valid" in forAll(arbitraryZoneOffset)(_ => succeed)
  "arbitrary zoneIds" should "be valid" in forAll(arbitraryZoneId)(_ => succeed)
  "arbitrary offsetDateTimes" should "be valid" in forAll(arbitraryOffsetDateTime)(_ => succeed)
  "arbitrary offsetTimes" should "be valid" in forAll(arbitraryOffsetTime)(_ => succeed)
  "arbitrary zonedDateTimes" should "be valid" in forAll(arbitraryZonedDateTime)(_ => succeed)
  "arbitrary dayOfWeeks" should "be valid" in forAll(arbitraryDayOfWeek)(_ => succeed)
  "arbitrary periods" should "be valid" in forAll(arbitraryPeriod)(_ => succeed)

}
