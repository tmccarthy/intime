package au.id.tmm.intime.std

package object syntax {
  object duration       extends Syntaxes.DurationOps.ToDurationOps
  object instant        extends Syntaxes.InstantOps.ToInstantOps
  object localDate      extends Syntaxes.LocalDateOps.ToLocalDateOps
  object localDateTime  extends Syntaxes.LocalDateTimeOps.ToLocalDateTimeOps
  object localTime      extends Syntaxes.LocalTimeOps.ToLocalTimeOps
  object offsetDateTime extends Syntaxes.OffsetDateTimeOps.ToOffsetDateTimeOps
  object offsetTime     extends Syntaxes.OffsetTimeOps.ToOffsetTimeOps
  object period         extends Syntaxes.PeriodOps.ToPeriodOps
  object year           extends Syntaxes.YearOps.ToYearOps
  object yearMonth      extends Syntaxes.YearMonthOps.ToYearMonthOps
  object zonedDateTime  extends Syntaxes.ZonedDateTimeOps.ToZonedDateTimeOps

  object all
      extends AnyRef
      with Syntaxes.DurationOps.ToDurationOps
      with Syntaxes.InstantOps.ToInstantOps
      with Syntaxes.LocalDateOps.ToLocalDateOps
      with Syntaxes.LocalDateTimeOps.ToLocalDateTimeOps
      with Syntaxes.LocalTimeOps.ToLocalTimeOps
      with Syntaxes.OffsetDateTimeOps.ToOffsetDateTimeOps
      with Syntaxes.OffsetTimeOps.ToOffsetTimeOps
      with Syntaxes.PeriodOps.ToPeriodOps
      with Syntaxes.YearOps.ToYearOps
      with Syntaxes.YearMonthOps.ToYearMonthOps
      with Syntaxes.ZonedDateTimeOps.ToZonedDateTimeOps
}
