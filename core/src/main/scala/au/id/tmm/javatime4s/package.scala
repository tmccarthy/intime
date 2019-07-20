package au.id.tmm

import java.time.temporal.ChronoField

import au.id.tmm.javatime4s.orderings.CanonicalOrderings
import au.id.tmm.javatime4s.syntax.Syntaxes

package object javatime4s extends CanonicalOrderings with Syntaxes {
  private[javatime4s] val NANOS_PER_SECOND = ChronoField.NANO_OF_SECOND.range().getMaximum + 1
}
