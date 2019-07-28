package au.id.tmm.intime.scalacheck

import java.time._
import java.time.temporal.Temporal

import au.id.tmm.intime._
import com.github.ghik.silencer.silent
import org.scalacheck.Shrink

import scala.collection.SortedSet

/**
  * Defines instances of `org.scalacheck.Shrink` for those classes in the `java.time` package for which an instance can
  * be defined. Broadly, classes that represent a point on the time line are shrunk toward epoch (`1970-01-01T00:00Z`).
  * Others are shrunk toward their minimum value.
  */
//noinspection ScalaDeprecation
@silent("deprecated")
trait ShrinkInstances {

  implicit val shrinkDuration: Shrink[Duration] = shrinkToEpoch[Duration, Duration](
    epoch = Duration.ZERO,
    difference = _ - _,
    addDuration = _ + _,
    divideDuration = _ / _,
    negateDuration = -_,
  )

  implicit val shrinkInstant: Shrink[Instant] = shrinkToEpochUsingDuration(Instant.EPOCH, _ + _)

  implicit val shrinkYear: Shrink[Year] = Shrink.xmap[Int, Year](Year.of, _.getValue)(
    shrinkToEpoch[Int, Int](
      epoch = LocalDate.EPOCH.getYear,
      difference = _ - _,
      addDuration = _ + _,
      divideDuration = _ / _,
      negateDuration = -_,
    ),
  )

  implicit val shrinkMonth: Shrink[Month] = shrinkEnum(Month.values)

  implicit val shrinkYearMonth: Shrink[YearMonth] = shrinkToEpochUsingPeriod(
    epoch = YearMonth.from(LocalDate.EPOCH),
    difference = (ym1, ym2) => Period.between(ym2.atDay(1), ym1.atDay(1)),
    addDuration = _ + _,
  )

  implicit val shrinkLocalDate: Shrink[LocalDate] = shrinkToEpochUsingPeriod(
    epoch = LocalDate.EPOCH,
    difference = (d1, d2) => Period.between(d2, d1),
    addDuration = _ + _,
  )

  implicit val shrinkLocalTime: Shrink[LocalTime] = Shrink.shrinkAny

  implicit val shrinkLocalDateTime: Shrink[LocalDateTime] = Shrink.shrinkAny

  implicit val shrinkMonthDay: Shrink[MonthDay] = Shrink.shrinkAny

  implicit val shrinkZoneOffset: Shrink[ZoneOffset] = Shrink.shrinkAny

  implicit val shrinkOffsetDateTime: Shrink[OffsetDateTime] = Shrink.shrinkAny

  implicit val shrinkOffsetTime: Shrink[OffsetTime] = Shrink.shrinkAny

  implicit val shrinkZonedDateTime: Shrink[ZonedDateTime] = Shrink.shrinkAny

  implicit val shrinkDayOfWeek: Shrink[DayOfWeek] = shrinkEnum(DayOfWeek.values)

  implicit val shrinkPeriod: Shrink[Period] = Shrink.shrinkAny

  private def shrinkEnum[A : Ordering](all: Iterable[A]): Shrink[A] = {
    val sortedSet = SortedSet[A](all.toVector: _*)

    Shrink { value =>
      sortedSet.until(value).toStream.reverse
    }
  }

  private def shrinkToEpochUsingDuration[A <: Temporal](epoch: A, addDuration: (A, Duration) => A): Shrink[A] =
    shrinkToEpoch[A, Duration](
      epoch,
      difference = (a1, a2) => Duration.between(a2, a1),
      addDuration,
      divideDuration = _ / _,
      negateDuration = -_,
    )

  private def shrinkToEpochUsingPeriod[A <: Temporal](
    epoch: A,
    difference: (A, A) => Period,
    addDuration: (A, Period) => A,
  ): Shrink[A] =
    shrinkToEpoch[A, Period](
      epoch,
      difference,
      addDuration,
      divideDuration = (p, d) => Period.of(p.getYears / d, p.getMonths / d, p.getDays / d),
      negateDuration = -_,
    )

  private def shrinkToEpoch[A, D](
    epoch: A,
    difference: (A, A) => D,
    addDuration: (A, D) => A,
    divideDuration: (D, Int) => D,
    negateDuration: D => D,
  ): Shrink[A] = {
    def nextAfter(a: A): Stream[A] = {
      if (a == epoch) return Stream.empty

      val durationToEpoch = difference(a, epoch)
      val durationToNext  = divideDuration(durationToEpoch, 2)

      val next         = addDuration(epoch, durationToNext)
      val negativeNext = addDuration(epoch, negateDuration(durationToNext))

      if (next == epoch) {
        next #:: Stream.empty
      } else {

        next #:: negativeNext #:: nextAfter(next)
      }
    }

    Shrink(nextAfter)
  }

}

object ShrinkInstances extends ShrinkInstances
