import DependencySettings._
import MakeProjects._
import xerial.sbt.Sonatype.GitHubHosting

addCompilerPlugin("org.typelevel" % "kind-projector" % "0.10.3" cross CrossVersion.binary)

inThisBuild(
  List(
    organization := "au.id.tmm.intime",
    publishMavenStyle := true,
    sonatypeProjectHosting := Some(GitHubHosting("tmccarthy", "intime", "Timothy McCarthy", "ebh042@gmail.com")),
    homepage := Some(url("https://github.com/tmccarthy/intime")),
    startYear := Some(2019),
    licenses := List("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
    developers := List(
      Developer(
        "tmccarthy",
        "Timothy McCarthy",
        "ebh042@gmail.com",
        url("http://tmm.id.au"),
      )
    ),
    scmInfo := Some(ScmInfo(url("https://github.com/tmccarthy/intime"), "scm:git:https://github.com/tmccarthy/intime.git")),
    pgpPublicRing := file("/tmp/secrets/pubring.kbx"),
    pgpSecretRing := file("/tmp/secrets/secring.gpg"),
    releaseEarlyWith := SonatypePublisher,
  )
)

lazy val root = project
  .in(file("."))
  .settings(rootProjectSettings)
  .settings(console := (console in Compile in core).value)
  .aggregate(
    core,
    cats,
    scalaCheck,
    argonaut,
  )

lazy val core = project
  .in(file("core"))
  .settings(subProjectSettings("core"))

lazy val cats = project
  .in(file("cats"))
  .settings(subProjectSettings("cats"))
  .settings(
    catsDependency,
    catsTestKitDependency,
  )
  .dependsOn(core, scalaCheck % "test->compile")

lazy val scalaCheck = project
  .in(file("scalacheck"))
  .settings(subProjectSettings("scalacheck"))
  .settings(
    libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.14.0",
  )
  .dependsOn(core)

lazy val argonaut = project
  .in(file("argonaut"))
  .settings(subProjectSettings("argonaut"))
  .settings(
    libraryDependencies += "io.argonaut" %% "argonaut" % "6.2.3",
  )
  .dependsOn(scalaCheck % "test->compile")

addCommandAlias("check", ";+test;scalafmtCheckAll")
