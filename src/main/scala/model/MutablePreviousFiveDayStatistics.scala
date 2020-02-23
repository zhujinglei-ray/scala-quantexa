package model

import scala.collection.mutable

class MutablePreviousFiveDayStatistics(transactionDay: Int,
                                       accountId: String) {
  val _transactionDay: Int = this.transactionDay
  val _accountId: String = this.accountId
  var _dailyTotalMapInPrevious5Days: mutable.Map[Int, Double] = scala.collection.mutable.Map[Int, Double]()

  var _dailyTotal = 0.0
  var _averageInPreviousFiveDays = 0.0
  var _totalAATransactionValue = 0.0
  var _totalCCTransactionValue = 0.0
  var _totalFFTransactionValue = 0.0

  var _totalBBTransactionValue = 0.0
  var _totalDDTransactionValue = 0.0
  var _totalEETransactionValue = 0.0
  var _totalGGTransactionValue = 0.0

  def setMaxInPreviousFiveDays(value: Double) = this._dailyTotal = value

  def setAverageInPreviousFiveDays(value: Double) = this._averageInPreviousFiveDays = value

  def setTotalAATransactionValue(value: Double) = this._totalAATransactionValue = value

  def setTotalCCTransactionValue(value: Double) = this._totalCCTransactionValue = value

  def setTotalFFTransactionValue(value: Double) = this._totalFFTransactionValue = value

  def setTotalBBTransactionValue(value: Double) = this._totalBBTransactionValue = value

  def setTotalDDTransactionValue(value: Double) = this._totalDDTransactionValue = value

  def setTotalEETransactionValue(value: Double) = this._totalEETransactionValue = value

  def setTotalGGTransactionValue(value: Double) = this._totalGGTransactionValue = value

  def updateDayValueMap(value: Double, transactionDay: Int): Map[Int, Double] = {
    this._dailyTotalMapInPrevious5Days.get(transactionDay) match {
      case Some(e) => {
        this._dailyTotalMapInPrevious5Days.update(transactionDay, value + this._dailyTotalMapInPrevious5Days(transactionDay))
      }
      case None => this._dailyTotalMapInPrevious5Days.put(transactionDay, value)
    }
    this._dailyTotalMapInPrevious5Days.toMap
  }
}
