package au.id.tmm.intime.std.extras

import java.time.temporal.ChronoField
import java.time.{Duration => JDuration}

import scala.concurrent.duration.{
  Duration => SDuration,
  FiniteDuration => SFiniteDuration,
  NANOSECONDS => S_NANOSECONDS,
}

// TODO these all need docs
object ScalaConcurrentDurationConversions {

  private[extras] val NANOS_PER_SECOND: Long = ChronoField.NANO_OF_SECOND.range().getMaximum + 1

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

  def jDurationToSDurationTotal(jDuration: JDuration): SDuration =
    jDurationToSDuration(jDuration).getOrElse {
      if (jDuration.isNegative) SDuration.MinusInf else SDuration.Inf
    }

  def sFiniteDurationToJDuration(sFiniteDuration: SFiniteDuration): JDuration =
    JDuration.ofNanos(sFiniteDuration.toNanos)

  def sDurationToJDuration(sDuration: SDuration): Either[Errors.ScalaConcurrentDurationIsInfinite, JDuration] =
    sDuration match {
      case infinite: SDuration.Infinite     => Left(new Errors.ScalaConcurrentDurationIsInfinite(infinite))
      case sFiniteDuration: SFiniteDuration => Right(sFiniteDurationToJDuration(sFiniteDuration))
    }

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
        : Either[Errors.JavaTimeDurationTooLargeForScalaConcurrentDurationException, SFiniteDuration] = ???

      def toScalaConcurrentUnsafe: SFiniteDuration = ???

      def toScalaConcurrentOrInfinite: SDuration = ???
    }

    object JDurationOps {
      trait ToJDurationOps {}
    }

    final class SDurationOps private (sDuration: SDuration) {
      def toJava: Either[Errors.ScalaConcurrentDurationIsInfinite, JDuration] = ???

      def toJavaUnsafe: JDuration = ???

      def toJavaTotal: JDuration = ???
    }

    final class SFiniteDurationOps private[ScalaConcurrentDurationConversions] (sFiniteDuration: SFiniteDuration) {
      def toJava: JDuration = ???
    }

    object SDurationOps {
      trait ToSDurationOps extends LowPriorityToSDurationOps {
        implicit def toSFiniteDurationOps(sFiniteDuration: SFiniteDuration): SFiniteDurationOps =
          new SFiniteDurationOps(sFiniteDuration)
      }

      trait LowPriorityToSDurationOps {
        implicit def toSDurationOps(sDuration: SDuration): SDurationOps = new SDurationOps(sDuration)
      }
    }

  }

}
