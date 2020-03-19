package com.azavea.pgsockets4s.error

import cats.implicits._
import io.circe._
import io.circe.generic.semiauto._
import io.circe.syntax._

sealed abstract class CrudError

object CrudError {

  implicit val decCrudErrror: Decoder[CrudError] = Decoder[NotFound].widen

  implicit val encCrudError: Encoder[CrudError] = new Encoder[CrudError] {

    def apply(thing: CrudError): Json = thing match {
      case t: NotFound => t.asJson
    }
  }
}

case class NotFound(msg: String = "Not found") extends CrudError

object NotFound {
  implicit val encNotFound: Encoder[NotFound] = deriveEncoder
  implicit val decNotFound: Decoder[NotFound] = deriveDecoder
}
