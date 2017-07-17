name := """LogiLan"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)


scalaVersion := "2.11.11"

libraryDependencies ++= Seq(
  javaJpa,
  "org.hibernate" % "hibernate-entitymanager" % "5.1.0.Final",
  javaJdbc,
  cache,
  javaWs,
  "org.apache.poi" % "poi-ooxml" % "3.7",
  "mysql" % "mysql-connector-java" % "5.1.12",
  "org.jsoup" % "jsoup" % "1.8.2",
  "org.apache.commons" % "commons-email" % "1.3.3",
  "org.apache.poi" % "poi" % "3.8",
  "org.mindrot" % "jbcrypt" % "0.3m",
  "org.json"%"org.json"%"chargebee-1.0",
  "net.sourceforge.jtds" % "jtds" % "1.2"
)
