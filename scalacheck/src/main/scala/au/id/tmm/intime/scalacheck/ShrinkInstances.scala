package au.id.tmm.intime.scalacheck

import java.time._

import au.id.tmm.intime.std.implicits._
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

  private val shrinkLongAlwaysPositive: Shrink[Long] = approachZero(0L)(_ + _, _ / _, -_, includeNegatives = false)

  implicit val shrinkDuration: Shrink[Duration] = approachZero(
    zero = Duration.ZERO,
  )(
    add = _ + _,
    divide = _ / _,
    negate = -_,
    isSmall = _ < Duration.ofMillis(500),
  )

  implicit val shrinkPeriod: Shrink[Period] = approachZero(
    zero = Period.ZERO,
  )(
    add = _ + _,
    divide = (p, d) => Period.of(p.getYears / d, p.getMonths / d, p.getDays / d),
    negate = -_,
  )

  implicit val shrinkInstant: Shrink[Instant] =
    Shrink.xmap[Duration, Instant](Instant.EPOCH + _, Duration.between(Instant.EPOCH, _))

  implicit val shrinkMonth: Shrink[Month] = shrinkEnum(Month.values)

  implicit val shrinkYearMonth: Shrink[YearMonth] = {
    val epoch = YearMonth.from(Instant.EPOCH.atZone(ZoneOffset.UTC))

    Shrink.xmap[Period, YearMonth](epoch + _, ym => Period.between(epoch.atDay(1), ym.atDay(1)))
  }

  implicit val shrinkLocalDate: Shrink[LocalDate] = {
    val epoch = Instant.EPOCH.atZone(ZoneOffset.UTC).toLocalDate

    Shrink.xmap[Period, LocalDate](epoch + _, Period.between(epoch, _))
  }

  implicit val shrinkLocalTime: Shrink[LocalTime] =
    Shrink.xmap[Long, LocalTime](LocalTime.ofNanoOfDay, _.toNanoOfDay)(shrinkLongAlwaysPositive)

  implicit val shrinkZonedDateTime: Shrink[ZonedDateTime] = Shrink { zdt =>
    shrinkInstant
      .shrink(zdt.toInstant)
      .map(_.atZone(zdt.getZone))
  }

  implicit val shrinkLocalDateTime: Shrink[LocalDateTime] =
    Shrink.xmap[ZonedDateTime, LocalDateTime](_.toLocalDateTime, _.atZone(ZoneOffset.UTC))

  implicit val shrinkMonthDay: Shrink[MonthDay] = Shrink { monthDay =>
    for {
      month <- Range.inclusive(monthDay.getMonthValue, 1, -1).toStream.map(Month.of)

      maxDaysForThisMonth =
        if (month == monthDay.getMonth) {
          monthDay.getDayOfMonth - 1
        } else {
          month.maxLength()
        }

      day <- Range.inclusive(maxDaysForThisMonth, 1, -1).toStream
    } yield MonthDay.of(month, day)
  }

  implicit val shrinkZoneOffset: Shrink[ZoneOffset] =
    Shrink.xmap[Int, ZoneOffset](ZoneOffset.ofTotalSeconds, _.getTotalSeconds)

  implicit val shrinkOffsetDateTime: Shrink[OffsetDateTime] = Shrink { odt =>
    shrinkInstant
      .shrink(odt.toInstant)
      .map(_.atOffset(odt.getOffset))
  }

  implicit val shrinkOffsetTime: Shrink[OffsetTime] = Shrink { ot =>
    shrinkLocalTime
      .shrink(ot.toLocalTime)
      .map(_.atOffset(ot.getOffset))
  }

  implicit val shrinkDayOfWeek: Shrink[DayOfWeek] = shrinkEnum(DayOfWeek.values)

  private def shrinkEnum[A : Ordering](all: Iterable[A]): Shrink[A] = {
    val sortedSet = SortedSet[A](all.toVector: _*)

    Shrink { value =>
      sortedSet.until(value).toStream.reverse
    }
  }

  private def approachZero[A](
    zero: A,
  )(
    add: (A, A) => A,
    divide: (A, Int) => A,
    negate: A => A,
    includeNegatives: Boolean = true,
    isSmall: A => Boolean = (a: A) => a == zero,
  ): Shrink[A] = {
    def nextAfter(a: A): Stream[A] = {
      if (a == zero || isSmall(a)) return Stream.empty

      val distanceToZero = add(a, negate(zero))
      val distanceToNext = divide(distanceToZero, 2)

      val next = add(zero, distanceToNext)

      if (next == zero) {
        next #:: Stream.empty
      } else if (includeNegatives) {
        val negativeNext = negate(next)
        next #:: negativeNext #:: nextAfter(next)
      } else {
        next #:: nextAfter(next)
      }
    }

    Shrink(nextAfter)
  }

}

object ShrinkInstances extends ShrinkInstances
