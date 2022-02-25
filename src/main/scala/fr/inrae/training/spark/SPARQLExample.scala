package fr.inrae.training.spark

import net.sansa_stack.query.spark.api.domain.ResultSetSpark
import net.sansa_stack.query.spark.ontop.QueryEngineFactoryOntop
import net.sansa_stack.rdf.spark.io._
import org.apache.jena.graph.Triple
import org.apache.jena.query.ResultSet
import org.apache.jena.rdf.model.Model
import org.apache.jena.riot.Lang
import org.apache.jena.sparql.core.Var
import org.apache.jena.sparql.engine.binding.Binding
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

/**
 * An example stub how to work with the SPARQL query engine.
 *
 * @note Never forget to set a Spark master URL
 */
object SPARQLExample {

  def main(spark : SparkSession, args: Array[String]): Unit = {

    // lets assume two separate RDF files

    val pathToRdfFile1 = "./src/test/resources/metabolights_askomics.ttl"
    val pathToRdfFile2 = "./src/test/resources/example.nt"

    // load the first file into an RDD of triples (from an N-Triples file here)
    val triples1 = spark.rdf(Lang.TURTLE)(pathToRdfFile1)

    // create the main query engine
    // we do provide two different SPARQL-to-SQL rewriter backends, Sparqlify and Ontop
    val queryEngineFactory = new QueryEngineFactoryOntop(spark) // Ontop
    // or
    // val queryEngineFactory = new QueryEngineFactorySparqlify(spark) // Sparqlify

    // create the query execution factory for the first dataset
    val qef1 = queryEngineFactory.create(triples1)

    // depending on the query type, finally execute the query
    doSelectQuery()

    //doConstructQuery()
    //doAskQuery()

    // a) SELECT query returns a ResultSetSpark which holds an
    //    RDD of bindings and the result variables
    def doSelectQuery(): Unit = {
      val query = "SELECT * WHERE {?s ?p ?o} LIMIT 10"
      val qe = qef1.createQueryExecution(query)
      val result: ResultSetSpark = qe.execSelectSpark()
      val resultBindings: RDD[Binding] = result.getBindings // the bindings, i.e. mappings from vars to RDF resources
      val resultVars: Seq[Var] = result.getResultVars // the result vars of the SPARQL query

      println(resultVars)
    }



    // b) CONSTRUCT query returns an RDD of triples
    def doConstructQuery(): Unit = {
      val query = "CONSTRUCT ..."
      val qe = qef1.createQueryExecution(query)
      val result: RDD[Triple] = qe.execConstructSpark()
    }

    // c) ASK query returns a boolean value
    def doAskQuery(): Unit = {
      val query = "ASK ..."
      val qe = qef1.createQueryExecution(query)
      val result: Boolean = qe.execAsk()
    }


    // you may have noticed that for SELECT and CONSTRUCT queries we used methods ending on "Spark()"
    // the reason here is that those method keep the results distributed, i.e. as an RDD
    // For convenience, we do also support those methods without this behaviour, i.e. the results will be fetched to the driver
    // and can be processed without the Spark pros and cons:


    //doSelectQueryToLocal()
    //doConstructQueryToLocal()

    // a) SELECT query returns an Apache Jena ResultSet wrapping bindings and variables
    def doSelectQueryToLocal(): Unit = {
      val query = "SELECT ..."
      val qe = qef1.createQueryExecution(query)
      val result: ResultSet = qe.execSelect()
    }


    // b) CONSTRUCT query and return an Apache Jena Model wrapping the triples as Statements
    def doConstructQueryToLocal(): Unit = {
      val query = "CONSTRUCT ..."
      val qe = qef1.createQueryExecution(query)
      val result: Model = qe.execConstruct()
    }

    // so far we used only a single dataset, but during the workflow you might be using different datasets
    // in that case you have to take into account that a single query execution factory is immutable and bound
    // to a specific datasets
    // that means for another dataset we have to create another query execution factory similar to our first one:

    // load the second file into an RDD of triples (from an N-Triples file here)
    val triples2 = spark.rdf(Lang.TURTLE)(pathToRdfFile2)

    // create the query execution factory for the first dataset
    val qef2 = queryEngineFactory.create(triples2)

    // then run queries on the second dataset by using the our new query execution factory:
    val query = "SELECT ..."
    val qe = qef2.createQueryExecution(query)
    val result: RDD[Triple] = qe.execConstructSpark()

    // if you want to run some queries on both datasets you have to merge both before creating the query execution factory
    // so either do it already during loading
    val triples12 = spark.rdf(Lang.TURTLE)(Seq(pathToRdfFile1, pathToRdfFile2).mkString(","))
    // or compute the union of them
    // val triples12 = triples1.union(triples2)

    // then as before, create query execution factory
    val qef12 = queryEngineFactory.create(triples12)

    // and run the queries ...
  }

}