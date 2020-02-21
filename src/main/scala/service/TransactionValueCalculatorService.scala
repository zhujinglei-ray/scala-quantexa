package service

import model.{AverageValueEachDay, DailyStatistics, Transaction}

class TransactionValueCalculatorService {

  def getTotalTransactionValueForEachDay(transactions: List[Transaction]): Map[Int, Double] = {
    transactions.groupBy(x => x.transactionDay).map {
      dailyRecord =>
        (dailyRecord._1, dailyRecord._2.foldLeft(0.0)(_ + _.transactionAmount))
    }
  }

  def getAverageTransactionPerAccountForEachTye(transactions: List[Transaction]):  Map[String, Map[String,Double]] = {
    transactions.groupBy(x => x.accountId).map {
      recordByAccount =>
        (recordByAccount._1, recordByAccount._2.groupBy(y => y.category).map {
          valueByCategory => (valueByCategory._1,getAverageValueForOneTypeTransaction(valueByCategory._2))
        })
    }
  }

  def getStatisticsForPreviousFiveDays(transactions: List[Transaction]): Unit = {
    transactions.groupBy(x=>x.transactionDay).map{
      recordByDay=>
        (recordByDay._1,convertToDailyStatistics(recordByDay._2))
    }
  }


  def getDailyStatistics(transactions: List[Transaction]): Unit ={

  }


  private def getAverageValueForOneTypeTransaction(typedTransactions: List[Transaction]): Double = {

    val transactionNum = typedTransactions.length

    val sumOfTransaction = typedTransactions.foldLeft(0.0)(_ + _.transactionAmount)

    val averageValue: PartialFunction[Int, Double] = {
      case numsOfEntry: Int if (numsOfEntry != 0) => sumOfTransaction / transactionNum
      case _ => 0.0
    }

    averageValue(transactionNum)
  }

  private def convertToDailyStatistics(typedTransactions: List[Transaction]): DailyStatistics ={
    val totalTransactionAmountInOneDay = typedTransactions.foldLeft(0.0)(_ + _.transactionAmount)
    val day = typedTransactions.head.transactionDay
    val accountId = typedTransactions.head.accountId
    var aATransactionValue =0.0
    var cCTransactionValue =0.0
    var fFTransactionValue =0.0

    typedTransactions.foreach{
      x => {
        if(x.category=="AA") aATransactionValue=x.transactionAmount
        if(x.category=="CC") cCTransactionValue=x.transactionAmount
        if(x.category=="FF") fFTransactionValue=x.transactionAmount
      }
    }
    DailyStatistics(day,accountId,totalTransactionAmountInOneDay,aATransactionValue,cCTransactionValue,fFTransactionValue)
  }
}
