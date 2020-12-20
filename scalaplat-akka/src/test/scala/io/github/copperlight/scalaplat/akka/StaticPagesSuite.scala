package io.github.copperlight.scalaplat.akka

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.typesafe.config.ConfigFactory
import org.scalatest.funsuite.AnyFunSuite

class StaticPagesSuite extends AnyFunSuite with ScalatestRouteTest {

  val endpoint = new StaticPages(ConfigFactory.load())

  test("/static/test") {
    Get("/static/test") ~> endpoint.routes ~> check {
      assert(responseAs[String] === "test text file\n")
    }
  }

  test("/static") {
    Get("/static") ~> endpoint.routes ~> check {
      assert(response.status === StatusCodes.OK)
      assert(responseAs[String].contains("Index Page"))
    }
  }

  test("/static/") {
    Get("/static/") ~> endpoint.routes ~> check {
      assert(response.status === StatusCodes.OK)
      assert(responseAs[String].contains("Index Page"))
    }
  }

  test("/") {
    Get("/") ~> endpoint.routes ~> check {
      assert(response.status === StatusCodes.MovedPermanently)
      val loc = response.headers.find(_.is("location")).map(_.value)
      assert(loc === Some("/ui"))
    }
  }

  test("/ui") {
    Get("/ui") ~> endpoint.routes ~> check {
      assert(response.status === StatusCodes.OK)
      assert(responseAs[String].contains("Index Page"))
    }
  }

  test("/ui/foo/bar") {
    Get("/ui/foo/bar") ~> endpoint.routes ~> check {
      assert(response.status === StatusCodes.OK)
      assert(responseAs[String].contains("Index Page"))
    }
  }
}
