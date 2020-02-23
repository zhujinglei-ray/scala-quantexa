import org.apache.log4j.{Level, LogManager}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions._
import service.DataSetReaderService

object SparkSolverApp extends App {
  val inputPutFilePath = "src/transactions.txt"
  val dataReader = new DataSetReaderService(inputPutFilePath)
  val inputTransactionsList = dataReader.getListOfTransactions()
  val spark = SparkSession.builder.appName("sparktest").master("local").getOrCreate()
  LogManager.getRootLogger.setLevel(Level.WARN)
  /* Convert list to RDD */
  val rdd = spark.sparkContext.parallelize(inputTransactionsList)
  val df = spark.createDataFrame(rdd)

  /* customized average for five day to fulfil the agg in window function */
  def customizedAverageFor5Day: Double => Double = { s => s / 5 }

  import org.apache.spark.sql.functions.udf

  val fiveDayAverage = udf(customizedAverageFor5Day)

  //Question 1: Calculate the total transaction value for all transactions for each day
  val resultOfQOne = df.groupBy("transactionDay", "accountId", "category")
    .avg("transactionAmount")
    .orderBy("transactionDay", "accountId")

  resultOfQOne.repartition(1).write.option("header", "true")
    .csv("/Users/zhuj/scala-quantexa/spark-result/resultOfQuestion1BySpark")


  //Question 2: Calculate the average value of transactions per account for each type of transaction
  val resultOfQTwo = df.groupBy("accountId")
    .pivot("category")
    .avg("transactionAmount")
    .na.fill(0.0)
    .orderBy("accountId")

  resultOfQTwo.repartition(1).write.option("header", "true")
    .csv("/Users/zhuj/scala-quantexa/spark-result/resultOfQuestion2BySpark")


  //Question 3: Calculate statistics for each account number for the previous five days of transactions
  val dataFrameOfQThree = df.groupBy("transactionDay", "accountId")
    .pivot("category").sum("transactionAmount")
    .na.fill(0.0)
    .withColumn("sum_of_the_day", expr("AA+BB+CC+DD+EE+FF+GG"))
    .withColumn("avg_daily", expr("sum_of_the_day/5"))

  val listToRemove = List(1, 2, 3, 4, 5)
  val dayWindow = Window.partitionBy("accountId").orderBy("transactionDay").rangeBetween(-5, Window.currentRow - 1)
  val resultOfQThree = dataFrameOfQThree
    .withColumn("Maximum", max("sum_of_the_day").over(dayWindow))
    .withColumn("Average", sum("avg_daily").over(dayWindow))
    .withColumn("AA Total Value", sum("AA").over(dayWindow))
    .withColumn("CC Total Value", sum("CC").over(dayWindow))
    .withColumn("FF Total Value", sum("FF").over(dayWindow))
    .drop("AA", "BB", "CC", "DD", "EE", "FF", "GG", "sum_of_the_day", "avg_daily")
    .filter(!(col("transactionDay") isin (listToRemove: _*)))
    .orderBy("transactionDay", "accountId")

  resultOfQThree.repartition(1).write.option("header", "true").csv("/Users/zhuj/scala-quantexa/spark-result/resultOfQuestion3BySpark")
}
