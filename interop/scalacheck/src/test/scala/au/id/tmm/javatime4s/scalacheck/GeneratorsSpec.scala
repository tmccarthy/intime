package au.id.tmm.javatime4s.scalacheck

import org.scalatest.FlatSpec
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class GeneratorsSpec extends FlatSpec with ScalaCheckDrivenPropertyChecks {

  implicit val config: PropertyCheckConfiguration = PropertyCheckConfiguration(
    minSuccessful = 5000,
    workers = 2,
  )

  "generated durations" should "be valid" in forAll(genDuration)(_ => succeed)
  "generated instants" should "be valid" in forAll(genInstant)(_ => succeed)
  "generated years" should "be valid" in forAll(genYear)(_ => succeed)
  "generated months" should "be valid" in forAll(genMonth)(_ => succeed)
  "generated yearMonths" should "be valid" in forAll(genYearMonth)(_ => succeed)
  "generated localDates" should "be valid" in forAll(genLocalDate)(_ => succeed)
  "generated localTimes" should "be valid" in forAll(genLocalTime)(_ => succeed)
  "generated localDateTimes" should "be valid" in forAll(genLocalDateTime)(_ => succeed)
  "generated monthDays" should "be valid" in forAll(genMonthDay)(_ => succeed)
  "generated zoneOffsets" should "be valid" in forAll(genZoneOffset)(_ => succeed)
  "generated zoneIds" should "be valid" in forAll(genZoneId)(_ => succeed)
  "generated offsetDateTimes" should "be valid" in forAll(genOffsetDateTime)(_ => succeed)
  "generated offsetTimes" should "be valid" in forAll(genOffsetTime)(_ => succeed)
  "generated zonedDateTimes" should "be valid" in forAll(genZonedDateTime)(_ => succeed)
  "generated dayOfWeeks" should "be valid" in forAll(genDayOfWeek)(_ => succeed)
  "generated periods" should "be valid" in forAll(genPeriod)(_ => succeed)

}
