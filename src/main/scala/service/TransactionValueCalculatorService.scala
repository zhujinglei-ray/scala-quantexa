package service

import model.{DailyStatistics, PreviousFiveDayStatistics, Transaction}

import scala.collection.mutable.ListBuffer

class TransactionValueCalculatorService {

  def getTotalTransactionValueForEachDay(transactions: List[Transaction]): Map[Int, Double] = {
    transactions.groupBy(x => x.transactionDay).map {
      dailyRecord =>
        (dailyRecord._1, dailyRecord._2.foldLeft(0.0)(_ + _.transactionAmount))
    }
  }

  def getAverageTransactionPerAccountForEachType(transactions: List[Transaction]): Map[String, Map[String, Double]] = {
    transactions.groupBy(x => x.accountId).map {
      recordByAccount =>
        (recordByAccount._1, recordByAccount._2.groupBy(y => y.category).map {
          valueByCategory => (valueByCategory._1, getAverageValueForOneTypeTransaction(valueByCategory._2))
        })
    }
  }

  def getStatisticsForPreviousFiveDays(transactions: List[Transaction]): List[PreviousFiveDayStatistics] = {

    val allDailyStatistics = getDailyStatistics(transactions)
    val accIdSet = allDailyStatistics.keySet.groupBy(k => k._2).keySet

    val resultList = new ListBuffer[PreviousFiveDayStatistics]
    for (day <- 6 to 29; idSet <- accIdSet) {
      resultList.append(getPreviousFiveDayResultByDayAndAcc(day, idSet, allDailyStatistics))
    }

    resultList.toList
  }

  def getPreviousFiveDayResultByDayAndAcc(day: Int, acc: String,
                                          dailyStatisticsMap: Map[(Int, String), DailyStatistics]): PreviousFiveDayStatistics = {
    var maxInPreviousFiveDays = 0.0
    var sumInPreviousFiveDays = 0.0
    var totalAATransactionValue = 0.0
    var totalCCTransactionValue = 0.0
    var totalFFTransactionValue = 0.0
    for (previousDay <- day - 5 until day) {
      if (dailyStatisticsMap.contains(previousDay, acc)) {
        val result = dailyStatisticsMap((previousDay, acc))
        maxInPreviousFiveDays = math.max(maxInPreviousFiveDays, result.totalTransactionAmountInOneDay)
        sumInPreviousFiveDays += result.totalTransactionAmountInOneDay
        totalAATransactionValue += result.aATransactionValue
        totalCCTransactionValue += result.cCTransactionValue
        totalFFTransactionValue += result.fFTransactionValue
      }
    }
    PreviousFiveDayStatistics(day, acc, maxInPreviousFiveDays, sumInPreviousFiveDays / 5, totalAATransactionValue, totalCCTransactionValue, totalFFTransactionValue)
  }

  def getDailyStatistics(transactions: List[Transaction]): Map[(Int, String), DailyStatistics] = {
    val resultMap = scala.collection.mutable.Map[(Int, String), DailyStatistics]()
    transactions.groupBy(x => x.transactionDay).map {
      recordByDay =>
        recordByDay._2.groupBy(y => y.accountId).map {
          recordByAcc =>
            resultMap += (recordByDay._1, recordByAcc._1) -> convertToDailyStatistics(recordByAcc._2)
        }
    }
    resultMap.toMap
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

  private def convertToDailyStatistics(transactionsById: List[Transaction]): DailyStatistics = {
    val transactionAmountInOneDay = transactionsById.foldLeft(0.0)(_ + _.transactionAmount)
    val day = transactionsById.head.transactionDay
    val accountId = transactionsById.head.accountId
    var aATransactionValue = 0.0
    var cCTransactionValue = 0.0
    var fFTransactionValue = 0.0

    transactionsById.foreach {
      x => {
        if (x.category == "AA") aATransactionValue = x.transactionAmount
        if (x.category == "CC") cCTransactionValue = x.transactionAmount
        if (x.category == "FF") fFTransactionValue = x.transactionAmount
      }
    }
    DailyStatistics(day, accountId, transactionAmountInOneDay, aATransactionValue, cCTransactionValue, fFTransactionValue)
  }


}
