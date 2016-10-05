name := "pcb-etl"

version := "0.0.1"

scalaVersion := "2.11.8"

lazy val akkaVersion = "2.4.11"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-camel" % akkaVersion,
  "com.pcb" %% "pcb-messages" % "0.0.1-SNAPSHOT",
  "org.scalatest" %% "scalatest" % "3.0.0" % "test",
  "ch.qos.logback" % "logback-classic" % "1.1.7"
)
