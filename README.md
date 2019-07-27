# `intime`
[![CircleCI](https://circleci.com/gh/tmccarthy/intime/tree/master.svg?style=svg)](https://circleci.com/gh/tmccarthy/intime/tree/master)
[![Maven Central](https://img.shields.io/maven-central/v/au.id.tmm.intime/intime-core_2.13.svg)](https://repo.maven.apache.org/maven2/au/id/tmm/intime/intime-core_2.13/)

Libraries for integration between the [`java.time`](https://docs.oracle.com/javase/8/docs/api/java/time/package-summary.html) 
classes and common Scala libraries.

* [`intime-core`](#intime-core) provides integration with the core Scala library
* [`intime-cats`](#intime-cats) provides instances for the [Cats](https://github.com/typelevel/cats) FP library
* [`intime-argonaut`](#intime-argonaut) provides encoders and decoders for the [Argonaut](https://github.com/argonaut-io/argonaut) JSON library
* [`intime-scalacheck`](#intime-scalacheck) provides instances for the [Scalacheck](https://github.com/rickynils/scalacheck) property-based-testing library

Add the following to your `build.sbt` file:

```scala
val intimeVersion = "0.0.2"

libraryDependencies += "au.id.tmm.intime" %% "intime-core"       % intimeVersion
libraryDependencies += "au.id.tmm.intime" %% "intime-cats"       % intimeVersion          // Cats integration
libraryDependencies += "au.id.tmm.intime" %% "intime-argonaut"   % intimeVersion          // Argonaut integration
libraryDependencies += "au.id.tmm.intime" %% "intime-scalacheck" % intimeVersion % "test" // Scalacheck integration
```

## `intime-core`

`intime-core` adds integrations with the Scala standard library. Add it to your project with:

```scala
libraryDependencies += "au.id.tmm.intime" %% "intime-core" % "0.0.2"
```

#### `Ordering` instances for ordered classes

`intime-core` provides `Ordering` instances for all classes in the `java.time` package for which an ordering can be 
defined. This includes the most common classes like `Instant` and `LocalDate`.

```scala
import java.time._
import au.id.tmm.intime._

val dates: List[LocalDate] = List(
  LocalDate.of(2003, 3, 23),
  LocalDate.of(2015, 3, 29),
  LocalDate.of(2007, 4, 28),
)

dates.sorted // Sorts list of dates
```

#### `PartialOrdering` instances for `Period`

`Period` presents some problems when it comes to defining an `Ordering` instance, as any two instances cannot 
necessarily be compared (is 1 month longer or shorter than 30 days?). `intime-core` provides a `PartialOrdering` for 
each, handling those cases where an ordering can be computed.

#### Overloaded operators

`intime-core` provides overloaded operators for arithmetic and comparison operations on `java.time` classes:

```scala
import java.time._
import au.id.tmm.intime._

LocalDate.of(1999, 6, 20) + Period.ofDays(3) // 1999-06-23
Instant.EPOCH - Duration.ofSeconds(5)        // 1969-12-31T23:59:55Z
Period.ofDays(5) * 3                         // P15D
- Duration.ofHours(42)                       // PT-42H
Duration.ofDays(30) / 10                     // P3D
Instant.MAX > Instant.EPOCH                  // true 
```

## `intime-cats`

`intime-cats` adds integrations with [Cats](https://github.com/typelevel/cats). Add it to your project with:

```scala
libraryDependencies += "au.id.tmm.intime" %% "intime-cats" % "0.0.2"
```

All instances are tested with [discipline](https://github.com/typelevel/discipline).

#### `Hash` and `Show` instances

`intime-cats` provides `Hash` and `Show` instances for all classes in `java.time`.

```scala
import java.time._
import au.id.tmm.intime.cats._

import cats.syntax.show._
import cats.syntax.eq._

LocalDate.of(1999, 6, 20).show  // 1999-06-20
Instant.EPOCH === Instant.EPOCH // true
```

#### `Order` and `PartialOrder` instances

`intime-cats` uses the orderings in `intime-core` to define Cats `Order` instances (`PartialOrder` for `Period`).

```scala
import java.time._
import au.id.tmm.intime.cats._

import cats.syntax.partialOrder._

LocalDate.of(2003, 3, 23) < LocalDate.of(2015, 3, 29) // true

Period.ofYears(1) < Period.ofMonths(13)               // true
Period.ofDays(30) partialCompare Period.ofMonths(1)   // NaN
```

#### `CommutativeGroup` instances for `Period` and `Duration`

```scala
import java.time._
import au.id.tmm.intime.cats._

import cats.syntax.group._

Duration.ofDays(1) |+| Duration.ofHours(2) // P1DT2H
Duration.ofDays(1) |-| Duration.ofHours(2) // PT22H
```

## `intime-scalacheck`

`intime-scalacheck` adds integrations with [Scalacheck](https://github.com/rickynils/scalacheck). Add it to your project 
with:

```scala
libraryDependencies += "au.id.tmm.intime" %% "intime-scalacheck" % "0.0.2" % "test"
```

#### `Arbitrary` instances

`intime-scalacheck` provides instances of `Arbitrary` for all classes in `java.time`. These can be used to generate 
arbitrary instances for property-based testing.

```scala
import java.time._
import au.id.tmm.intime.scalacheck._

import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks._

forAll { localDate: LocalDate =>
  assert(localDate.plusDays(1) isAfter localDate)
}
```

#### Generators for "sensible" datetime values

`intime-scalacheck` provides generators for "sensible" values of `java.time` classes. The generated values are all 
between 1900 and 2100, allowing property-based-tests that don't have to worry about peculiarities like 
[year zero](https://en.wikipedia.org/wiki/Year_zero) or durations that overflow.

```scala
import java.time._
import au.id.tmm.intime.scalacheck._

import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks._

forAll(genSensibleLocalDate) { localDate: LocalDate =>
  assert(localDate.getYear >= 1900)
}
```

#### `Choose` instances

`intime-scalacheck` provides instances of `Choose`, which let you define your own generators that produce values within
a range.

```scala
import java.time._
import au.id.tmm.intime.scalacheck._

import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks._

val rangeGenerator: Gen[LocalDate] = Gen.choose(
  min = LocalDate.of(2019, 5, 30),
  max = LocalDate.of(2019, 7, 14),
)

forAll(rangeGenerator) { localDate: LocalDate =>
  assert(localDate.getYear == 2019)
}
```

## `intime-argonaut`

`intime-argonaut` provides integration with the [Argonaut](https://github.com/argonaut-io/argonaut) library for JSON
handling. Add it to your project with:

```scala
libraryDependencies += "au.id.tmm.intime" %% "intime-argonaut" % "0.0.2"
```

#### Standard encoders and decoders

`intime-argonaut` defines `EncodeJson` and `DecodeJson` instances for all classes in the `java.time` package. They are 
encoded and decoded to JSON strings according to the most obvious format (see 
[`StandardCodecs`](argonaut/src/main/scala/au/id/tmm/intime/argonaut/StandardCodecs.scala).

```scala
import java.time._
import au.id.tmm.intime.argonaut._

import argonaut.Argonaut._

LocalDate.of(2019, 7, 14).asJson    // jString("2019-07-14")
jString("2019-07-14").as[LocalDate] // DecodeResult.ok(LocalDate.of(2019, 7, 14))
```

#### Custom encoders and decoders

`intime-argonaut` allows for the definition of custom `EncodeJson` and `DecodeJson` instances using instances of 
[`DateTimeFormatter`](https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html).

```scala
import java.time._
import au.id.tmm.intime.argonaut._

import argonaut.Argonaut._

val formatter = DateTimeFormatter.ofPattern("MM-dd-uuuu")
implicit val customCodec = DateTimeFormatterCodecs.localDateCodecFrom(formatter)

LocalDate.of(2019, 7, 14).asJson    // jString("07-14-2019")
jString("07-14-2019").as[LocalDate] // DecodeResult.ok(LocalDate.of(2019, 7, 14))
```

#### Known issues

* In Java 8, the standard codec for `ZonedDateTime` will fail to decode when the zone is `GMT`. This is fixed in Java 11.
* In Java 8, the standard codec for `Duration` will drop the negative sign for durations between 0 and -1 seconds. This 
  is fixed in Java 11.
