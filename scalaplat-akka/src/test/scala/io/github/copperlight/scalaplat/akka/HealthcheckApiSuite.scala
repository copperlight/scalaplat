package io.github.copperlight.scalaplat.akka

import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Provider

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.RouteTestTimeout
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.netflix.atlas.json.Json
import com.netflix.iep.service.Service
import com.netflix.iep.service.ServiceManager
import com.netflix.iep.service.State
import org.scalatest.funsuite.AnyFunSuite

class HealthcheckApiSuite extends AnyFunSuite with ScalatestRouteTest {

  import scala.concurrent.duration._

  implicit val routeTestTimeout = RouteTestTimeout(5.second)

  private val serviceHealth = new AtomicBoolean(false)

  val services = new java.util.HashSet[Service]
  services.add(new Service {

    override def state(): State = State.RUNNING

    override def name(): String = "test"

    override def isHealthy: Boolean = serviceHealth.get()
  })

  val serviceManager = new ServiceManager(services)

  val provider = new Provider[ServiceManager] {

    override def get(): ServiceManager = serviceManager
  }
  val endpoint = new HealthcheckApi(provider)

  test("/healthcheck pre-start") {
    serviceHealth.set(false)
    Get("/healthcheck") ~> endpoint.routes ~> check {
      assert(response.status === StatusCodes.InternalServerError)
      val data = Json.decode[Map[String, Boolean]](responseAs[String])
      assert(!data("test"))
    }
  }

  test("/healthcheck post-start") {
    serviceHealth.set(true)
    Get("/healthcheck") ~> endpoint.routes ~> check {
      assert(response.status === StatusCodes.OK)
      val data = Json.decode[Map[String, Boolean]](responseAs[String])
      assert(data("test"))
    }
  }
}
