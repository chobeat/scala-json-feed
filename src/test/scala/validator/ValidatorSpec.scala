package validator

import cats.data.{NonEmptyList, Validated}
import cats.data.Validated.Invalid
import io.circe.CursorOp.DownField
import io.circe.DecodingFailure
import org.scalatest._



class ValidatorSpec extends FlatSpec with Matchers {
  def parsingError(fields: String*)=Validated.invalidNel(
    DecodingFailure("Attempt to decode value on failed cursor", fields.map(DownField).toList))

  "The validate function" should "return an empty JSONFeedDocument" in {
    val input = "{}"
    Validator.validate(input) shouldEqual Right(JSONFeedDocument(None))
  }

  "The validateItem function" should "return a JSONFeedItem" in {
    val input = """{"id":"abc","tags":["a","b","c"]}"""

    Validator.validateItem(input) shouldEqual Right(
      JSONFeedItem("abc", tags = Some(List("a", "b", "c"))))

  }

  it should "return an error if the id is missing" in {
    val input = """{"tags":["a","b","c"]}"""

    Validator.validateItem(input) shouldEqual parsingError("id")

  }
}
