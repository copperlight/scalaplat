package io.github.copperlight.scalaplat.json

import com.fasterxml.jackson.databind.BeanDescription
import com.fasterxml.jackson.databind.DeserializationConfig
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.deser.Deserializers

/**
  * Identifies classes that are eligible for using the custom case class
  * deserializer.
  */
class CaseClassDeserializers extends Deserializers.Base {

  override def findBeanDeserializer(
    javaType: JavaType,
    config: DeserializationConfig,
    beanDesc: BeanDescription
  ): CaseClassDeserializer = {

    if (Reflection.isCaseClass(javaType.getRawClass))
      new CaseClassDeserializer(javaType, config, beanDesc)
    else
      null
  }
}
