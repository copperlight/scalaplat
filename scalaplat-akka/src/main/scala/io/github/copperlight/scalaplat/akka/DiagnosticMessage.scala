package io.github.copperlight.scalaplat.akka

import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.MediaTypes
import akka.http.scaladsl.model.StatusCode
import com.netflix.atlas.json.JsonSupport

object DiagnosticMessage {
  final val Info: String = "info"
  final val Warning: String = "warn"
  final val Error: String = "error"
  final val Close: String = "close"

  def info(message: String): DiagnosticMessage = {
    DiagnosticMessage(Info, message, None)
  }

  def warning(message: String): DiagnosticMessage = {
    DiagnosticMessage(Warning, message, None)
  }

  def error(message: String): DiagnosticMessage = {
    DiagnosticMessage(Error, message, None)
  }

  def error(t: Throwable): DiagnosticMessage = {
    error(s"${t.getClass.getSimpleName}: ${t.getMessage}")
  }

  val close: DiagnosticMessage = {
    DiagnosticMessage(Close, "operation complete", None)
  }

  def error(status: StatusCode, t: Throwable): HttpResponse = {
    error(status, s"${t.getClass.getSimpleName}: ${t.getMessage}")
  }

  def error(status: StatusCode, msg: String): HttpResponse = {
    val errorMsg = DiagnosticMessage.error(msg)
    val entity = HttpEntity(MediaTypes.`application/json`, errorMsg.toJson)
    HttpResponse(status = status, entity = entity)
  }
}

case class DiagnosticMessage(`type`: String, message: String, percent: Option[Double])
  extends JsonSupport {

  def typeName: String = `type`
}
