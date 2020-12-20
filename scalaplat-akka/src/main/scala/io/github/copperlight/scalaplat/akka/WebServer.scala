package io.github.copperlight.scalaplat.akka

import javax.inject.Inject
import javax.inject.Singleton
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import com.netflix.iep.service.AbstractService
import com.netflix.iep.service.ClassFactory
import com.netflix.spectator.api.Registry
import com.typesafe.config.Config
import com.typesafe.scalalogging.StrictLogging

import scala.concurrent.Await
import scala.concurrent.Future
import scala.concurrent.duration.Duration

/**
 * Web server instance.
 *
 * @param config
 *     System configuration used for main server settings. In particular `atlas.akka.port`
 *     is used for setting the server port.
 * @param classFactory
 *     Used to create instances of class names from the config file via the injector. The
 *     endpoints listed in `atlas.akka.endpoints` will be created using this factory.
 * @param registry
 *     Metrics registry for reporting server stats.
 * @param system
 *     Instance of the actor system.
 */
@Singleton
class WebServer @Inject() (
                            config: Config,
                            classFactory: ClassFactory,
                            registry: Registry,
                            implicit val system: ActorSystem,
                            implicit val materializer: Materializer
                          ) extends AbstractService
  with StrictLogging {

  private implicit val executionContext = system.dispatcher

  private val port = config.getInt("atlas.akka.port")

  private var bindingFuture: Future[ServerBinding] = _

  protected def startImpl(): Unit = {
    val handler = new RequestHandler(config, classFactory)
    bindingFuture = Http()
      .newServerAt("0.0.0.0", port)
      .bindFlow(Route.toFlow(handler.routes))
    logger.info(s"started $name on port $port")
  }

  protected def stopImpl(): Unit = {
    if (bindingFuture != null) {
      Await.ready(bindingFuture.flatMap(_.unbind()), Duration.Inf)
    }
  }

  def actorSystem: ActorSystem = system
}
