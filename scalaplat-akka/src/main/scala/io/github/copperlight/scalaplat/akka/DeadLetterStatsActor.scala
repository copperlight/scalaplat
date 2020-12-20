package io.github.copperlight.scalaplat.akka

import akka.actor.Actor
import akka.actor.AllDeadLetters
import com.netflix.spectator.api.Registry
import com.typesafe.config.Config
import com.typesafe.scalalogging.StrictLogging

/**
 * Update counter for dead letters in the actor system. The counter name is `akka.deadLetters`
 * and has dimensions for:
 *
 *  - `class`: the type of dead letter. Value should be either DeadLetter or SuppressedDeadLetter.
 *  - `sender`: summary of path for sender. See Paths for more details.
 *  - `recipient`: summary of path for recipient. See Paths for more details.
 *
 * To use subscribe to the dead letters on the event stream:
 *
 * http://doc.akka.io/docs/akka/2.4.0/scala/event-bus.html#Dead_Letters
 *
 * @param registry
 *     Spectator registry to use for metrics.
 * @param config
 *     Config to use for creating the path mapper using the `atlas.akka.path-pattern`
 *     This pattern maps an actor path to a tag value for the metric. This should be
 *     chosen to avoid parts of the path such as incrementing counters in the path of
 *     short lived actors.
 */
class DeadLetterStatsActor(registry: Registry, config: Config) extends Actor with StrictLogging {

  context.system.eventStream.subscribe(self, classOf[AllDeadLetters])

  private val pathMapper = Paths.createMapper(config.getConfig("atlas.akka"))
  private val deadLetterId = registry.createId("akka.deadLetters")

  def receive: Receive = {
    case letter: AllDeadLetters =>
      // format: off
      val id = deadLetterId.withTags(
        "class",     letter.getClass.getSimpleName,
        "sender",    pathMapper(letter.sender.path),
        "recipient", pathMapper(letter.recipient.path)
      )
      registry.counter(id).increment()
    // format: on
  }
}
