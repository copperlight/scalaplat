package io.github.copperlight.scalaplat.akka

import akka.http.scaladsl.model.IllegalUriException
import akka.http.scaladsl.model.Uri
import org.scalatest.funsuite.AnyFunSuite

class UriParsingSuite extends AnyFunSuite {

  private def query(mode: Uri.ParsingMode): String = {
    Uri("/foo?regex=a|b|c", mode).query().get("regex").get
  }

  test("relaxed: regex with |") {
    assert(query(Uri.ParsingMode.Relaxed) === "a|b|c")
  }

  test("strict: regex with |") {
    intercept[IllegalUriException] {
      assert(query(Uri.ParsingMode.Strict) === "a|b|c")
    }
  }
}
