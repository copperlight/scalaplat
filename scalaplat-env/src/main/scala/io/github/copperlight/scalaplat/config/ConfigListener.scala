package io.github.copperlight.scalaplat.config

import com.typesafe.config.{Config, ConfigMemorySize}

import java.time.temporal.TemporalAmount
import java.time.{Duration, Period}
import java.util.function.Consumer
import scala.jdk.CollectionConverters._

/**
  * Listener that will get invoked when the config is updated.
  */
trait ConfigListener {

  type JBoolean = java.lang.Boolean
  type JDouble = java.lang.Double
  type JInteger = java.lang.Integer
  type JList[T] = java.util.List[T]
  type JLong = java.lang.Long

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
        val c1: Option[Config] = if (path == null) {
          Some(previous)
        } else {
          ListenerUtils.getConfig(previous, path)
        }

        val c2: Option[Config] = if (path == null) {
          Some(current)
        } else {
          ListenerUtils.getConfig(current, path)
        }

        if (ListenerUtils.hasChanged(c1, c2)) {
          c2.map(consumer.accept)
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
    val accessor = (config: Config, name: String) => config.getConfig(name)
    forConfigEntry(property, consumer, accessor)
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
  def forConfigList(property: String, consumer: Consumer[List[Config]]): ConfigListener = {
    val accessor = (config: Config, name: String) => config.getConfigList(name).asScala.toList
    forConfigEntry(property, consumer, accessor)
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
    val accessor = (config: Config, name: String) => config.getString(name)
    forConfigEntry(property, consumer, accessor)
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
  def forStringList(property: String, consumer: Consumer[JList[String]]): ConfigListener = {
    val accessor = (config: Config, name: String) => config.getStringList(name)
    forConfigEntry(property, consumer, accessor)
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
    val accessor = (config: Config, name: String) => config.getBoolean(name)
    forConfigEntry(property, consumer, accessor)
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
  def forBooleanList(property: String, consumer: Consumer[JList[JBoolean]]): ConfigListener = {
    val accessor = (config: Config, name: String) => config.getBooleanList(name)
    forConfigEntry(property, consumer, accessor)
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
  def forInt(property: String, consumer: Consumer[Int]): ConfigListener = {
    val accessor = (config: Config, name: String) => config.getInt(name)
    forConfigEntry(property, consumer, accessor)
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
  def forIntList(property: String, consumer: Consumer[JList[JInteger]]): ConfigListener = {
    val accessor = (config: Config, name: String) => config.getIntList(name)
    forConfigEntry(property, consumer, accessor)
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
    val accessor = (config: Config, name: String) => config.getLong(name)
    forConfigEntry(property, consumer, accessor)
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
  def forLongList(property: String, consumer: Consumer[JList[JLong]]): ConfigListener = {
    val accessor = (config: Config, name: String) => config.getLongList(name)
    forConfigEntry(property, consumer, accessor)
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
  def forBytes(property: String, consumer: Consumer[JLong]): ConfigListener = {
    val accessor = (config: Config, name: String) => config.getBytes(name)
    forConfigEntry(property, consumer, accessor)
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
  def forBytesList(property: String, consumer: Consumer[JList[JLong]]): ConfigListener = {
    val accessor = (config: Config, name: String) => config.getBytesList(name)
    forConfigEntry(property, consumer, accessor)
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
    val accessor = (config: Config, name: String) => config.getMemorySize(name)
    forConfigEntry(property, consumer, accessor)
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
  def forMemorySizeList(property: String, consumer: Consumer[JList[ConfigMemorySize]]): ConfigListener = {
    val accessor = (config: Config, name: String) => config.getMemorySizeList(name)
    forConfigEntry(property, consumer, accessor)
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
    val accessor = (config: Config, name: String) => config.getDouble(name)
    forConfigEntry(property, consumer, accessor)
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
  def forDoubleList(property: String, consumer: Consumer[JList[JDouble]]): ConfigListener = {
    val accessor = (config: Config, name: String) => config.getDoubleList(name)
    forConfigEntry(property, consumer, accessor)
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
    val accessor = (config: Config, name: String) => config.getNumber(name)
    forConfigEntry(property, consumer, accessor)
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
  def forNumberList(property: String, consumer: Consumer[JList[Number]]): ConfigListener = {
    val accessor = (config: Config, name: String) => config.getNumberList(name)
    forConfigEntry(property, consumer, accessor)
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
    val accessor = (config: Config, name: String) => config.getDuration(name)
    forConfigEntry(property, consumer, accessor)
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
  def forDurationList(property: String, consumer: Consumer[JList[Duration]]): ConfigListener = {
    val accessor = (config: Config, name: String) => config.getDurationList(name)
    forConfigEntry(property, consumer, accessor)
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
    val accessor = (config: Config, name: String) => config.getPeriod(name)
    forConfigEntry(property, consumer, accessor)
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
    val accessor = (config: Config, name: String) => config.getTemporal(name)
    forConfigEntry(property, consumer, accessor)
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
      val v1 = ListenerUtils.getOrNone(previous, property, accessor)
      val v2 = ListenerUtils.getOrNone(current, property, accessor)
      if (ListenerUtils.hasChanged(v1, v2)) v2.map(consumer.accept)
    }
  }

  /**
    * Invoked when the config is updated by a call to [[DynamicConfigManager.setOverrideConfig]].
    * This method will be invoked from the thread setting the override. It should be cheap to
    * allow changes to quickly propagate to all listeners. If an exception is thrown, then a
    * warning will be logged and the manager will move on.
    *
    * @param previous
    *   Previous config instance.
    * @param current
    *   Current config instance with the update override applied over the base layer.
    */
  def onUpdate(previous: Config, current: Config): Unit
}
