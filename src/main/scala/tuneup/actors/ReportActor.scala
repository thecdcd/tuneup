package tuneup.actors

import akka.actor._
import dispatch._

import scala.concurrent.ExecutionContext

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

  override def receive: Receive = {
    case x: SeriesJson => {
      val req = dispatch.url(url) / "db" / database / "series" <<? Map("time_precision" -> "s") << x.toJson setContentType("application/json", "UTF-8") as_!(username, password)
      val resp = Http(req OK as.String)
    }
    case x => {
      log.warning("Receiving unknown message")
      log.warning(x.toString)
    }
  }

}

sealed trait SeriesJson {
  def toJson: String
}

case class SeriesList(list: List[SeriesData]) extends SeriesJson {
  def toJson = "[ " + list.map(x => x.toJson).mkString(", ") + " ]"
}

case class SeriesData(series: String, column: String, value: Double) extends SeriesJson {
  def toJson = "{ \"name\": \"" + series + "\", \"columns\": [ \"" + column + "\" ], \"points\": [ [" + value + "] ] }"
}

case class Series(name: String, columns: List[String], points: List[List[Double]])