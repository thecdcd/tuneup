package tuneup.services

import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicBoolean
import java.util.logging.Logger

import com.google.common.util.concurrent.AbstractExecutionThreadService
import com.google.inject.Inject
import tuneup.mesos.MesosDriverFactory

class TuneupService @Inject()(val log: Logger, val mesosDriver: MesosDriverFactory = null)
  extends AbstractExecutionThreadService {
  val running = new AtomicBoolean(false)
  val latch = new CountDownLatch(1)

  override def startUp() = {
    assert(!running.get(), "This service is already running!")
    log.info("Starting TuneUp service...")

    running.set(true)
    mesosDriver.start()

    super.startUp()
  }

  override def shutDown() = {
    log.info("Stopping TuneUp service...")
    mesosDriver.close()

    running.set(false)
  }

  override def triggerShutdown(): Unit = {
    latch.countDown()

    super.triggerShutdown()
  }

  override def run() = {
    latch.await()
  }
}
