package model

class MutablePreviousFiveDayStatistics(transactionDay: Int,
                                       accountId: String) {
  val _transactionDay = this.transactionDay
  val _accountId = this.accountId
  var _maxInPreviousFiveDays = 0.0
  var _averageInPreviousFiveDays = 0.0
  var _totalAATransactionValue = 0.0
  var _totalCCTransactionValue = 0.0
  var _totalFFTransactionValue = 0.0

  var _totalBBTransactionValue = 0.0
  var _totalDDTransactionValue = 0.0
  var _totalEETransactionValue = 0.0
  var _totalGGTransactionValue = 0.0

  def setMaxInPreviousFiveDays(value: Double) = this._maxInPreviousFiveDays = value

  def setAverageInPreviousFiveDays(value: Double) = this._averageInPreviousFiveDays = value

  def setTotalAATransactionValue(value: Double) = this._totalAATransactionValue = value

  def setTotalCCTransactionValue(value: Double) = this._totalCCTransactionValue = value

  def setTotalFFTransactionValue(value: Double) = this._totalFFTransactionValue = value

  def setTotalBBTransactionValue(value: Double) = this._totalBBTransactionValue = value

  def setTotalDDTransactionValue(value: Double) = this._totalDDTransactionValue = value

  def setTotalEETransactionValue(value: Double) = this._totalEETransactionValue = value

  def setTotalGGTransactionValue(value: Double) = this._totalGGTransactionValue = value
}
