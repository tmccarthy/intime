package au.id.tmm.intime.std.syntax

import scala.math.Ordering

// This class is the equivalent of Ordering.Ops in 2.12 and Ordering.OrderingOps in 2.13. The
// rename means that in order to cross compile we have to define one ourselves.
abstract class PartialOrderingOps[T] private[syntax] (lhs: T)(implicit partialOrdering: PartialOrdering[T]) {
  def <(rhs: T): Boolean     = partialOrdering.lt(lhs, rhs)
  def <=(rhs: T): Boolean    = partialOrdering.lteq(lhs, rhs)
  def >(rhs: T): Boolean     = partialOrdering.gt(lhs, rhs)
  def >=(rhs: T): Boolean    = partialOrdering.gteq(lhs, rhs)
  def equiv(rhs: T): Boolean = partialOrdering.equiv(lhs, rhs)
}

// This class is the equivalent of Ordering.Ops in 2.12 and Ordering.OrderingOps in 2.13. The
// rename means that in order to cross compile we have to define one ourselves.
abstract class OrderingOps[T] private[syntax] (lhs: T)(implicit ordering: Ordering[T])
    extends PartialOrderingOps[T](lhs)(ordering) {
  def max(rhs: T): T = ordering.max(lhs, rhs)
  def min(rhs: T): T = ordering.min(lhs, rhs)
}
