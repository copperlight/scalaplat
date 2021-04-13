package io.github.copperlight.scalaplat.config

import com.typesafe.config.{Config, ConfigFactory}
import munit.FunSuite

import java.util.concurrent.atomic.AtomicInteger

class DynamicConfigManagerSuite extends FunSuite {

  private def config(props: String*) = {
    val str = props.mkString("\n")
    ConfigFactory.parseString(str)
  }

  private def newInstance(baseConfig: Config): DynamicConfigManagerImpl = {
    DynamicConfigManager.create(baseConfig)
  }

  test("base config") {
    val mgr = newInstance(config("a = 1"))
    assertEquals("1", mgr.get.getString("a"))
  }

  test("override config") {
    val mgr = newInstance(config("a = 1", "b = 2"))
    mgr.setOverrideConfig(config("a = 2", "c = 3"))
    assertEquals("2", mgr.get.getString("a"))
    assertEquals("2", mgr.get.getString("b"))
    assertEquals("3", mgr.get.getString("c"))
  }

  test("override references base") {
    val mgr = newInstance(config("a = 1"))
    mgr.setOverrideConfig(config("b = \"test_\"${a}"))
    assertEquals("test_1", mgr.get.getString("b"))
  }

  test("override does not resolve") {
    val mgr = newInstance(config("a = 1"))
    intercept[com.typesafe.config.ConfigException.UnresolvedSubstitution] {
      mgr.setOverrideConfig(config("b = \"test_\"${aa}"))
    }
  }

  test("bad config is not used") {
    val mgr = newInstance(config("a = 1"))

    intercept[com.typesafe.config.ConfigException.UnresolvedSubstitution] {
      mgr.setOverrideConfig(config("b = \"test_\"${aa}", "a = 2"))
    }

    assertEquals("1", mgr.get.getString("a"))
  }

  test("listener") {
//    val value = new AtomicInteger
//    val mgr = newInstance(config("a.b = 1"))
//    mgr.addListener(ConfigListener.forPath("a", (c: Config) => value.set(c.getInt("b"))))
//    assertEquals(1, value.get)
//    mgr.setOverrideConfig(config("a.b = 2"))
//    assertEquals(2, value.get)
  }

  test("listener only called on change") {
//    val value = new AtomicInteger
//    val mgr = newInstance(config("a.b = 1"))
//    mgr.addListener(ConfigListener.forPath("a", (c: Config) => {
//      def foo(c: Config) = {
//        val v = c.getInt("b")
//        if (v == value.get) fail("listener invoked without a change in the value")
//        value.set(v)
//      }
//
//      foo(c)
//    }))
//    mgr.setOverrideConfig(config("a.b = 1"))
  }

  test("listener failure ignored") {

  }

  test("listener remove") {

  }

  test("config listener") {

  }

  test("config list listener") {

  }

  test("string listener") {

  }

  test("string listener null prop") {

  }

  test("string list listener") {

  }

  test("boolean listener") {

  }

  test("boolean list listener") {

  }

  test("int listener") {

  }

  test("int list listener") {

  }

  test("long listener") {

  }

  test("long list listener") {

  }

  test("bytes listener") {

  }

  test("bytes list listener") {

  }

  test("memory size listener") {

  }

  test("memory size list listener") {

  }

  test("double listener") {

  }

  test("double list listener") {

  }

  test("number listener") {

  }

  test("number list listener") {

  }

  test("duration listener") {

  }

  test("duration list listener") {

  }

  test("period listener") {

  }

  test("temporal listener") {

  }
}
