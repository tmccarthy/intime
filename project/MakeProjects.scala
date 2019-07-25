import sbt.Keys
import sbt.Keys._
import xerial.sbt.Sonatype.autoImport.sonatypeProfileName

object MakeProjects {

  private val baseProjectName = "intime"

  private val primaryScalaVersion = "2.13.0"
  private val otherScalaVersions = List("2.12.8")

  def rootProjectSettings = Seq(
      skip in publish := true,
      name := baseProjectName,
    )

  def subProjectSettings(name: String) = Seq(
    Keys.name := s"$baseProjectName-$name",
    scalaVersion := primaryScalaVersion,
    crossScalaVersions := Seq(primaryScalaVersion) ++ otherScalaVersions,
    ScalacSettings.scalacSetting,
    publishConfiguration := publishConfiguration.value.withOverwrite(true),
    publishLocalConfiguration := publishLocalConfiguration.value.withOverwrite(true),
    sonatypeProfileName := "au.id.tmm",
  ) ++ DependencySettings.commonDependencies

}
