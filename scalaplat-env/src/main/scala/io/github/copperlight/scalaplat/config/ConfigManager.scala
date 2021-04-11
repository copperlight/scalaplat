package io.github.copperlight.scalaplat.config

import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.StrictLogging

import java.io.File

/**
 * Helper for loading the Typesafe Config instance. In most cases, for apps using the scalaplat
 * libraries, this should be used instead of the [[ConfigFactory]]. It supports loading
 * additional configuration files based on the context via the `copperlight.scalaplat.include`
 * setting.
 */
object ConfigManager extends StrictLogging {
  private val CONFIG: Config = load()

  /** Get a cached copy of the config loaded from the default class loader. */
  def get: Config = CONFIG

  /** Load config using the default class loader. */
  def load(): Config = load(pickClassLoader())

  /** Load config using the specified class loader. */
  def load(classLoader: ClassLoader): Config = {
    val prop = "copperlight.scalaplat.env.account-type"
    val baseConfig = ConfigFactory.load(classLoader)
    val envConfigName = s"scalaplat-${baseConfig.getString(prop)}.conf"
    val envConfig = loadConfigByName(classLoader, envConfigName)
    loadIncludes(classLoader, envConfig.withFallback(baseConfig).resolve)
  }

  private def pickClassLoader(): ClassLoader = {
    Option(Thread.currentThread().getContextClassLoader).fold {
      logger.warn(s"Thread.currentThread().getContextClassLoader is null, " +
        s"using loader for ${ConfigManager.getClass.getName}")
      ConfigManager.getClass.getClassLoader
    } { cl =>
      cl
    }
  }

  private def loadConfigByName(classLoader: ClassLoader, name: String): Config = {
    logger.debug(s"loading config $name")

    if (name.startsWith("file:")) {
      val f = new File(name.substring("file:".length))
      ConfigFactory.parseFile(f)
    } else {
      ConfigFactory.parseResources(classLoader, name)
    }
  }

  private def loadIncludes(classLoader: ClassLoader, baseConfig: Config): Config = {
    val prop = "copperlight.scalaplat.include"
    var acc = baseConfig

    baseConfig.getStringList(prop).forEach { name =>
      val cfg = loadConfigByName(classLoader, name)
      acc = cfg.withFallback(acc)
    }

    acc.resolve()
  }
}
