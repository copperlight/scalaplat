package io.github.copperlight.scalaplat.config

import com.typesafe.config.{Config, ConfigFactory, ConfigMemorySize}
import com.typesafe.scalalogging.StrictLogging
import munit.FunSuite

import java.time.temporal.TemporalAmount
import java.time.{Duration, Period}
import java.util.concurrent.atomic.{AtomicInteger, AtomicReference}

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

    val consumer: Config => Unit = (c: Config) => {
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

    logger.info(s"${mgr.get}")
    logger.info(s"${value.get}")
    mgr.setOverrideConfig(config("a.b = 2"))
    assertEquals(2, value.get.getInt("b"))

    logger.info(s"${value.get}")
    mgr.setOverrideConfig(config("a.b = null"))
    assertEquals(value.get.hasPath("b"), false)

    logger.info(s"${value.get}")
    mgr.setOverrideConfig(config("a = null"))
    assertEquals(value.get, null)

    mgr.setOverrideConfig(config("a.b = \"foo\""))
    assertEquals("foo", value.get.getString("b"))
  }

  test("config list listener") {
    val value: AtomicReference[List[Config]] = new AtomicReference[List[Config]]
    val mgr: DynamicConfigManager = newInstance(config("a.b = [{c=1},{c=2}]"))
    mgr.addListener(ConfigListener.forConfigList("a.b", (c: List[Config]) => value.set(c)))
    assertEquals(2, value.get.size)

    mgr.setOverrideConfig(config("a.b = []"))
    assertEquals(value.get.isEmpty, true)
  }

  test("string listener") {
    val value: AtomicReference[String] = new AtomicReference[String]
    val mgr: DynamicConfigManager = newInstance(config("a.b = 1"))
    mgr.addListener(ConfigListener.forString("a.b", (s: String) => value.set(s)))

    mgr.setOverrideConfig(config("a.b = 2"))
    assertEquals("2", value.get)

    mgr.setOverrideConfig(config("a.b = null"))
    assertEquals(value.get, null)

    mgr.setOverrideConfig(config("a.b = \"foo\""))
    assertEquals("foo", value.get)
  }

  test("string listener null prop") {
    intercept[java.lang.NullPointerException] {
      ConfigListener.forString(null, (_: String) => {})
    }
  }

  test("string list listener") {
    val value: AtomicReference[List[String]] = new AtomicReference[List[String]]
    val mgr: DynamicConfigManager = newInstance(config("a.b = [1,2]"))
    mgr.addListener(ConfigListener.forStringList("a.b", (s: List[String]) => value.set(s)))

    mgr.setOverrideConfig(config("a.b = [\"foo\"]"))
    assertEquals(List("foo"), value.get)
  }

  test("boolean listener") {
    val value: AtomicReference[Boolean] = new AtomicReference[Boolean]
    val mgr: DynamicConfigManager = newInstance(config("a.b = false"))
    mgr.addListener(ConfigListener.forBoolean("a.b", b => value.set(b.asInstanceOf[Boolean])))
    assertEquals(value.get, false)
    mgr.setOverrideConfig(config("a.b = true"))
    assertEquals(value.get, true)
  }

  test("boolean list listener") {
    val value: AtomicReference[List[Boolean]] = new AtomicReference[List[Boolean]]
    val mgr: DynamicConfigManager = newInstance(config("a.b = [false]"))
    mgr.addListener(ConfigListener.forBooleanList("a.b", (b: List[Boolean]) => value.set(b)))
    assertEquals(value.get.head, false)
    mgr.setOverrideConfig(config("a.b = [true]"))
    assertEquals(value.get.head, true)
  }

  test("int listener") {
    val value: AtomicReference[Int] = new AtomicReference[Int]
    val mgr: DynamicConfigManager = newInstance(config("a.b = 1"))
    mgr.addListener(ConfigListener.forInt("a.b", i => value.set(i.asInstanceOf[Int])))

    mgr.setOverrideConfig(config("a.b = 2"))
    assertEquals(2, value.get)

    // for scala Ints, nulls become zero
    mgr.setOverrideConfig(config("a.b = null"))
    assertEquals(value.get, 0)

    // wrong type fails to update - logs warning, but does not throw
    mgr.setOverrideConfig(config("a.b = \"foo\""))
    assertEquals(value.get, 0)
  }

  test("int list listener") {
    val value: AtomicReference[List[Int]] = new AtomicReference[List[Int]]
    val mgr: DynamicConfigManager = newInstance(config("a.b = [1]"))
    mgr.addListener(ConfigListener.forIntList("a.b", (i: List[Int]) => value.set(i)))
    assertEquals(1, value.get.head)
    mgr.setOverrideConfig(config("a.b = [2]"))
    assertEquals(2, value.get.head)
  }

  test("long listener") {
    val value: AtomicReference[Long] = new AtomicReference[Long]
    val mgr: DynamicConfigManager = newInstance(config("a.b = 1"))
    mgr.addListener(ConfigListener.forLong("a.b", l => value.set(l.asInstanceOf[Long])))
    assertEquals(1L, value.get)
    mgr.setOverrideConfig(config("a.b = 2"))
    assertEquals(2L, value.get)
  }

  test("long list listener") {
    val value: AtomicReference[List[Long]] = new AtomicReference[List[Long]]
    val mgr: DynamicConfigManager = newInstance(config("a.b = [1]"))
    mgr.addListener(ConfigListener.forLongList("a.b", (l: List[Long]) => value.set(l)))
    assertEquals(1L, value.get.head)
    mgr.setOverrideConfig(config("a.b = [2]"))
    assertEquals(2L, value.get.head)
  }

  test("bytes listener") {
    val value: AtomicReference[Long] = new AtomicReference[Long]
    val mgr: DynamicConfigManager = newInstance(config("a.b = 1k"))
    mgr.addListener(ConfigListener.forBytes("a.b", l => value.set(l.asInstanceOf[Long])))
    assertEquals(1024L, value.get)
    mgr.setOverrideConfig(config("a.b = 2k"))
    assertEquals(2048L, value.get)
  }

  test("bytes list listener") {
    val value: AtomicReference[List[Long]] = new AtomicReference[List[Long]]
    val mgr: DynamicConfigManager = newInstance(config("a.b = [1k]"))
    mgr.addListener(ConfigListener.forBytesList("a.b", (l: List[Long]) => value.set(l)))
    assertEquals(1024L, value.get.head)
    mgr.setOverrideConfig(config("a.b = [2k]"))
    assertEquals(2048L, value.get.head)
  }

  test("memory size listener") {
    val value: AtomicReference[ConfigMemorySize] = new AtomicReference[ConfigMemorySize]
    val mgr: DynamicConfigManager = newInstance(config("a.b = 1k"))
    mgr.addListener(ConfigListener.forMemorySize("a.b", (c: ConfigMemorySize) => value.set(c)))
    assertEquals(ConfigMemorySize.ofBytes(1024L), value.get)
    mgr.setOverrideConfig(config("a.b = 2k"))
    assertEquals(ConfigMemorySize.ofBytes(2048L), value.get)
  }

  test("memory size list listener") {
    val value: AtomicReference[List[ConfigMemorySize]] = new AtomicReference[List[ConfigMemorySize]]
    val mgr: DynamicConfigManager = newInstance(config("a.b = [1k]"))
    mgr.addListener(ConfigListener.forMemorySizeList("a.b", (c: List[ConfigMemorySize]) => value.set(c)))
    assertEquals(ConfigMemorySize.ofBytes(1024L), value.get.head)
    mgr.setOverrideConfig(config("a.b = [2k]"))
    assertEquals(ConfigMemorySize.ofBytes(2048L), value.get.head)
  }

  test("double listener") {
    val value: AtomicReference[Double] = new AtomicReference[Double]
    val mgr: DynamicConfigManager = newInstance(config("a.b = 1.0"))
    mgr.addListener(ConfigListener.forDouble("a.b", d => value.set(d.asInstanceOf[Double])))
    assertEquals(1.0, value.get)
    mgr.setOverrideConfig(config("a.b = 2.0"))
    assertEquals(2.0, value.get)
  }

  test("double list listener") {
    val value: AtomicReference[List[Double]] = new AtomicReference[List[Double]]
    val mgr: DynamicConfigManager = newInstance(config("a.b = [1.0]"))
    mgr.addListener(ConfigListener.forDoubleList("a.b", (d: List[Double]) => value.set(d)))
    assertEquals(1.0, value.get.head)
    mgr.setOverrideConfig(config("a.b = [2.0]"))
    assertEquals(2.0, value.get.head)
  }

  test("number listener") {
    val value: AtomicReference[Number] = new AtomicReference[Number]
    val mgr: DynamicConfigManager = newInstance(config("a.b = 1.0"))
    mgr.addListener(ConfigListener.forNumber("a.b", (n: Number) => value.set(n)))
    assertEquals(1.asInstanceOf[Number], value.get)
    mgr.setOverrideConfig(config("a.b = 2.0"))
    assertEquals(2.asInstanceOf[Number], value.get)
  }

  test("number list listener") {
    val value: AtomicReference[List[Number]] = new AtomicReference[List[Number]]
    val mgr: DynamicConfigManager = newInstance(config("a.b = [1.0]"))
    mgr.addListener(ConfigListener.forNumberList("a.b", (n: List[Number]) => value.set(n)))
    assertEquals(1.asInstanceOf[Number], value.get.head)
    mgr.setOverrideConfig(config("a.b = [2.0]"))
    assertEquals(2.asInstanceOf[Number], value.get.head)
  }

  test("duration listener") {
    val value: AtomicReference[Duration] = new AtomicReference[Duration]
    val mgr: DynamicConfigManager = newInstance(config("a.b = 1d"))
    mgr.addListener(ConfigListener.forDuration("a.b", (d: Duration) => value.set(d)))
    assertEquals(Duration.ofDays(1), value.get)
    mgr.setOverrideConfig(config("a.b = 2d"))
    assertEquals(Duration.ofDays(2), value.get)
  }

  test("duration list listener") {
    val value: AtomicReference[List[Duration]] = new AtomicReference[List[Duration]]
    val mgr: DynamicConfigManager = newInstance(config("a.b = [1d]"))
    mgr.addListener(ConfigListener.forDurationList("a.b", (d: List[Duration]) => value.set(d)))
    assertEquals(Duration.ofDays(1), value.get.head)
    mgr.setOverrideConfig(config("a.b = [2d]"))
    assertEquals(Duration.ofDays(2), value.get.head)
  }

  test("period listener") {
    val value: AtomicReference[Period] = new AtomicReference[Period]
    val mgr: DynamicConfigManager = newInstance(config("a.b = 1d"))
    mgr.addListener(ConfigListener.forPeriod("a.b", (p: Period) => value.set(p)))
    assertEquals(Period.ofDays(1), value.get)
    mgr.setOverrideConfig(config("a.b = 2d"))
    assertEquals(Period.ofDays(2), value.get)
  }

  test("temporal listener") {
    val value: AtomicReference[TemporalAmount] = new AtomicReference[TemporalAmount]
    val mgr: DynamicConfigManager = newInstance(config("a.b = 1d"))
    mgr.addListener(ConfigListener.forTemporal("a.b", (t: TemporalAmount) => value.set(t)))
    assertEquals(Duration.ofDays(1).asInstanceOf[TemporalAmount], value.get)
    mgr.setOverrideConfig(config("a.b = 2d"))
    assertEquals(Duration.ofDays(2).asInstanceOf[TemporalAmount], value.get)
  }
}
