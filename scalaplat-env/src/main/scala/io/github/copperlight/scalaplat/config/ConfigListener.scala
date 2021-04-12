package io.github.copperlight.scalaplat.config

import com.typesafe.config.Config

import java.util.function.BiFunction
import java.util.function.Consumer

/**
  * Listener that will get invoked when the config is updated.
  */
trait ConfigListener {

  /**
    * Create a listener instance that will invoke the consumer when an update causes a
    * change for the specified path.
    *
    * @param path
    *  Config prefix to get from the config. If null, then the full config will be used.
    * @param consumer
    *  Handler that will get invoked if the update resulted in a change for the specified
    *  path.
    * @return
    *  Listener instance that forwards changes for the path to the consumer.
    */
  def forPath(path: String, consumer: Consumer[Config]): ConfigListener = {
    (previous: Config, current: Config) =>
      {
        val c1: Config = if (path == null) {
          previous
        } else {
          ListenerUtils.getConfig(previous, path)
        }

        val c2: Config = if (path == null) {
          current
        } else {
          ListenerUtils.getConfig(current, path)
        }

        if (ListenerUtils.hasChanged(c1, c2)) {
          consumer.accept(c2)
        }
      }
  }

  /**
   * Create a listener instance that will invoke the consumer when an update causes a
   * change for the specified property.
   *
   * @param property
   *  Property to get from the config.
   * @param consumer
   *  Handler that will get invoked if the update resulted in a change for the property.
   * @return
   *  Listener instance that forwards changes for the property to the consumer.
   */
  def forConfig(property: String, consumer: Consumer[Config]): ConfigListener = {
    forConfigEntry(property, consumer, (cfg: Config, name: String) => cfg.getConfig(name))
  }

  /**
    * Create a listener instance that will invoke the consumer when an update causes a
    * change for the specified property.
    *
    * @param property
    *   Property to get from the config.
    * @param consumer
    *   Handler that will get invoked if the update resulted in a change for the property.
    * @param accessor
    *   Function used to access the property value from the config.
    * @return
    *   Listener instance that forwards changes for the property to the consumer.
    */
  def forConfigEntry[T](
    property: String,
    consumer: Consumer[T],
    accessor: BiFunction[Config, String, T]
  ): ConfigListener = {
    if (property == null) throw new NullPointerException("property cannot be null")

    (previous: Config, current: Config) => {
      val v1 = ListenerUtils.getOrNull(previous, property, accessor)
      val v2 = ListenerUtils.getOrNull(current, property, accessor)
      if (ListenerUtils.hasChanged(v1, v2)) consumer.accept(v2)
    }
  }

  /**
    * Invoked when the config is updated by a call to
    * [[DynamicConfigManager#setOverrideConfig ( Config )]]. This method will be invoked
    * from the thread setting the override. It should be cheap to allow changes to quickly
    * propagate to all listeners. If an exception is thrown, then a warning will be logged
    * and the manager will move on.
    *
    * @param previous
    *   Previous config instance.
    * @param current
    *   Current config instance with the update override applied over the base layer.
    */
  def onUpdate(previous: Config, current: Config): Unit
}
