package au.id.tmm.intime.scalacheck

import java.time._

import org.scalacheck.Shrink
import org.scalatest.FlatSpec

class ShrinkInstancesSpec extends FlatSpec {

  behavior of "the shrink for Duration"

  testShrink(Duration.ofDays(1024))(
    Duration.ofDays(512),
    Duration.ofDays(-512),
    Duration.ofDays(256),
    Duration.ofDays(-256),
    Duration.ofDays(128),
    Duration.ofDays(-128),
    Duration.ofDays(64),
    Duration.ofDays(-64),
    Duration.ofDays(32),
    Duration.ofDays(-32),
    Duration.ofDays(16),
    Duration.ofDays(-16),
    Duration.ofDays(8),
    Duration.ofDays(-8),
    Duration.ofDays(4),
    Duration.ofDays(-4),
    Duration.ofDays(2),
    Duration.ofDays(-2),
    Duration.ofDays(1),
    Duration.ofDays(-1),
    Duration.ofHours(12),
    Duration.ofHours(-12),
    Duration.ofHours(6),
    Duration.ofHours(-6),
    Duration.ofHours(3),
    Duration.ofHours(-3),
  )

  testShrinkIsEmpty(Duration.ZERO)

  behavior of "the shrink for Instant"

  testShrink(Instant.parse("2019-07-28T03:28:00Z"))(
    Instant.parse("1994-10-14T13:44:00Z"),
    Instant.parse("1945-03-20T10:16:00Z"),
    Instant.parse("1982-05-24T06:52:00Z"),
    Instant.parse("1957-08-10T17:08:00Z"),
    Instant.parse("1976-03-13T03:26:00Z"),
    Instant.parse("1963-10-21T20:34:00Z"),
    Instant.parse("1973-02-05T13:43:00Z"),
    Instant.parse("1966-11-26T10:17:00Z"),
    Instant.parse("1971-07-20T18:51:30Z"),
    Instant.parse("1968-06-14T05:08:30Z"),
    Instant.parse("1970-10-10T21:25:45Z"),
    Instant.parse("1969-03-24T02:34:15Z"),
    Instant.parse("1970-05-22T10:42:52.500Z"),
    Instant.parse("1969-08-12T13:17:07.500Z"),
    Instant.parse("1970-03-12T17:21:26.250Z"),
    Instant.parse("1969-10-22T06:38:33.750Z"),
    Instant.parse("1970-02-05T08:40:43.125Z"),
    Instant.parse("1969-11-26T15:19:16.875Z"),
    Instant.parse("1970-01-18T16:20:21.562500Z"),
    Instant.parse("1969-12-14T07:39:38.437500Z"),
  )

  testShrinkIsEmpty(Instant.EPOCH)

  behavior of "the shrink for Year"

  testShrink(Year.of(2019))(
    Year.of(1994),
    Year.of(1946),
    Year.of(1982),
    Year.of(1958),
    Year.of(1976),
    Year.of(1964),
    Year.of(1973),
    Year.of(1967),
    Year.of(1971),
    Year.of(1969),
    Year.of(1970),
  )

  testShrinkIsEmpty(YearMonth.of(1970, 1))

  behavior of "the shrink for Month"

  testShrink(Month.DECEMBER)(
    Month.NOVEMBER,
    Month.OCTOBER,
    Month.SEPTEMBER,
    Month.AUGUST,
    Month.JULY,
    Month.JUNE,
    Month.MAY,
    Month.APRIL,
    Month.MARCH,
    Month.FEBRUARY,
    Month.JANUARY,
  )

  testShrink(Month.MARCH)(
    Month.FEBRUARY,
    Month.JANUARY,
  )

  testShrinkIsEmpty(Month.JANUARY)

  behavior of "the shrink for YearMonth"

  testShrink(YearMonth.of(2019, 7))(
    YearMonth.of(1994, 4),
    YearMonth.of(1945, 10),
    YearMonth.of(1982, 2),
    YearMonth.of(1957, 12),
    YearMonth.of(1976, 1),
    YearMonth.of(1964, 1),
    YearMonth.of(1973, 1),
    YearMonth.of(1967, 1),
    YearMonth.of(1971, 1),
    YearMonth.of(1969, 1),
    YearMonth.of(1970, 1),
  )

