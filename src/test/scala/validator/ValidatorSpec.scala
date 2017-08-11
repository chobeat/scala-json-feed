package validator

import org.json4s.JValue
import org.json4s.JsonAST.JObject
import org.scalatest._

import scala.util.Success

class ValidatorSpec extends FlatSpec with Matchers {
  "The Validator object" should "return a Success(JValue)" in {
    val input = "{}"
  Validator.validate(input) shouldEqual Success(JObject(List()))
  }
}
