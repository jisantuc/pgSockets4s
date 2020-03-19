package com.azavea.pgsockets4s.api.services

import cats.effect.IO
import org.http4s._
import org.http4s.dsl.io._

object HelloRoutes {

  def helloRoutes = HttpRoutes.of[IO] {
    case GET -> Root / "hello" =>
      Ok("hello")
  }
}
