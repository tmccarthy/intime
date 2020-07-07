package au.id.tmm.intime

package object std {

  object implicits {

    // format: off
    object duration       extends AnyRef with instances.InstanceTraits.DurationInstances       with syntax.DurationOps.ToDurationOps
    object instant        extends AnyRef with instances.InstanceTraits.InstantInstances        with syntax.InstantOps.ToInstantOps
    object localDate      extends AnyRef with instances.InstanceTraits.LocalDateInstances      with syntax.LocalDateOps.ToLocalDateOps
    object localDateTime  extends AnyRef with instances.InstanceTraits.LocalDateTimeInstances  with syntax.LocalDateTimeOps.ToLocalDateTimeOps
    object localTime      extends AnyRef with instances.InstanceTraits.LocalTimeInstances      with syntax.LocalTimeOps.ToLocalTimeOps
    object offsetDateTime extends AnyRef with instances.InstanceTraits.OffsetDateTimeInstances with syntax.OffsetDateTimeOps.ToOffsetDateTimeOps
    object offsetTime     extends AnyRef with instances.InstanceTraits.OffsetTimeInstances     with syntax.OffsetTimeOps.ToOffsetTimeOps
    object period         extends AnyRef with instances.PeriodInstances                        with syntax.PeriodOps.ToPeriodOps
    object year           extends AnyRef with instances.InstanceTraits.YearInstances           with syntax.YearOps.ToYearOps
    object yearMonth      extends AnyRef with instances.InstanceTraits.YearMonthInstances      with syntax.YearMonthOps.ToYearMonthOps
    object zonedDateTime  extends AnyRef with instances.InstanceTraits.ZonedDateTimeInstances  with syntax.ZonedDateTimeOps.ToZonedDateTimeOps
    // format: on

    object all
        extends AnyRef
        with instances.InstanceTraits.DurationInstances
        with syntax.DurationOps.ToDurationOps
        with instances.InstanceTraits.InstantInstances
        with syntax.InstantOps.ToInstantOps
        with instances.InstanceTraits.LocalDateInstances
        with syntax.LocalDateOps.ToLocalDateOps
        with instances.InstanceTraits.LocalDateTimeInstances
        with syntax.LocalDateTimeOps.ToLocalDateTimeOps
        with instances.InstanceTraits.LocalTimeInstances
        with syntax.LocalTimeOps.ToLocalTimeOps
        with instances.InstanceTraits.OffsetDateTimeInstances
        with syntax.OffsetDateTimeOps.ToOffsetDateTimeOps
        with instances.InstanceTraits.OffsetTimeInstances
        with syntax.OffsetTimeOps.ToOffsetTimeOps
        with instances.PeriodInstances
        with syntax.PeriodOps.ToPeriodOps
        with instances.InstanceTraits.YearInstances
        with syntax.YearOps.ToYearOps
        with instances.InstanceTraits.YearMonthInstances
        with syntax.YearMonthOps.ToYearMonthOps
        with instances.InstanceTraits.ZonedDateTimeInstances
        with syntax.ZonedDateTimeOps.ToZonedDateTimeOps

  }
}
