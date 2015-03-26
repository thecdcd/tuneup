package tuneup.config

import akka.actor.{ActorSystem, Props}
import akka.routing.RoundRobinPool
import com.google.inject.name.Named
import com.google.inject.{AbstractModule, Inject, Provides, Singleton}
import com.typesafe.config.{Config, ConfigFactory}
import tuneup.actors.ReportActor

class ReportModule(val config: ReportingConfiguration) extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[ReportingConfiguration]).toInstance(config)
  }

  @Provides
  @Singleton
  @Named("username")
  def providesUsername = {
    config.reportUser()
  }

  @Provides
  @Singleton
  @Named("password")
  def providesPassword = {
    config.reportPassword()
  }

  @Provides
  @Singleton
  @Named("database")
  def providesDatabase = {
    config.reportDatabase()
  }

  @Provides
  @Singleton
  @Named("url")
  def providesUrl = {
    s"http://${config.reportHost()}:${config.reportPort()}"
  }

  /**
   * Default Akka configuration.
   *
   * @return default config
   */
  @Provides
  @Singleton
  @Named("fallback")
  def providesRegularConfig: Config = {
    ConfigFactory.load
  }

  /**
   * Custom config setting max-connections for spray.can to the value passed in 
   * {@link tuneup.config.ReportingConfiguration#reportPoolSize}.
   *
   * @return custom config
   */
  @Inject
  @Provides
  @Singleton
  @Named("custom")
  def providesCustomConfig: Config = {
    ConfigFactory.parseString(
      s"""spray.can {
         |host-connector {
         |  max-connections = ${config.reportPoolSize()}
          |
          |  pipelining = off
          |}
          |}""".stripMargin)
  }

  /**
   * "Complete" configuration. This is a mering of custom and default configurations.
   *
   * @param custom custom config
   * @param fall default config
   * @return merged config
   */
  @Inject
  @Provides
  @Singleton
  @Named("complete")
  def providesConfig(@Named("custom") custom: Config, @Named("fallback") fall: Config): Config = {
    ConfigFactory.load(custom.withFallback(fall))
  }

  @Inject
  @Provides
  @Singleton
  def providesActorSystem(@Named("complete") conf: Config) = {
    ActorSystem("tuneup-service-system", conf)
  }

  @Inject
  @Provides
  @Singleton
  @Named("router")
  def providesActorRef(actorSystem: ActorSystem,
                       @Named("username") user: String,
                       @Named("password") pass: String,
                       @Named("url") url: String,
                       @Named("database") db: String) = {
    actorSystem.actorOf(RoundRobinPool(config.reportPoolSize()).props(
      Props(new ReportActor(user, pass, url, db)(actorSystem.dispatcher)))
    )
  }

}
