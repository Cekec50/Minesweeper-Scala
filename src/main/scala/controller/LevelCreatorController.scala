package controller

import model.Board
import model.Field

class LevelCreatorController {

  def mapBooleanToString(board: Board): List[List[String]] = {
    board.fields.map(_.map(field => if (field.getIsMine) "#" else "-"))
  }
  def addRow(board: Board): Board = {
    new Board(mapBooleanToString(board) :+ List.fill(board.cols)("-"))

  }
  def addColumn(board: Board): Board = {
    new Board(mapBooleanToString(board).zip(List.fill(board.rows)("-")).map { case (row, v) => row :+ v })

  }
  def deleteRow(board: Board): Board = {
    new Board(mapBooleanToString(board).dropRight(1))
  }
  def deleteColumn(board: Board): Board = {
    new Board(mapBooleanToString(board).map(_.dropRight(1)))
  }

  def selectField(board: Board, fieldSelected: Field): Unit = {
    board.fields.flatten.foreach(field => field.enabled = true)
    fieldSelected.revealField
  }

  def toggleMines(board: Board, highlightedFields: List[((Int, Int), Boolean)]): Board = {
    new Board(board.fields.zipWithIndex.map { case (row, i) =>
      row.zipWithIndex.map { case (elem, j) =>
        if (highlightedFields.map(_._1).contains((i, j))) {
          if (elem.getIsMine) "-"
          else "#"
        }
        else{
          if (elem.getIsMine) "#"
          else "-"
        }
      }
    })
  }

  def setMinesElseClear(board: Board, highlightedFields: List[((Int, Int), Boolean)]): Board = {
    val highlightedMap = highlightedFields.toMap // (i, j) -> Boolean for O(1) lookup

    new Board(board.fields.zipWithIndex.map { case (row, i) =>
      row.zipWithIndex.map { case (elem, j) =>
        highlightedMap.get((i, j)) match {
          case Some(true)  => "#" // mine field
          case _           => "-" // normal field (false or not included)
        }
      }
    })
  }

  def clearArea(board: Board, highlightedFields: List[((Int, Int), Boolean)]): Board = {
    new Board(board.fields.zipWithIndex.map { case (row, i) =>
      row.zipWithIndex.map { case (elem, j) =>
        if (highlightedFields.map(_._1).contains((i, j))) {
           "-"
        }
        else{
          if (elem.getIsMine) "#"
          else "-"
        }
      }
    })
  }

}
