package fr.inrae.training.spark


import net.sansa_stack.rdf.spark.io._
import org.apache.jena.riot.Lang
import org.apache.jena.graph.Triple
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
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

    val lang = Lang.NTRIPLES
    val triples : RDD[Triple] = spark.rdf(lang)(input)

    triples.saveAsNTriplesFile(output)

    spark.stop
  }


  def main(args : Array[String]) =  {
    val spark = SparkSession
      .builder()
      .appName("spark-training-test")
      .getOrCreate()

    doMain(spark)

  }

}
