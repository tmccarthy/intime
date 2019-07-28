package au.id.tmm.intime.scalacheck

import java.time._

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

  implicit val shrinkDuration: Shrink[Duration] = approachZero(Duration.ZERO, _ + _, _ / _, -_)

  implicit val shrinkPeriod: Shrink[Period] = approachZero(
    zero = Period.ZERO,
    add = _ + _,
    divide = (p, d) => Period.of(p.getYears / d, p.getMonths / d, p.getDays / d),
    negate = -_,
  )

  implicit val shrinkInstant: Shrink[Instant] =
    Shrink.xmap[Duration, Instant](Instant.EPOCH + _, Duration.between(Instant.EPOCH, _))

  implicit val shrinkYear: Shrink[Year] = {
    val epoch = Year.from(LocalDate.EPOCH)

    Shrink.xmap[Int, Year](epoch.plusYears(_), y => y.minusYears(epoch.getValue).getValue)
  }

  implicit val shrinkMonth: Shrink[Month] = shrinkEnum(Month.values)

  implicit val shrinkYearMonth: Shrink[YearMonth] = {
    val epoch = YearMonth.from(LocalDate.EPOCH)

    Shrink.xmap[Period, YearMonth](epoch + _, ym => Period.between(epoch.atDay(1), ym.atDay(1)))
  }

  implicit val shrinkLocalDate: Shrink[LocalDate] =
    Shrink.xmap[Period, LocalDate](LocalDate.EPOCH + _, Period.between(LocalDate.EPOCH, _))

  implicit val shrinkLocalTime: Shrink[LocalTime] = Shrink { localTime =>
    Shrink
      .shrink(localTime.toNanoOfDay)
      .filter(_ >= 0)
      .map(LocalTime.ofNanoOfDay)
  }

  implicit val shrinkZonedDateTime: Shrink[ZonedDateTime] = Shrink { zdt =>
    shrinkInstant
      .shrink(zdt.toInstant)
      .map(_.atZone(zdt.getZone))
  }

  implicit val shrinkLocalDateTime: Shrink[LocalDateTime] =
    Shrink.xmap[ZonedDateTime, LocalDateTime](_.toLocalDateTime, _.atZone(ZoneOffset.UTC))

  implicit val shrinkMonthDay: Shrink[MonthDay] = Shrink.shrinkAny

  implicit val shrinkZoneOffset: Shrink[ZoneOffset] = Shrink.shrinkAny

  implicit val shrinkOffsetDateTime: Shrink[OffsetDateTime] = Shrink.shrinkAny

  implicit val shrinkOffsetTime: Shrink[OffsetTime] = Shrink.shrinkAny

  implicit val shrinkDayOfWeek: Shrink[DayOfWeek] = shrinkEnum(DayOfWeek.values)

  private def shrinkEnum[A : Ordering](all: Iterable[A]): Shrink[A] = {
    val sortedSet = SortedSet[A](all.toVector: _*)

    Shrink { value =>
      sortedSet.until(value).toStream.reverse
    }
  }

  // TODO short-circuit when you're close enough to zero
  private def approachZero[A](
    zero: A,
    add: (A, A) => A,
    divide: (A, Int) => A,
    negate: A => A,
  ): Shrink[A] = {
    def nextAfter(a: A): Stream[A] = {
      if (a == zero) return Stream.empty

      val distanceToZero = add(a, negate(zero))
      val distanceToNext = divide(distanceToZero, 2)

      val next         = add(zero, distanceToNext)
      val negativeNext = negate(next)

      if (next == zero) {
        next #:: Stream.empty
      } else if (next == negativeNext) {
        next #:: nextAfter(next)
      } else {
        next #:: negativeNext #:: nextAfter(next)
      }
    }

    Shrink(nextAfter)
  }

}

object ShrinkInstances extends ShrinkInstances
