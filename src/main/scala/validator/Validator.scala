package validator
import org.json4s._
import org.json4s.jackson.JsonMethods._

import scala.util.{Success, Try}

/**
  * Created by simone on 11.08.17.
  */
object Validator {

  def validate(s:String):Try[JValue]={
    val result = parse(s)

    Success(result)
  }
}
