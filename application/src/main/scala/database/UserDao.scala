package com.azavea.pgsockets4s.database

import java.util.UUID

import com.azavea.pgsockets4s.datamodel.User
import com.azavea.pgsockets4s.database.util.Dao
import doobie.util.fragment.Fragment
import doobie._
import doobie.implicits._
import doobie.postgres.implicits._

object UserDao extends Dao[User] {
  val tableName: String = "users"

  /** An abstract select statement to be used for constructing queries */
  def selectF: Fragment = fr"SELECT id, email FROM" ++ tableF

  def create(user: User.Create): ConnectionIO[User] = {
    (fr"INSERT INTO" ++ tableF ++ fr"""
      (id, email)
    VALUES
      (uuid_generate_v4(), ${user.email})
    """).update.withUniqueGeneratedKeys[User]("id", "email")
  }

  def update(id: UUID, user: User): ConnectionIO[Int] = {
    val updateQuery = fr"UPDATE" ++ tableF ++ fr"SET email = ${user.id} WHERE id = ${id}"
    updateQuery.update.run
  }

}
