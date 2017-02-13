
// compile with sbt spDist
name := "kafkatToSparkSQL"

version := "1.0"

scalaVersion := "2.11.8"


resolvers += "Spark Packages Repo" at "http://dl.bintray.com/spark-packages/maven"



libraryDependencies += "org.apache.spark" % "spark-streaming_2.11" % "2.1.0"
libraryDependencies += "org.apache.spark" % "spark-streaming-kafka_2.11" % "1.6.3"
libraryDependencies += "org.apache.spark" % "spark-core_2.11" % "2.1.0"
libraryDependencies += "com.databricks" % "spark-csv_2.11" % "1.5.0"
libraryDependencies += "com.databricks" % "spark-avro_2.11" % "3.1.0"
libraryDependencies += "org.apache.spark" % "spark-sql_2.11" % "2.1.0"
libraryDependencies += "com.bigstep" % "datalake-client-libraries" % "1.4"


