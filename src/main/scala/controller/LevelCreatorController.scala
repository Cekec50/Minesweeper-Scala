package controller

import model.Board

class LevelCreatorController {

  def addRow(board: Board): Board = {
    new Board(List.fill(board.rows + 1, board.cols)("-"))
  }
  def addColumn(board: Board): Board = {
    new Board(List.fill(board.rows, board.cols + 1)("-"))
  }
  def deleteRow(board: Board): Board = {
    new Board(List.fill(if (board.rows > 1) board.rows - 1 else 1 , board.cols)("-"))
  }
  def deleteColumn(board: Board): Board = {
    new Board(List.fill(board.rows, if (board.cols > 1) board.cols - 1 else 1)("-"))
  }

}
