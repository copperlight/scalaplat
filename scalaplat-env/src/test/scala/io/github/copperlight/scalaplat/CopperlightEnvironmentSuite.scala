package io.github.copperlight.scalaplat

import com.typesafe.scalalogging.StrictLogging
import munit.FunSuite

import scala.reflect.runtime.universe.MethodSymbol

class CopperlightEnvironmentSuite extends FunSuite with StrictLogging {

  test("checkMethods") {
    val cl = Thread.currentThread.getContextClassLoader
    Thread.currentThread.setContextClassLoader(null)

    try {
      val mirror = scala.reflect.runtime.currentMirror

      val accessors = mirror
        .classSymbol(CopperlightEnvironment.getClass)
        .toType
        .members
        .sorted
        .collect {
          case m: MethodSymbol if m.isGetter && m.isPublic => m
        }

      val instance = mirror.reflect(CopperlightEnvironment)

      logger.info(s"found ${accessors.length} public getters")
      assert(accessors.nonEmpty)

      accessors.foreach { a =>
        val value = instance.reflectMethod(a).apply().toString
        logger.info(s"$a: $value")
        assert(value.nonEmpty)
      }
    } finally {
      Thread.currentThread.setContextClassLoader(cl)
    }
  }
}
