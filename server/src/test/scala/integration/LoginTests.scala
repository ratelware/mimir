package integration

import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.{ContentTypes, StatusCodes}
import akka.http.scaladsl.model.headers._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.testkit.{TestActor, TestProbe}
import com.ratelware.science.slr.server.api.SessionAPI
import com.ratelware.science.slr.shared.definitions.{Password, SessionId, Username}
import com.ratelware.science.slr.shared.messages.session.{InitializeSession, TerminateSession}
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import org.scalatest.{MustMatchers, OptionValues, WordSpec}


class LoginTests extends WordSpec with ScalatestRouteTest with MustMatchers with FailFastCirceSupport with OptionValues {
  implicit val timeout = akka.util.Timeout(5, scala.concurrent.duration.SECONDS)

  "SessionAPI" when {
    val sessionManagerMock = TestProbe()
    val routes = Route.seal(SessionAPI.routes(sessionManagerMock.ref))
    import io.circe.generic.auto._
    import io.circe.syntax._

    "login request is received" should {
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

        Post("/login")
          .withEntity(ContentTypes.`application/json`, SessionId("0").asJson.noSpaces.getBytes)
          .withHeaders(`Content-Type`(ContentTypes.`application/json`)) ~>
          routes ~>
          check {
            status must equal(StatusCodes.BadRequest)
          }
      }

      "request initialization from TestProbe and return a response with Set-Cookie header set if structure is fine" in {
        val sessionCookieId = "123456798"
        sessionManagerMock.setAutoPilot({
          case (sender, msg: InitializeSession) =>
            sender ! Some(SessionId(sessionCookieId))
            TestActor.KeepRunning
          case _ => TestActor.NoAutoPilot
        })
        Post("/login")
          .withEntity(ContentTypes.`application/json`, InitializeSession(Username("ABC"), Password("DEF")).asJson.noSpaces.getBytes)
          .withHeaders(`Content-Type`(ContentTypes.`application/json`)) ~>
          routes ~>
          check {
            status must equal(StatusCodes.Created)
            header("Set-Cookie").value.name() must equal("Set-Cookie")
            header("Set-Cookie").value.value() must equal(s"sessionId=$sessionCookieId")
          }
      }

      "can perform logout after login finishes" in {
        val sessionCookieId = "123456798"
        sessionManagerMock.setAutoPilot({
          case (sender, msg: InitializeSession) =>
            sender ! Some(SessionId(sessionCookieId))
            TestActor.KeepRunning
          case _ => TestActor.NoAutoPilot
        })
        Post("/login")
          .withEntity(
            ContentTypes.`application/json`,
            InitializeSession(Username("ABC"), Password("DEF")).asJson.noSpaces.getBytes
          ) ~> routes ~>
          check {
            status must equal(StatusCodes.Created)
            header("Set-Cookie").value.name() must equal("Set-Cookie")
            header("Set-Cookie").value.value() must equal(s"sessionId=$sessionCookieId")
          }
      }
    }

    "logout request is received" should {
      "return Forbidden if session did not exist" in {
        sessionManagerMock.setAutoPilot({
          case (sender, msg: TerminateSession) =>
            sender ! None
            TestActor.KeepRunning
          case _ => TestActor.NoAutoPilot
        })

        Post("/logout").withEntity(
          ContentTypes.`application/json`,
          TerminateSession(SessionId("1234")).asJson.noSpaces.getBytes
        ) ~> routes ~> check {
          status must equal(StatusCodes.Forbidden)
        }
      }
    }

    "return OK if session existed" in {
      sessionManagerMock.setAutoPilot({
        case (sender, msg: TerminateSession) =>
          sender ! Some(())
          TestActor.KeepRunning
        case _ => TestActor.NoAutoPilot
      })

      Post("/logout").withEntity(
        ContentTypes.`application/json`,
        TerminateSession(SessionId("1234")).asJson.noSpaces.getBytes
      ) ~> routes ~> check {
        status must equal(StatusCodes.OK)
      }
    }
  }
}
