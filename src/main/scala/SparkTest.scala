import org.apache.spark._

object SparkTest {
  def main(args: Array[String]) {
    val inputFile =  "/Users/zhuj/scala-quantexa/src/transactions.txt"
    val conf = new SparkConf().setAppName("WordCount").setMaster("local")
    val sc = new SparkContext(conf)
    val textFile = sc.textFile(inputFile)
    val wordCount = textFile.flatMap(line => line.split(",")).map(word => (word, 1)).reduceByKey((a, b) => a + b)
    wordCount.foreach(println)
  }
}
