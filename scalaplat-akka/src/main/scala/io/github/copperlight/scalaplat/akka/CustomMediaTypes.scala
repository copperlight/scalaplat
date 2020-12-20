package io.github.copperlight.scalaplat.akka

import akka.http.scaladsl.model.MediaType
import akka.http.scaladsl.model.MediaType.Compressible

object CustomMediaTypes {

  val `application/x-jackson-smile`: MediaType.Binary =
    MediaType.applicationBinary("x-jackson-smile", Compressible)
}
