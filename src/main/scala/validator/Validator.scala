package validator
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._
import scala.util.{Success, Try}

/**
  * Created by simone on 11.08.17.
  */

case class JSONFeedDocument(content:Option[String])
object Validator {


  def validate(s:String): Either[Error, JSONFeedDocument] ={
    decode[JSONFeedDocument](s)

  }
}
