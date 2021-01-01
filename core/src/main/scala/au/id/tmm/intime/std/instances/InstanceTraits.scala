package au.id.tmm.intime.std.instances

import java.time._

/**
  * Most of these `Ordering` instances are provided by an implicit conversion for `Comparable` types. However, some
  * types (like `LocalDate`) encounter issues due to type variance in Scala 2.12. Once support for 2.12 is dropped,
  * these traits can probably be deleted. See https://github.com/tmccarthy/intime/issues/15
  */
object InstanceTraits {

  trait DurationInstances {
    implicit val intimeOrderingForJavaTimeDuration: Ordering[Duration] = _ compareTo _
  }

  trait InstantInstances {
    implicit val intimeOrderingForJavaTimeInstant: Ordering[Instant] = _ compareTo _
  }

  trait LocalDateInstances {
    implicit val intimeOrderingForJavaTimeLocalDate: Ordering[LocalDate] = _ compareTo _
  }

  trait LocalDateTimeInstances {
    implicit val intimeOrderingForJavaTimeLocalDateTime: Ordering[LocalDateTime] = _ compareTo _
  }

  trait LocalTimeInstances {
    implicit val intimeOrderingForJavaTimeLocalTime: Ordering[LocalTime] = _ compareTo _
  }

  trait MonthDayInstances {
    implicit val intimeOrderingForJavaTimeMonthDay: Ordering[MonthDay] = _ compareTo _
  }

  trait OffsetDateTimeInstances {
    implicit val intimeOrderingForJavaTimeOffsetDateTime: Ordering[OffsetDateTime] = _ compareTo _
  }

  trait OffsetTimeInstances {
    implicit val intimeOrderingForJavaTimeOffsetTime: Ordering[OffsetTime] = _ compareTo _
  }

  trait YearInstances {
    implicit val intimeOrderingForJavaTimeYear: Ordering[Year] = _ compareTo _
  }

  trait YearMonthInstances {
    implicit val intimeOrderingForJavaTimeYearMonth: Ordering[YearMonth] = _ compareTo _
  }

  trait ZonedDateTimeInstances {
    implicit val intimeOrderingForJavaTimeZonedDateTime: Ordering[ZonedDateTime] = _ compareTo _
  }

  trait ZoneOffsetInstances {
    implicit val intimeOrderingForJavaTimeZoneOffset: Ordering[ZoneOffset] = _ compareTo _
  }

  trait DayOfWeekInstances {
    implicit val intimeOrderingForJavaTimeDayOfWeek: Ordering[DayOfWeek] = _ compareTo _
  }

  trait MonthInstances {
    implicit val intimeOrderingForJavaTimeMonth: Ordering[Month] = _ compareTo _
  }

}
