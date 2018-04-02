package com.ratelware.science.slr.ui.quantities

object FileSizeFormatter {
  val orders = Map(
    0 -> "",
    1 -> "kilo",
    2 -> "mega",
    3 -> "giga",
    4 -> "tera",
    5 -> "peta",
    6 -> "exa"
  )


  def format(sizeInBytes: Double): String = {
    val order = getOrder(0, sizeInBytes)
    f"${order._2}%4.2f ${orders(order._1)}bytes"
  }

  def getOrder(currentOrder: Int, remainingSize: Double): (Int, Double) = {
    if(remainingSize > 1024) getOrder(currentOrder + 1, remainingSize / 1024)
    else (currentOrder, remainingSize)
  }
}
