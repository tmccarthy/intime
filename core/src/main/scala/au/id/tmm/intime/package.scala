package au.id.tmm

import java.time.temporal.ChronoField

import au.id.tmm.intime.orderings.CanonicalOrderings
import au.id.tmm.intime.syntax.Syntaxes

package object intime extends CanonicalOrderings with Syntaxes {
  private[intime] val NANOS_PER_SECOND = ChronoField.NANO_OF_SECOND.range().getMaximum + 1
}
