package isometries

import model.Board

trait Transparent extends Isometry{


  abstract override def apply(board: Board,
                              highlightedFields: List[((Int, Int), Boolean)],
                              pivotField: (Int, Int),
                              reflection: Int):
  (Board, List[((Int, Int), Boolean)], (Int, Int), Int) = {
    super.apply(board, highlightedFields, pivotField, reflection)
  }
}
