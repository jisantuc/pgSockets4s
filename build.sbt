cancelable in Global := true
onLoad in Global ~= (_ andThen ("project application" :: _))

import sbt._
import explicitdeps.ExplicitDepsPlugin.autoImport.moduleFilterRemoveValue

// Versions
val CatsEffectVersion = "2.1.1"
val CatsVersion       = "2.1.0"
val CirceFs2Version   = "0.13.0"
val CirceVersion      = "0.13.0"
val DeclineVersion    = "0.6.2"
val EmojiVersion      = "1.2.1"
val Fs2Version        = "2.2.2"
val Http4sVersion     = "0.21.1"
val LogbackVersion    = "1.2.3"
val NatchezVersion    = "0.0.10"
val RefinedVersion    = "0.9.3"
val ScapegoatVersion  = "1.3.8"
val ShapelessVersion  = "2.3.3"
val SkunkVersion      = "0.0.7"
val Specs2Version     = "4.6.0"

// Dependencies
val catsCore          = "org.typelevel"  %% "cats-core"           % CatsVersion
val catsEffect        = "org.typelevel"  %% "cats-effect"         % CatsEffectVersion
val circeCore         = "io.circe"       %% "circe-core"          % CirceVersion
val circeFs2          = "io.circe"       %% "circe-fs2"           % CirceFs2Version
val circeGeneric      = "io.circe"       %% "circe-generic"       % CirceVersion
val decline           = "com.monovore"   %% "decline"             % DeclineVersion
val fs2               = "co.fs2"         %% "fs2-core"            % Fs2Version
val http4sBlazeServer = "org.http4s"     %% "http4s-blaze-server" % Http4sVersion
val http4sCore        = "org.http4s"     %% "http4s-core"         % Http4sVersion
val http4sDsl         = "org.http4s"     %% "http4s-dsl"          % Http4sVersion
val http4sServer      = "org.http4s"     %% "http4s-server"       % Http4sVersion
val logbackClassic    = "ch.qos.logback" % "logback-classic"      % LogbackVersion % "runtime"
val natchez           = "org.tpolecat"   %% "natchez-core"        % NatchezVersion
val refined           = "eu.timepit"     %% "refined"             % RefinedVersion
val shapeless         = "com.chuusai"    %% "shapeless"           % ShapelessVersion
val skunk             = "org.tpolecat"   %% "skunk-core"          % SkunkVersion
val skunkMacros       = "org.tpolecat"   %% "skunk-macros"        % SkunkVersion
val specs2Core        = "org.specs2"     %% "specs2-core"         % Specs2Version % "test"

// Enable a basic import sorter -- rules are defined in .scalafix.conf
scalafixDependencies in ThisBuild +=
  "com.nequissimus" %% "sort-imports" % "0.3.2"

lazy val settings = Seq(
  organization := "com.azavea",
  name := "pgsockets4s",
  version := "0.0.1-SNAPSHOT",
  scalaVersion := "2.12.11",
  scalafmtOnCompile := true,
  scapegoatVersion in ThisBuild := ScapegoatVersion,
  addCompilerPlugin("org.spire-math" %% "kind-projector"     % "0.9.6"),
  addCompilerPlugin("com.olegpy"     %% "better-monadic-for" % "0.2.4"),
  addCompilerPlugin(
    "org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full
  ),
  addCompilerPlugin(scalafixSemanticdb),
  autoCompilerPlugins := true,
  assemblyJarName in assembly := "application-assembly.jar",
  assemblyMergeStrategy in assembly := {
    case "reference.conf"                       => MergeStrategy.concat
    case "application.conf"                     => MergeStrategy.concat
    case n if n.startsWith("META-INF/services") => MergeStrategy.concat
    case n if n.endsWith(".SF") || n.endsWith(".RSA") || n.endsWith(".DSA") =>
      MergeStrategy.discard
    case "META-INF/MANIFEST.MF" => MergeStrategy.discard
    case _                      => MergeStrategy.first
  },
  excludeDependencies ++= Seq(
    "log4j"     % "log4j",
    "org.slf4j" % "slf4j-log4j12",
    "org.slf4j" % "slf4j-nop"
  ),
  externalResolvers := Seq(
    DefaultMavenRepository,
    Resolver.sonatypeRepo("snapshots"),
    Resolver.typesafeIvyRepo("releases"),
    "jitpack".at("https://jitpack.io"),
    Resolver.file("local", file(Path.userHome.absolutePath + "/.ivy2/local"))(
      Resolver.ivyStylePatterns
    )
  ),
  unusedCompileDependenciesFilter -= moduleFilter(
    "com.sksamuel.scapegoat",
    "scalac-scapegoat-plugin"
  ),
)

lazy val dependencies = Seq(
  catsCore,
  catsEffect,
  circeCore,
  circeGeneric,
  decline,
  fs2,
  http4sCore,
  http4sBlazeServer,
  http4sDsl,
  http4sServer,
  logbackClassic,
  natchez,
  refined,
  shapeless,
  skunk,
  skunkMacros,
  specs2Core
)

lazy val application = (project in file("application"))
  .settings(settings: _*)
  .settings({
    libraryDependencies ++= dependencies
  })
lazy val applicationRef = LocalProject("application")
