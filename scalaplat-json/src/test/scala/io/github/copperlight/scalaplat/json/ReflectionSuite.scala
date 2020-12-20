package io.github.copperlight.scalaplat.json

import org.scalatest.funsuite.AnyFunSuite

import scala.util.Try

class ReflectionSuite extends AnyFunSuite {
  import ReflectionSuite._

  test("create instance") {
    val desc = Reflection.createDescription(classOf[Simple])
    val args = desc.newInstanceArgs
    assert(desc.newInstance(args) === Simple(27, null))

    desc.setField(args, "foo", 42)
    assert(desc.newInstance(args) === Simple(42, null))

    desc.setField(args, "bar", "abc")
    assert(desc.newInstance(args) === Simple(42, "abc"))
  }

  test("create instance, additional field") {
    val desc = Reflection.createDescription(classOf[Simple])
    val args = desc.newInstanceArgs
    desc.setField(args, "notPresent", 42)
    assert(desc.newInstance(args) === Simple(27, null))
  }

  test("create instance, invalid type") {
    val desc = Reflection.createDescription(classOf[Simple])
    val args = desc.newInstanceArgs
    desc.setField(args, "bar", 42)
    intercept[IllegalArgumentException] { desc.newInstance(args) }
  }

  test("isCaseClass") {
    assert(Reflection.isCaseClass(classOf[Simple]) === true)
    assert(Reflection.isCaseClass(classOf[Bar]) === false)
  }

  test("isCaseClass Option") {
    assert(Reflection.isCaseClass(classOf[Option[_]]) === false)
  }

  test("isCaseClass Either") {
    assert(Reflection.isCaseClass(classOf[Either[_, _]]) === false)
  }

  test("isCaseClass Try") {
    assert(Reflection.isCaseClass(classOf[Try[_]]) === false)
  }
}

object ReflectionSuite {

  case class SimpleInner(foo: Int, bar: String = "abc")

  class Bar(foo: Int)

}

case class Simple(foo: Int = 27, bar: String)
