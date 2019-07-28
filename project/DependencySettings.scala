import sbt.Keys.libraryDependencies
import sbt._
import sbt.librarymanagement.{CrossVersion, ModuleID}

object DependencySettings {

  val commonDependencies: Seq[Def.Setting[Seq[ModuleID]]] = Seq(
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % Test,
    libraryDependencies += "com.github.ghik" %% "silencer-lib" % "1.4.1" % Provided,
    libraryDependencies += compilerPlugin("com.github.ghik" %% "silencer-plugin" % "1.4.1"),
  )

  val catsDependency = libraryDependencies += {
    CrossVersion.partialVersion(Keys.scalaVersion.value) match {
      case Some((2, 13))     => "org.typelevel" %% "cats-core" % "2.0.0-M4"
      case Some((2, 12)) | _ => "org.typelevel" %% "cats-core" % "1.6.1"
    }
  }

  val catsTestKitDependency = libraryDependencies += {
    CrossVersion.partialVersion(Keys.scalaVersion.value) match {
      case Some((2, 13))     => "org.typelevel" %% "cats-testkit" % "2.0.0-M4" % Test
      case Some((2, 12)) | _ => "org.typelevel" %% "cats-testkit" % "1.6.1"    % Test
    }
  }

}
