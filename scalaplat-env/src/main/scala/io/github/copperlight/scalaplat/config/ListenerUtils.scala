package io.github.copperlight.scalaplat.config

import com.typesafe.config.{Config, ConfigMemorySize}

import scala.jdk.CollectionConverters._

object ListenerUtils {

  def hasChanged(previous: Any, current: Any): Boolean = {
    (previous != null && previous != current) || (previous == null && current != null)
  }

  def getOrNull[T >: Null](
    config: Config,
    path: String,
    accessor: (Config, String) => T
  ): T = {
    if (config != null && config.hasPath(path)) {
      accessor.apply(config, path)
    } else {
      null
    }
  }

  val getBoolean: (Config, String) => java.lang.Boolean = {
    (config, path) => config.getBoolean(path)
  }

  val getBooleanList: (Config, String) => List[java.lang.Boolean] = {
    (config, path) => config.getBooleanList(path).asScala.toList
  }

  val getBytes: (Config, String) => java.lang.Long = {
    (config, path) => config.getBytes(path)
  }

  val getBytesList: (Config, String) => List[java.lang.Long] = {
    (config, path) => config.getBytesList(path).asScala.toList
  }

  val getMemorySize: (Config, String) => ConfigMemorySize = {
    (config, path) => config.getMemorySize(path)
  }

  val getMemorySizeList: (Config, String) => List[ConfigMemorySize] = {
    (config, path) => config.getMemorySizeList(path).asScala.toList
  }

  val getConfig: (Config, String) => Config = {
    (config, path) => config.getConfig(path)
  }

  val getConfigList: (Config, String) => List[Config] = {
    (config, path) => config.getConfigList(path).asScala.toList
  }

  val getInt: (Config, String) => Integer = {
    (config, path) => config.getInt(path)
  }

  val getIntList: (Config, String) => List[Integer] = {
    (config, path) => config.getIntList(path).asScala.toList
  }

  val getLong: (Config, String) => java.lang.Long = {
    (config, path) => config.getLong(path)
  }

  val getLongList: (Config, String) => List[java.lang.Long] = {
    (config, path) => config.getLongList(path).asScala.toList
  }

  val getString: (Config, String) => String = {
    (config, path) => config.getString(path)
  }

  val getStringList: (Config, String) => List[String] = {
    (config, path) => config.getStringList(path).asScala.toList
  }

  def getConfig(config: Config, path: String): Config = {
    getOrNull(config, path, getConfig)
  }
}
