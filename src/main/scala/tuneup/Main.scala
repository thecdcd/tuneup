package tuneup

import com.google.inject.Module
import mesosphere.chaos.{App, AppConfiguration}
import org.rogach.scallop.ScallopConf
import tuneup.config.{MesosConfiguration, ReportModule, ReportingConfiguration, TuneupModule}
import tuneup.services.TuneupService

object Main extends App {
  lazy val conf = new ScallopConf(args)
    with AppConfiguration with MesosConfiguration
    with ReportingConfiguration

  override def modules(): Iterable[_ <: Module] = {
    Seq(
      new ReportModule(conf),
      new TuneupModule(conf)
    )
  }

  run(
    classOf[TuneupService]
  )
}
