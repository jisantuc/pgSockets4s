package com.azavea.pgsockets4s.api.services

import cats.effect._
import cats.implicits._
import fs2._
import org.http4s._
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.websocket._
import org.http4s.websocket.WebSocketFrame
import org.http4s.websocket.WebSocketFrame._
import skunk.data.Identifier
import skunk.Session

class SocketService[F[_]](sessionResource: Resource[F, Session[F]])(
    implicit F: ConcurrentEffect[F],
    timer: Timer[F]
) extends Http4sDsl[F] {

  def routes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root / "hello" =>
      Ok("Hello world.")

    case GET -> Root / "ws" / channelName =>
      def toClient(session: Session[F]): Stream[F, WebSocketFrame] =
        Identifier.fromString(channelName) traverse { ident =>
          session.channel(ident).listen(1).map(d => Text(s"Ping! $d"))
        } map {
          case Right(frame) => frame
          case Left(_)      => throw new Exception("boom")
        }

      val fromClient: Pipe[F, WebSocketFrame, Unit] = _.evalMap {
        case Text(t, _) => F.delay(println(t))
        case f          => F.delay(println(s"Unknown type: $f"))
      }

      sessionResource.use { session =>
        WebSocketBuilder[F].build(toClient(session), fromClient)
      }
  }

  def stream: Stream[F, ExitCode] =
    BlazeServerBuilder[F]
      .bindHttp(8080)
      .withHttpApp(routes.orNotFound)
      .serve
}

object SocketService {

  def apply[F[_]: ConcurrentEffect: Timer](sessionResource: Resource[F, Session[F]]): SocketService[F] =
    new SocketService(sessionResource)
}
