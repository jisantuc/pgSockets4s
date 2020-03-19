cancelable in Global := true
onLoad in Global ~= (_ andThen ("project application" :: _))

import sbt._

// Versions
val CirceFs2Version = "0.11.0"
val CirceVersion = "0.11.1"
val DeclineVersion = "0.6.2"
val DoobieVersion = "0.7.1"
val EmojiVersion = "1.2.1"
val FlywayVersion = "5.2.4"
val GeotrellisServer = "3.4.0-9-geeb1ede-SNAPSHOT"
val Http4sVersion = "0.20.10"
val Log4CatsVersion = "0.3.0"
val LogbackVersion = "1.2.3"
val PostGISVersion = "2.2.1"
val PureConfigVersion = "0.12.1"
val RefinedVersion = "0.9.3"
val ScapegoatVersion = "1.3.8"
val SparkVersion = "2.4.2"
val Specs2Version = "4.6.0"
val TapirVersion = "0.10.1"

// Dependencies
val circeCore = "io.circe" %% "circe-core" % CirceVersion
val circeFs2 = "io.circe" %% "circe-fs2" % CirceFs2Version
val circeGeneric = "io.circe" %% "circe-generic" % CirceVersion
val circeRefined = "io.circe" %% "circe-refined" % CirceVersion
val decline = "com.monovore" %% "decline" % DeclineVersion
val declineRefined = "com.monovore" %% "decline-refined" % DeclineVersion
val doobie = "org.tpolecat" %% "doobie-core" % DoobieVersion
val doobieHikari = "org.tpolecat" %% "doobie-hikari" % DoobieVersion
val doobiePostgres = "org.tpolecat" %% "doobie-postgres" % DoobieVersion
val doobiePostgresCirce = "org.tpolecat" %% "doobie-postgres-circe" % DoobieVersion
val doobieRefined = "org.tpolecat" %% "doobie-refined" % DoobieVersion
val doobieScalatest = "org.tpolecat" %% "doobie-scalatest" % DoobieVersion % "test"
val doobieSpecs2 = "org.tpolecat" %% "doobie-specs2" % DoobieVersion % "test"
val emoji = "com.lightbend" %% "emoji" % EmojiVersion
val flyway = "org.flywaydb" % "flyway-core" % FlywayVersion
val geotrellisServerCore = "com.azavea.geotrellis" %% "geotrellis-server-core" % GeotrellisServer
val geotrellisServerStac = "com.azavea.geotrellis" %% "geotrellis-server-stac" % GeotrellisServer
val http4s = "org.http4s" %% "http4s-blaze-server" % Http4sVersion
val http4sCirce = "org.http4s" %% "http4s-circe" % Http4sVersion
val http4sDsl = "org.http4s" %% "http4s-dsl" % Http4sVersion
val http4sServer = "org.http4s" %% "http4s-blaze-server" % Http4sVersion
val log4cats = "io.chrisdavenport" %% "log4cats-slf4j" % Log4CatsVersion
val logbackClassic = "ch.qos.logback" % "logback-classic" % LogbackVersion
val postgis = "net.postgis" % "postgis-jdbc" % PostGISVersion
val pureConfig = "com.github.pureconfig" %% "pureconfig" % PureConfigVersion
val refined = "eu.timepit" %% "refined" % RefinedVersion
val refinedCats = "eu.timepit" %% "refined-cats" % RefinedVersion
val spark                 = "org.apache.spark"       %% "spark-core"               % SparkVersion
val specs2Core = "org.specs2" %% "specs2-core" % Specs2Version % "test"
val tapir = "com.softwaremill.tapir" %% "tapir-core" % TapirVersion
val tapirCirce = "com.softwaremill.tapir" %% "tapir-json-circe" % TapirVersion
val tapirHttp4sServer = "com.softwaremill.tapir" %% "tapir-http4s-server" % TapirVersion
val tapirOpenAPICirceYAML = "com.softwaremill.tapir" %% "tapir-openapi-circe-yaml" % TapirVersion
val tapirOpenAPIDocs = "com.softwaremill.tapir" %% "tapir-openapi-docs" % TapirVersion
val tapirSwaggerUIHttp4s = "com.softwaremill.tapir" %% "tapir-swagger-ui-http4s" % TapirVersion

// Enable a basic import sorter -- rules are defined in .scalafix.conf
scalafixDependencies in ThisBuild +=
  "com.nequissimus" %% "sort-imports" % "0.3.2"

lazy val settings = Seq(
  organization := "com.azavea",
  name := "pgsockets4s",
  version := "0.0.1-SNAPSHOT",
  scalaVersion := "2.12.10",
  scalafmtOnCompile := true,
  scapegoatVersion in ThisBuild := Versions.ScapegoatVersion,
  addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.6"),
  addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.2.4"),
  addCompilerPlugin(
    "org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full
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
    Resolver.bintrayRepo("azavea", "maven"),
    Resolver.bintrayRepo("azavea", "geotrellis"),
    "locationtech-releases" at "https://repo.locationtech.org/content/groups/releases",
    "locationtech-snapshots" at "https://repo.locationtech.org/content/groups/snapshots",
    Resolver.bintrayRepo("guizmaii", "maven"),
    Resolver.bintrayRepo("colisweb", "maven"),
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
  doobie,
  doobieHikari,
  doobiePostgres,
  doobiePostgresCirce,
  doobieRefined,
  doobieScalatest,
  doobieSpecs2,
  emoji,
  flyway,
  geotrellisServerCore,
  geotrellisServerStac,
  http4s,
  http4sCirce,
  http4sDsl,
  http4sServer,
  log4cats,
  postgis,
  pureConfig,
  refined,
  refinedCats,
  spark,
  tapir,
  tapirCirce,
  tapirHttp4sServer,
  tapirOpenAPICirceYAML,
  tapirOpenAPIDocs,
  tapirSwaggerUIHttp4s
)

lazy val application = (project in file("application"))
  .settings(settings: _*)
  .settings({
    libraryDependencies ++= dependencies
  })
lazy val applicationRef = LocalProject("application")
