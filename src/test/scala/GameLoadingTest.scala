import controller.FileController
import model.Board
import org.scalatest.funsuite.AnyFunSuite

import java.io.File
import scala.io.Source



class GameLoadingTest extends AnyFunSuite {


  // --- Level Loading ---
  test("Load correctly formatted level file") {
    val file = new File("src/test/resources/test-level-1.txt")

    val (board: Board, seconds: Int, moves: Int) = FileController.loadLevelFromFile(file)

    val allFields = board.fields.flatten
    assert(allFields.forall(f => f.isInstanceOf[model.Field]))

    assert(board.rows == 5)
    assert(board.cols == 5)
  }
  test("All fields correctly recognized as mines or safe") {
    val file = new File("src/test/resources/test-level-1.txt")

    val (board: Board, seconds: Int, moves: Int) = FileController.loadLevelFromFile(file)

    assert(board.fields(0)(0).getIsMine)
    assert(board.fields(4)(0).getIsMine)
    assert(board.fields(4)(4).getIsMine)
    assert(board.fields(0)(4).getIsMine)
    assert(board.fields(2)(2).getIsMine)

    assert(!board.fields(1)(0).getIsMine)
    assert(!board.fields(1)(1).getIsMine)
    assert(!board.fields(1)(2).getIsMine)
    assert(!board.fields(1)(3).getIsMine)
    assert(!board.fields(1)(4).getIsMine)
  }
  test("Load empty level file") {
    val badFile = new File("src/test/resources/test-empty.txt")

    intercept[Exception] {
      FileController.loadLevelFromFile(badFile)
    }
  }
  test("Load level file with unknow symbol") {
    val badFile = new File("src/test/resources/test-level-unknown.txt")

    intercept[Exception] {
      FileController.loadLevelFromFile(badFile)
    }
  }

  // --- Save Game ---
  test("Saving game produces valid save file") {
    // 🧱 Create a small dummy board
    val file = new File("src/test/resources/test-level-1.txt")
    val (board: Board, seconds: Int, moves: Int) = FileController.loadLevelFromFile(file)
    board.fields(0)(0).flagField(true)
    board.fields(1)(1).flagField(true)

    val tmpFile = File.createTempFile("test_save", ".txt")
    tmpFile.deleteOnExit()

    FileController.saveGameToFile(tmpFile, board, moves = 5, seconds = 120)

    val lines = Source.fromFile(tmpFile).getLines().toList

    val boardLines = lines.take(lines.length - 2)

    // Check that each board line only contains valid symbols
    boardLines.foreach { line =>
      assert(line.matches("[#\\-\\+\\?!]+"), s"Invalid board encoding in line: $line")
    }

    assert(lines.exists(_.startsWith("m:")), "Missing moves line")
    assert(lines.exists(_.startsWith("s:")), "Missing seconds line")
  }
  test("Loading saved game restores field states") {
    val file = new File("src/test/resources/test-saved-level.txt")

    val (board, seconds, moves) = FileController.loadLevelFromFile(file)

    assert(moves == 12, s"Expected 12 moves, got $moves")
    assert(seconds == 10, s"Expected 10 seconds, got $seconds")

    assert(board.fields.length == 10)
    assert(board.fields.head.length == 10)

    val row0 = board.fields(0)
    (0 until 5).foreach { c =>
      assert(row0(c).getIsMine, s"Row 0, Col $c should be mine")
      assert(row0(c).getIsFlagged, s"Row 0, Col $c should be flagged")
    }
    (5 until 10).foreach { c =>
      assert(row0(c).getIsMine, s"Row 0, Col $c should be mine")
      assert(!row0(c).getIsFlagged, s"Row 0, Col $c should NOT be flagged")
    }

    val row9 = board.fields(9)
    assert(row9(0).getIsFlagged && !row9(0).getIsMine, "Row 9, Col 0 should be flagged non-mine ('?')")
    assert(row9(1).getIsMine && row9(1).getIsFlagged, "Row 9, Col 1 should be flagged mine ('!')")

    val plusFields = for {
      r <- 0 until board.fields.length
      c <- 0 until board.fields(r).length
      f = board.fields(r)(c)
      if !f.enabled
    } yield f
    assert(plusFields.nonEmpty, "Expected some revealed ('+') fields")
  }
  test("Invalid or truncated save file throws error") {
    val badFile = new File("src/test/resources/test-saved-level-unknown.txt")

    intercept[Exception] {
      FileController.loadLevelFromFile(badFile)
    }
  }

}
