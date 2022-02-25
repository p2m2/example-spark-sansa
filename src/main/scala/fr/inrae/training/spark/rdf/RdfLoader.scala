package fr.inrae.training.spark.rdf

import net.sansa_stack.query.spark._
import net.sansa_stack.rdf.spark.io._
import net.sansa_stack.rdf.spark.partition._
import org.apache.commons.io.IOUtils
import org.apache.jena.graph.Triple
import org.apache.jena.riot.{Lang, RDFDataMgr}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql
import org.apache.spark.sql.SparkSession

import scala.collection.JavaConverters._

object RdfLoader {
  /**
   * Load Rdd
   * @param spark
   * @param input
   */
  def loadAsRdd(spark : SparkSession, input: String): RDD[Triple] = {
    val lang: Lang = Lang.NTRIPLES
    spark.rdf(lang)(input)
  }

  def loadAsDataFrame(spark : SparkSession, lang : Lang, input: String) : sql.DataFrame = {
    spark.read.rdf(lang)(input)
  }

  def loadStringTTL(spark : SparkSession,triplesString : String) :  RDD[Triple] = {

    val it = RDFDataMgr.createIteratorTriples(IOUtils.toInputStream(triplesString, "UTF-8"), Lang.NTRIPLES, "http://example.org/").asScala.toSeq
    spark.sparkContext.parallelize(it)
  }

}
