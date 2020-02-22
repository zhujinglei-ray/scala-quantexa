package service

import scala.util.Try
import model.{AverageValueByCategoryPerAccount, PreviousFiveDayStatistics, Transaction, TransactionCategory}

class ResultPrinter {
  def printQ1Result(resultMap: Map[Int, Double]): Unit = {
    println()
    println("Calculating the total transaction value for all transactions for each day")
    println("------------------------------------------------------------------------")
    println("Day        Value")
    for (day <- 1 to 31) {
      val dailySum = resultMap.getOrElse(day, 0)
      println(day + "   " + dailySum)
    }
  }

  def printQ2Result(resultMap: Map[String, Map[String, Double]]): Unit = {
    println()
    println("Calculating the average value of transactions per account for each type of transaction")
    println("-------------------------------------------------------------------------------")
    println("AccountId,AA,BB,CC,DD,EE,FF,GG")
    resultMap.keySet.toList.sortBy(f => tryToInt(f.substring(1, f.length - 1))).foreach(
      accId => {
        val averageValue = constructAverageValueByCategoryPerAccount(accId, resultMap(accId))
        printf("%s,%s,%s,%s,%s,%s,%s,%s \n", averageValue.accountId,
          averageValue.categoryAaAvgValue, averageValue.categoryBbAvgValue,
          averageValue.categoryCcAvgValue, averageValue.categoryDdAvgValue,
          averageValue.categoryEeAvgValue, averageValue.categoryFfAvgValue,
          averageValue.categoryGgAvgValue)
      }
    )
  }

  def printQ3Result(resultMap:List[PreviousFiveDayStatistics]): Unit ={
    println()
    println("Calculating statistics for each account number for the previous five days of transactions")
    println("-------------------------------------------------------------------------------")
    println("Day,AccountId,Maximum,Average,AA Total Value,CC Total Value,FF Total Value")
    for(statistics<-resultMap){
      printf("%s,%s,%s,%s,%s,%s,%s \n",statistics.transactionDay,statistics.accountId,statistics.maxInPreviousFiveDays,statistics.averageInPreviousFiveDays,statistics.totalAATransactionValue,statistics.totalCCTransactionValue,statistics.totalFFTransactionValue)
    }
  }


  private def constructAverageValueByCategoryPerAccount(id: String, categoryAverageValueMap: Map[String, Double]): AverageValueByCategoryPerAccount = {
    val aAAvgValue = categoryAverageValueMap.getOrElse(TransactionCategory.AA.toString, 0.0)
    val bBAvgValue = categoryAverageValueMap.getOrElse(TransactionCategory.BB.toString, 0.0)
    val cCAvgValue = categoryAverageValueMap.getOrElse(TransactionCategory.CC.toString, 0.0)
    val dDAvgValue = categoryAverageValueMap.getOrElse(TransactionCategory.DD.toString, 0.0)
    val eEAvgValue = categoryAverageValueMap.getOrElse(TransactionCategory.EE.toString, 0.0)
    val fFAvgValue = categoryAverageValueMap.getOrElse(TransactionCategory.FF.toString, 0.0)
    val gGAvgValue = categoryAverageValueMap.getOrElse(TransactionCategory.GG.toString, 0.0)
    AverageValueByCategoryPerAccount(id, aAAvgValue, bBAvgValue, cCAvgValue, dDAvgValue, eEAvgValue, fFAvgValue, gGAvgValue)
  }

  def getAllCategoryType(transactions: List[Transaction]): List[String] = {
    val category = transactions.groupBy(transaction => transaction.category).keys
    category.toList.sorted
  }

  def tryToInt(s: String) = Try(s.toInt).toOption

}
