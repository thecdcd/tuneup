package tuneup.mesos

import java.util.logging.Logger

import tuneup.config.MesosConfiguration
import org.apache.mesos.Protos.{FrameworkInfo, Status}
import org.apache.mesos.{MesosSchedulerDriver, Scheduler}

class MesosDriverFactory(val mesosScheduler: Scheduler, val frameworkInfo: FrameworkInfo, val config: MesosConfiguration) {
  private[this] val log = Logger.getLogger(getClass.getName)

  var mesosDriver: Option[MesosSchedulerDriver] = None

  def makeDriver(): Unit = {
    mesosDriver = Some(new MesosSchedulerDriver(mesosScheduler, frameworkInfo, config.mesosMaster()))
  }

  def get(): MesosSchedulerDriver = {
    if (mesosDriver.isEmpty) {
      makeDriver()
    }

    mesosDriver.get
  }

  def start() {
    val status = get().start()

    if (status != Status.DRIVER_RUNNING) {
      log.severe(s"MesosSchedulerDriver start resulted in: $status. Exiting")
      System.exit(1)
    } else {
      log.info("MesosDriver started successfully.")
    }
  }

  def close() {
    assert(mesosDriver.nonEmpty, "Attempted to close a non-initialized driver.")
    if (mesosDriver.isEmpty) {
      System.exit(1)
    }

    get().stop(true)
    mesosDriver = None
  }
}
