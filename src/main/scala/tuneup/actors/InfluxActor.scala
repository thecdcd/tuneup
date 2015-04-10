package tuneup.actors

import akka.actor._
import dispatch._
import tuneup.config.ReportingConfiguration
import tuneup.services.TuneupSeries.SeriesJson

import scala.concurrent.ExecutionContext

/**
 * Actor that communicates with InfluxDB.
 *
 */
class InfluxActor(val config: ReportingConfiguration)(implicit val system: ExecutionContext)
  extends Actor with ActorLogging {

  val username = config.reportUser()
  val password = config.reportPassword()
  val url = s"http://${config.reportHost()}:${config.reportPort()}"
  val database = config.reportDatabase()

  override def receive: Receive = {
    case x: SeriesJson =>
      val req = dispatch.url(url) / "db" / database / "series" <<? Map("time_precision" -> "s") << x.toJson setContentType("application/json", "UTF-8") as_!(username, password)
      val resp = Http(req OK as.String)
    case x =>
      log.warning("Receiving unknown message")
      log.warning(x.toString)
  }

}
