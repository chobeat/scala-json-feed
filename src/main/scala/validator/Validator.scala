package validator
import cats.data.ValidatedNel
import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._

import scala.util.{Success, Try}

/**
  * Created by simone on 11.08.17.
  */

case class JSONFeedDocument(content:Option[String])

case class JSONFeedAuthor(name:Option[String],
                          url:Option[String],
                          avatar:Option[String])

case class JSONFeedItem(id:String,
                        url:Option[String]=None,
                        external_url:Option[String]=None,
                        title:Option[String]=None,
                        content_html:Option[String]=None,
                        content_text:Option[String]=None,
                        image: Option[String]=None,
                        banner_image:Option[String]=None,
                        date_published:Option[String]=None,
                        date_modified: Option[String]=None,
                        author:Option[JSONFeedAuthor]=None,
                        tags:Option[List[String]]=None
                       )

object Validator {


  def validate(s:String): ValidatedNel[Error, JSONFeedDocument] ={
    decodeAccumulating[JSONFeedDocument](s)

  }

  def validateItem(s:String): ValidatedNel[Error, JSONFeedItem] ={
    decodeAccumulating[JSONFeedItem](s)

  }

}
