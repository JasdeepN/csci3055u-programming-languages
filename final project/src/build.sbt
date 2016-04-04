name := "app"

version := "1.0"

scalaVersion := "2.11.7"

scalacOptions ++= Seq("-unchecked", "-deprecation")

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor"  % "2.4.3",
  "com.typesafe.akka" %% "akka-slf4j" % "2.4.3",
  "com.typesafe.akka" %% "akka-testkit" % "2.4.3",
  "org.scalatest" %% "scalatest" % "2.2.4",
  "com.typesafe.akka" %% "akka-remote" % "2.4.3"
)
