import sbt.Keys.libraryDependencies
import sbt.librarymanagement.syntax._
import sbt.librarymanagement.{CrossVersion, ModuleID}
import sbt.{Def, Keys}

object DependencySettings {

  val commonDependencies: Seq[Def.Setting[Seq[ModuleID]]] = Seq(
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % Test,
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
