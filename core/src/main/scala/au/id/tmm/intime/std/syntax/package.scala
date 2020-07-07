package au.id.tmm.intime.std

/**
  * Defines operator overloads for classes in the `java.time` package. These are always simple
  * aliases:
  * <ul>
  *   <li>`plus` is aliased to `+`</li>
  *   <li>`minus` is aliased to `-`</li>
  *   <li>`dividedBy` is aliased to `/`</li>
  *   <li>`multipliedBy` is aliased to `*`</li>
  *   <li>`negated` is aliased to `unary_-`</li>
  * </ul>
  *
 * Those classes for which there is an ordering defined in `au.id.tmm.intime.instances.Orderings`
  * also have the comparison operators `<`, `<=`, `>`, `>=`, `equiv`, `min` and `max` defined via
  * `OrderingOps`.
  */
package object syntax {
  object duration       extends DurationOps.ToDurationOps
  object instant        extends InstantOps.ToInstantOps
  object localDate      extends LocalDateOps.ToLocalDateOps
  object localDateTime  extends LocalDateTimeOps.ToLocalDateTimeOps
  object localTime      extends LocalTimeOps.ToLocalTimeOps
  object offsetDateTime extends OffsetDateTimeOps.ToOffsetDateTimeOps
  object offsetTime     extends OffsetTimeOps.ToOffsetTimeOps
  object period         extends PeriodOps.ToPeriodOps
  object year           extends YearOps.ToYearOps
  object yearMonth      extends YearMonthOps.ToYearMonthOps
  object zonedDateTime  extends ZonedDateTimeOps.ToZonedDateTimeOps

  object all
      extends AnyRef
      with DurationOps.ToDurationOps
      with InstantOps.ToInstantOps
      with LocalDateOps.ToLocalDateOps
      with LocalDateTimeOps.ToLocalDateTimeOps
      with LocalTimeOps.ToLocalTimeOps
      with OffsetDateTimeOps.ToOffsetDateTimeOps
      with OffsetTimeOps.ToOffsetTimeOps
      with PeriodOps.ToPeriodOps
      with YearOps.ToYearOps
      with YearMonthOps.ToYearMonthOps
      with ZonedDateTimeOps.ToZonedDateTimeOps
}
