package io.github.copperlight.scalaplat.akka

import akka.actor.ActorPath
import com.typesafe.config.ConfigFactory
import org.scalatest.funsuite.AnyFunSuite

/** Sanity check the default pattern for extracting an id from the path. */
class PathsSuite extends AnyFunSuite {

  private val mapper = Paths.createMapper(ConfigFactory.load().getConfig("atlas.akka"))

  private def path(str: String): ActorPath = ActorPath.fromString(str)

  test("contains dashes") {
    val id = mapper(path("akka://test/system/IO-TCP/$123"))
    assert("IO-TCP" === id)
  }

  test("path with child") {
    val id = mapper(path("akka://test/user/foo/$123"))
    assert("foo" === id)
  }

  test("temporary actor") {
    val id = mapper(path("akka://test/user/$123"))
    assert("uncategorized" === id)
  }

  test("stream supervisor") {
    val id = mapper(path("akka://test/user/StreamSupervisor-99961"))
    assert("StreamSupervisor-" === id)
  }
}
