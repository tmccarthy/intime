import DependencySettings._
import MakeProjects._
import org.eclipse.jgit.api.Git

addCompilerPlugin("org.typelevel" % "kind-projector" % "0.10.3" cross CrossVersion.binary)

inThisBuild(
  List(
    organization := "Timothy McCarthy",
    homepage := Some(url("https://github.com/tmccarthy")),
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
    scmInfo := Some(ScmInfo(url("https://github.com/tmccarthy/java-time-4s"), "scm:git:https://github.com/tmccarthy/java-time-4s.git")),
    version := Git.open(root.base).describe().setTags(true).call(),
  )
)

lazy val root = project
  .in(file("."))
  .settings(rootProjectSettings)
  .settings(console := (console in Compile in core).value)
  .aggregate(
    core,
    interopCats,
    interopScalaCheck,
  )

lazy val core = project
  .in(file("core"))
  .settings(subProjectSettings("core"))

lazy val interopCats = project
  .in(file("interop/cats"))
  .settings(subProjectSettings("cats-interop"))
  .settings(
    catsDependency,
    catsTestKitDependency,
  )
  .dependsOn(core)

lazy val interopScalaCheck = project
  .in(file("interop/scalacheck"))
  .settings(subProjectSettings("scalacheck-interop"))
  .settings(
    libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.14.0",
  )
