name := "tuneup"

version := "1.0"

scalaVersion := "2.11.6"

resolvers ++= Seq(
  "Mesosphere Public Repo" at "http://downloads.mesosphere.io/maven",
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
)

libraryDependencies ++= Seq(
  "mesosphere" %% "mesos-utils" % "0.20.1-1",
  "mesosphere" %% "chaos" % "0.6.1",
  "org.apache.mesos" % "mesos" % "0.20.1",
  "com.typesafe.akka" %% "akka-actor" % "2.3.9",
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.2"
)
