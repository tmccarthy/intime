package au.id.tmm.intime.std.extras

import java.time.{Duration => JDuration}

import scala.concurrent.duration.{Duration => SDuration, FiniteDuration => SFiniteDuration}

object ScalaConcurrentDurationConversions {

  // TODO scaladoc should explain option
  def jDurationToSDuration(jDuration: JDuration): Option[SFiniteDuration] = ???

  def sFiniteDurationToJDuration(sFiniteDuration: SFiniteDuration): JDuration =
    JDuration.ofNanos(sFiniteDuration.toNanos)

  def sDurationToJDuration(sDuration: SDuration): Option[JDuration] = sDuration match {
    case infinite: SDuration.Infinite => None
    case sFiniteDuration: SFiniteDuration => Some(sFiniteDurationToJDuration(sFiniteDuration))
  }

}
