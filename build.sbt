import DependencySettings._

addCompilerPlugin("org.typelevel" % "kind-projector" % "0.10.3" cross CrossVersion.binary)

val settingsHelper = ProjectSettingsHelper("au.id.tmm","intime")()

settingsHelper.settingsForBuild

lazy val root = project
  .in(file("."))
  .settings(settingsHelper.settingsForRootProject)
  .settings(console := (console in Compile in core).value)
  .aggregate(
    core,
    cats,
    scalaCheck,
    argonaut,
  )

lazy val core = project
  .in(file("core"))
  .settings(settingsHelper.settingsForSubprojectCalled("core"))

lazy val cats = project
  .in(file("cats"))
  .settings(settingsHelper.settingsForSubprojectCalled("cats"))
  .settings(
    catsDependency,
    catsTestKitDependency,
  )
  .dependsOn(core, scalaCheck % "test->compile")

lazy val scalaCheck = project
  .in(file("scalacheck"))
  .settings(settingsHelper.settingsForSubprojectCalled("scalacheck"))
  .settings(
    libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.14.0",
  )
  .dependsOn(core)

lazy val argonaut = project
  .in(file("argonaut"))
  .settings(settingsHelper.settingsForSubprojectCalled("argonaut"))
  .settings(
    libraryDependencies += "io.argonaut" %% "argonaut" % "6.2.3",
  )
  .dependsOn(scalaCheck % "test->compile")

addCommandAlias("check", ";+test;scalafmtCheckAll")
