val settingsHelper = ProjectSettingsHelper("au.id.tmm", "intime")()

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

val catsVersion          = "2.2.0-M3"
val catsEffectVersion    = "2.1.3"
val fs2Version           = "2.4.1"
val scalaCheckVersion    = "1.14.3"
val scalaTestPlusVersion = "3.2.0.1-M2"

lazy val core = project
  .in(file("core"))
  .settings(settingsHelper.settingsForSubprojectCalled("core"))
  .settings(
    libraryDependencies += "org.typelevel" %% "cats-effect" % catsEffectVersion % Test,
    libraryDependencies += "org.typelevel" %% "cats-core"   % catsVersion       % Test,
  )

lazy val cats = project
  .in(file("cats"))
  .settings(settingsHelper.settingsForSubprojectCalled("cats"))
  .settings(
    libraryDependencies += "org.typelevel" %% "cats-core"              % catsVersion,
    libraryDependencies += "org.typelevel" %% "cats-testkit"           % catsVersion % Test,
    libraryDependencies += "org.typelevel" %% "cats-testkit-scalatest" % "1.0.1"     % Test,
  )
  .dependsOn(core, scalaCheck % "test->compile")

lazy val scalaCheck = project
  .in(file("scalacheck"))
  .settings(settingsHelper.settingsForSubprojectCalled("scalacheck"))
  .settings(
    libraryDependencies += "org.scalacheck"    %% "scalacheck"      % scalaCheckVersion,
    libraryDependencies += "org.scalatestplus" %% "scalacheck-1-14" % scalaTestPlusVersion % Test,
  )
  .dependsOn(core)

lazy val argonaut = project
  .in(file("argonaut"))
  .settings(settingsHelper.settingsForSubprojectCalled("argonaut"))
  .settings(
    libraryDependencies += "io.argonaut"       %% "argonaut"        % "6.2.3",
    libraryDependencies += "org.scalatestplus" %% "scalacheck-1-14" % scalaTestPlusVersion % Test,
  )
  .dependsOn(scalaCheck % "test->compile")

addCommandAlias("check", ";+test;scalafmtCheckAll")
