name := "pcb-db"

version := "0.0.1"

scalaVersion := "2.11.8"

lazy val akkaVersion = "2.4.11"
lazy val slickVersion = "3.1.1"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-remote" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.slick" %% "slick" % slickVersion,
  "com.typesafe.slick" %% "slick-hikaricp" % slickVersion, 
  "com.pcb" %% "pcb-messages" % "0.0.1-SNAPSHOT",
  "com.h2database" % "h2" % "1.4.192",
  "org.scalatest" %% "scalatest" % "3.0.0" % "test",
  "ch.qos.logback" % "logback-classic" % "1.1.7"
)
