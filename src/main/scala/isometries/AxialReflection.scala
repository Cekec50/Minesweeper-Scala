package isometries
import model.Board

case class AxialReflection(someArgument: Any) extends Isometry {
  // the argument type will be changed later
  override def apply(board: Board): Board = {
    // TODO: board transformation for axial reflection
    board
  }

  override def inverse: Isometry = this
}
