package isometries
import model.Board

case class AxialReflection(pivot: (Int, Int) = (0, 0)) extends Isometry {
  // the argument type will be changed later
  override def apply(board: Board, highlightedFields: List[((Int, Int), Boolean)], pivotField: (Int, Int), reflection: Int):
  (Board, List[((Int, Int), Boolean)], (Int, Int), Int) = {
    val (x0, y0) = if (pivot == (0, 0)) pivotField else pivot

    val newHighlighedFields = highlightedFields.map { case ((x1, y1), value) =>
      var x2, y2: Int = 0
      reflection match {
        case 0 =>
          x2 = 2*x0 - x1
          y2 = y1
        case 1 =>
          x2 = x1
          y2 = 2*y0 - y1
        case 2 =>
          x2 = x0 + (y1 - y0)
          y2 = y0 + (x1 - x0)
        case 3 =>
          x2 = x0 - (y1 - y0)
          y2 = y0 - (x1 - x0)
      }
      ((x2, y2), value)
    }
    val highlightedMap = newHighlighedFields.toMap

    val newBoard = new Board(board.fields.zipWithIndex.map { case (row, i) =>
      row.zipWithIndex.map { case (elem, j) =>
        highlightedMap.get((i, j)) match {
          case Some(true)  => "#" // mine field
          case _           => "-" // normal field (false or not included)
        }
      }
    })
    (newBoard, newHighlighedFields, pivotField, reflection)
  }

  override def inverse: Isometry = this

}
