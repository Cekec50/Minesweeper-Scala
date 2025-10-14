package isometries

import model.Board

trait Isometry {
  def apply(board: Board, highlightedFields: List[((Int, Int), Boolean)], pivotField: (Int, Int), reflection: Int):
  (Board, List[((Int, Int), Boolean)], (Int, Int), Int)
  def inverse: Isometry

  def >>>(next: Isometry): Isometry = {
    def self = this
    new Isometry {
      override def apply(board: Board, highlightedFields: List[((Int, Int), Boolean)], pivotField: (Int, Int), reflection: Int):
      (Board, List[((Int, Int), Boolean)], (Int, Int), Int)= {
        val (tempBoard, tempHighlightedFields, tempPivotField, tempReflection) = self.apply(board, highlightedFields, pivotField, reflection)
        next.apply(tempBoard, tempHighlightedFields, tempPivotField, tempReflection)
      }
      override def inverse: Isometry =
        next.inverse >>> self.inverse // The order is reversed

      override def add(x:Int): Int = ???

    }
  }
  def add(x:Int): Int

}


