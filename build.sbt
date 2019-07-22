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
    scmInfo := Some(ScmInfo(url("https://github.com/tmccarthy/intime"), "scm:git:https://github.com/tmccarthy/intime.git")),
    version := Git.open(root.base).describe().setTags(true).call(),
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
  .dependsOn(scalaCheck % "compile->test")

lazy val scalaCheck = project
  .in(file("scalacheck"))
  .settings(subProjectSettings("scalacheck"))
  .settings(
    libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.14.0",
  )
  .dependsOn(core % "compile->test")

lazy val argonaut = project
  .in(file("argonaut"))
  .settings(subProjectSettings("argonaut"))
  .settings(
    libraryDependencies += "io.argonaut" %% "argonaut" % "6.2.3",
  )
  .dependsOn(scalaCheck % "compile->test")
