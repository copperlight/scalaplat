package io.github.copperlight.scalaplat.akka

import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.server.RequestContext
import akka.http.scaladsl.server.RouteResult

import scala.concurrent.Promise

/**
 * Helper for porting some of the actor per request use-cases from spray over
 * to akka-http. Based on:
 *
 * https://markatta.com/codemonkey/blog/2016/08/03/actor-per-request-with-akka-http/
 */
case class ImperativeRequestContext(value: AnyRef, ctx: RequestContext) {

  val promise: Promise[RouteResult] = Promise()

  private implicit val ec = ctx.executionContext

  def complete(res: HttpResponse): Unit = {
    ctx.complete(res).onComplete(promise.complete)
  }

  def fail(t: Throwable): Unit = {
    ctx.fail(t).onComplete(promise.complete)
  }
}
