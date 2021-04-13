package io.github.copperlight.scalaplat.config

import com.typesafe.config.Config

object ListenerUtils {

  def hasChanged[T](previous: Option[T], current: Option[T]): Boolean = {
    (previous.isDefined && previous != current) || (previous.isEmpty && current.isDefined)
  }

  def getOrNone[T](
    config: Config,
    path: String,
    accessor: (Config, String) => T
  ): Option[T] = {
    Option(config).flatMap { c =>
      if (c.hasPath(path)) Some(accessor(config, path)) else None
    }
  }

  def getConfig(config: Config, path: String): Option[Config] = {
    val accessor = (config: Config, path: String) => config.getConfig(path)
    getOrNone(config, path, accessor)
  }
}
