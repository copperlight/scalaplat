package io.github.copperlight.scalaplat.akka

import akka.actor.Actor
import akka.actor.ActorSystem
import akka.util.Timeout
import com.netflix.iep.service.DefaultClassFactory
import com.typesafe.config.ConfigFactory
import org.scalatest.funsuite.AnyFunSuite

import scala.concurrent.Await

class ActorServiceSuite extends AnyFunSuite {

  import scala.concurrent.duration._
  implicit val timeout = Timeout(5.seconds)

  test("simple actor") {
    val config = ConfigFactory.parseString(s"""
                                              |atlas.akka.actors = [
                                              |  {
                                              |    name = test
                                              |    class = "${classOf[ActorServiceSuite.EchoActor].getName}"
                                              |  }
                                              |]
      """.stripMargin)
    val system = ActorSystem("test", config)
    val service = new ActorService(system, config, new DefaultClassFactory())
    service.start()

    try {
      val ref = system.actorSelection("/user/test")
      val v = Await.result(akka.pattern.ask(ref, "ping"), Duration.Inf)
      assert(v === "ping")
    } finally {
      service.stop()
      Await.ready(system.terminate(), Duration.Inf)
    }
  }

  test("actor with router config") {
    val config = ConfigFactory.parseString(s"""
                                              |atlas.akka.actors = [
                                              |  {
                                              |    name = test
                                              |    class = "${classOf[ActorServiceSuite.EchoActor].getName}"
                                              |  }
                                              |]
                                              |
                                              |akka.actor.deployment {
                                              |  /test {
                                              |    router = round-robin-pool
                                              |    nr-of-instances = 2
                                              |  }
                                              |}
      """.stripMargin)
    val system = ActorSystem("test", config)
    val service = new ActorService(system, config, new DefaultClassFactory())
    service.start()

    try {
      val ref = system.actorSelection("/user/test")
      val v = Await.result(akka.pattern.ask(ref, "ping"), Duration.Inf)
      assert(v === "ping")
    } finally {
      service.stop()
      Await.ready(system.terminate(), Duration.Inf)
    }
  }
}

object ActorServiceSuite {

  class EchoActor extends Actor {

    override def receive: Receive = {
      case v => sender() ! v
    }
  }
}
