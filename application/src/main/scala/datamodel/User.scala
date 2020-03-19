package com.azavea.pgsockets4s.datamodel

import java.util.UUID

import io.circe._
import io.circe.generic.semiauto._

case class User(id: UUID, email: String)

object User {
  implicit val userDecoder: Decoder[User] = deriveDecoder[User]
  implicit val userEncoder: Encoder[User] = deriveEncoder[User]

  case class Create(email: String)

  object Create {
    implicit val userCreateDecoder: Decoder[Create] = deriveDecoder[Create]
    implicit val userCreateEncoder: Encoder[Create] = deriveEncoder[Create]

  }

}
