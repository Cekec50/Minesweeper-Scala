package isometries

import model.Board

trait Transparent /*extends Isometry*/{

/*
  abstract override def apply(board: Board): Board = {
    def newBoard = super.apply(board)
    println("Transparent trait used!")
    newBoard
  }
  abstract override def add(x: Int): Int = {
    def newX = super.add(x)
    def newNewX = newX + 100
    println("Transparent: " + newNewX)
    newNewX
  }*/
}
