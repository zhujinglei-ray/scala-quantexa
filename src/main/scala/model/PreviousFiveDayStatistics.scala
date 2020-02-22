package model

case class PreviousFiveDayStatistics(transactionDay: Int,
                                     accountId: String,
                                     maxInPreviousFiveDays: Double,
                                     averageInPreviousFiveDays: Double,
                                     totalAATransactionValue:Double,
                                     totalCCTransactionValue:Double,
                                     totalFFTransactionValue:Double)
