package service

class ResultPrinter {
  def printQ1Result(resultMap:Map[Int, Double]): Unit ={
    println("Calculating the total transaction value for all transactions for each day")
    println("------------------------------------------------------------------------")
    println("Day        Value")
    for(day <-1 to 31){
      val dailySum = resultMap.getOrElse(day,0)
      println(day + "   " + dailySum)
    }
  }
}
