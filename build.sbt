ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.12.15"

initialize := {
  val _ = initialize.value // run the previous initialization
  val required = 55.0
  val current  = sys.props("java.class.version").toFloat
  assert( current >= required, s"Unsupported JDK: java.specification.version $current < $required")
}

lazy val urlSansaDistributionJar = "https://github.com/SANSA-Stack/SANSA-Stack/releases/download/v0.8.3_DistAD/DistAD_SANSA_examples.jar"
lazy val sansaReleaseJar = "./lib/"+urlSansaDistributionJar.split("/").last
lazy val sparkVersion = "3.0.1"
lazy val scalaTestVersion = "3.2.11"

/** Manage Sansa */
lazy val downloadSansaJar = taskKey[Unit]("Manage Sansa Jar distribution")
downloadSansaJar := {
  import scala.concurrent.blocking
  import java.io.File
  import java.net.URL
  import sys.process._
  println(" -- Manage Sansa Jar distribution -- ")
  if (! new File("./lib").exists())  new File("./lib").mkdir()
  if (! new File(sansaReleaseJar).exists()) {
     blocking((new URL(urlSansaDistributionJar) #> new File(sansaReleaseJar)).run().exitValue())
   }
  println(" -- " + sansaReleaseJar + " ok")
}

lazy val root = (project in file("."))
  .settings(
    name := "spark-msd-examples",
    Compile / unmanagedJars +=  file(sansaReleaseJar) ,
    libraryDependencies ++=Seq(
      "org.apache.spark" %% "spark-sql" % sparkVersion % "test,provided",
      "org.apache.spark" %% "spark-core" % sparkVersion % "test,provided",
      "org.scalatest" %% "scalatest" % scalaTestVersion % "test,provided",
    ),
    //Compile / compile := Def.sequential(downloadSansaJar, Compile / compile ).value,
    resolvers ++= Seq(
      "AKSW Maven Releases" at "https://maven.aksw.org/archiva/repository/internal",
      "AKSW Maven Snapshots" at "https://maven.aksw.org/archiva/repository/snapshots",
      "oss-sonatype" at "https://oss.sonatype.org/content/repositories/snapshots/",
      "Apache repository (snapshots)" at "https://repository.apache.org/content/repositories/snapshots/",
      "Sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/", "NetBeans" at "https://bits.netbeans.org/nexus/content/groups/netbeans/", "gephi" at "https://raw.github.com/gephi/gephi/mvn-thirdparty-repo/",
      Resolver.defaultLocal,
      Resolver.mavenLocal,
      "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository",
      "Apache Staging" at "https://repository.apache.org/content/repositories/staging/"
    ),
    assembly / assemblyMergeStrategy  := {
      case PathList("META-INF", xs @ _*) => MergeStrategy.discard
      case x => MergeStrategy.first
    }
  )

Global / onChangedBuildSource := ReloadOnSourceChanges