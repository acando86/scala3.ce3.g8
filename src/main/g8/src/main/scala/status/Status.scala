package status
import cats.effect.{IO, IOApp}
import cats.effect.std.Console
import cats.implicits.*
import cats.Show
import com.monovore.decline.Opts
import fs2.*
import cats.Functor
import utils.*
import cli.*
import utils.simpleConsole
import sttp.tapir.*
import sttp.tapir.json.circe.*
import sttp.tapir.generic.auto.*
import cats.effect.ExitCode
import org.http4s.HttpRoutes
import sttp.tapir.server.http4s.Http4sServerInterpreter
import org.http4s.blaze.server.BlazeServerBuilder
import scala.language.unsafeNulls
import sttp.tapir.server.ServerEndpoint

case class StatusResponse(status: String) derives CanEqual, MyCodecAsObject

object StatusEndpoint:
  val ok: StatusResponse = StatusResponse("ok")

  val endpoint: PublicEndpoint[Unit, Nothing, StatusResponse, Any] =
    infallibleEndpoint.get
      .in("status")
      .out(jsonBody[StatusResponse])

  val handler: Unit => IO[Either[Nothing, StatusResponse]] = _ => IO.pure(Right(ok))

  val fullEndpoint = endpoint.serverLogic(handler)
