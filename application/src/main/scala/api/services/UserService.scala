package com.azavea.pgsockets4s.api.services

import java.util.UUID

import cats.effect._
import cats.implicits._
import com.azavea.pgsockets4s.api.endpoints.UserEndpoints
import com.azavea.pgsockets4s.database.UserDao
import com.azavea.pgsockets4s.datamodel.User
import doobie.util.transactor.Transactor
import doobie._
import doobie.implicits._
import org.http4s._
import org.http4s.dsl.Http4sDsl
import tapir.server.http4s._
import eu.timepit.refined.auto._

class UsersService[F[_]: Sync](xa: Transactor[F])(
    implicit contextShift: ContextShift[F]
) extends Http4sDsl[F] {

  def listUsers: F[Either[Unit, List[User]]] =
    UserDao.query.list.transact(xa).map(Either.right)

  def getUser(id: UUID): F[Either[Unit, User]] =
    UserDao.query.filter(id).selectOption.transact(xa) map {
      case Some(user) => Either.right(user)
      case _          => Either.left(())
    }

  def createUser(user: User.Create): F[Either[Unit, User]] =
    UserDao.create(user).transact(xa) map {
      case user: User => Either.right(user)
      case _          => Either.left(())
    }

  def deleteUser(id: UUID): F[Either[Unit, Unit]] =
    UserDao.query.filter(id).delete.transact(xa) map {
      case 1 => Either.right(())
      case _ => Either.left(())
    }

  def updateUser(id: UUID, user: User): F[Either[Unit, Unit]] =
    UserDao.update(id, user).transact(xa) map {
      case 1 => Either.right(())
      case _ => Either.left(())
    }

  val routes
    : HttpRoutes[F] = UserEndpoints.listUsers.toRoutes(_ => listUsers) <+> UserEndpoints.getUser
    .toRoutes(id => getUser(id)) <+> UserEndpoints.createUser.toRoutes(
    json => createUser(json)
  ) <+> UserEndpoints.deleteUser.toRoutes(id => deleteUser(id)) <+> UserEndpoints.updateUser
    .toRoutes { case (json, id) => updateUser(id, json) }
}
