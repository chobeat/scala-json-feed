package scalajsonfeed.core
import java.net.URL

import cats.data.ValidatedNel
import io.circe.generic.auto._
import cats.syntax.either._
import Models._
import io.circe._, parser._, io.circe.generic.semiauto._

object JSONFeedParser {
  implicit val decodeURL: Decoder[URL] = Decoder.decodeString.emap { str =>
    {
      Either.catchNonFatal(new URL(str)).leftMap(t => s"$t is not a valid url")
    }

  }

  implicit val decoderItem =
    deriveDecoder[JSONFeedItem].emap { item =>
      if (item.content_html.isEmpty && item.content_text.isEmpty)
        Left("Both `content_text` and `content_html` are missing")
      else Right(item)
    }

  implicit val decodeHub: Decoder[JSONFeedHub] =
    Decoder.forProduct2("type", "url")(JSONFeedHub.apply)



  def parse(s: String): ValidatedNel[Error, JSONFeedDocument] = {
    decodeAccumulating[JSONFeedDocument](s)

  }

  def parseItem(s: String): ValidatedNel[Error, JSONFeedItem] = {
    decodeAccumulating[JSONFeedItem](s)

  }

  def isValidFeedDocument(s: String): Boolean =
    parse(s).isValid

}
