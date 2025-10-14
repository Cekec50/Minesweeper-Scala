package isometries

import model.Board

case class Rotation(clockwise:Boolean = true) extends Isometry  {
  override def apply(
                      board: Board,
                      highlightedFields: List[((Int, Int), Boolean)],
                      pivotField: (Int, Int),
                      reflection: Int
                    ): (Board, List[((Int, Int), Boolean)], (Int, Int), Int) = {

    val (x0, y0) = pivotField

    // Rotate highlighted fields
    val newHighlightedFields = highlightedFields.map { case ((x1, y1), value) =>
      val (x2, y2) = if (clockwise) {
        (x0 + (y1 - y0), y0 - (x1 - x0))
      } else {
        (x0 - (y1 - y0), y0 + (x1 - x0))
      }
      ((x2, y2), value)
    }

    // If mixed with Expandable, expand the board
    val expandedBoard = this match {
      case e: Expandable => e.extendBoard(board, newHighlightedFields)
      case _ => board
    }

    // Build highlighted map for easier lookup
    val highlightedMap = newHighlightedFields.toMap

    // Construct new board
    val newBoard = new Board(expandedBoard.fields.zipWithIndex.map { case (row, i) =>
      row.zipWithIndex.map { case (elem, j) =>
        highlightedMap.get((i, j)) match {
          case Some(true) => "#"  // mine
          case _ => "-"          // normal
        }
      }
    })
    (newBoard, newHighlightedFields, pivotField, reflection)
  }

  override def inverse: Isometry = Rotation(!clockwise)

  override def add(x: Int): Int = {
    def newX = x + 1
    println("Rotation: " + newX)
    newX
  }
}


