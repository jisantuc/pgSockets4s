package com.azavea.pgsockets4s.database.util

import cats.implicits._
import doobie._
import doobie.postgres.circe.jsonb.implicits._
import io.circe._
import io.circe.syntax._

import scala.reflect.runtime.universe.TypeTag

object CirceJsonbMeta {

  def apply[Type: TypeTag: Encoder: Decoder] = {
    val get = Get[Json].tmap[Type](_.as[Type].valueOr(throw _))
    val put = Put[Json].tcontramap[Type](_.asJson)
    new Meta[Type](get, put)
  }
}

trait CirceJsonbMeta {
  // If you want to be able to put Json in the database and there is a circe codec available
  // you can do the following: implicit val <item>Meta: Meta[Item] = CirceJsonBMeta[Item]
}
