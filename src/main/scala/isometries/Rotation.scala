package isometries

import controller.LevelCreatorController
import model.Board

case class Rotation(clockwise:Boolean = true) extends Isometry  {
  override def apply(
                      board: Board,
                      highlightedFields: List[((Int, Int), Boolean)],
                      pivotField: (Int, Int),
                      reflection: Int
                    ): (Board, List[((Int, Int), Boolean)], (Int, Int), Int) = {

    val (x0, y0) = pivotField

    val newHighlightedFields = highlightedFields.map { case ((x1, y1), value) =>
      val (x2, y2) =
        if (clockwise)
          (x0 + (y1 - y0), y0 - (x1 - x0))
        else
          (x0 - (y1 - y0), y0 + (x1 - x0))
      ((x2, y2), value)
    }


    val newBoard = LevelCreatorController.updateBoardWithHighlightedFields(board, newHighlightedFields)

    (newBoard, newHighlightedFields, pivotField, reflection)
  }

  override def inverse: Isometry = Rotation(!clockwise)

}


