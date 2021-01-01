package au.id.tmm.intime.std.extras

import java.time.{Duration => JDuration}

import scala.concurrent.duration.{
  Duration => SDuration,
  FiniteDuration => SFiniteDuration,
  NANOSECONDS => S_NANOSECONDS,
}

object ScalaConcurrentDurationConversions {

  // TODO scaladoc should explain option
  def jDurationToSDuration(
    jDuration: JDuration,
  ): Either[Errors.JavaTimeDurationTooLargeForScalaConcurrentDurationException, SFiniteDuration] =
    try {
      Right(SFiniteDuration(jDuration.toNanos, S_NANOSECONDS))
    } catch {
      case i: IllegalArgumentException =>
        Left(Errors.JavaTimeDurationTooLargeForScalaConcurrentDurationException(jDuration)(i))
      case e: ArithmeticException =>
        Left(Errors.JavaTimeDurationTooLargeForScalaConcurrentDurationException(jDuration)(e))
    }

  def sFiniteDurationToJDuration(sFiniteDuration: SFiniteDuration): JDuration =
    JDuration.ofNanos(sFiniteDuration.toNanos)

  def sDurationToJDuration(sDuration: SDuration): Option[JDuration] =
    sDuration match {
      case infinite: SDuration.Infinite     => None
      case sFiniteDuration: SFiniteDuration => Some(sFiniteDurationToJDuration(sFiniteDuration))
    }

  object Errors {
    final case class JavaTimeDurationTooLargeForScalaConcurrentDurationException(jDuration: JDuration)(cause: Exception)
        extends Exception(s"Duration $jDuration does not fit within a ${SDuration.getClass.getCanonicalName}", cause)
  }

}
