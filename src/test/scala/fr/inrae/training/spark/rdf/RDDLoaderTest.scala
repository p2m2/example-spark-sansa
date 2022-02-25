package fr.inrae.training.spark.rdf

import org.apache.jena.riot.Lang
import org.apache.spark.sql.SparkSession
import org.scalatest.flatspec.AnyFlatSpec
import net.sansa_stack.query.spark._
import net.sansa_stack.rdf.common.partition.core.RdfPartitionerDefault
import net.sansa_stack.rdf.spark.partition._
import org.apache.jena.graph.Triple
import org.apache.spark.rdd.RDD

class MainTest extends AnyFlatSpec {
  var spark = SparkSession
    .builder()
    .master("local[*]")
    .getOrCreate()

  "loadAsRdd" should "load a Rdd" in {
    val n3_input="./src/test/resources/example.nt"
    RdfLoader.loadAsRdd(spark,n3_input).take(2).foreach(println(_))
  }

  "loadAsDataFrame" should "load a DataFrame" in {
    val n3_input="./src/test/resources/metabolights_askomics.ttl"
    val lang = Lang.TURTLE
    RdfLoader.loadAsDataFrame(spark,lang,n3_input).take(10).foreach(println(_))
  }

  "loadStringTTL" should "load a RDD" in {
    val triplesString =
      """<urn:s1> <urn:p> "2021-02-25T16:30:12Z"^^<http://www.w3.org/2001/XMLSchema#dateTime> .
        |<urn:s2> <urn:p> "2021-02-26"^^<http://www.w3.org/2001/XMLSchema#date> .
        |<urn:s3> <urn:p> "5"^^<http://www.w3.org/2001/XMLSchema#int> .
        |<urn:s4> <urn:p> "6"^^<http://www.w3.org/2001/XMLSchema#long> .
        |      """.stripMargin
    val graphRdd : RDD[Triple]= RdfLoader.loadStringTTL(spark,triplesString)

    val qef = graphRdd.verticalPartition(RdfPartitionerDefault).sparqlify

    val resultSet = qef.createQueryExecution("SELECT ?o { ?s ?p ?o }")
      .execSelectSpark()
  }
}
