scala-json-feed offers support for the [JSONFeed](https://jsonfeed.org) format and makes it easily usable in Scala.

The project is currently WIP and the first goal is to achieve full support of the format and correct validation.

Feel free to contribute with ideas, code and requests.

This is my first published and supported library so any criticism on the API, build and release methods are welcome.  

# Example

## Parsing

```
import scalajsonfeed.core.JSONFeedParser._
val input:String = """
{
   "title":"a title",
   "items":[
      {
         "id":"1",
         "tags":[
            "a",
            "b",
            "c"
         ],
         "content_text":"content"
      },
      {
         "id":"2",
         "content_text":"some other content"
      }
   ]
}
 """
val result = parse(input)

result.foreach(d => {
      println(s"Title: ${d.title}")
})
```