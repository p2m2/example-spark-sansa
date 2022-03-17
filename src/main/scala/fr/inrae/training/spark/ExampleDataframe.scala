package fr.inrae.training.spark

import net.sansa_stack.ml.spark.utils.SPARQLQuery
import net.sansa_stack.rdf.spark.io._
import net.sansa_stack.rdf.spark.model._
import org.apache.jena.graph.Triple
import org.apache.jena.riot.Lang
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

object ExampleDataframe extends App {

  val rdfFile = "./src/test/resources/metabolights_askomics.ttl"
  val local = true

      val conf = SparkSession.builder()
        .appName("spark-msd-"+this.getClass.getSimpleName.toLowerCase)
        .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
        .config("spark.kryo.registrator", "net.sansa_stack.rdf.spark.io.JenaKryoRegistrator")

      val spark =
        if (local)
          conf.master("local[*]").getOrCreate()
        else
          conf.getOrCreate()

      doMLSansaSparqlTransformer(spark,rdfFile)

  /**
   *
   * @param input path to file that contains the data (in N-Triples format)
   * @param output the output directory
   * @return
   */
  def doMLSansaSparqlTransformer(spark : SparkSession, input: String, query : String = "SELECT ?s WHERE { ?s ?p ?o. } limit 10") = {

    val dataset: Dataset[Triple] = spark.read.rdf(Lang.TURTLE)(input).toDS()
    println("=================Count triples==================")
    println(dataset.count())

    println("==================SparqlQuery=================")
    println(query)

    val sparqlQuery: SPARQLQuery = SPARQLQuery(query)

    println("==============Dataframes=====================")
    val res: DataFrame = sparqlQuery.transform(dataset)
    println("==============Show Dataframe=====================")
    val resultNodes = res.collect()
    res.show(5)
  }

}
