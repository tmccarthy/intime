package au.id.tmm.intime.scalacheck

import java.time.temporal.{TemporalAccessor, TemporalField, ValueRange => TemporalValueRange}

import org.scalacheck.Gen

object TemporalGenUtils {

  def genField(temporalField: TemporalField): Gen[Long] =
    genFrom(temporalField.range())

  def genIntField(temporalField: TemporalField): Gen[Int] =
    genField(temporalField).map(_.toInt)

  def genFieldAt(temporalField: TemporalField, temporal: TemporalAccessor): Gen[Long] =
    genFrom(temporalField.rangeRefinedBy(temporal))

  def genIntFieldAt(temporalField: TemporalField, temporal: TemporalAccessor): Gen[Int] =
    genFieldAt(temporalField, temporal).map(_.toInt)

  def genFrom(range: TemporalValueRange): Gen[Long] =
    Gen.choose(range.getMinimum, range.getMaximum)

}
