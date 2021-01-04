package au.id.tmm.intime.scalacheck

import java.time.temporal.{ChronoField, TemporalAccessor}

import org.scalacheck.Gen
import org.scalacheck.Gen.Choose

private[scalacheck] object ChooseUtils {

  def combineChooses[T1, T2, O](
    extractT1: O => T1,
    extractT2: O => T2,
    combine: (T1, T2) => O,
    t2Floor: T1 => T2,
    t2Ceil: T1 => T2,
  )(implicit
    t1Choose: Choose[T1],
    t1Ordering: Ordering[T1],
    t2Choose: Choose[T2],
  ): Choose[O] =
    (minO, maxO) => {
      val minT1 = extractT1(minO)
      val minT2 = extractT1(maxO)

      for {
        t1 <- t1Choose.choose(minT1, minT2)

        t2 <- t2Choose.choose(
          min = if (t1Ordering.lteq(t1, minT1)) extractT2(minO) else t2Floor(t1),
          max = if (t1Ordering.gteq(t1, minT2)) extractT2(maxO) else t2Ceil(t1),
        )
      } yield combine(t1, t2)
    }

  def makeChronoFieldRangeChoose[A <: TemporalAccessor](
    fields: ChronoField*,
  )(
    makeA: PartialFunction[List[Long], A],
  ): Choose[A] =
    new Choose[A] {
      override def choose(min: A, max: A): Gen[A] = generateFieldValues(min, max).map(makeA)

      private def generateFieldValues(
        min: A,
        max: A,
        generatorForPreviousFields: Gen[List[Long]] = Gen.const(Nil),
        fieldsAlreadyGenerated: List[ChronoField] = Nil,
        fieldsRemaining: List[ChronoField] = fields.toList,
      ): Gen[List[Long]] =
        fieldsRemaining match {
          case Nil => generatorForPreviousFields
          case thisField :: fieldsRemaining =>
            generatorForPreviousFields.flatMap { valuesForPreviousFields =>
              val valuesWithHandledFields: List[(Long, ChronoField)] =
                valuesForPreviousFields zip fieldsAlreadyGenerated

              val isAtMinBoundary = valuesWithHandledFields.forall {
                case (generatedValue, field) =>
                  generatedValue <= field.getFrom(min)
              }

              val isAtMaxBoundary = valuesWithHandledFields.forall {
                case (generatedValue, field) =>
                  generatedValue >= field.getFrom(max)
              }

              val generatorForThisFieldValue = Gen.choose[Long](
                min = if (isAtMinBoundary) thisField.getFrom(min) else thisField.range().getMinimum,
                max = if (isAtMaxBoundary) thisField.getFrom(max) else thisField.range().getMaximum,
              )

              generateFieldValues(
                min,
                max,
                generatorForThisFieldValue.map(valuesForPreviousFields :+ _),
                fieldsAlreadyGenerated :+ thisField,
                fieldsRemaining,
              )
            }

        }
    }

}
