package fr.inrae.training.spark

import org.apache.spark.sql.SparkSession
import org.scalatest.flatspec.AnyFlatSpec

import java.io.File

class MainTest extends AnyFlatSpec {
  var spark = SparkSession
    .builder()
    .master("local[*]")
    .getOrCreate()

  "my function doMain" should "work" in {
    Main.doMain(spark)
  }

  "doWriteNt" should "read nt file and write nt file" in {
    val n3_input="./src/test/resources/example.nt"
    val tp = File.createTempFile("out-", ".n3").getPath
    Main.doWriteNt(spark,n3_input,tp)
  }
/*
  "sparqlQuery" should "" in {
    val n3_input="./src/test/resources/example.nt"
    val tp = File.createTempFile("out2-", ".n3").getPath
    Main.sparqlQuery(spark,n3_input)
  }
*/
}


