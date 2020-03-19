package com.azavea.pgsockets4s.api

import cats.effect._
import cats.implicits._
import com.azavea.pgsockets4s.api.commands.{Commands}
import com.azavea.pgsockets4s.api.services.SocketService
import org.http4s.implicits._
import org.http4s.server.blaze._
import org.http4s.server.middleware._
import org.http4s.server.{Router, Server => HTTP4sServer}

object Server extends IOApp {

  private def createServer: Resource[IO, HTTP4sServer[IO]] =
    for {
      s <- Resource.liftF { IO.pure { "hey" } }
      _ = println(s)
      router = CORS(
        Router(
          "/api" -> ResponseLogger
            .httpRoutes(false, false)(SocketService[IO].routes)
        )
      ).orNotFound
      server <- {
        BlazeServerBuilder[IO]
          .bindHttp(8080, "0.0.0.0")
          .withHttpApp(router)
          .resource
      }
    } yield {
      server
    }

  override def run(args: List[String]): IO[ExitCode] = {
    import Commands._

    applicationCommand.parse(args) map {
      case RunServer() =>
        createServer
          .use(_ => IO.never)
          .as(ExitCode.Success)
    } match {
      case Left(e) =>
        IO {
          println(e.toString())
        } map { _ =>
          ExitCode.Error
        }
      case Right(s) => s
    }
  }
}
