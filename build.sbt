import com.typesafe.sbt.SbtScalariform._
import scalariform.formatter.preferences._
import sbt.Resolver

name := """LostPetWebapp"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.6"

libraryDependencies ++= Seq(
  jdbc,
//  cache,
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
libraryDependencies += "com.typesafe" % "config" % "1.3.1" //Road Config File
//Twitter4j
// https://mvnrepository.com/artifact/org.twitter4j/twitter4j-core
libraryDependencies += "org.twitter4j" % "twitter4j-core" % "4.0.7"

enablePlugins(DockerPlugin, JavaAppPackaging)
// Dockerfileに書く内容
packageName in Docker := "lost-pet-webapp"
version in Docker := "1.0"
dockerRepository := Some("celalink-lostpet")
maintainer in Docker := "celalink <yu-yanai@celalink.jp>"
dockerExposedPorts := List(8080)
dockerBaseImage := "openjdk:8u151-jre-stretch"


// silhouette Setting
resolvers += Resolver.jcenterRepo

resolvers += "Sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies ++= Seq(
  "org.reactivemongo" %% "play2-reactivemongo" % "0.16.0-play26",
  "com.mohiva" %% "play-silhouette-persistence-reactivemongo" % "5.0.6",
  "com.mohiva" %% "play-silhouette" % "5.0.5",
  "com.mohiva" %% "play-silhouette-password-bcrypt" % "5.0.5",
  "com.mohiva" %% "play-silhouette-persistence" % "5.0.5",
  "com.mohiva" %% "play-silhouette-crypto-jca" % "5.0.5",
  "org.webjars" %% "webjars-play" % "2.6.3",
  "org.webjars" % "bootstrap" % "3.3.7-1" exclude("org.webjars", "jquery"),
  "org.webjars" % "jquery" % "3.2.1",
  "org.webjars" % "jquery-ui" % "1.12.1",
  "net.codingwell" %% "scala-guice" % "4.1.0",
  "com.iheart" %% "ficus" % "1.4.3",
  "com.typesafe.play" %% "play-mailer" % "6.0.1",
  "com.typesafe.play" %% "play-mailer-guice" % "6.0.1",
  "com.enragedginger" %% "akka-quartz-scheduler" % "1.6.1-akka-2.5.x",
  "com.adrianhurt" %% "play-bootstrap" % "1.4-P26-B3-SNAPSHOT",
  "com.mohiva" %% "play-silhouette-testkit" % "5.0.5" % "test",
  "com.typesafe.play" % "play-json-joda_2.12" % "2.6.0", // use Joda DateTime Format in Play 2.6
  specs2 % Test,
  ehcache,
  guice,
  filters
)

//routesImport += "utils.route.Binders._"

// https://github.com/playframework/twirl/issues/105
//TwirlKeys.templateImports := Seq()

/*
scalacOptions ++= Seq(
  "-deprecation", // Emit warning and location for usages of deprecated APIs.
  "-feature", // Emit warning and location for usages of features that should be imported explicitly.
  "-unchecked", // Enable additional warnings where generated code depends on assumptions.
  "-Xfatal-warnings", // Fail the compilation if there are any warnings.
  //"-Xlint", // Enable recommended additional warnings.
  "-Ywarn-adapted-args", // Warn if an argument list is modified to match the receiver.
  "-Ywarn-dead-code", // Warn when dead code is identified.
  "-Ywarn-inaccessible", // Warn about inaccessible types in method signatures.
  "-Ywarn-nullary-override", // Warn when non-nullary overrides nullary, e.g. def foo() over def foo.
  "-Ywarn-numeric-widen", // Warn when numerics are widened.
  // Play has a lot of issues with unused imports and unsued params
  // https://github.com/playframework/playframework/issues/6690
  // https://github.com/playframework/twirl/issues/105
  "-Xlint:-unused,_"
)

//********************************************************
// Scalariform settings
//********************************************************

scalariformAutoformat := true

ScalariformKeys.preferences := ScalariformKeys.preferences.value
  .setPreference(FormatXml, false)
  .setPreference(DoubleIndentConstructorArguments, false)
  .setPreference(DanglingCloseParenthesis, Preserve)
*/

