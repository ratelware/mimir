package integration

import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.{ContentTypes, StatusCodes}
import akka.http.scaladsl.model.headers._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.testkit.TestProbe
import com.ratelware.science.slr.server.api.SessionAPI
import com.ratelware.science.slr.shared.definitions.SessionId
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import org.scalatest.{MustMatchers, WordSpec}


class LoginTests extends WordSpec with ScalatestRouteTest with MustMatchers with FailFastCirceSupport {
  implicit val timeout = akka.util.Timeout(5, scala.concurrent.duration.SECONDS)

  "SessionAPI" when {
    import io.circe.generic.auto._
    "login request is received" should {
      val testProbe = TestProbe()
      val routes = Route.seal(SessionAPI.routes(testProbe.ref))

      "return MethodNotAllowed if it is GET instead of POST" in {
        Get("/login") ~> routes ~> check {
          status must equal(StatusCodes.MethodNotAllowed)
        }
      }

      "return UnsupportedMediaType if it is not an application/json" in {
        Post("/login")
          .withEntity("ABC") ~> routes ~> check {
          status must equal(StatusCodes.UnsupportedMediaType)
        }
      }

      "return BadRequest if structure does not meed requirements" in {
        import io.circe.generic.auto._
        import io.circe.syntax._

        Post("/login")
          .withEntity(ContentTypes.`application/json`, SessionId("0").asJson.noSpaces.getBytes)
          .withHeaders(`Content-Type`(ContentTypes.`application/json`)) ~>
          routes ~>
          check {
            status must equal(StatusCodes.BadRequest)
          }
      }
    }
  }
}
