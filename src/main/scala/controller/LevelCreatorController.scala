package controller

import model.Board
import model.Field

object LevelCreatorController {


  def addRow(board: Board): Board = {
      new Board(board.getLayout(true) :+ List.fill(board.cols)("-"))


  }
  def addColumn(board: Board): Board = {
    new Board(board.getLayout(true).zip(List.fill(board.rows)("-")).map { case (row, v) => row :+ v })

  }
  def deleteRow(board: Board): Board = {
    if (board.rows > 1)
      new Board(board.getLayout(true).dropRight(1))
    else board
  }
  def deleteColumn(board: Board): Board = {
    if (board.cols > 1)
      new Board(board.getLayout(true).map(_.dropRight(1)))
    else board
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

//  def setMinesElseClear(board: Board, highlightedFields: List[((Int, Int), Boolean)]): Board = {
//    val highlightedMap = highlightedFields.toMap // (i, j) -> Boolean for O(1) lookup
//
//    new Board(board.fields.zipWithIndex.map { case (row, i) =>
//      row.zipWithIndex.map { case (elem, j) =>
//        highlightedMap.get((i, j)) match {
//          case Some(true)  => "#" // mine field
//          case _           => "-" // normal field (false or not included)
//        }
//      }
//    })
//  }

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

  def updateBoardWithHighlightedFields(board: Board, highlightedFields: List[((Int, Int), Boolean)]): Board = {
    val highlightedMap = highlightedFields.toMap

    new Board(board.fields.zipWithIndex.map { case (row, i) =>
      row.zipWithIndex.map { case (elem, j) =>
        highlightedMap.get((i, j)) match {
          case Some(true)  => "#" // mine field
          case _           => "-" // normal field
        }
      }
    })
  }
}
