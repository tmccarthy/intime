package au.id.tmm.intime.cats

package object instances {

  object dayOfWeek      extends DayOfWeekInstances
  object duration       extends DurationInstances
  object instant        extends InstantInstances
  object localDate      extends LocalDateInstances
  object localDateTime  extends LocalDateTimeInstances
  object localTime      extends LocalTimeInstances
  object monthDay       extends MonthDayInstances
  object month          extends MonthInstances
  object offsetDateTime extends OffsetDateTimeInstances
  object offsetTime     extends OffsetTimeInstances
  object period         extends PeriodInstances
  object year           extends YearInstances
  object yearMonth      extends YearMonthInstances
  object zonedDateTime  extends ZonedDateTimeInstances
  object zoneId         extends ZoneIdInstances
  object zoneOffset     extends ZoneOffsetInstances

  object all
      extends AnyRef
      with DayOfWeekInstances
      with DurationInstances
      with InstantInstances
      with LocalDateInstances
      with LocalDateTimeInstances
      with LocalTimeInstances
      with MonthDayInstances
      with MonthInstances
      with OffsetDateTimeInstances
      with OffsetTimeInstances
      with PeriodInstances
      with YearInstances
      with YearMonthInstances
      with ZonedDateTimeInstances
      with ZoneIdInstances
      with ZoneOffsetInstances

}
