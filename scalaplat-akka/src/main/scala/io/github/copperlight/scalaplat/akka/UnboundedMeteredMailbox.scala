package io.github.copperlight.scalaplat.akka

import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.TimeUnit

import akka.actor.ActorPath
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.dispatch.Envelope
import akka.dispatch.MailboxType
import akka.dispatch.MessageQueue
import akka.dispatch.ProducesMessageQueue
import akka.dispatch.UnboundedMessageQueueSemantics
import com.netflix.spectator.api.Spectator
import com.netflix.spectator.api.patterns.PolledMeter
import com.typesafe.config.Config

object UnboundedMeteredMailbox {

  private case class Entry(v: Envelope, t: Long = System.nanoTime)

  class MeteredMessageQueue(path: String) extends MessageQueue with UnboundedMessageQueueSemantics {

    private final val queue = new ConcurrentLinkedQueue[Entry]

    private val registry = Spectator.globalRegistry()
    private val insertCounter = registry.counter("akka.queue.insert", "path", path)
    private val waitTimer = registry.timer("akka.queue.wait", "path", path)
    PolledMeter
      .using(registry)
      .withName("akka.queue.size")
      .withTag("path", path)
      .monitorSize(queue)

    def enqueue(receiver: ActorRef, handle: Envelope): Unit = {
      insertCounter.increment()
      queue.offer(Entry(handle))
    }

    def dequeue(): Envelope = {
      val tmp = queue.poll()
      if (tmp == null) null
      else {
        val dur = System.nanoTime - tmp.t
        waitTimer.record(dur, TimeUnit.NANOSECONDS)
        tmp.v
      }
    }

    def numberOfMessages: Int = queue.size

    def hasMessages: Boolean = !queue.isEmpty

    def cleanUp(owner: ActorRef, deadLetters: MessageQueue): Unit = {
      queue.clear()
    }
  }
}

class UnboundedMeteredMailbox(settings: ActorSystem.Settings, config: Config)
  extends MailboxType
    with ProducesMessageQueue[UnboundedMeteredMailbox.MeteredMessageQueue] {

  import com.netflix.atlas.akka.UnboundedMeteredMailbox._

  private val Path = config.getString("path-pattern").r

  /** Summarizes a path for use in a metric tag. */
  def tagValue(path: ActorPath): String = {
    path.toString match {
      case Path(v) => v
      case _       => "uncategorized"
    }
  }

  final override def create(owner: Option[ActorRef], system: Option[ActorSystem]): MessageQueue = {
    val path = owner.fold("unknown")(r => tagValue(r.path))
    new MeteredMessageQueue(path)
  }
}