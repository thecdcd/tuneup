package tuneup.actors

import akka.actor._
import spray.client.pipelining._
import spray.http.{HttpResponse, HttpRequest, BasicHttpCredentials}
import spray.json.DefaultJsonProtocol

import scala.concurrent.{Future, ExecutionContext}
import scala.util.{Failure, Success}

/**
 * Actor that communicates with InfluxDB.
 *
 */
class ReportActor(val username: String,
                  val password: String,
                  val url: String,
                  val database: String)
                 (implicit val system: ExecutionContext)
  extends Actor with ActorLogging {

  object ReportJsonProtocol extends DefaultJsonProtocol {
    implicit val seriesFormat = jsonFormat3(Series)
  }

  val pipeline: HttpRequest => Future[HttpResponse] = (
    addCredentials(BasicHttpCredentials(username, password))
      ~> sendReceive
    )

  import ReportJsonProtocol._
  import spray.httpx.SprayJsonSupport._

  override def receive: Receive = {
    case SeriesData(s, c, v) => {
      val series = Series(s, List(c), List(List(v)))
      pipeline {
        Post(s"$url/db/$database/series?time_precision=s", List(series))
      }
    }
    case x => {
      log.warning("Receiving unknown message")
      log.warning(x.toString)
    }
  }

}

case class SeriesData(series: String, column: String, value: Double)
case class Series(name: String, columns: List[String], points: List[List[Double]])