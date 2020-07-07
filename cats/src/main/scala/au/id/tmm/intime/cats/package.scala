package au.id.tmm.intime

package object cats {

  object implicits {
    object dayOfWeek      extends instances.DayOfWeekInstances
    object duration       extends instances.DurationInstances
    object instant        extends instances.InstantInstances
    object localDate      extends instances.LocalDateInstances
    object localDateTime  extends instances.LocalDateTimeInstances
    object localTime      extends instances.LocalTimeInstances
    object monthDay       extends instances.MonthDayInstances
    object month          extends instances.MonthInstances
    object offsetDateTime extends instances.OffsetDateTimeInstances
    object offsetTime     extends instances.OffsetTimeInstances
    object period         extends instances.PeriodInstances
    object year           extends instances.YearInstances
    object yearMonth      extends instances.YearMonthInstances
    object zonedDateTime  extends instances.ZonedDateTimeInstances
    object zoneId         extends instances.ZoneIdInstances
    object zoneOffset     extends instances.ZoneOffsetInstances

    object all
        extends AnyRef
        with instances.DayOfWeekInstances
        with instances.DurationInstances
        with instances.InstantInstances
        with instances.LocalDateInstances
        with instances.LocalDateTimeInstances
        with instances.LocalTimeInstances
        with instances.MonthDayInstances
        with instances.MonthInstances
        with instances.OffsetDateTimeInstances
        with instances.OffsetTimeInstances
        with instances.PeriodInstances
        with instances.YearInstances
        with instances.YearMonthInstances
        with instances.ZonedDateTimeInstances
        with instances.ZoneIdInstances
        with instances.ZoneOffsetInstances

  }

}
