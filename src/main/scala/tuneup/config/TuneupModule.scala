package tuneup.config

import com.google.inject.{AbstractModule, Provides, Singleton}
import tuneup.mesos.{MesosDriverFactory, TuneupScheduler}
import org.apache.mesos.Protos.FrameworkInfo
import org.apache.mesos.Scheduler

class TuneupModule(val config: MesosConfiguration) extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[MesosConfiguration]).toInstance(config)
    bind(classOf[Scheduler]).to(classOf[TuneupScheduler]).asEagerSingleton()
  }

  @Provides
  @Singleton
  def provideFrameworkInfo(): FrameworkInfo = {
    val frameworkInfo = FrameworkInfo.newBuilder()
      .setName(config.mesosFrameworkName())
      .setRole(config.mesosRole())
      .setUser(config.mesosUser())
      .setFailoverTimeout(config.failoverTimeoutSeconds())
      .setCheckpoint(true)

    frameworkInfo.build()
  }

  @Provides
  @Singleton
  def provideMesosDriverFactory(mesosScheduler: Scheduler,
                                frameworkInfo: FrameworkInfo): MesosDriverFactory = {
    new MesosDriverFactory(mesosScheduler, frameworkInfo, config)
  }

}
