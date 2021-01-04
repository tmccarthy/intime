package au.id.tmm.intime

package object scalacheck
    extends ArbitraryInstances
    with ChooseInstances
    with CogenInstances
    with SensibleJavaTimeGenerators
    with ShrinkInstances {

  object duration       extends DurationInstances
  object instant        extends InstantInstances
  object localDate      extends LocalDateInstances
  object localDateTime  extends LocalDateTimeInstances
  object localTime      extends LocalTimeInstances
  object monthDay       extends MonthDayInstances
  object offsetDateTime extends OffsetDateTimeInstances
  object offsetTime     extends OffsetTimeInstances
  object year           extends YearInstances
  object yearMonth      extends YearMonthInstances
  object zonedDateTime  extends ZonedDateTimeInstances
  object zoneOffset     extends ZoneOffsetInstances
  object dayOfWeek      extends DayOfWeekInstances
  object month          extends MonthInstances
  object period         extends PeriodInstances

  object all
      extends AnyRef
      with DurationInstances
      with InstantInstances
      with LocalDateInstances
      with LocalDateTimeInstances
      with LocalTimeInstances
      with MonthDayInstances
      with OffsetDateTimeInstances
      with OffsetTimeInstances
      with YearInstances
      with YearMonthInstances
      with ZonedDateTimeInstances
      with ZoneOffsetInstances
      with DayOfWeekInstances
      with MonthInstances
      with PeriodInstances

}
