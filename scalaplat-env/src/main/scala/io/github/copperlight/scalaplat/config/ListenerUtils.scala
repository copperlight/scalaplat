package io.github.copperlight.scalaplat.config

import com.typesafe.config.Config

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
      accessor(config, path)
    } else {
      null
    }
  }

  def getConfig(config: Config, path: String): Config = {
    getOrNull(config, path, (config: Config, path: String) => config.getConfig(path))
  }
}
