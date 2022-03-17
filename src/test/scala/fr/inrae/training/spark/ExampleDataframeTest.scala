package fr.inrae.training.spark

import org.apache.spark.sql.SparkSession
import org.scalatest.flatspec.AnyFlatSpec

import java.io.File

class ExampleDataframeTest extends AnyFlatSpec {
  var spark = SparkSession
    .builder()
    .master("local[*]")
    //.appName("spark-msd-"+this.getClass.getSimpleName.toLowerCase)
    .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    .config("spark.kryo.registrator", "net.sansa_stack.rdf.spark.io.JenaKryoRegistrator")
    .getOrCreate()

  "doMLSansaSparqlTransformer" should "read Turtle file and show 5 element" in {
    val input="./src/test/resources/metabolights_askomics.ttl"
    ExampleDataframe.doMLSansaSparqlTransformer(spark,input)
  }
}


