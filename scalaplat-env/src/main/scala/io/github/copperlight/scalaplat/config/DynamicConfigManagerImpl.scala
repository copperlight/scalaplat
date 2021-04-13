package io.github.copperlight.scalaplat.config

import com.typesafe.config.Config
import com.typesafe.scalalogging.StrictLogging

import java.util.concurrent.ConcurrentHashMap

/**
  * Default implementation of the dynamic config manager interface.
  */
class DynamicConfigManagerImpl(baseConfig: Config) extends DynamicConfigManager with StrictLogging {
  type ConfigListenerSet = ConcurrentHashMap.KeySetView[ConfigListener, java.lang.Boolean]

  private var current: Config = baseConfig
  private val listeners: ConfigListenerSet = ConcurrentHashMap.newKeySet

  override def get: Config = current

  override def setOverrideConfig(`override`: Config): Unit = {
    val previous = current
    current = `override`.withFallback(baseConfig).resolve

    listeners.forEach { (listener: ConfigListener) =>
      invokeListener(listener, previous, current)
    }
  }

  private def invokeListener(listener: ConfigListener, previous: Config, current: Config): Unit = {
    try {
      listener.onUpdate(previous, current)
    } catch {
      case e: Exception =>
        logger.warn("failed to update a listener", e)
    }
  }

  override def addListener(listener: ConfigListener): Unit = {
    listeners.add(listener)
    invokeListener(listener, null, current)
  }

  override def removeListener(listener: ConfigListener): Unit = {
    listeners.remove(listener)
  }
}
