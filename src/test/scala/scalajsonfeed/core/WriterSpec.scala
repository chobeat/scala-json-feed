package scalajsonfeed.core

import cats.data.Validated
import org.scalatest.{FlatSpec, Matchers}
import Models._

class WriterSpec extends FlatSpec with Matchers {

  "The writeJSONFeedDocument function" should "return a string in JSON format" in {
    val input = JSONFeedDocument(title = "a title")
    JSONFeedWriter
      .writeJSONFeedDocument(input)
      .replaceAll("\\s", "") shouldEqual """{"title":"a title", "items":[]}"""
      .replaceAll("\\s", "")
  }
}
