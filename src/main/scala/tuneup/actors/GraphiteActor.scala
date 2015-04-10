package tuneup.actors

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorLogging}
import akka.io.Tcp._
import akka.io.{IO, Tcp}
import akka.util.ByteString
import tuneup.config.ReportingConfiguration
import tuneup.services.TuneupSeries.{SeriesData, SeriesList}

class GraphiteActor(val config: ReportingConfiguration)
  extends Actor with ActorLogging {

  import context.system

  val server: String = config.reportHost()
  val port: Int = config.reportPort()
  val prefix: String = config.reportDatabase()

  IO(Tcp) ! Connect(new InetSocketAddress(server, port))

  override def receive: Receive = {
    case CommandFailed(_: Connect) =>
      log.warning(s"Failed to connect to '$server:$port'")
      context stop self
    case c@Connected(remote, local) =>
      val connection = sender()
      connection ! Register(self)

      context become {
        case SeriesData(slave, resource, value) =>
          val date = System.currentTimeMillis / 1000
          val msg = s"$prefix.slave.$slave.resource.$resource $value $date\n"
          connection ! Write(ByteString.apply(msg))
        case SeriesList(l) =>
          l.foreach(m => context.parent ! m)
    case x: ByteString =>
          connection ! Write(x)
        case Received(data) =>
          log.warning(s"received: ${data.mkString}")
        case _: ConnectionClosed =>
          log.warning("connection was closed")
          context stop self
        case x =>
          log.warning("recv unknown msg")
          log.warning(x.toString)
      }
    case x =>
      log.warning("Received unknown message")
      log.warning(x.toString)
  }
}
