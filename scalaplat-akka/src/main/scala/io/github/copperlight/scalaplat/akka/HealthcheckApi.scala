package io.github.copperlight.scalaplat.akka

import javax.inject.Provider

import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.MediaTypes
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.netflix.atlas.akka.CustomDirectives._
import com.netflix.atlas.json.Json
import com.netflix.iep.service.ServiceManager
import com.typesafe.scalalogging.StrictLogging

/**
 * Healthcheck endpoint based on health status of the ServiceManager.
 */
class HealthcheckApi(serviceManagerProvider: Provider[ServiceManager])
  extends WebApi
    with StrictLogging {

  def routes: Route = {
    endpointPath("healthcheck") {
      get {
        val status =
          if (serviceManager.isHealthy) StatusCodes.OK else StatusCodes.InternalServerError
        val entity = HttpEntity(MediaTypes.`application/json`, summary)
        complete(HttpResponse(status = status, entity = entity))
      }
    }
  }

  private def serviceManager: ServiceManager = serviceManagerProvider.get

  private def summary: String = {
    import scala.jdk.CollectionConverters._
    val states = serviceManager.services().asScala.map(s => s.name -> s.isHealthy).toMap
    Json.encode(states)
  }
}
