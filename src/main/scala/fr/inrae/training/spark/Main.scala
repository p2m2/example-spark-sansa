package fr.inrae.training.spark

import net.sansa_stack.ml.spark.utils.SPARQLQuery
import net.sansa_stack.query.spark._
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

    val sparqlQueryString ="SELECT ?s WHERE { ?s ?p ?o. } limit 10"
    val sparqlQuery: SPARQLQuery = SPARQLQuery(sparqlQueryString)
    println(sparqlQuery)
    //val res: DataFrame = sparqlQuery.transform(dataset)
    //val resultNodes = res.collect()
    //println(resultNodes.)
  }
/*
  def sparqlQuery(spark : SparkSession, input: String) = {

    val lang = Lang.NTRIPLES
    val triples = spark.rdf(lang)(input)

    val sparqlQuery = "SELECT * WHERE {?s ?p ?o} LIMIT 10"
    val result : QueryExecutionFactorySpark = triples.sparql()
    println(result.createQueryExecution(QueryFactory.create(sparqlQuery)).execSelectSpark() )
  }
*/
  def main(args : Array[String]) =  {
    val spark = SparkSession
      .builder()
      .appName("spark-training-test")
      .getOrCreate()

    doMain(spark)

  }

}
