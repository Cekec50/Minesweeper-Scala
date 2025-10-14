import controller.{FileController, LevelCreatorController}
import model.Board
import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite

import java.io.File
import scala.io.Source

class LevelCreatorTest extends AnyFunSuite with BeforeAndAfter{
  var levelCreatorController: LevelCreatorController = _
  var board: Board = _
  var board2: Board = _

  before{
    levelCreatorController = new LevelCreatorController()
    val file = new File("src/test/resources/test-level-2.txt")
    val (boardTmp, _ , _) = FileController.loadLevelFromFile(file)
    board = boardTmp
    val file2 = new File("src/test/resources/test-level-3.txt")
    var (boardTmp2, _ , _) = FileController.loadLevelFromFile(file2)
    board2 = boardTmp2
  }

  // --- Basic Map Editing ---
  test("Add row/column") {
    val rows = board.rows
    val cols = board.cols

    board = levelCreatorController.addRow(board)
    assert(board.rows == rows + 1)
    assert(board.cols == cols)

    board = levelCreatorController.addColumn(board)
    assert(board.rows == rows + 1)
    assert(board.cols == cols + 1)
  }
  test("Remove row/column") {
    val rows = board.rows
    val cols = board.cols

    board = levelCreatorController.deleteRow(board)
    assert(board.rows == rows - 1)
    assert(board.cols == cols)

    board = levelCreatorController.deleteColumn(board)
    assert(board.rows == rows - 1)
    assert(board.cols == cols - 1)

  }
  test("Replace field type") {
    val fieldWasMine = board.fields(0)(0).getIsMine
    board = levelCreatorController.toggleMines(board, List(((0,0), true)))

    assert(board.fields(0)(0).getIsMine != fieldWasMine)
  }
  test("Clear rectangular region") {
    assert(board.fields(1)(1).getIsMine)
    assert(board.fields(1)(2).getIsMine)
    assert(board.fields(2)(1).getIsMine)

    val selectedFields = List(((1,1), true), ((1,2), true), ((2,1), true), ((2,2), true) )
    board = levelCreatorController.clearArea(board, selectedFields)

    assert(!board.fields(1)(1).getIsMine)
    assert(!board.fields(1)(2).getIsMine)
    assert(!board.fields(2)(1).getIsMine)
  }

  test("Saving a valid level creates correct file content") {
    board2 = levelCreatorController.toggleMines(board2, List(((0,0), true), ((0,1), true), ((1,0), true), ((1,1), true) ))

    val file: File = FileController.saveLevelToFile(board2, "beginner", "test_level_save.txt")
    assert(file.exists(), "Level file should exist")

    val lines = scala.io.Source.fromFile(file).getLines().toList
    assert(lines.length == board2.rows)

    // Check that mines are correctly placed
    assert(lines(0)(0) == '#')
    assert(lines(0)(1) == '#')
    assert(lines(1)(0) == '#')
    assert(lines(1)(1) == '#')

    val boardLines = lines.take(lines.length - 2)

    // Check that each board line only contains valid symbols
    boardLines.foreach { line =>
      assert(line.matches("[#\\-]+"), s"Invalid board encoding in line: $line")
    }

    file.delete()
  }

}