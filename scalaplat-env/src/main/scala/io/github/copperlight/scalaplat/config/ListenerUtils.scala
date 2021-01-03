package io.github.copperlight.scalaplat.config

import com.typesafe.config.Config

import java.util.function.BiFunction

class ListenerUtils {

  def hasChanged(previous: Any, current: Any): Boolean = {
    (previous != null && !(previous == current)) || (previous == null && current != null)
  }

  def getOrNull[T >: Null](
    config: Config,
    path: String,
    accessor: BiFunction[Config, String, T]
  ): T = {
    if (config != null && config.hasPath(path)) {
      accessor.apply(config, path)
    } else {
      null
    }
  }

  def getConfig(config: Config, path: String): Config = {
    getOrNull(config, path, Config.getConfig _)
  }
}
