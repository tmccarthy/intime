import sbt.Keys.libraryDependencies
import sbt._
import sbt.librarymanagement.{CrossVersion, ModuleID}

object DependencySettings {

  private val silencerVersion = "1.7.0"

  val commonDependencies: Seq[Def.Setting[Seq[ModuleID]]] = Seq(
    libraryDependencies += "org.scalatest"       %% "scalatest"         % "3.2.0-M4"      % Test,
    libraryDependencies += "com.github.ghik"     %% "silencer-lib"      % silencerVersion % Provided cross CrossVersion.full,

    libraryDependencies += compilerPlugin("com.github.ghik" %% "silencer-plugin" % silencerVersion cross CrossVersion.full),
  )

}
