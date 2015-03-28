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
    case x: SeriesData => {
      val series = seriesDataToSeries(x)
      pipeline {
        Post(s"$url/db/$database/series?time_precision=s", List(series))
      }
    }
    case SeriesList(x) => {
      val seriesList = x.map(sd => seriesDataToSeries(sd))
      pipeline {
        Post(s"$url/db/$database/series?time_precision=s", seriesList)
      }
    }
    case x => {
      log.warning("Receiving unknown message")
      log.warning(x.toString)
    }
  }

  def seriesDataToSeries(sd: SeriesData): Series = {
    Series(sd.series, List(sd.column), List(List(sd.value)))
  }

}

case class SeriesList(list: List[SeriesData])
case class SeriesData(series: String, column: String, value: Double)
case class Series(name: String, columns: List[String], points: List[List[Double]])