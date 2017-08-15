package core

import java.net.{MalformedURLException, URL}
import Models._
import cats.data.Validated
import io.circe.CursorOp.DownField
import io.circe.DecodingFailure
import org.scalatest._

class ParserSpec extends FlatSpec with Matchers {
  def parsingError(fields: String*) =
    error("Attempt to decode value on failed cursor", fields: _*)

  def error(msg: String, fields: String*) =
    Validated.invalidNel(DecodingFailure(msg, fields.map(DownField).toList))

  "The validate function" should "return a valid JSONFeedDocument" in {
    val input =
      """
        {"title": "a title", "items":[]}
      """
    JSONFeedParser.parse(input) shouldEqual Validated.valid(
      JSONFeedDocument(title = "a title"))
  }

  it should "return a valid list of hubs" in {
    val input =
      """
        {"title": "a title",
        "hubs": [
        {"type":"a type", "url":"http://test.com"},
        {"type":"another type", "url":"http://test.com"}
        ],
        "items":[]
        }
      """
    JSONFeedParser.parse(input) shouldEqual Validated.valid(
      JSONFeedDocument(
        title = "a title",
        hubs = Some(
          List(JSONFeedHub("a type", new URL("http://test.com")),
               JSONFeedHub("another type", new URL("http://test.com")))))
    )

  }

  "The validateItem function" should "return a JSONFeedItem" in {
    val input =
      """{"id":"abc","tags":["a","b","c"], "content_text":"content", "items":[]}"""

    JSONFeedParser.parseItem(input) shouldEqual Validated.valid(
      JSONFeedItem("abc",
                   tags = Some(List("a", "b", "c")),
                   content_text = Some("content")))

  }

  it should "return an error if the id is missing" in {
    val input = """{"tags":["a","b","c"], "content_text":"content"}"""

    JSONFeedParser.parseItem(input) shouldEqual parsingError("id")

  }
  it should "return an error if the image is not a valid url" in {
    val input = """{"id":"abc","tags":["a","b","c"],"image":"not-an-url" }"""

    JSONFeedParser.parseItem(input) shouldEqual Validated.invalidNel(
      DecodingFailure(new MalformedURLException(
                        "no protocol: not-an-url is not a valid url").toString,
                      List(DownField("image"))))

  }

  it should "return a URL object if the image is a valid url" in {
    val input =
      s"""{"id":"abc","tags":["a","b","c"],"image": "http://test.com/image.png", "content_text":"content"}"""

    JSONFeedParser.parseItem(input).map(_.image) shouldEqual Validated
      .valid(Some(new URL("http://test.com/image.png")))

  }

  it should "return an error if both content_text and content_html are not set" in {
    val input =
      s"""{"id":"abc","tags":["a","b","c"]}"""
    JSONFeedParser.parseItem(input) shouldEqual Validated.invalidNel(
      DecodingFailure("Both `content_text` and `content_html` are missing",List()))

  }

  "The isValidFeedDocument" should "return true with a valid document" in {
    val input =
      """{
                  |    "version": "https://jsonfeed.org/version/1",
                  |    "user_comment": "This is a podcast feed. You can add this feed to your podcast client using the following URL: http://therecord.co/feed.json",
                  |    "title": "The Record",
                  |    "home_page_url": "http://therecord.co/",
                  |    "feed_url": "http://therecord.co/feed.json",
                  |    "items": [
                  |        {
                  |            "id": "http://therecord.co/chris-parrish",
                  |            "title": "Special #1 - Chris Parrish",
                  |            "url": "http://therecord.co/chris-parrish",
                  |            "content_text": "Chris has worked at Adobe and as a founder of Rogue Sheep, which won an Apple Design Award for Postage. Chris’s new company is Aged & Distilled with Guy English — which shipped Napkin, a Mac app for visual collaboration. Chris is also the co-host of The Record. He lives on Bainbridge Island, a quick ferry ride from Seattle.",
                  |            "content_html": "Chris has worked at <a href=\"http://adobe.com/\">Adobe</a> and as a founder of Rogue Sheep, which won an Apple Design Award for Postage. Chris’s new company is Aged & Distilled with Guy English — which shipped <a href=\"http://aged-and-distilled.com/napkin/\">Napkin</a>, a Mac app for visual collaboration. Chris is also the co-host of The Record. He lives on <a href=\"http://www.ci.bainbridge-isl.wa.us/\">Bainbridge Island</a>, a quick ferry ride from Seattle.",
                  |            "summary": "Brent interviews Chris Parrish, co-host of The Record and one-half of Aged & Distilled.",
                  |            "date_published": "2014-05-09T14:04:00-07:00",
                  |            "attachments": [
                  |                {
                  |                    "url": "http://therecord.co/downloads/The-Record-sp1e1-ChrisParrish.m4a",
                  |                    "mime_type": "audio/x-m4a",
                  |                    "size_in_bytes": 89970236,
                  |                    "duration_in_seconds": 6629
                  |                }
                  |            ]
                  |        }
                  |    ]
                  |}""".stripMargin

    JSONFeedParser.isValidFeedDocument(input) should be(true)

  }

  it should "return false if a document is not a valid JSON" in {
    val input = """{
                  |   --- "title": "The Record" +++<;
                  |}""".stripMargin

    JSONFeedParser.isValidFeedDocument(input) should be(false)
  }

  it should "return false if a document is not a valid JSONFeed" in {
    val input = """{
                  |  "a_field": "some_content"
                  |}""".stripMargin

    JSONFeedParser.isValidFeedDocument(input) should be(false)
  }
}
