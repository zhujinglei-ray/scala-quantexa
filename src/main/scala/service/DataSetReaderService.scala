package service

import model.Transaction

import scala.io.Source

class DataSetReaderService(val filePath: String) {

  def getListOfTransactions(): List[Transaction] = {
    println("Resource is being imported from " + filePath)
    val transactions: List[Transaction] = getTransactionsLines.map {
      transactionsLines =>
        val split = transactionsLines.split(',')
        Transaction(split(0), split(1), split(2).toInt, split(3), split(4).toDouble)
    }.toList

    closeResources()

    transactions
  }

  private def getTransactionsLines: Iterator[String] = {
    Source.fromFile(filePath).getLines().drop(1)
  }

  private def closeResources(): Unit = {
    Source.fromFile(filePath).close()
    println("Resource have been close")
  }
}
