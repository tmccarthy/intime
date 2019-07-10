package au.id.tmm.javatime4s.instances

import java.time._

trait Orderings {
  implicit val durationOrdering:       Ordering[Duration]       = _ compareTo _
  implicit val instantOrdering:        Ordering[Instant]        = _ compareTo _
  implicit val localDateOrdering:      Ordering[LocalDate]      = _ compareTo _
  implicit val localDateTimeOrdering:  Ordering[LocalDateTime]  = _ compareTo _
  implicit val localTimeOrdering:      Ordering[LocalTime]      = _ compareTo _
  implicit val monthDayOrdering:       Ordering[MonthDay]       = _ compareTo _
  implicit val offsetDateTimeOrdering: Ordering[OffsetDateTime] = _ compareTo _
  implicit val offsetTimeOrdering:     Ordering[OffsetTime]     = _ compareTo _
  implicit val yearOrdering:           Ordering[Year]           = _ compareTo _
  implicit val yearMonthOrdering:      Ordering[YearMonth]      = _ compareTo _
  implicit val zonedDateTimeOrdering:  Ordering[ZonedDateTime]  = _ compareTo _
  implicit val zoneOffsetOrdering:     Ordering[ZoneOffset]     = _ compareTo _
  implicit val dayOfWeekOrdering:      Ordering[DayOfWeek]      = _ compareTo _
  implicit val monthOrdering:          Ordering[Month]          = _ compareTo _
}
