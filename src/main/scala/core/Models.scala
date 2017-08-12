package core

import java.net.URL

import io.circe.Encoder
import io.circe._, io.circe.generic.semiauto._
object Models {

  case class JSONFeedDocument(version: Option[URL] = None,
                              title: String,
                              home_page_url: Option[URL] = None,
                              feed_url: Option[URL] = None,
                              description: Option[String] = None,
                              user_comment: Option[String] = None,
                              next_url: Option[URL] = None,
                              icon: Option[URL] = None,
                              favicon: Option[URL] = None,
                              expired: Option[Boolean] = None,
                              hubs: Option[List[JSONFeedHub]] = None)

  case class JSONFeedHub(typ: String, url: URL)

  case class JSONFeedAuthor(name: Option[String],
                            url: Option[String],
                            avatar: Option[String])

  case class JSONFeedItem(id: String,
                          url: Option[URL] = None,
                          external_url: Option[URL] = None,
                          title: Option[String] = None,
                          content_html: Option[String] = None,
                          content_text: Option[String] = None,
                          image: Option[URL] = None,
                          banner_image: Option[URL] = None,
                          date_published: Option[String] = None,
                          date_modified: Option[String] = None,
                          author: Option[JSONFeedAuthor] = None,
                          tags: Option[List[String]] = None,
                          attachments: Option[List[Attachment]] = None)

  case class Attachment(
      url: URL,
      mime_type: String,
      title: Option[String],
      size_in_bytes: Option[Int],
      duration_in_seconds: Option[Int]
  )

  implicit val encodeUrl: Encoder[URL] =
    Encoder.forProduct1("url")(u => u.toString)

}
