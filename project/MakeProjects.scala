import sbt.Keys
import sbt.Keys._

object MakeProjects {

  private val baseProjectName = "java-time-4s"

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
  ) ++ DependencySettings.commonDependencies

}
