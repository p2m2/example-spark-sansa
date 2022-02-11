package fr.inrae.training.spark

import net.sansa_stack.rdf.spark.io._
import net.sansa_stack.rdf.spark.model._
import org.apache.jena.graph.Triple
import org.apache.jena.riot.Lang
import org.apache.spark.sql.{Dataset, SparkSession}

object Main {

  def doMain(spark : SparkSession) = {
    import spark.implicits._
    spark.range(0,1000).map( _ * 2 ).show()
  }

  /**
   *
   * @param input path to file that contains the data (in N-Triples format)
   * @param output the output directory
   * @return
   */
  def doWriteNt(spark : SparkSession, input: String, output : String) = {

    val dataset: Dataset[Triple] = spark.read.rdf(Lang.TURTLE)(input).toDS()
    println(dataset.count())
    val sparqlQueryString ="SELECT ?s WHERE { ?s ?p <http://data.linkedmdb.org/movie/film> }"
  //  val triplesRDD = dataset.rdd.sparql(sparqlQuery)
  }

  def main(args : Array[String]) =  {
    val spark = SparkSession
      .builder()
      .appName("spark-training-test")
      .getOrCreate()

    doMain(spark)

  }

}
