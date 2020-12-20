package io.github.copperlight.scalaplat.akka

import akka.http.scaladsl.testkit.RouteTestTimeout
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.funsuite.AnyFunSuite

class TestApiSuite extends AnyFunSuite with ScalatestRouteTest {

  import scala.concurrent.duration._

  implicit val routeTestTimeout = RouteTestTimeout(5.second)

  val endpoint = new TestApi(system)
  val routes = RequestHandler.standardOptions(endpoint.routes)

  test("/query-parsing-directive") {
    Get("/query-parsing-directive?regex=a|b|c") ~> routes ~> check {
      assert(responseAs[String] === "a|b|c")
    }
  }

  test("/query-parsing-explicit") {
    Get("/query-parsing-explicit?regex=a|b|c") ~> routes ~> check {
      assert(responseAs[String] === "a|b|c")
    }
  }

  test("/chunked") {
    Get("/chunked") ~> routes ~> check {
      assert(response.status.intValue === 200)
      assert(chunks.size === 42)
    }
  }

}
