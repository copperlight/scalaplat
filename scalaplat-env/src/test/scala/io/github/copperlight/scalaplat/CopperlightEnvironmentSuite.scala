package io.github.copperlight.scalaplat

import org.scalatest.funsuite.AnyFunSuite

import java.lang.reflect.Modifier

class CopperlightEnvironmentSuite extends AnyFunSuite {

  test("checkMethods") {
    val cl = Thread.currentThread.getContextClassLoader
    Thread.currentThread.setContextClassLoader(null)

    try {
      for (field <- CopperlightEnvironment.getClass.getDeclaredFields) {
        if (Modifier.isPublic(field.getModifiers)) {
          try {
            field.setAccessible(true)
            field.get(CopperlightEnvironment)
          } catch {
            case e: Exception =>
              throw new RuntimeException(s"failed to invoke ${field.getName}", e)
          }
        }
      }
    } finally {
      Thread.currentThread.setContextClassLoader(cl)
    }
  }
}
