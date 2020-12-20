package io.github.copperlight.scalaplat.akka

import java.util.regex.Pattern

import akka.actor.ActorPath
import com.typesafe.config.Config

/** Helper for creating mapping functions to extract a tag value based on an actor path. */
object Paths {

  type Mapper = ActorPath => String

  /** Create a mapping function from a config object using the `path-pattern` field. */
  def createMapper(config: Config): Mapper = createMapper(config.getString("path-pattern"))

  /** Create a mapper from a regex string. */
  def createMapper(pattern: String): Mapper = createMapper(Pattern.compile(pattern))

  /**
   * Creates a mapping function that extracts a tag value based on the
   * pattern. The pattern should have a single capturing group that contains
   * the string to use for the tag value.
   */
  def createMapper(pattern: Pattern): Mapper = { path =>
  {
    val matcher = pattern.matcher(path.toString)
    if (matcher.matches() && matcher.groupCount() == 1) matcher.group(1) else "uncategorized"
  }
  }
}