  testShrinkIsEmpty(YearMonth.of(1970, 1))

  behavior of "the shrink for LocalDate"

  testShrink(LocalDate.of(2019, 7, 28))(
    LocalDate.of(1994, 4, 14),
    LocalDate.of(1945, 9, 18),
    LocalDate.of(1982, 2, 7),
    LocalDate.of(1957, 11, 25),
    LocalDate.of(1976, 1, 4),
    LocalDate.of(1963, 12, 29),
    LocalDate.of(1973, 1, 2),
    LocalDate.of(1966, 12, 31),
    LocalDate.of(1971, 1, 1),
    LocalDate.of(1969, 1, 1),
    LocalDate.of(1970, 1, 1),
  )

  testShrinkIsEmpty(LocalDate.EPOCH)

  behavior of "the shrink for LocalTime"

  testShrink(LocalTime.of(13, 49, 58, 65536))(
    LocalTime.parse("06:54:59.000032768"),
    LocalTime.parse("03:27:29.500016384"),
    LocalTime.parse("01:43:44.750008192"),
    LocalTime.parse("00:51:52.375004096"),
    LocalTime.parse("00:25:56.187502048"),
    LocalTime.parse("00:12:58.093751024"),
    LocalTime.parse("00:06:29.046875512"),
    LocalTime.parse("00:03:14.523437756"),
    LocalTime.parse("00:01:37.261718878"),
    LocalTime.parse("00:00:48.630859439"),
    LocalTime.parse("00:00:24.315429719"),
    LocalTime.parse("00:00:12.157714859"),
    LocalTime.parse("00:00:06.078857429"),
    LocalTime.parse("00:00:03.039428714"),
    LocalTime.parse("00:00:01.519714357"),
    LocalTime.parse("00:00:00.759857178"),
    LocalTime.parse("00:00:00.379928589"),
    LocalTime.parse("00:00:00.189964294"),
    LocalTime.parse("00:00:00.094982147"),
    LocalTime.parse("00:00:00.047491073"),
  )

  testShrinkIsEmpty(LocalTime.MIN)

  behavior of "the shrink for LocalDateTime"

  testShrink(LocalDateTime.parse("2019-07-28T17:08:33"))(
    LocalDateTime.parse("1994-10-14T20:34:16.500"),
    LocalDateTime.parse("1945-03-20T03:25:43.500"),
    LocalDateTime.parse("1982-05-24T10:17:08.250"),
    LocalDateTime.parse("1957-08-10T13:42:51.750"),
    LocalDateTime.parse("1976-03-13T05:08:34.125"),
    LocalDateTime.parse("1963-10-21T18:51:25.875"),
    LocalDateTime.parse("1973-02-05T14:34:17.062500"),
    LocalDateTime.parse("1966-11-26T09:25:42.937500"),
    LocalDateTime.parse("1971-07-20T19:17:08.531250"),
    LocalDateTime.parse("1968-06-14T04:42:51.468750"),
    LocalDateTime.parse("1970-10-10T21:38:34.265625"),
    LocalDateTime.parse("1969-03-24T02:21:25.734375"),
    LocalDateTime.parse("1970-05-22T10:49:17.132812500"),
    LocalDateTime.parse("1969-08-12T13:10:42.867187500"),
    LocalDateTime.parse("1970-03-12T17:24:38.566406250"),
    LocalDateTime.parse("1969-10-22T06:35:21.433593750"),
    LocalDateTime.parse("1970-02-05T08:42:19.283203125"),
    LocalDateTime.parse("1969-11-26T15:17:40.716796875"),
    LocalDateTime.parse("1970-01-18T16:21:09.641601562"),
    LocalDateTime.parse("1969-12-14T07:38:50.358398438"),
  )

  testShrinkIsEmpty(LocalDateTime.parse("1970-01-01T00:00:00"))

  behavior of "the shrink for MonthDay"

