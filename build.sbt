cancelable in Global := true
onLoad in Global ~= (_ andThen ("project application" :: _))

import sbt._

// Versions
val CirceFs2Version   = "0.13.0"
val CirceVersion      = "0.13.0"
val DeclineVersion    = "0.6.2"
val EmojiVersion      = "1.2.1"
val Http4sVersion     = "0.21.1"
val Log4CatsVersion   = "0.3.0"
val LogbackVersion    = "1.2.3"
val PureConfigVersion = "0.12.1"
val RefinedVersion    = "0.9.3"
val ScapegoatVersion  = "1.3.8"
val Specs2Version     = "4.6.0"

// Dependencies
val circeCore      = "io.circe"              %% "circe-core"          % CirceVersion
val circeFs2       = "io.circe"              %% "circe-fs2"           % CirceFs2Version
val circeGeneric   = "io.circe"              %% "circe-generic"       % CirceVersion
val circeRefined   = "io.circe"              %% "circe-refined"       % CirceVersion
val decline        = "com.monovore"          %% "decline"             % DeclineVersion
val declineRefined = "com.monovore"          %% "decline-refined"     % DeclineVersion
val emoji          = "com.lightbend"         %% "emoji"               % EmojiVersion
val http4s         = "org.http4s"            %% "http4s-blaze-server" % Http4sVersion
val http4sCirce    = "org.http4s"            %% "http4s-circe"        % Http4sVersion
val http4sDsl      = "org.http4s"            %% "http4s-dsl"          % Http4sVersion
val http4sServer   = "org.http4s"            %% "http4s-blaze-server" % Http4sVersion
val log4cats       = "io.chrisdavenport"     %% "log4cats-slf4j"      % Log4CatsVersion
val logbackClassic = "ch.qos.logback"        % "logback-classic"      % LogbackVersion
val pureConfig     = "com.github.pureconfig" %% "pureconfig"          % PureConfigVersion
val refined        = "eu.timepit"            %% "refined"             % RefinedVersion
val refinedCats    = "eu.timepit"            %% "refined-cats"        % RefinedVersion
val specs2Core     = "org.specs2"            %% "specs2-core"         % Specs2Version % "test"

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
  )
)

lazy val dependencies = Seq(
  specs2Core,
  logbackClassic,
  circeCore,
  circeGeneric,
  circeRefined,
  decline,
  declineRefined,
  emoji,
  http4s,
  http4sCirce,
  http4sDsl,
  http4sServer,
  log4cats,
  pureConfig,
  refined,
  refinedCats
)

lazy val application = (project in file("application"))
  .settings(settings: _*)
  .settings({
    libraryDependencies ++= dependencies
  })
lazy val applicationRef = LocalProject("application")
