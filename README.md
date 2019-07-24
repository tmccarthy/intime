# intime

Libraries for integration between the [`java.time`](https://docs.oracle.com/javase/8/docs/api/java/time/package-summary.html) 
classes and common Scala libraries.

## Rationale

The `java.time` package is a wonderful library for representing dates, times and durations. It also has the advantage of
coming packaged with Scala on the JVM. Unfortunately, its support within the Scala standard library and broader 
ecosystem is somewhat lacking.

The intention of this project is to provide integration between the `java.time` classes and Scala libraries, at least
until that integration can be made a first-class-citizen of those libraries.

## Standard library integration with `intime-core`

`intime-core` adds integrations with the Scala standard library. Add it to your project with:

```scala
libraryDependencies += "au.id.tmm.intime" %% "intime-core" % "0.0.1"
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

#### `PartialOrdering` instances for `Period` and `ZoneId`

`Period` and `ZoneId` present some problems when it comes to defining an `Ordering` instance, as any two instances 
cannot necessarily be compared (is 1 month longer or shorter than 30 days?). `intime-core` provides a `PartialOrdering`
for each, handling those cases where an ordering can be computed.

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

## Cats integration with `intime-cats`

`intime-cats` adds integrations with [Cats](https://github.com/typelevel/cats). Add it to your project with:

```scala
libraryDependencies += "au.id.tmm.intime" %% "intime-cats" % "0.0.1"
```

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

`intime-cats` uses the orderings in `intime-core` to define Cats `Order` instances (`PartialOrder` for `Period` and 
`ZoneId`).

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

## Scalacheck integration with `intime-scalacheck`

## Argonaut integration with `intime-argonaut`
