package isometries

import model.Board

object IsometryObj {

  def translate: Isometry = AxialReflection() >>> AxialReflection()

  def centralSymmetry:  Isometry = Rotation((0, 0)) >>> Rotation((0, 0)) >>> AxialReflection()

  def combination = translate >>> centralSymmetry

  def board:Board = new Board(List.fill(board.rows + 1, board.cols)("-"))
  def newBoard : Board = combination(board)
}
