package io.github.copperlight.scalaplat.akka

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.typesafe.config.Config

/**
 * Adds a directive to serve static pages from the 'www' resource directory.
 */
class StaticPages(config: Config) extends WebApi {

  private val defaultPage = config.getString("atlas.akka.static.default-page")

  def routes: Route = {
    pathEndOrSingleSlash {
      redirect(defaultPage, StatusCodes.MovedPermanently)
    } ~
      prefixRoutes
  }

  def prefixRoutes: Route = {

    val staticFiles = pathPrefix("static") {
      pathEndOrSingleSlash {
        getFromResource("www/index.html")
      } ~
        getFromResourceDirectory("www")
    }

    import scala.jdk.CollectionConverters._
    val singlePagePrefixes = config.getConfigList("atlas.akka.static.single-page-prefixes")
    singlePagePrefixes.asScala.foldLeft(staticFiles) { (acc, cfg) =>
      val prefix = cfg.getString("prefix")
      val resource = cfg.getString("resource")
      acc ~ pathPrefix(prefix) {
        getFromResource(resource)
      }
    }
  }
}
