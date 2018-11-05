name := """LostPatWebapp"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.6"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.1" % Test,
  "com.typesafe.play" %% "anorm" % "2.5.3"
)

//MySQLと接続する設定
libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.8-dmr"

//play 2.6 Setting
libraryDependencies += guice
libraryDependencies += openId
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.0"
libraryDependencies += "com.typesafe.play" %% "play-iteratees" % "2.6.1"
libraryDependencies += "com.typesafe.play" %% "play-iteratees-reactive-streams" % "2.6.1"

//enablePlugins(JavaAppPackaging)
enablePlugins(JavaServerAppPackaging)

// Dockerfileに書く内容
packageName in Docker := "lostPet-webapp"
version in Docker := "1.0"
dockerRepository := Some("celalink-lostpet")
maintainer in Docker := "celalink <yu-yanai@celalink.jp>"
dockerExposedPorts := List(8080)
dockerBaseImage := "openjdk:latest"

