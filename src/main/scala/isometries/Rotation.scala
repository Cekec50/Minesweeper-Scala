package isometries

import model.Board

case class Rotation(center: (Int, Int), clockwise:Boolean = true) extends Isometry  {
  override def apply(board: Board): Board = {
    // TODO: board transformation for rotation
    board
  }
  override def inverse: Isometry = Rotation(center, !clockwise)
}


