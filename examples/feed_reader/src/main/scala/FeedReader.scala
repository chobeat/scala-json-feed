import scala.io.StdIn.readLine
import core.JSONFeedParser.parse

object FeedReader {
  def main(args: Array[String]): Unit = {
    val url = readLine("Please enter a JSONFeed url:")
    val requestResult = scala.io.Source.fromURL(url).mkString
    val document = parse(requestResult)
    document.foreach(d => {
      println(s"Title: ${d.title}")
      for { item <- d.items } yield println(s"""
            |Item ${item.id}
            |${item.content.text.getOrElse("")}
            |${item.content.html.getOrElse("")}
          """.stripMargin)

    })
  }
}
