package io.github.copperlight.scalaplat.config

import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.StrictLogging
import munit.FunSuite

import java.util.concurrent.atomic.{AtomicInteger, AtomicReference}
import java.util.function.Consumer

class DynamicConfigManagerSuite extends FunSuite with StrictLogging {

  private def config(props: String*) = {
    val str = props.mkString("\n")
    ConfigFactory.parseString(str)
  }

  private def newInstance(baseConfig: Config): DynamicConfigManager = {
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
    val value = new AtomicInteger
    val mgr = newInstance(config("a.b = 1"))
    mgr.addListener(ConfigListener.forPath("a", (c: Config) => value.set(c.getInt("b"))))
    assertEquals(1, value.get)
    mgr.setOverrideConfig(config("a.b = 2"))
    assertEquals(2, value.get)
  }

  test("listener only called on change") {
    val value = new AtomicInteger
    val mgr = newInstance(config("a.b = 1"))

    val consumer: Consumer[Config] = (c: Config) => {
        val v = c.getInt("b")
        if (v == value.get) fail("listener invoked without a change in the value")
        value.set(v)
    }

    mgr.addListener(ConfigListener.forPath("a", consumer))
    mgr.setOverrideConfig(config("a.b = 1"))
  }

  test("listener failure ignored") {
    val value = new AtomicInteger
    val mgr = newInstance(config("a.b = 1"))
    mgr.addListener(ConfigListener.forPath("c", (c: Config) => value.addAndGet(c.getInt("b"))))
    mgr.addListener(ConfigListener.forPath("a", (c: Config) => value.addAndGet(c.getInt("b"))))
    mgr.setOverrideConfig(config("a.b = 2"))
    assertEquals(3, value.get)
  }

  test("listener remove") {
    val value = new AtomicInteger
    val mgr = newInstance(config("a.b = 1"))

    val listener = ConfigListener.forPath("a", (c: Config) => value.set(c.getInt("b")))
    mgr.addListener(listener)
    mgr.setOverrideConfig(config("a.b = 2"))
    assertEquals(2, value.get)

    mgr.removeListener(listener)
    mgr.setOverrideConfig(config("a.b = 3"))
    assertEquals(2, value.get)
  }

  test("config listener") {
    val value: AtomicReference[Config] = new AtomicReference[Config]
    val mgr: DynamicConfigManager = newInstance(config("a.b = 1"))
    mgr.addListener(ConfigListener.forConfig("a", (c: Config) => value.set(c)))

    mgr.setOverrideConfig(config("a.b = 2"))
    assertEquals(2, value.get.getInt("b"))

    mgr.setOverrideConfig(config("a.b = null"))
    assertEquals(value.get.hasPath("b"), false)

    mgr.setOverrideConfig(config("a = null"))
    assertEquals(value.get, null)

    mgr.setOverrideConfig(config("a.b = \"foo\""))
    assertEquals("foo", value.get.getString("b"))
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
