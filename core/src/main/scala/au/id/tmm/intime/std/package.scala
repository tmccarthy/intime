package au.id.tmm.intime

import au.id.tmm.intime.std.syntax.Syntaxes

package object std {

  object implicits {

    // format: off
    object duration       extends AnyRef with instances.InstanceTraits.DurationInstances       with Syntaxes.DurationOps.ToDurationOps
    object instant        extends AnyRef with instances.InstanceTraits.InstantInstances        with Syntaxes.InstantOps.ToInstantOps
    object localDate      extends AnyRef with instances.InstanceTraits.LocalDateInstances      with Syntaxes.LocalDateOps.ToLocalDateOps
    object localDateTime  extends AnyRef with instances.InstanceTraits.LocalDateTimeInstances  with Syntaxes.LocalDateTimeOps.ToLocalDateTimeOps
    object localTime      extends AnyRef with instances.InstanceTraits.LocalTimeInstances      with Syntaxes.LocalTimeOps.ToLocalTimeOps
    object offsetDateTime extends AnyRef with instances.InstanceTraits.OffsetDateTimeInstances with Syntaxes.OffsetDateTimeOps.ToOffsetDateTimeOps
    object offsetTime     extends AnyRef with instances.InstanceTraits.OffsetTimeInstances     with Syntaxes.OffsetTimeOps.ToOffsetTimeOps
    object period         extends AnyRef with instances.PeriodInstances                        with Syntaxes.PeriodOps.ToPeriodOps
    object year           extends AnyRef with instances.InstanceTraits.YearInstances           with Syntaxes.YearOps.ToYearOps
    object yearMonth      extends AnyRef with instances.InstanceTraits.YearMonthInstances      with Syntaxes.YearMonthOps.ToYearMonthOps
    object zonedDateTime  extends AnyRef with instances.InstanceTraits.ZonedDateTimeInstances  with Syntaxes.ZonedDateTimeOps.ToZonedDateTimeOps
    // format: on

    object all
        extends AnyRef
        with instances.InstanceTraits.DurationInstances
        with Syntaxes.DurationOps.ToDurationOps
        with instances.InstanceTraits.InstantInstances
        with Syntaxes.InstantOps.ToInstantOps
        with instances.InstanceTraits.LocalDateInstances
        with Syntaxes.LocalDateOps.ToLocalDateOps
        with instances.InstanceTraits.LocalDateTimeInstances
        with Syntaxes.LocalDateTimeOps.ToLocalDateTimeOps
        with instances.InstanceTraits.LocalTimeInstances
        with Syntaxes.LocalTimeOps.ToLocalTimeOps
        with instances.InstanceTraits.OffsetDateTimeInstances
        with Syntaxes.OffsetDateTimeOps.ToOffsetDateTimeOps
        with instances.InstanceTraits.OffsetTimeInstances
        with Syntaxes.OffsetTimeOps.ToOffsetTimeOps
        with instances.PeriodInstances
        with Syntaxes.PeriodOps.ToPeriodOps
        with instances.InstanceTraits.YearInstances
        with Syntaxes.YearOps.ToYearOps
        with instances.InstanceTraits.YearMonthInstances
        with Syntaxes.YearMonthOps.ToYearMonthOps
        with instances.InstanceTraits.ZonedDateTimeInstances
        with Syntaxes.ZonedDateTimeOps.ToZonedDateTimeOps

  }
}
