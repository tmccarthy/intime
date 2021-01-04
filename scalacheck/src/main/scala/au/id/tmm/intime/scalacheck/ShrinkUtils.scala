package au.id.tmm.intime.scalacheck

import com.github.ghik.silencer.silent
import org.scalacheck.Shrink

import scala.collection.SortedSet

@silent("deprecated")
private[scalacheck] object ShrinkUtils {

  def shrinkEnum[A : Ordering](all: Iterable[A]): Shrink[A] = {
    val sortedSet = SortedSet[A](all.toVector: _*)

    Shrink { value =>
      sortedSet.until(value).toStream.reverse
    }
  }

  def approachZero[A](
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
