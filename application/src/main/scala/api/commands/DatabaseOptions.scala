package com.azavea.pgsockets4s.api.commands

import eu.timepit.refined.types.numeric._

case class DatabaseOptions(
    databasePort: PosInt,
    databaseHost: String,
    databaseName: String,
    databasePassword: String,
    databaseUser: String
)

object DatabaseOptions {

  def default: DatabaseOptions = DatabaseOptions(
    PosInt(5432),
    "localhost",
    "pgsockets4s",
    "pgsockets4s",
    "pgsockets4s"
  )
}

// def databaseConfig(implicit contextShift: ContextShift[IO]): Opts[DatabaseConfig] =
//     ((
//       databaseUser,
//       databasePassword,
//       databaseHost,
//       databasePort,
//       databaseName
//     ) mapN DatabaseConfig).validate(
//       e":boom: Unable to connect to database - please ensure database is configured and listening at entered port"
//     ) { config =>
//       val xa =
//         Transactor
//           .fromDriverManager[IO](config.driver, config.jdbcUrl, config.dbUser, config.dbPass)
//       val select = Try {
//         fr"SELECT 1".query[Int].unique.transact(xa).unsafeRunSync()
//       }
//       select.toEither match {
//         case Right(_) => true
//         case Left(_)  => false
//       }
//     }
// }
