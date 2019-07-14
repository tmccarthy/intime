package au.id.tmm.javatime4s.scalacheck

import java.time._
import java.time.temporal.ChronoField._
import java.time.temporal.{ChronoField, TemporalAccessor}

import org.scalacheck.Gen
import org.scalacheck.Gen.Choose

trait ChooseInstances {
  implicit val chooseDuration: Choose[Duration] = new ChronoFieldRangeChoose[Duration](
    fields = List(
      INSTANT_SECONDS,
      NANO_OF_SECOND,
    ),
    { case second :: nano :: Nil => Duration.ofSeconds(second, nano) }
  )

  implicit val chooseInstant: Choose[Instant] = new ChronoFieldRangeChoose[Instant](
      List(
        INSTANT_SECONDS,
        NANO_OF_SECOND,
      ),
      { case second :: nano :: Nil => Instant.ofEpochSecond(second, nano) },
    )

  implicit val chooseYear: Choose[Year]                     = (min, max) =>
    Gen.choose[Long](min.getValue, max.getValue).map(l => Year.of(l.toInt))

  implicit val chooseMonth: Choose[Month]                   = (min, max) =>
    Gen.choose[Long](min.getValue, max.getValue).map(l => Month.of(l.toInt))

  implicit val chooseYearMonth: Choose[YearMonth]           = (min, max) =>
    for {
      year <- Gen.choose[Int](min.getYear, max.getYear)

      month <- Gen.choose(
        min = if (year <= min.getYear) min.getMonthValue else MONTH_OF_YEAR.range().getMinimum,
        max = if (year >= max.getYear) max.getMonthValue else MONTH_OF_YEAR.range().getMaximum,
      )
    } yield YearMonth.of(year.toInt, month.toInt)

  implicit val chooseLocalDate: Choose[LocalDate]           = (min, max) => ???

  implicit val chooseLocalTime: Choose[LocalTime] = new ChronoFieldRangeChoose[LocalTime](
    fields = List(
      HOUR_OF_DAY,
      MINUTE_OF_HOUR,
      SECOND_OF_MINUTE,
      NANO_OF_SECOND,
    ),
    { case hour :: minute :: second :: nano :: Nil => LocalTime.of(hour.toInt, minute.toInt, second.toInt, nano.toInt) },
  )

  implicit val chooseLocalDateTime: Choose[LocalDateTime]   = (min, max) => ???
  implicit val chooseMonthDay: Choose[MonthDay]             = (min, max) => ???
  implicit val chooseZoneOffset: Choose[ZoneOffset]         = (min, max) => ???
  implicit val chooseZoneId: Choose[ZoneId]                 = (min, max) => ???
  implicit val chooseOffsetDateTime: Choose[OffsetDateTime] = (min, max) => ???
  implicit val chooseOffsetTime: Choose[OffsetTime]         = (min, max) => ???
  implicit val chooseZonedDateTime: Choose[ZonedDateTime]   = (min, max) => ???
  implicit val chooseDayOfWeek: Choose[DayOfWeek]           = (min, max) => ???
  implicit val choosePeriod: Choose[Period]                 = (min, max) => ???

  private def genBlah[A <: TemporalAccessor](
    min: A,
    max: A,
    fields: List[ChronoField],
    makeA: PartialFunction[List[Long], A],
  ): Gen[A] = {

    def recThing(
      genSoFar: Gen[List[Long]],
      handledFields: List[ChronoField],
      remainingFields: List[ChronoField],
    ): Gen[List[Long]] =

      remainingFields match {
        case thisField :: remainingFields => {
          genSoFar.flatMap { generatedValues =>

            val x = generatedValues zip handledFields
            val atMinBoundary = x.forall { case (generatedValue, field) =>
              generatedValue <= field.getFrom(min)
            }

            val atMaxBoundary = x.forall { case (generatedValue, field) =>
              generatedValue >= field.getFrom(max)
            }

            val generatorForThisFieldValue = Gen.choose[Long](
              min = if (atMinBoundary) thisField.getFrom(min) else thisField.range().getMinimum,
              max = if (atMaxBoundary) thisField.getFrom(max) else thisField.range().getMaximum,
            )

            recThing(generatorForThisFieldValue.map(generatedValues :+ _), handledFields :+ thisField, remainingFields)
          }
        }
        case Nil => genSoFar
      }


    recThing(Gen.const(Nil), Nil, fields)
      .map(makeA)
  }

  private final class ChronoFieldRangeChoose[A <: TemporalAccessor](
    fields: List[ChronoField],
    makeA: PartialFunction[List[Long], A],
  ) extends Choose[A] {
    override def choose(min: A, max: A): Gen[A] = makeA(generateFieldValues(min, max))

    private def generateFieldValues(
      min: A,
      max: A,
      generatorForPreviousFields: Gen[List[Long]] = Gen.const(Nil),
      fieldsAlreadyGenerated: List[ChronoField] = Nil,
      fieldsRemaining: List[ChronoField] = this.fields,
    ): Gen[List[Long]] = fieldsRemaining match {
      case Nil => generatorForPreviousFields
      case thisField :: fieldsRemaining =>
        generatorForPreviousFields.flatMap { valuesForPreviousFields =>

          val valuesWithHandledFields: List[(Long, ChronoField)] =
            valuesForPreviousFields zip fieldsAlreadyGenerated

          val isAtMinBoundary = valuesWithHandledFields.forall { case (generatedValue, field) =>
            generatedValue <= field.getFrom(min)
          }

          val isAtMaxBoundary = valuesWithHandledFields.forall { case (generatedValue, field) =>
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

object ChooseInstances extends ChooseInstances
