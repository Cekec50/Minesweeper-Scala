import controller.FileController
import org.scalatest.funsuite.AnyFunSuite

class HighscoreTest extends AnyFunSuite{

  // --- Scoring System ---

  test("Saving a new high score persists it correctly") {
    FileController.saveScore("TestName", 100, "src/test/resources/test-highscores.txt")

    val scores = FileController.loadScores("src/test/resources/test-highscores.txt")

    assert(scores.contains(("TestName", 100)))
  }

  test("Empty score file returns empty list") {
    val scores = FileController.loadScores("src/test/resources/test-empty.txt")

    assert(scores == List())
  }

  test("Invalid score file (corrupted format) throws or returns empty safely") {
    val scores = FileController.loadScores("src/test/resources/test-moves.txt")

    assert(scores == List())
  }

}