  behavior of "the shrink for ZoneOffset"

  behavior of "the shrink for OffsetDateTime"

  behavior of "the shrink for OffsetTime"

  behavior of "the shrink for ZonedDateTime"

  testShrink(ZonedDateTime.parse("2019-07-28T17:14:04+10:00[Australia/Melbourne]"))(
    ZonedDateTime.parse("1994-10-15T01:37:02+10:00[Australia/Melbourne]"),
    ZonedDateTime.parse("1945-03-20T18:22:58+10:00[Australia/Melbourne]"),
    ZonedDateTime.parse("1982-05-24T17:48:31+10:00[Australia/Melbourne]"),
    ZonedDateTime.parse("1957-08-11T02:11:29+10:00[Australia/Melbourne]"),
    ZonedDateTime.parse("1976-03-13T13:54:15.500+10:00[Australia/Melbourne]"),
    ZonedDateTime.parse("1963-10-22T06:05:44.500+10:00[Australia/Melbourne]"),
    ZonedDateTime.parse("1973-02-06T00:57:07.750+11:00[Australia/Melbourne]"),
    ZonedDateTime.parse("1966-11-26T20:02:52.250+10:00[Australia/Melbourne]"),
    ZonedDateTime.parse("1971-07-21T04:58:33.875+10:00[Australia/Melbourne]"),
    ZonedDateTime.parse("1968-06-14T15:01:26.125+10:00[Australia/Melbourne]"),
    ZonedDateTime.parse("1970-10-11T07:29:16.937500+10:00[Australia/Melbourne]"),
    ZonedDateTime.parse("1969-03-24T12:30:43.062500+10:00[Australia/Melbourne]"),
    ZonedDateTime.parse("1970-05-22T20:44:38.468750+10:00[Australia/Melbourne]"),
    ZonedDateTime.parse("1969-08-12T23:15:21.531250+10:00[Australia/Melbourne]"),
    ZonedDateTime.parse("1970-03-13T03:22:19.234375+10:00[Australia/Melbourne]"),
    ZonedDateTime.parse("1969-10-22T16:37:40.765625+10:00[Australia/Melbourne]"),
    ZonedDateTime.parse("1970-02-05T18:41:09.617187500+10:00[Australia/Melbourne]"),
    ZonedDateTime.parse("1969-11-27T01:18:50.382812500+10:00[Australia/Melbourne]"),
    ZonedDateTime.parse("1970-01-19T02:20:34.808593750+10:00[Australia/Melbourne]"),
    ZonedDateTime.parse("1969-12-14T17:39:25.191406250+10:00[Australia/Melbourne]"),
  )

  testShrinkIsEmpty(Instant.EPOCH.atOffset(ZoneOffset.UTC))

  behavior of "the shrink for DayOfWeek"

  testShrink(DayOfWeek.SUNDAY)(
    DayOfWeek.SATURDAY,
    DayOfWeek.FRIDAY,
    DayOfWeek.THURSDAY,
    DayOfWeek.WEDNESDAY,
    DayOfWeek.TUESDAY,
    DayOfWeek.MONDAY,
  )

  testShrink(DayOfWeek.FRIDAY)(
    DayOfWeek.THURSDAY,
    DayOfWeek.WEDNESDAY,
    DayOfWeek.TUESDAY,
    DayOfWeek.MONDAY,
  )

  testShrink(DayOfWeek.MONDAY)(
    )

  behavior of "the shrink for Period"

  private def testShrinkIsEmpty[A : Shrink](a: A): Unit = testShrink(a)()

  private def testShrink[A : Shrink](a: A)(expectedShrunkSeq: A*): Unit = {
    val expectedShrunk = expectedShrunkSeq.toVector

    val testDescription =
      if (expectedShrunk.isEmpty) {
        s"not shrink $a"
      } else {
        s"shrink $a to ${expectedShrunk.mkString}"
      }

    it should testDescription in {
      assert(implicitly[Shrink[A]].shrink(a).take(20 max expectedShrunk.size).toVector === expectedShrunk)
    }
  }

}
