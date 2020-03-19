package com.azavea.pgsockets4s.api.services

import cats.effect._
import fs2._
import org.http4s._
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.websocket._
import org.http4s.websocket.WebSocketFrame
import org.http4s.websocket.WebSocketFrame._

import scala.concurrent.duration._

class SocketService[F[_]](implicit F: ConcurrentEffect[F], timer: Timer[F]) extends Http4sDsl[F] {

  def routes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root / "hello" =>
      Ok("Hello world.")

    case GET -> Root / "ws" =>
      val toClient: Stream[F, WebSocketFrame] =
        Stream.awakeEvery[F](1.seconds).map(d => Text(s"Ping! $d"))
      val fromClient: Pipe[F, WebSocketFrame, Unit] = _.evalMap {
        case Text(t, _) => F.delay(println(t))
        case f          => F.delay(println(s"Unknown type: $f"))
      }
      WebSocketBuilder[F].build(toClient, fromClient)
  }

  def stream: Stream[F, ExitCode] =
    BlazeServerBuilder[F]
      .bindHttp(8080)
      .withHttpApp(routes.orNotFound)
      .serve
}

object SocketService {

  def apply[F[_]: ConcurrentEffect: Timer]: SocketService[F] =
    new SocketService[F]
}
