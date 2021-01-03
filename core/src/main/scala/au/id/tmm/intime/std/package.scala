package au.id.tmm.intime

import java.time.temporal.ChronoField

package object std {

  private[intime] val NANOS_PER_SECOND: Long = ChronoField.NANO_OF_SECOND.range().getMaximum + 1

}
