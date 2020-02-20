package model

case class DailyStatistics(transactionDay: Int,
                           accountId: String,
                           totalTransactionAmountInOneDay: Double,
                           aATransactionValue:Double,
                           cCTransactionValue:Double,
                           fFTransactionValue:Double
                           )
