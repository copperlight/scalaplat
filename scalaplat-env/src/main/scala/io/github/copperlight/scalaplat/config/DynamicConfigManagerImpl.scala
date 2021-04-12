package io.github.copperlight.scalaplat.config

import com.typesafe.config.Config
import org.slf4j.LoggerFactory

import java.util.concurrent.ConcurrentHashMap

/**
 * Default implementation of the dynamic config manager interface.
 */
class DynamicConfigManagerImpl extends DynamicConfigManager {
  private val LOGGER = LoggerFactory.getLogger(classOf[DynamicConfigManagerImpl])

  private val baseConfig: Config = null
  private var current: Config = null

  private val listeners = ConcurrentHashMap.newKeySet

  /** Create a new instance. */
  def this(baseConfig: Config) {
    this()
    this.baseConfig = baseConfig
    this.current = baseConfig
  }

  override def get: Config = current

  override def setOverrideConfig(`override`: Config): Unit = {
    val previous = current
    current = `override`.withFallback(baseConfig).resolve
    listeners.forEach((listener: ConfigListener) => invokeListener(listener, previous, current))
  }

  private def invokeListener(listener: ConfigListener, previous: Config, current: Config): Unit = {
    try {
      listener.onUpdate(previous, current)
    } catch {
      case e: Exception =>
        LOGGER.warn("failed to update a listener", e)
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
