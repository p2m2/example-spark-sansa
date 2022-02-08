ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.11.12"
lazy val sansaVersion = "0.7.1"

lazy val root = (project in file("."))
  .settings(
    name := "spark-sansa-read-turtle-example",
    libraryDependencies ++=Seq(
      "org.apache.spark" %% "spark-sql" % "2.4.8" % "test,provided",
      "org.scalatest" %% "scalatest" % "3.2.11" % "test",
      "org.slf4j" % "jcl-over-slf4j" % "1.7.35" % "test",
      "io.netty" % "netty-all" % "4.1.73.Final"% "test",
      "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.13.1" % "test",
      "com.fasterxml.jackson.core" % "jackson-databind" % "2.13.1",
      "com.github.jsonld-java" % "jsonld-java" % "0.13.4",
      "org.codehaus.groovy" % "groovy-all" % "3.0.9" pomOnly(),
      "net.sansa-stack" %% "sansa-rdf-spark" % sansaVersion,
      "net.sansa-stack" %% "sansa-owl-spark" % sansaVersion,
    //  "net.sansa-stack" %% "sansa-inference-spark" % sansaVersion,
      "net.sansa-stack" %% "sansa-query-spark" % sansaVersion,
    //  "net.sansa-stack" %% "sansa-ml-spark" % sansaVersion

    ),
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
    )

  )

Global / onChangedBuildSource := ReloadOnSourceChanges