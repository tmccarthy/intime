import org.eclipse.jgit.api.Git

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
    version := Git.open(root.base).describe().setTags(true).call()
  )
)

lazy val commonSettings = Def.settings(
  scalaVersion := "2.12.8",
  crossScalaVersions := Seq("2.11.12", "2.12.8"),
)

lazy val root = project
  .in(file("."))
  .settings(commonSettings)
  .settings(
    skip in publish := true,
    console := (console in Compile in core).value,
    name := "java-time-4s",
  )
  .aggregate(
    core,
    interopCats,
  )

lazy val core = project
  .in(file("core"))
  .settings(name := "java-time-4s-core")
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest"      % "3.0.3",
      "au.id.tmm"     %% "tmm-test-utils" % "0.2.15",
    )
  )

lazy val interopCats = project
  .in(file("interop/cats"))
  .settings(name := "java-time-4s-cats-interop")
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-core"   % "1.6.1"
    )
  )
  .dependsOn(core)

