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