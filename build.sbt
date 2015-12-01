// See crossrelease.sh for valid combinations of akkaVersion and crossScalaVersion.

// Developed against 2.3.*, see crossrelease.sh for test/build versions.
akkaVersion := "2.3.13"

organization := "nl.grons"

name := "metrics-scala"

lazy val baseVersion = "3.5.2-Indix"

version <<= akkaVersion { av =>
  val akkaVersion = if (av.nonEmpty) "_a" + av.split('.').take(2).mkString(".") else ""
  baseVersion + akkaVersion
}

description <<= (scalaVersion, akkaVersion) { (sv, av) =>
  val akkaDescription = if (av.nonEmpty) "Akka " + av +" and " else ""
  "metrics-scala for " + akkaDescription + "Scala " + sbt.cross.CrossVersionUtil.binaryScalaVersion(sv)
}

// Developed against 2.10, see crossrelease.sh for test/build versions.
scalaVersion := "2.10.5"

crossScalaVersions := Seq("2.10.5", "2.11.7")

crossVersion := CrossVersion.binary

resolvers ++= Seq(
  "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository"
)

libraryDependencies <++= (scalaVersion) { sv =>
  Seq(
    "io.dropwizard.metrics" % "metrics-core" % "4.0.0-SNAPSHOT",
    "io.dropwizard.metrics" % "metrics-healthchecks" % "4.0.0-SNAPSHOT",
    "junit" % "junit" % "4.11" % "test",
    "org.scalatest" %% "scalatest" % "2.2.5" % "test",
    // Override version that scalatest depends on:
    "org.scala-lang" % "scala-reflect" % sv % "test",
    "org.mockito" % "mockito-all" % "1.10.19" % "test"
  )
}

libraryDependencies <++= (akkaVersion) { av =>
  if (av.nonEmpty)
    Seq(
      "com.typesafe.akka" %% "akka-actor" % av,
      "com.typesafe.akka" %% "akka-testkit" % av % "test"
    )
  else
    Seq()
}

unmanagedSourceDirectories in Compile <<= (unmanagedSourceDirectories in Compile, sourceDirectory in Compile, akkaVersion) { (sds: Seq[java.io.File], sd: java.io.File, av: String) =>
  val extra = new java.io.File(sd, "akka")
  (if (av.nonEmpty && extra.exists) Seq(extra) else Seq()) ++ sds
}

unmanagedSourceDirectories in Test <<= (unmanagedSourceDirectories in Test, sourceDirectory in Test, akkaVersion) { (sds: Seq[java.io.File], sd: java.io.File, av: String) =>
  val extra = new java.io.File(sd, "akka")
  (if (av.nonEmpty && extra.exists) Seq(extra) else Seq()) ++ sds
}

javacOptions ++= Seq("-Xmx512m", "-Xms128m", "-Xss10m", "-source", "1.6", "-target", "1.6")

javaOptions ++= Seq("-Xmx512m", "-Djava.awt.headless=true")

scalacOptions ++= Seq("-deprecation", "-unchecked")

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (version.value.trim.endsWith("SNAPSHOT"))
    Some("Indix Snapshot Artifactory" at "http://artifacts.indix.tv:8081/artifactory/libs-snapshot-local")
  else
    Some("Indix Release Artifactory" at "http://artifacts.indix.tv:8081/artifactory/libs-release-local")
}

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

licenses := Seq("Apache 2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))
