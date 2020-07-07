package au.id.tmm.intime.std

package object instances {

  object duration       extends InstanceTraits.DurationInstances
  object instant        extends InstanceTraits.InstantInstances
  object localDate      extends InstanceTraits.LocalDateInstances
  object localDateTime  extends InstanceTraits.LocalDateTimeInstances
  object localTime      extends InstanceTraits.LocalTimeInstances
  object monthDay       extends InstanceTraits.MonthDayInstances
  object offsetDateTime extends InstanceTraits.OffsetDateTimeInstances
  object offsetTime     extends InstanceTraits.OffsetTimeInstances
  object year           extends InstanceTraits.YearInstances
  object yearMonth      extends InstanceTraits.YearMonthInstances
  object zonedDateTime  extends InstanceTraits.ZonedDateTimeInstances
  object zoneOffset     extends InstanceTraits.ZoneOffsetInstances
  object dayOfWeek      extends InstanceTraits.DayOfWeekInstances
  object month          extends InstanceTraits.MonthInstances
  object period         extends PeriodInstances

  object all
      extends AnyRef
      with InstanceTraits.DurationInstances
      with InstanceTraits.InstantInstances
      with InstanceTraits.LocalDateInstances
      with InstanceTraits.LocalDateTimeInstances
      with InstanceTraits.LocalTimeInstances
      with InstanceTraits.MonthDayInstances
      with InstanceTraits.OffsetDateTimeInstances
      with InstanceTraits.OffsetTimeInstances
      with InstanceTraits.YearInstances
      with InstanceTraits.YearMonthInstances
      with InstanceTraits.ZonedDateTimeInstances
      with InstanceTraits.ZoneOffsetInstances
      with InstanceTraits.DayOfWeekInstances
      with InstanceTraits.MonthInstances
      with PeriodInstances

}
