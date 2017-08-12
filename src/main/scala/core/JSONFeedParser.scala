package core
import java.net.URL

import cats.data.ValidatedNel
import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import cats.syntax.either._
import Models._


object JSONFeedParser {
  implicit val decodeURL: Decoder[URL] = Decoder.decodeString.emap { str =>
    {
      Either.catchNonFatal(new URL(str)).leftMap(t => s"$t is not a valid url")
    }

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
