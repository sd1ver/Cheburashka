package ru.poliwrath.account.server
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object AccountServerStarter extends App{

  val logger = LoggerFactory.getLogger(AccountServerStarter.getClass)

  logger.info("Account server started")

  implicit val system: ActorSystem = ActorSystem("my-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val route =
    path("hello") {
      get {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
      }
    }

  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

  logger.info(s"Server online at http://localhost:8080/hello\nPress RETURN to stop...")
  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())


}