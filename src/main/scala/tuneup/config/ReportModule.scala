package tuneup.config

import akka.actor.{ActorSystem, Props}
import akka.routing.RoundRobinPool
import com.google.inject.name.Named
import com.google.inject.{AbstractModule, Inject, Provides, Singleton}
import tuneup.actors.ReportActor

class ReportModule(val config: ReportingConfiguration) extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[ReportingConfiguration]).toInstance(config)
  }

  @Provides
  @Singleton
  @Named("url")
  def providesUrl = {
    s"http://${config.reportHost()}:${config.reportPort()}"
  }

  @Provides
  @Singleton
  @Named("batchsize")
  def providesBatchSize: Int = {
    config.reportBatchSize()
  }

  @Inject
  @Provides
  @Singleton
  def providesActorSystem = {
    ActorSystem("tuneup-service-system")
  }

  @Inject
  @Provides
  @Singleton
  @Named("router")
  def providesActorRef(actorSystem: ActorSystem,
                       @Named("url") url: String) = {
    actorSystem.actorOf(RoundRobinPool(config.reportPoolSize()).props(
      Props(new ReportActor(config.reportUser(), config.reportPassword(), url, config.reportDatabase())(actorSystem.dispatcher)))
    )
  }

}
