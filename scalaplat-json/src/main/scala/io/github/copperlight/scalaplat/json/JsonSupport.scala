package io.github.copperlight.scalaplat.json

import java.io.StringWriter

import com.fasterxml.jackson.core.JsonGenerator

trait JsonSupport {

  def encode(gen: JsonGenerator): Unit = {
    Json.encode(gen, this)
  }

  def toJson: String = {
    val w = new StringWriter
    val gen = Json.newJsonGenerator(w)
    encode(gen)
    gen.close()
    w.toString
  }
}
