import org.apache.spark.sql.SparkSession
import org.apache.spark.SparkConf
import org.apache.spark.sql.functions._
import java.util.TimerTask
import java.util.Timer

object kafkaToSparkSQL{

  def main(args: Array[String]): Unit = {
   if (args.length != 6) {
      System.err.println(s"""
        |Usage: microBatch <microbatch_interval> <application_name> <spark_master> <kafka_brokers> <kafka_topics>
        |  <microbatch_interval> is the amount of time in seconds between two succesive runs of the job
        |  <application_name> is the name of the application as it will appear in YARN. It will be prefixed with "microbatch"
        |  <spark_master> is the connection string to the application master
        |  <kafka_brokers> is the connection string to the kafka kafka_brokers
        |  <kafka_topic> is the name of the topic to read from
        |  <kafka_offset> is the offset to start from: latest OR earliest
                """.stripMargin)
      System.exit(1)
    }
        //read arguments
        val Array(micro_time,app_name, spark_master, kafka_brokers, kafka_topic, kafka_offset) = args
        val batch_interval = micro_time.toInt*1000

        //intialize Spark Session
        val spark = SparkSession.builder().master(spark_master).appName("streaming"+app_name).enableHiveSupport().getOrCreate()

        import spark.implicits._
        //Read Stream from kafka topic
        val dataStream = spark.readStream.format("kafka").option("kafka.bootstrap.servers",kafka_brokers).option("subscribe",kafka_topic).option("startingOffsets", kafka_offset).load()
        //Query dataStream
        val lines = dataStream.selectExpr("CAST(value AS STRING)").as[(String)]
        //Write to Table
       val write_to_table = lines.writeStream.outputMode("append").format("parquet").start() 
       //val write_to_table = lines.writeStream.format("console").start() 
        write_to_table.awaitTermination()
        }

}
