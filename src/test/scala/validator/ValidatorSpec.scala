package validator

import java.net.{MalformedURLException, URL}

import cats.data.{NonEmptyList, Validated}
import cats.data.Validated.Invalid
import io.circe.CursorOp.DownField
import io.circe.DecodingFailure
import org.scalatest._

class ValidatorSpec extends FlatSpec with Matchers {
  def parsingError(fields: String*) =
    error("Attempt to decode value on failed cursor", fields: _*)

  def error(msg: String, fields: String*) =
    Validated.invalidNel(DecodingFailure(msg, fields.map(DownField).toList))

  "The validate function" should "return a valid JSONFeedDocument" in {
    val input =
      """
        {"title": "a title"}
      """
    Validator.validate(input) shouldEqual Validated.valid(
      JSONFeedDocument(title = "a title"))
  }

  it should "return a valid list of hubs" in {
    val input =
      """
        {"title": "a title",
        "hubs": [
        {"type":"a type", "url":"http://test.com"},
        {"type":"another type", "url":"http://test.com"}
        ]
        }
      """
    Validator.validate(input) shouldEqual Validated.valid(
      JSONFeedDocument(
        title = "a title",
        hubs = Some(
          List(JSONFeedHub("a type", new URL("http://test.com")),
               JSONFeedHub("another type", new URL("http://test.com")))))
    )

  }

  "The validateItem function" should "return a JSONFeedItem" in {
    val input = """{"id":"abc","tags":["a","b","c"]}"""

    Validator.validateItem(input) shouldEqual Validated.valid(
      JSONFeedItem("abc", tags = Some(List("a", "b", "c"))))

  }

  it should "return an error if the id is missing" in {
    val input = """{"tags":["a","b","c"]}"""

    Validator.validateItem(input) shouldEqual parsingError("id")

  }
  it should "return an error if the image is not a valid url" in {
    val input = """{"id":"abc","tags":["a","b","c"],"image":"not-an-url" }"""

    Validator.validateItem(input) shouldEqual Validated.invalidNel(
      DecodingFailure(new MalformedURLException(
                        "no protocol: not-an-url is not a valid url").toString,
                      List(DownField("image"))))

  }

  it should "return a URL object if the image is a valid url" in {
    val input =
      s"""{"id":"abc","tags":["a","b","c"],"image": "http://test.com/image.png"}"""

    Validator.validateItem(input).map(_.image) shouldEqual Validated.valid(
      Some(new URL("http://test.com/image.png")))

  }
}
