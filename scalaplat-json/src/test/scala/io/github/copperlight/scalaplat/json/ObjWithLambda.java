package io.github.copperlight.scalaplat.json;

import java.util.function.Function;

/**
 * Simple test object to verify lambda issue with paranamer is fixed:
 *
 * https://github.com/FasterXML/jackson-module-paranamer/issues/13
 * https://github.com/paul-hammant/paranamer/issues/17
 *
 * <pre>
 * java.lang.ArrayIndexOutOfBoundsException: 55596
 *   at com.fasterxml.jackson.module.paranamer.shaded.BytecodeReadingParanamer$ClassReader.readUnsignedShort(BytecodeReadingParanamer.java:722)
 *   at com.fasterxml.jackson.module.paranamer.shaded.BytecodeReadingParanamer$ClassReader.accept(BytecodeReadingParanamer.java:571)
 *   at com.fasterxml.jackson.module.paranamer.shaded.BytecodeReadingParanamer$ClassReader.access$200(BytecodeReadingParanamer.java:338)
 *   at com.fasterxml.jackson.module.paranamer.shaded.BytecodeReadingParanamer.lookupParameterNames(BytecodeReadingParanamer.java:103)
 *   at com.fasterxml.jackson.module.paranamer.shaded.CachingParanamer.lookupParameterNames(CachingParanamer.java:79)
 * </pre>
 */
public class ObjWithLambda {

  private String foo;

  public void setFoo(String value) {
    final Function<String, String> check = v -> {
      if (v == null) throw new NullPointerException("value cannot be null");
      return v;
    };
    foo = check.apply(value);
  }

  public String getFoo() {
    return foo;
  }
}
