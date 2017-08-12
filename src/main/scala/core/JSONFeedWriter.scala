package core

import io.circe.syntax._
import Models._
import io.circe.generic.auto._

import io.circe.Printer

object JSONFeedWriter {
  val printer = Printer(preserveOrder = true, dropNullKeys = true, indent = " ")

  def writeJSONFeedDocument(document:JSONFeedDocument)={
    printer.pretty(document.asJson)
  }

}
