package isometries

import model.Board

trait NonExpandable extends Isometry {
  abstract override def apply(board: Board,
                              highlightedFields: List[((Int, Int), Boolean)],
                              pivotField: (Int, Int),
                              reflection: Int):
  (Board, List[((Int, Int), Boolean)], (Int, Int), Int) = {


    val (transformedBoard, transformedFields, newPivot, refl) = super.apply(board, highlightedFields, pivotField, reflection)

    val (expandedBoard, shiftedFields) = dropFields(transformedBoard, transformedFields)

    (expandedBoard, shiftedFields, newPivot, refl)
  }
  def dropFields(board: Board, highlightedFields: List[((Int, Int), Boolean)]):
  (Board, List[((Int, Int), Boolean)]) = {

    (board,  highlightedFields.filter{case ((x, y), _) => x < board.rows && x >= 0 && y < board.cols && y >= 0  } )

  }

}
