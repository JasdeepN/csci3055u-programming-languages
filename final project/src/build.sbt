name := "final_project"

version := "1.0"

scalaVersion := "2.11.7"

scalaSource in Compile := file("/home/jasdeep/Documents/school/CSCI 3055U Programming Languages/csci3055u-programming-languages/final project/src/threads")

scalacOptions ++= Seq("-unchecked", "-deprecation")

unmanagedJars in Compile += {
  val ps = new sys.SystemProperties
  val jh = ps("java.home")
  Attributed.blank(file(jh) / "lib/ext/jfxrt.jar")
  }

fork in run := true


resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
"com.typesafe.akka" %% "akka-actor"  % "2.4.3",
"com.typesafe.akka" %% "akka-slf4j" % "2.4.3",
"com.typesafe.akka" %% "akka-testkit" % "2.4.3",
"org.scalatest" %% "scalatest" % "2.2.4",
"com.typesafe.akka" %% "akka-remote" % "2.4.3",
"org.scalafx" %% "scalafx" % "8.0.60-R9"
)
