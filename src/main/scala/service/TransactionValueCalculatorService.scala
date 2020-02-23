package service

import model.{DailyStatistics, MutablePreviousFiveDayStatistics, PreviousFiveDayStatistics, Transaction}

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

  def getStatisticsForPreviousFiveDaysViaMapSolution(transactions: List[Transaction]): List[MutablePreviousFiveDayStatistics] = {

    var inMemoryMap = scala.collection.mutable.Map[(Int, String), MutablePreviousFiveDayStatistics]()

    transactions.foreach(transaction => {
      val startDay = transaction.transactionDay
      val accId = transaction.accountId
      val targetUpdateStatisticList = getTargetUpdateStatisticList(startDay, accId)
      targetUpdateStatisticList.foreach(
        compoundKey => {
          inMemoryMap.get(compoundKey) match {
            case Some(e) => inMemoryMap.update(compoundKey, updateMutableStatistics(transaction, inMemoryMap(compoundKey)))
            case None => inMemoryMap.put(compoundKey, addMutableStatistics(compoundKey._1, transaction))
          }
        }
      )
    })

    inMemoryMap.values.toList
          .filter(y => {
          y._transactionDay < 30 && y._transactionDay > 5
        }).sortBy(x => x._transactionDay)
  }


  private def getPreviousFiveDayResultByDayAndAcc(day: Int, acc: String,
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

  private def getDailyStatistics(transactions: List[Transaction]): Map[(Int, String), DailyStatistics] = {
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
      case numsOfEntry: Int if numsOfEntry != 0 => sumOfTransaction / transactionNum
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

  private def updateMutableStatistics(transaction: Transaction, targetTransaction: MutablePreviousFiveDayStatistics): MutablePreviousFiveDayStatistics = {
    var average = targetTransaction._averageInPreviousFiveDays + (transaction.transactionAmount / 5)
    val currentMax = targetTransaction.updateDayValueMap(transaction.transactionAmount,transaction.transactionDay).values.toList.max
    transaction.category match {
      case "AA" => targetTransaction.setTotalAATransactionValue(transaction.transactionAmount + targetTransaction._totalAATransactionValue)
      case "CC" => targetTransaction.setTotalCCTransactionValue(transaction.transactionAmount + targetTransaction._totalCCTransactionValue)
      case "FF" => targetTransaction.setTotalFFTransactionValue(transaction.transactionAmount + targetTransaction._totalFFTransactionValue)
      case "BB" => targetTransaction.setTotalBBTransactionValue(transaction.transactionAmount + targetTransaction._totalBBTransactionValue)
      case "DD" => targetTransaction.setTotalDDTransactionValue(transaction.transactionAmount + targetTransaction._totalDDTransactionValue)
      case "EE" => targetTransaction.setTotalEETransactionValue(transaction.transactionAmount + targetTransaction._totalEETransactionValue)
      case "GG" => targetTransaction.setTotalGGTransactionValue(transaction.transactionAmount + targetTransaction._totalGGTransactionValue)
    }
    targetTransaction.setAverageInPreviousFiveDays(average)
    targetTransaction.setMaxInPreviousFiveDays(currentMax)
    targetTransaction
  }

  private def addMutableStatistics(day: Int, transaction: Transaction): MutablePreviousFiveDayStatistics = {
    var statistics = new MutablePreviousFiveDayStatistics(day, transaction.accountId)

    statistics.setAverageInPreviousFiveDays(transaction.transactionAmount / 5)

    val currentMax = statistics.updateDayValueMap(transaction.transactionAmount,transaction.transactionDay).values.toList.max
    statistics.setMaxInPreviousFiveDays(currentMax)
    transaction.category match {
      case "AA" => statistics.setTotalAATransactionValue(transaction.transactionAmount)
      case "CC" => statistics.setTotalCCTransactionValue(transaction.transactionAmount)
      case "FF" => statistics.setTotalFFTransactionValue(transaction.transactionAmount)
      case "BB" => statistics.setTotalBBTransactionValue(transaction.transactionAmount)
      case "DD" => statistics.setTotalDDTransactionValue(transaction.transactionAmount)
      case "EE" => statistics.setTotalEETransactionValue(transaction.transactionAmount)
      case "GG" => statistics.setTotalGGTransactionValue(transaction.transactionAmount)
    }
    statistics
  }

  private def getTargetUpdateStatisticList(day: Int, accId: String): List[(Int, String)] = {
    val targetList = new ListBuffer[(Int, String)]
    for (i <- day + 1 to (day + 5)) {
      targetList.append((i, accId))
    }
    targetList.toList
  }
}
