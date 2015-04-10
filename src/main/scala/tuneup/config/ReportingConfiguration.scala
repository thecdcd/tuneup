package tuneup.config

import org.rogach.scallop.ScallopConf

trait ReportingConfiguration extends ScallopConf {

  lazy val reportHost = opt[String]("report_host",
    descr = "The hostname that is running reporting backend.",
    default = Some("127.0.0.1"),
    required = true,
    noshort = true)

  lazy val reportPort = opt[Int]("report_port",
    descr = "The port the reporting backend is using.",
    default = Some(8086),
    required = true,
    noshort = true)

  lazy val reportDatabase = opt[String]("report_db",
    descr = "The database to use for storing series data.",
    default = Some("mesos_resources"),
    required = true,
    noshort = true)

  lazy val reportUser = opt[String]("report_user",
    descr = "The user that has access to the reporting database.",
    default = Some("root"),
    noshort = true)

  lazy val reportPassword = opt[String]("report_pass",
    descr = "The password to use when connecting to reporting backend.",
    default = Some("root"),
    noshort = true)

  lazy val reportPoolSize = opt[Int]("report_pool_size",
    descr = "Number of reporting actors to maintain. A higher number should result in more concurrency.",
    default = Some(32),
    required = true,
    noshort = true)

  lazy val reportBatchSize = opt[Int]("report_batch_size",
    descr = "The number of series to send at one time.",
    default = Some(10),
    required = true,
    noshort = true)

  lazy val reportBackend = opt[String]("report_backend",
    descr = "The type of reporting service being used. Valid options are influx or graphite and influx will be chosen as default.",
    default = Some("influx"),
    required = true,
    noshort = true)

}
