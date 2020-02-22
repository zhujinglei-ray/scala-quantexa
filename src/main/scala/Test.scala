import model.Transaction
import service.{DataSetReaderService, TransactionValueCalculatorService}

object Test extends App {
  val inputPutFilePath ="src/transactions.txt"
  val dataReader = new DataSetReaderService(inputPutFilePath)
  val transactionValueCalculator = new TransactionValueCalculatorService
  val inputTransactionsList = dataReader.getListOfTransactions()
//  val result1 = transactionValueCalculator.getTotalTransactionValueForEachDay(inputTransactionsList)
//  println(result1)
//  val result2 = transactionValueCalculator.getAverageTransactionPerAccountForEachTye(inputTransactionsList)
//  println(result2)

  val result = transactionValueCalculator.getTotalTransactionValueForEachDayList(inputTransactionsList)
  val result1 = result.toList.sortBy(_.day)
  println(result1)
}
