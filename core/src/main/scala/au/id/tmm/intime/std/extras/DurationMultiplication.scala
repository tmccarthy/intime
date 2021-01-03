package au.id.tmm.intime.std.extras

import java.lang.Double.isFinite
import java.time.Duration

import au.id.tmm.intime.std.NANOS_PER_SECOND

object DurationMultiplication {

  private val NANOS_PER_SECOND_BIGINT = BigInt(NANOS_PER_SECOND)

  def multiply(duration: Duration, k: Double): Duration =
    k match {
      case 1                 => duration
      case -1                => duration.negated
      case 0                 => Duration.ZERO
      case d if !isFinite(d) => throw new ArithmeticException(d.toString)
      case _ => {
        val safeNanos: BigInt = (BigInt(duration.getSeconds) * NANOS_PER_SECOND_BIGINT) + BigInt(duration.getNano)

        val newNanos: BigInt = (BigDecimal(safeNanos) * k).toBigInt

        val newSecondsPart: BigInt = newNanos / NANOS_PER_SECOND_BIGINT
        val newNanosPart: BigInt   = newNanos % NANOS_PER_SECOND_BIGINT

        if (!newSecondsPart.isValidLong || !newNanosPart.isValidLong) {
          throw new ArithmeticException(s"$newSecondsPart, $newNanosPart")
        } else {
          Duration.ofSeconds(newSecondsPart.toLong, newNanosPart.toLong)
        }
      }
    }

  def divide(duration: Duration, k: Double): Duration = multiply(duration, 1d / k)

  object Syntax {
    private[std] trait DurationMultiplicationOps {
      protected def duration: Duration

      def multipliedBy(k: Double): Duration = DurationMultiplication.multiply(duration, k)
      def *(k: Double): Duration            = DurationMultiplication.multiply(duration, k)

      def dividedBy(k: Double): Duration = DurationMultiplication.divide(duration, k)
      def /(k: Double): Duration         = DurationMultiplication.divide(duration, k)
    }
  }

}
