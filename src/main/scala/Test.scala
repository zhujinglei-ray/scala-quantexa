import model.Transaction
import service.{DataSetReaderService, ResultPrinter, TransactionValueCalculatorService}

object Test extends App {
  val inputPutFilePath ="src/transactions.txt"
  val dataReader = new DataSetReaderService(inputPutFilePath)
  val transactionValueCalculator = new TransactionValueCalculatorService
  val resultPrinter = new ResultPrinter

  val inputTransactionsList = dataReader.getListOfTransactions()
//  val resultMapOfQ1 = transactionValueCalculator.getTotalTransactionValueForEachDay(inputTransactionsList)
//  val resultMapOfQ2 = transactionValueCalculator.getAverageTransactionPerAccountForEachType(inputTransactionsList)

  //Question 1: Calculate the total transaction value for all transactions for each day
//  resultPrinter.printQ1Result(resultMapOfQ1)

  //Question 2: Calculate the average value of transactions per account for each type of transaction
//  resultPrinter.printQ2Result(resultMapOfQ2)
  //Question 3: Calculate statistics for each account number for the previous five days of transactions
  println(transactionValueCalculator.getStatisticsForPreviousFiveDays(inputTransactionsList))
}
