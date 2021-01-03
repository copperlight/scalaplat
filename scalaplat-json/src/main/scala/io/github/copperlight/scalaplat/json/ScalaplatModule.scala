package io.github.copperlight.scalaplat.json

import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.databind.Module.SetupContext

/**
 * Adds custom serializers and deserializers for our use cases.
 */
private[json] class ScalaplatModule extends Module {

  override def getModuleName: String = "scalaplat"

  override def setupModule(context: SetupContext): Unit = {
    context.addDeserializers(new CaseClassDeserializers)
  }

  override def version(): Version = Version.unknownVersion()
}
