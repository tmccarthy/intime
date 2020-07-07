import ch.epfl.scala.sbt.release.AutoImported._
import com.typesafe.sbt.SbtPgp.autoImportImpl.{pgpPublicRing, pgpSecretRing}
import sbt.Keys._
import sbt._
import xerial.sbt.Sonatype
import xerial.sbt.Sonatype.GitHubHosting
import xerial.sbt.Sonatype.autoImport.sonatypeProjectHosting

final case class ProjectSettingsHelper private (
  sonatypeProfile: String,
  baseProjectName: String,
)(
  githubUser: String = "tmccarthy",
  githubProjectName: String = baseProjectName,
  githubUserFullName: String = "Timothy McCarthy",
  githubUserEmail: String = "ebh042@gmail.com",
  githubUserWebsite: String = "http://tmm.id.au",

  primaryScalaVersion: String = "2.13.2", // Change these in the circleci file if you change them here
  otherScalaVersions: List[String] = List("2.12.11"), // Change these in the circleci file if you change them here
) {

  def settingsForBuild = {
    List(
      releaseEarly / Keys.aggregate := false, // Workaround for https://github.com/scalacenter/sbt-release-early/issues/30
      Sonatype.SonatypeKeys.sonatypeProfileName := sonatypeProfile,
    ) ++ sbt.inThisBuild(
      List(
        addCompilerPlugin("org.typelevel" % "kind-projector" % "0.11.0" cross CrossVersion.full), // TODO upgrade this
        organization := sonatypeProfile + "." + baseProjectName,
        publishMavenStyle := true,
        sonatypeProjectHosting := Some(GitHubHosting(githubUser, githubProjectName, githubUserFullName, githubUserEmail)),
        homepage := Some(url(s"https://github.com/$githubUser/$githubProjectName")),
        startYear := Some(2019),
        licenses := List("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
        developers := List(
          Developer(
            githubUser,
            githubUserFullName,
            githubUserEmail,
            url(githubUserWebsite),
          )
        ),
        scmInfo := Some(ScmInfo(url(s"https://github.com/$githubUser/$githubProjectName"), s"scm:git:https://github.com/$githubUser/$githubProjectName.git")),
        pgpPublicRing := file("/tmp/secrets/pubring.kbx"),
        pgpSecretRing := file("/tmp/secrets/secring.gpg"),
        releaseEarlyWith := SonatypePublisher,
        releaseEarlyEnableInstantReleases := false,

        scalaVersion := primaryScalaVersion,
        crossScalaVersions := Seq(primaryScalaVersion) ++ otherScalaVersions,
      )
    )
  }

  def settingsForRootProject = Seq(
    publish / skip := true,
    name := baseProjectName,
    crossScalaVersions := Nil,
  )

  def settingsForSubprojectCalled(name: String) = Seq(
    Keys.name := s"$baseProjectName-$name",
    ScalacSettings.scalacSetting,
    publishConfiguration := publishConfiguration.value.withOverwrite(true),
    publishLocalConfiguration := publishLocalConfiguration.value.withOverwrite(true),
  ) ++ DependencySettings.commonDependencies

}
