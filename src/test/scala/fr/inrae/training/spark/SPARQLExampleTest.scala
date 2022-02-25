package fr.inrae.training.spark

import org.apache.spark.sql.SparkSession
import org.scalatest.flatspec.AnyFlatSpec

class SPARQLExampleTest extends AnyFlatSpec {
  "my function doMain" should "work" in {
    // SparkSession is needed
    val spark = SparkSession.builder
      .appName(s"SPARQL engine example")
      .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer") // we need Kryo serialization enabled with some custom serializers
      .config("spark.kryo.registrator", String.join(
        ", ",
        "net.sansa_stack.rdf.spark.io.JenaKryoRegistrator",
        "net.sansa_stack.query.spark.ontop.OntopKryoRegistrator",
        "net.sansa_stack.query.spark.sparqlify.KryoRegistratorSparqlify"))
      .config("spark.sql.crossJoin.enabled", true) // needs to be enabled if your SPARQL query does make use of cartesian product Note: in Spark 3.x it's enabled by default
      .master("local[*]")
      .getOrCreate()

    SPARQLExample.main(spark,Array())
  }
}
