package fr.inrae.training.spark

import org.apache.spark.sql.SparkSession
import org.scalatest.flatspec.AnyFlatSpec

import java.io.File

class MainTest extends AnyFlatSpec {
  var spark = SparkSession
    .builder()
    .master("local[*]")
    .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    .config("spark.kryo.registrator", "net.sansa_stack.rdf.spark.io.JenaKryoRegistrator")
    .getOrCreate()

  "my function doMain" should "work" in {
    Main.doMain(spark)
  }

  "doMLSansaSparqlTransformer" should "read nt file and show 5 element" in {
    val n3_input="./src/test/resources/example.nt"
    val tp = File.createTempFile("out-", ".n3").getPath
    Main.doMLSansaSparqlTransformer(spark,n3_input,tp)
  }
/*
  "sparqlQuery" should "" in {
    val n3_input="./src/test/resources/example.nt"
    val tp = File.createTempFile("out2-", ".n3").getPath
    Main.sparqlQuery(spark,n3_input)
  }
*/
}


