package tuneup.config

import org.rogach.scallop.ScallopConf

trait MesosConfiguration extends ScallopConf {

  lazy val mesosMaster = opt[String]("mesos_master",
    descr = "The URL of the Mesos master.",
    default = Some("local"),
    required = true,
    noshort = true)

  lazy val mesosUser = opt[String]("mesos_user",
    descr = "The Mesos user to run the processes under",
    default = Some("root"),
    noshort = true)

  lazy val mesosRole = opt[String]("mesos_role",
    descr = "The Mesos role to run tasks under",
    default = Some("*"),
    noshort = true)

  lazy val failoverTimeoutSeconds = opt[Long]("mesos_failover_timeout",
    descr = "The failover timeout in milliseconds for Mesos",
    default = Some(3000L),
    noshort = true)

  lazy val mesosFrameworkName = opt[String]("mesos_framework_name",
    descr = "The Mesos Framework name",
    default = Some("tuneup-framework"),
    required = true,
    noshort = true)

}
