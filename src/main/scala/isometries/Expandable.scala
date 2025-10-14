package isometries

import model.Board

trait Expandable {

  def extendBoard(board: Board, highlightedFields: List[((Int, Int), Boolean)]):
        Board = {

    println("Expanding trait used!")

    // Compute max coordinates from highlighted fields
    val maxRow = highlightedFields.map(_._1._1).maxOption.getOrElse(board.rows - 1)
    val maxCol = highlightedFields.map(_._1._2).maxOption.getOrElse(board.cols - 1)

    // Compute how many rows / columns to add
    val rowsToAdd = (maxRow + 1 - board.rows).max(0)
    val colsToAdd = (maxCol + 1 - board.cols).max(0)

    // Add rows
    val boardWithRows = (0 until rowsToAdd).foldLeft(board) { (b, _) =>
      new Board(board.fields.map(_.map(field => if (field.getIsMine) "#" else "-")) :+ List.fill(board.cols)("-"))
    }
    // Add columns
    val extendedBoard = (0 until colsToAdd).foldLeft(boardWithRows) { (b, _) =>
      new Board(board.fields.map(_.map(field => if (field.getIsMine) "#" else "-")).zip(List.fill(board.rows)("-")).map { case (row, v) => row :+ v })
    }
    extendedBoard

  }

}
