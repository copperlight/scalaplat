package io.github.copperlight.scalaplat.config

import com.typesafe.config.ConfigFactory
import munit.FunSuite

class ConfigManagerSuite extends FunSuite {
  test("account config") {
    assertEquals(ConfigManager.get.getBoolean("scalaplat.account-config-loaded"), true)
  }

  test("load from file") {
    assertEquals(ConfigManager.get.getBoolean("scalaplat.file-include-loaded"), true)
  }

  test("load from classpath") {
    assertEquals(ConfigManager.get.getBoolean("scalaplat.classpath-include-loaded"), true)
  }

  test("include overrides") {
    assertEquals(ConfigManager.get.getString("scalaplat.value"), "classpath")
  }

  test("include does not override substitutions") {
    assertEquals(ConfigManager.get.getString("scalaplat.substitute"), "application")
  }

  test("null context class loader") {
    val cl = Thread.currentThread().getContextClassLoader

    try {
      Thread.currentThread().setContextClassLoader(null)
      val config = ConfigManager.load()
      assertEquals(config.getString("copperlight.scalaplat.env.account-type"), "foo")
    } finally {
      Thread.currentThread().setContextClassLoader(cl)
    }
  }

  test("dynamic config") {
    assertEquals("classpath", ConfigManager.dynamicConfig.getString("scalaplat.value"))

    ConfigManager.dynamicConfigManager
      .setOverrideConfig(ConfigFactory.parseString("scalaplat.value = \"dynamic\""))

    assertEquals("dynamic", ConfigManager.dynamicConfig.getString("scalaplat.value"))
  }
}
