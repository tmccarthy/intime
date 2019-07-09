import org.eclipse.jgit.api.Git
import MakeProjects._

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
  )

lazy val core = project
  .in(file("core"))
  .settings(subProjectSettings("core"))

lazy val interopCats = project
  .in(file("interop/cats"))
  .settings(subProjectSettings("cats-interop"))
  .settings(DependencySettings.catsDependency)
  .dependsOn(core)

