package io.github.copperlight.scalaplat.akka

import java.io.StringReader
import java.util.Properties

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.testkit.RouteTestTimeout
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import org.scalatest.funsuite.AnyFunSuite

class ConfigApiSuite extends AnyFunSuite with ScalatestRouteTest {

  import scala.concurrent.duration._

  implicit val routeTestTimeout = RouteTestTimeout(5.second)

  val sysConfig = ConfigFactory.load()
  val endpoint = new ConfigApi(sysConfig, system)

  test("/config") {
    Get("/api/v2/config") ~> endpoint.routes ~> check {
      val config = ConfigFactory.parseString(responseAs[String])
      assert(sysConfig === config)
    }
  }

  test("/config/") {
    Get("/api/v2/config/") ~> endpoint.routes ~> check {
      val config = ConfigFactory.parseString(responseAs[String])
      assert(sysConfig === config)
    }
  }

  test("/config/java") {
    Get("/api/v2/config/java") ~> endpoint.routes ~> check {
      val config = ConfigFactory.parseString(responseAs[String])
      assert(sysConfig.getConfig("java") === config)
    }
  }

  test("/config/os.arch") {
    import scala.jdk.CollectionConverters._
    Get("/api/v2/config/os.arch") ~> endpoint.routes ~> check {
      val config = ConfigFactory.parseString(responseAs[String])
      val v = sysConfig.getString("os.arch")
      val expected = ConfigFactory.parseMap(Map("value" -> v).asJava)
      assert(expected === config)
    }
  }

  test("/config format hocon") {
    Get("/api/v2/config?format=hocon") ~> endpoint.routes ~> check {
      val config = ConfigFactory.parseString(responseAs[String])
      assert(sysConfig === config)
    }
  }

  test("/config format json") {
    Get("/api/v2/config?format=json") ~> endpoint.routes ~> check {
      val config = ConfigFactory.parseString(responseAs[String])
      assert(sysConfig === config)
    }
  }

  test("/config format properties") {
    Get("/api/v2/config?format=properties") ~> endpoint.routes ~> check {
      import scala.jdk.CollectionConverters._
      val props = new Properties
      props.load(new StringReader(responseAs[String]))
      val config = ConfigFactory.parseProperties(props)

      // The quoting for keys seems to get messed with somewhere between Properties and Config
      // conversions. Not considered important enough to mess with right now so ignoring for the
      // test case...
      def normalize(c: Config): Map[String, String] = {
        c.entrySet.asScala
          .filter(!_.getKey.contains("\""))
          .map(t => t.getKey -> s"${t.getValue.unwrapped}")
          .toMap
      }

      val expected = normalize(sysConfig)
      val actual = normalize(config)
      assert(expected === actual)
    }
  }

  test("/config bad format") {
    Get("/api/v2/config?format=foo") ~> endpoint.routes ~> check {
      assert(response.status === BadRequest)
    }
  }

  test("/config/foo") {
    Get("/api/v2/config/foo") ~> endpoint.routes ~> check {
      assert(response.status === NotFound)
    }
  }

}
