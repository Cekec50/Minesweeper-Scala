package isometries

import model.Board

trait NonTransparent extends Isometry {
  abstract override def apply(board: Board,
                              highlightedFields: List[((Int, Int), Boolean)],
                              pivotField: (Int, Int),
                              reflection: Int):
  (Board, List[((Int, Int), Boolean)], (Int, Int), Int) = {


    val (transformedBoard, transformedFields, newPivot, refl) = super.apply(board, highlightedFields, pivotField, reflection)

    val (newBoard, newHighlightedFields) = addMines(board, transformedBoard, transformedFields)

    (newBoard, newHighlightedFields, newPivot, refl)
  }

  def addMines(
                oldBoard: Board,
                board: Board,
                highlightedFields: List[((Int, Int), Boolean)]
              ): (Board, List[((Int, Int), Boolean)]) = {

    var layout = board.getLayout(true)

    val updatedHighlighted = highlightedFields.map { case ((r, c), isMine) =>
      if (r >= 0 && r < oldBoard.rows && c >= 0 && c < oldBoard.cols) {
        val wasMine = oldBoard.fields(r)(c).getIsMine
        if (wasMine) {
          layout = layout.updated(r, layout(r).updated(c, "#"))
          ((r, c), true)
        } else ((r, c), isMine)
      } else {
        ((r, c), isMine)
      }
    }

    val newBoard = new Board(layout)
    (newBoard, updatedHighlighted)
  }
}
