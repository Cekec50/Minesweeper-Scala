package isometries

import model.Board

trait Isometry {
  def apply(board: Board): Board
  def inverse: Isometry

  def >>>(next: Isometry): Isometry = {
    def self = this
    new Isometry {
      override def apply(board: Board): Board =
        next.apply(self.apply(board))

      override def inverse: Isometry =
        next.inverse >>> self.inverse // The order is reversed
    }
  }

}


