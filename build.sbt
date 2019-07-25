import DependencySettings._
import MakeProjects._
import org.eclipse.jgit.api.Git
import xerial.sbt.Sonatype.GitHubHosting

addCompilerPlugin("org.typelevel" % "kind-projector" % "0.10.3" cross CrossVersion.binary)

def versionFromGit =
  Git.open(root.base)
    .describe()
    .setTags(true)
    .setMatch("v[0-9].[0-9].[0-9]*")
    .call()
    .stripPrefix("v")

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
    isSnapshot := """^\d+\.\d+\.\d+$""".r.findFirstIn(versionFromGit).isEmpty,
    version := (if (isSnapshot.value) versionFromGit + "-SNAPSHOT" else versionFromGit),
    publishTo := {
      val nexus = "https://oss.sonatype.org/"
      if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
      else Some("releases" at nexus + "service/local/staging/deploy/maven2")
    },
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
