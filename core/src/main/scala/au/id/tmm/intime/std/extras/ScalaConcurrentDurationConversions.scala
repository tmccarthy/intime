package au.id.tmm.intime.std.extras

import java.time.{Duration => JDuration}

import au.id.tmm.intime.std.NANOS_PER_SECOND

import scala.concurrent.duration.{
  Duration => SDuration,
  FiniteDuration => SFiniteDuration,
  NANOSECONDS => S_NANOSECONDS,
}

object ScalaConcurrentDurationConversions {

  /**
    * Converts a `java.time.Duration` to a `scala.concurrent.duration.FiniteDuration`.
    *
   * Durations that exceed the maximum size for `scala.concurrent.duration.FiniteDuration` (±(2^63^-1)ns or about 292
    * years) result in an `Errors.JavaTimeDurationTooLargeForScalaConcurrentDurationException` being returned.
    */
  def jDurationToSDuration(
    jDuration: JDuration,
  ): Either[Errors.JavaTimeDurationTooLargeForScalaConcurrentDurationException, SFiniteDuration] =
    try {
      Right(SFiniteDuration(jDuration.toNanos, S_NANOSECONDS))
    } catch {
      case i: IllegalArgumentException =>
        Left(new Errors.JavaTimeDurationTooLargeForScalaConcurrentDurationException(jDuration, i))
      case e: ArithmeticException =>
        Left(new Errors.JavaTimeDurationTooLargeForScalaConcurrentDurationException(jDuration, e))
    }

  /**
    * Converts a `java.time.Duration` to a `scala.concurrent.duration.Duration`.
    *
   * Durations that exceed the maximum size for `scala.concurrent.duration.FiniteDuration` (±(2^63^-1)ns or about 292
    * years) are converted to either `MinusInf` or `Inf` depending on the sign.
    */
  def jDurationToSDurationTotal(jDuration: JDuration): SDuration =
    jDurationToSDuration(jDuration).getOrElse {
      if (jDuration.isNegative) SDuration.MinusInf else SDuration.Inf
    }

  /**
    * Converts a `scala.concurrent.duration.FiniteDuration` to a `java.time.Duration`.
    */
  def sFiniteDurationToJDuration(sFiniteDuration: SFiniteDuration): JDuration =
    JDuration.ofNanos(sFiniteDuration.toNanos)

  /**
    * Converts a `scala.concurrent.duration.Duration` to a `java.time.Duration`.
    *
   * Infinite durations result in an `Errors.ScalaConcurrentDurationIsInfinite` being returned.
    */
  def sDurationToJDuration(sDuration: SDuration): Either[Errors.ScalaConcurrentDurationIsInfinite, JDuration] =
    sDuration match {
      case infinite: SDuration.Infinite     => Left(new Errors.ScalaConcurrentDurationIsInfinite(infinite))
      case sFiniteDuration: SFiniteDuration => Right(sFiniteDurationToJDuration(sFiniteDuration))
    }

  /**
    * Converts a `scala.concurrent.duration.Duration` to a `java.time.Duration`.
    *
   * Infinite durations are converted to the maximum/minimum representable `java.time.Duration`, depending on their
    * size.
    */
  def sDurationToJDurationTotal(sDuration: SDuration): JDuration =
    sDuration match {
      case infinite: SDuration.Infinite =>
        infinite match {
          case d if d == SDuration.MinusInf => JDuration.ofSeconds(Long.MinValue, 0)
          case _                            => JDuration.ofSeconds(Long.MaxValue, NANOS_PER_SECOND - 1)
        }
      case duration: SFiniteDuration => sFiniteDurationToJDuration(duration)
    }

  object Errors {
    final class JavaTimeDurationTooLargeForScalaConcurrentDurationException(val jDuration: JDuration, cause: Exception)
        extends Exception(s"Duration $jDuration does not fit within a ${SDuration.getClass.getCanonicalName}", cause)

    final class ScalaConcurrentDurationIsInfinite(val sDuration: SDuration.Infinite)
        extends Exception(
          s"Duration $sDuration is infinite and does not fit within a ${classOf[JDuration].getCanonicalName}",
        )
  }

  object Syntax {

    final class JDurationOps private[std] (jDuration: JDuration) {
      def toScalaConcurrent
        : Either[Errors.JavaTimeDurationTooLargeForScalaConcurrentDurationException, SFiniteDuration] =
        jDurationToSDuration(jDuration)

      def toScalaConcurrentUnsafe: SFiniteDuration =
        jDurationToSDuration(jDuration) match {
          case Right(sFiniteDuration) => sFiniteDuration
          case Left(e)                => throw e
        }

      def toScalaConcurrentTotal: SDuration = jDurationToSDurationTotal(jDuration)
    }

    final class SDurationOps private[std] (sDuration: SDuration) {
      def toJava: Either[Errors.ScalaConcurrentDurationIsInfinite, JDuration] =
        sDurationToJDuration(sDuration)

      def toJavaUnsafe: JDuration =
        sDurationToJDuration(sDuration) match {
          case Right(jDuration) => jDuration
          case Left(e)          => throw e
        }

      def toJavaTotal: JDuration = sDurationToJDurationTotal(sDuration)
    }

    final class SFiniteDurationOps private[std] (sFiniteDuration: SFiniteDuration) {
      def toJava: JDuration = sFiniteDurationToJDuration(sFiniteDuration)
    }

  }

}
