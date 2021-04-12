package io.github.copperlight.scalaplat.config

import com.typesafe.config.Config
import com.typesafe.config.ConfigMemorySize

import java.time.Duration
import java.time.Period
import java.time.temporal.TemporalAmount
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
    *   Config prefix to get from the config. If null, then the full config will be used.
    * @param consumer
    *   Handler that will get invoked if the update resulted in a change for the specified
    *   path.
    * @return
    *   Listener instance that forwards changes for the path to the consumer.
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
   *   Property to get from the config.
   * @param consumer
   *   Handler that will get invoked if the update resulted in a change for the property.
   * @return
   *   Listener instance that forwards changes for the property to the consumer.
   */
  def forConfig(property: String, consumer: Consumer[Config]): ConfigListener = {
    forConfigEntry(property, consumer, (config: Config, name: String) => config.getConfig(name))
  }

  /**
   * Create a listener instance that will invoke the consumer when an update causes a
   * change for the specified property.
   *
   * @param property
   *   Property to get from the config.
   * @param consumer
   *   Handler that will get invoked if the update resulted in a change for the property.
   * @return
   *   Listener instance that forwards changes for the property to the consumer.
   */
  def forConfigList(property: String, consumer: Consumer[List[_ <: Config]]): ConfigListener = {
    forConfigEntry(property, consumer, (config: Config, name: String) => config.getConfigList(name))
  }

  /**
   * Create a listener instance that will invoke the consumer when an update causes a
   * change for the specified property.
   *
   * @param property
   *   Property to get from the config.
   * @param consumer
   *   Handler that will get invoked if the update resulted in a change for the property.
   * @return
   *   Listener instance that forwards changes for the property to the consumer.
   */
  def forString(property: String, consumer: Consumer[String]): ConfigListener = {
    forConfigEntry(property, consumer, (config: Config, name: String) => config.getString(name))
  }

  /**
   * Create a listener instance that will invoke the consumer when an update causes a
   * change for the specified property.
   *
   * @param property
   *   Property to get from the config.
   * @param consumer
   *   Handler that will get invoked if the update resulted in a change for the property.
   * @return
   *   Listener instance that forwards changes for the property to the consumer.
   */
  def forStringList(property: String, consumer: Consumer[List[String]]): ConfigListener = {
    forConfigEntry(property, consumer, (config: Config, name: String) => config.getStringList(name))
  }

  /**
   * Create a listener instance that will invoke the consumer when an update causes a
   * change for the specified property.
   *
   * @param property
   *   Property to get from the config.
   * @param consumer
   *   Handler that will get invoked if the update resulted in a change for the property.
   * @return
   *   Listener instance that forwards changes for the property to the consumer.
   */
  def forBoolean(property: String, consumer: Consumer[Boolean]): ConfigListener = {
    forConfigEntry(property, consumer, (config: Config, name: String) => config.getBoolean(name))
  }

  /**
   * Create a listener instance that will invoke the consumer when an update causes a
   * change for the specified property.
   *
   * @param property
   *   Property to get from the config.
   * @param consumer
   *   Handler that will get invoked if the update resulted in a change for the property.
   * @return
   *   Listener instance that forwards changes for the property to the consumer.
   */
  def forBooleanList(property: String, consumer: Consumer[List[Boolean]]): ConfigListener = {
    forConfigEntry(property, consumer, (config: Config, name: String) => config.getBooleanList(name))
  }

  /**
   * Create a listener instance that will invoke the consumer when an update causes a
   * change for the specified property.
   *
   * @param property
   *   Property to get from the config.
   * @param consumer
   *   Handler that will get invoked if the update resulted in a change for the property.
   * @return
   *   Listener instance that forwards changes for the property to the consumer.
   */
  def forInt(property: String, consumer: Consumer[Integer]): ConfigListener = {
    forConfigEntry(property, consumer, (config: Config, name: String) => config.getInt(name))
  }

  /**
   * Create a listener instance that will invoke the consumer when an update causes a
   * change for the specified property.
   *
   * @param property
   *   Property to get from the config.
   * @param consumer
   *   Handler that will get invoked if the update resulted in a change for the property.
   * @return
   *   Listener instance that forwards changes for the property to the consumer.
   */
  def forIntList(property: String, consumer: Consumer[List[Integer]]): ConfigListener = {
    forConfigEntry(property, consumer, (config: Config, name: String) => config.getIntList(name))
  }

  /**
   * Create a listener instance that will invoke the consumer when an update causes a
   * change for the specified property.
   *
   * @param property
   *   Property to get from the config.
   * @param consumer
   *   Handler that will get invoked if the update resulted in a change for the property.
   * @return
   *   Listener instance that forwards changes for the property to the consumer.
   */
  def forLong(property: String, consumer: Consumer[Long]): ConfigListener = {
    forConfigEntry(property, consumer, (config: Config, name: String) => config.getLong(name))
  }

  /**
   * Create a listener instance that will invoke the consumer when an update causes a
   * change for the specified property.
   *
   * @param property
   *   Property to get from the config.
   * @param consumer
   *   Handler that will get invoked if the update resulted in a change for the property.
   * @return
   *   Listener instance that forwards changes for the property to the consumer.
   */
  def forLongList(property: String, consumer: Consumer[List[Long]]): ConfigListener = {
    forConfigEntry(property, consumer, (config: Config, name: String) => config.getLongList(name))
  }

  /**
   * Create a listener instance that will invoke the consumer when an update causes a
   * change for the specified property.
   *
   * @param property
   *   Property to get from the config.
   * @param consumer
   *   Handler that will get invoked if the update resulted in a change for the property.
   * @return
   *   Listener instance that forwards changes for the property to the consumer.
   */
  def forBytes(property: String, consumer: Consumer[Long]): ConfigListener = {
    forConfigEntry(property, consumer, (config: Config, name: String) => config.getBytes(name))
  }

  /**
   * Create a listener instance that will invoke the consumer when an update causes a
   * change for the specified property.
   *
   * @param property
   *   Property to get from the config.
   * @param consumer
   *   Handler that will get invoked if the update resulted in a change for the property.
   * @return
   *   Listener instance that forwards changes for the property to the consumer.
   */
  def forBytesList(property: String, consumer: Consumer[List[Long]]): ConfigListener = {
    forConfigEntry(property, consumer, (config: Config, name: String) => config.getBytesList(name))
  }

  /**
   * Create a listener instance that will invoke the consumer when an update causes a
   * change for the specified property.
   *
   * @param property
   *   Property to get from the config.
   * @param consumer
   *   Handler that will get invoked if the update resulted in a change for the property.
   * @return
   *   Listener instance that forwards changes for the property to the consumer.
   */
  def forMemorySize(property: String, consumer: Consumer[ConfigMemorySize]): ConfigListener = {
    forConfigEntry(property, consumer, (config: Config, name: String) => config.getMemorySize(name))
  }

  /**
   * Create a listener instance that will invoke the consumer when an update causes a
   * change for the specified property.
   *
   * @param property
   *   Property to get from the config.
   * @param consumer
   *   Handler that will get invoked if the update resulted in a change for the property.
   * @return
   *   Listener instance that forwards changes for the property to the consumer.
   */
  def forMemorySizeList(property: String, consumer: Consumer[List[ConfigMemorySize]]): ConfigListener = {
    forConfigEntry(property, consumer, (config: Config, name: String) => config.getMemorySizeList(name))
  }

  /**
   * Create a listener instance that will invoke the consumer when an update causes a
   * change for the specified property.
   *
   * @param property
   *   Property to get from the config.
   * @param consumer
   *   Handler that will get invoked if the update resulted in a change for the property.
   * @return
   *   Listener instance that forwards changes for the property to the consumer.
   */
  def forDouble(property: String, consumer: Consumer[Double]): ConfigListener = {
    forConfigEntry(property, consumer, (config: Config, name: String) => config.getDouble(name))
  }

  /**
   * Create a listener instance that will invoke the consumer when an update causes a
   * change for the specified property.
   *
   * @param property
   *   Property to get from the config.
   * @param consumer
   *   Handler that will get invoked if the update resulted in a change for the property.
   * @return
   *   Listener instance that forwards changes for the property to the consumer.
   */
  def forDoubleList(property: String, consumer: Consumer[List[Double]]): ConfigListener = {
    forConfigEntry(property, consumer, (config: Config, name: String) => config.getDoubleList(name))
  }

  /**
   * Create a listener instance that will invoke the consumer when an update causes a
   * change for the specified property.
   *
   * @param property
   *   Property to get from the config.
   * @param consumer
   *   Handler that will get invoked if the update resulted in a change for the property.
   * @return
   *   Listener instance that forwards changes for the property to the consumer.
   */
  def forNumber(property: String, consumer: Consumer[Number]): ConfigListener = {
    forConfigEntry(property, consumer, (config: Config, name: String) => config.getNumber(name))
  }

  /**
   * Create a listener instance that will invoke the consumer when an update causes a
   * change for the specified property.
   *
   * @param property
   *   Property to get from the config.
   * @param consumer
   *   Handler that will get invoked if the update resulted in a change for the property.
   * @return
   *   Listener instance that forwards changes for the property to the consumer.
   */
  def forNumberList(property: String, consumer: Consumer[List[Number]]): ConfigListener = {
    forConfigEntry(property, consumer, (config: Config, name: String) => config.getNumberList(name))
  }

  /**
   * Create a listener instance that will invoke the consumer when an update causes a
   * change for the specified property.
   *
   * @param property
   *   Property to get from the config.
   * @param consumer
   *   Handler that will get invoked if the update resulted in a change for the property.
   * @return
   *   Listener instance that forwards changes for the property to the consumer.
   */
  def forDuration(property: String, consumer: Consumer[Duration]): ConfigListener = {
    forConfigEntry(property, consumer, (config: Config, name: String) => config.getDuration(name))
  }

  /**
   * Create a listener instance that will invoke the consumer when an update causes a
   * change for the specified property.
   *
   * @param property
   *   Property to get from the config.
   * @param consumer
   *   Handler that will get invoked if the update resulted in a change for the property.
   * @return
   *   Listener instance that forwards changes for the property to the consumer.
   */
  def forDurationList(property: String, consumer: Consumer[List[Duration]]): ConfigListener = {
    forConfigEntry(property, consumer, (config: Config, name: String) => config.getDurationList(name))
  }

  /**
   * Create a listener instance that will invoke the consumer when an update causes a
   * change for the specified property.
   *
   * @param property
   *   Property to get from the config.
   * @param consumer
   *   Handler that will get invoked if the update resulted in a change for the property.
   * @return
   *   Listener instance that forwards changes for the property to the consumer.
   */
  def forPeriod(property: String, consumer: Consumer[Period]): ConfigListener = {
    forConfigEntry(property, consumer, (config: Config, name: String) => config.getPeriod(name))
  }

  /**
   * Create a listener instance that will invoke the consumer when an update causes a
   * change for the specified property.
   *
   * @param property
   *   Property to get from the config.
   * @param consumer
   *   Handler that will get invoked if the update resulted in a change for the property.
   * @return
   *   Listener instance that forwards changes for the property to the consumer.
   */
  def forTemporal(property: String, consumer: Consumer[TemporalAmount]): ConfigListener = {
    forConfigEntry(property, consumer, (config: Config, name: String) => config.getTemporal(name))
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
    accessor: (Config, String) => T
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
