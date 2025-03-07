/*
 * Copyright (C) from 2022 The Play Framework Contributors <https://github.com/playframework>, 2011-2021 Lightbend Inc. <https://www.lightbend.com>
 */

package scalaguide.async.scalacomet

//#comet-imports
import org.apache.pekko.stream.scaladsl.Source
import org.apache.pekko.stream.Materializer
import play.api.http.ContentTypes
import play.api.libs.json._
import play.api.libs.Comet
import play.api.mvc._
//#comet-imports

// format: off
import play.api.test._
// format: on

class MockController(val controllerComponents: ControllerComponents)(implicit materializer: Materializer)
    extends BaseController {
  // #comet-string
  def cometString = Action {
    implicit val m                      = materializer
    def stringSource: Source[String, _] = Source(List("kiki", "foo", "bar"))
    Ok.chunked(stringSource.via(Comet.string("parent.cometMessage"))).as(ContentTypes.HTML)
  }
  // #comet-string

  // #comet-json
  def cometJson = Action {
    implicit val m                     = materializer
    def jsonSource: Source[JsValue, _] = Source(List(JsString("jsonString")))
    Ok.chunked(jsonSource.via(Comet.json("parent.cometMessage"))).as(ContentTypes.HTML)
  }
  // #comet-json
}

class ScalaCometSpec extends PlaySpecification {
  "play comet" should {
    "work with string" in new WithApplication() with Injecting {
      override def running() = {
        try {
          val controllerComponents = inject[ControllerComponents]
          val controller           = new MockController(controllerComponents)
          val result               = controller.cometString.apply(FakeRequest())
          contentAsString(result) must contain(
            "<html><body><script>parent.cometMessage('kiki');</script><script>parent.cometMessage('foo');</script><script>parent.cometMessage('bar');</script>"
          )
        } finally {
          app.stop()
        }
      }
    }

    "work with json" in new WithApplication() with Injecting {
      override def running() = {
        try {
          val controllerComponents = inject[ControllerComponents]
          val controller           = new MockController(controllerComponents)
          val result               = controller.cometJson.apply(FakeRequest())
          contentAsString(result) must contain("<html><body><script>parent.cometMessage(\"jsonString\");</script>")
        } finally {
          app.stop()
        }
      }
    }
  }
}
