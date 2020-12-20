package io.github.copperlight.scalaplat.akka

import akka.http.scaladsl.server.Route

/**
 * Base trait for classes providing an API to expose via the Atlas server.
 */
trait WebApi {

  def routes: Route
}
