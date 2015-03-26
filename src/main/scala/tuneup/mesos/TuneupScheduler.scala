package tuneup.mesos

import java.util

import akka.actor.ActorRef
import com.google.inject.Inject
import com.google.inject.name.Named
import tuneup.actors.SeriesData
import org.apache.mesos.Protos._
import org.apache.mesos.{Scheduler, SchedulerDriver}

class TuneupScheduler @Inject()(@Named("router") metricsLogger: ActorRef)
  extends Scheduler {
  override def registered(driver: SchedulerDriver, frameworkId: FrameworkID, masterInfo: MasterInfo): Unit = {}

  override def offerRescinded(driver: SchedulerDriver, offerId: OfferID): Unit = {}

  override def disconnected(driver: SchedulerDriver): Unit = {}

  override def reregistered(driver: SchedulerDriver, masterInfo: MasterInfo): Unit = {}

  override def slaveLost(driver: SchedulerDriver, slaveId: SlaveID): Unit = {}

  override def error(driver: SchedulerDriver, message: String): Unit = {}

  override def statusUpdate(driver: SchedulerDriver, status: TaskStatus): Unit = {}

  override def frameworkMessage(driver: SchedulerDriver, executorId: ExecutorID, slaveId: SlaveID, data: Array[Byte]): Unit = {}

  override def resourceOffers(driver: SchedulerDriver, offers: util.List[Offer]): Unit = {
    import scala.collection.JavaConverters._

    for (offer <- offers.asScala) {
      // decline offer.
      driver.declineOffer(offer.getId)

      val slave = offer.getSlaveId.getValue
      offer.getResourcesList.asScala.foreach { resource =>
        if (resource.hasScalar) {
          val resourceName = resource.getName
          metricsLogger ! SeriesData(s"slave.$slave.resource.$resourceName", "value", resource.getScalar.getValue: java.lang.Double)
        }
      }

    }
  }

  override def executorLost(driver: SchedulerDriver, executorId: ExecutorID, slaveId: SlaveID, status: Int): Unit = {}
}
