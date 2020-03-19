package com.azavea.pgsockets4s.api.commands

import cats.effect.{ContextShift, ExitCode, IO}
import com.monovore.decline._
import org.flywaydb.core.Flyway
import cats.implicits._

object Commands {

  final case class RunMigrations(databaseConfig: DatabaseConfig)

  final case class RunServer(apiConfig: ApiConfig, dbConfig: DatabaseConfig)

  private def runMigrationsOpts(
      implicit cs: ContextShift[IO]): Opts[RunMigrations] =
    Opts.subcommand("migrate", "Runs migrations against database") {
      Options.databaseConfig map RunMigrations
    }

  private def runServerOpts(implicit cs: ContextShift[IO]): Opts[RunServer] =
    Opts.subcommand("serve", "Runs web service") {
      (Options.apiConfig, Options.databaseConfig) mapN RunServer
    }

  def runMigrations(dbConfig: DatabaseConfig): IO[ExitCode] = IO {
    Flyway
      .configure()
      .dataSource(
        s"${dbConfig.jdbcUrl}",
        dbConfig.dbUser,
        dbConfig.dbPass
      )
      .locations("classpath:migrations/")
      .load()
      .migrate()
    ExitCode.Success
  }

  def applicationCommand(implicit cs: ContextShift[IO]): Command[Product] =
    Command("", "Welcome to the jungle") {
      runServerOpts orElse runMigrationsOpts
    }

}
