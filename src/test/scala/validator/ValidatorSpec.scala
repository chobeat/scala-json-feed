package validator

import org.scalatest._

import scala.util.Success

class ValidatorSpec extends FlatSpec with Matchers {
  "The Validator object" should "return a Success(JValue)" in {
    val input = "{}"
  Validator.validate(input) shouldEqual Right(JSONFeedDocument(None))
  }
}
