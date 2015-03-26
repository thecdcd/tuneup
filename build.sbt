name := "tuneup"

version := "1.0"

scalaVersion := "2.11.6"

resolvers ++= Seq(
  "Mesosphere Public Repo" at "http://downloads.mesosphere.io/maven",
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  "spray repo" at "http://repo.spray.io"
)

libraryDependencies ++= Seq(
  "mesosphere" %% "mesos-utils" % "0.20.1-1",
  "mesosphere" %% "chaos" % "0.6.1",
  "org.apache.mesos" % "mesos" % "0.20.1",
  "com.typesafe.akka" %% "akka-actor" % "2.3.9",
  "io.spray" %% "spray-client" % "1.3.2",
  "io.spray" %% "spray-json" % "1.3.1"
)
