package io.github.copperlight.scalaplat.akka

import javax.inject.Inject
import javax.inject.Singleton

import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.routing.FromConfig
import com.netflix.iep.service.AbstractService
import com.netflix.iep.service.ClassFactory
import com.typesafe.config.Config
import com.typesafe.scalalogging.StrictLogging

/**
 * Exposes actor system as service for healthcheck and proper shutdown. Additional
 * actors to start up can be specified using the `atlas.akka.actors` property.
 */
@Singleton
class ActorService @Inject() (system: ActorSystem, config: Config, classFactory: ClassFactory)
  extends AbstractService
    with StrictLogging {

  override def startImpl(): Unit = {
    import scala.jdk.CollectionConverters._
    config.getConfigList("atlas.akka.actors").asScala.foreach { cfg =>
      val name = cfg.getString("name")
      val cls = Class.forName(cfg.getString("class"))
      val ref = system.actorOf(newActor(name, cls), name)
      logger.info(s"created actor '${ref.path}' using class '${cls.getName}'")
    }
  }

  private def newActor(name: String, cls: Class[_]): Props = {
    val props = Props(classFactory.newInstance[Actor](cls))
    val routerCfgPath = s"akka.actor.deployment./$name.router"
    if (config.hasPath(routerCfgPath)) FromConfig.props(props) else props
  }

  override def stopImpl(): Unit = {}
}
