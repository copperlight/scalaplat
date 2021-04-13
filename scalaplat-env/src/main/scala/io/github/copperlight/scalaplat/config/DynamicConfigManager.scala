package io.github.copperlight.scalaplat.config

import com.typesafe.config.Config

/**
 * Base interface for a config manager that allows the base config to be updated with
 * an override layer dynamically at runtime.
 */
trait DynamicConfigManager {

  /**
   * Returns the current config instance, i.e., override with fallback to the base config.
   */
  def get: Config

  /**
   * Set the override config layer.
   */
  def setOverrideConfig(`override`: Config): Unit

  /**
   * Add a listener that will get invoked once when added and then each time the override config
   * layer is updated. When invoked for the initialization, the previous config value will be
   * `null`.
   */
  def addListener(listener: ConfigListener): Unit

  /**
   * Remove the listener so it will no longer get invoked.
   */
  def removeListener(listener: ConfigListener): Unit
}

object DynamicConfigManager {
  /**
   * Create a new instance of a dynamic config manager.
   *
   * @param baseConfig
   *   Base config layer that will be used as a fallback to the dynamic layer.
   * @return
   *   Config manager instance.
   */
  def create(baseConfig: Config) = new DynamicConfigManagerImpl(baseConfig)
}
