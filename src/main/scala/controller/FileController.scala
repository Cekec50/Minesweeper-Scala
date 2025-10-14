package controller



import model.Board
import scala.io.Source
import java.io.{File, PrintWriter}
import scala.swing.{Component, Dialog, FileChooser, Window}

object FileController {

  def saveGameToFile(file: File, board: Board, moves: Int, seconds: Int): Unit = {
    val writer = new PrintWriter(file)
    try {
      for (row <- board.fields) {
        val line = row.map { field =>
          (field.enabled, field.getIsMine, field.getIsFlagged) match {
            case (true, true, true)  => '!'
            case (true, true, false) => '#'
            case (true, false, true) => '?'
            case (true, false, false)=> '-'
            case (false, _, _)       => '+'
          }
        }.mkString
        writer.println(line)
      }
      writer.println(s"m:$moves")
      writer.println(s"s:$seconds")
    } finally writer.close()
  }
  def saveGame(parent: Window, board: Board, moves: Int, seconds: Int): Unit = {
    val chooser = new FileChooser(new java.io.File("."))
    chooser.title = "Save Game"
    if (chooser.showSaveDialog(parent) == FileChooser.Result.Approve) {
      saveGameToFile(chooser.selectedFile, board, moves, seconds)
      Dialog.showMessage(parent, "Game saved successfully!", "Save")
    }
  }
  def loadFile(parent: Window): File = {
    val chooser = new FileChooser(new java.io.File("."))
    chooser.title = "Select a Level File or Saved Game"

    if (chooser.showOpenDialog(parent) == FileChooser.Result.Approve) {
      chooser.selectedFile
    } else {
      throw new IllegalStateException("No file selected")
    }
  }

  def loadLevelFromFile(file: File): (Board, Int, Int) = {
    val allLines = Source.fromFile(file).getLines().toVector
    if (allLines.isEmpty)
      throw new IllegalArgumentException("Level file is empty")

    var moves = 0
    var seconds = 0
    var lines = allLines

    // Check if last two lines contain metadata
    if (allLines.length >= 2 &&
      allLines(allLines.length - 2).startsWith("m:") &&
      allLines(allLines.length - 1).startsWith("s:")) {

      moves = allLines(allLines.length - 2).stripPrefix("m:").toInt
      seconds = allLines(allLines.length - 1).stripPrefix("s:").toInt
      lines = allLines.dropRight(2) // remove last 2 lines
    }

    val rows = lines.length
    val cols = lines.head.length

    // Validate all rows same length
    if (!lines.forall(_.length == cols))
      throw new IllegalArgumentException("Level file is not rectangular")

    // Convert lines into List[List[String]]
    val layout: List[List[String]] = lines.map { line =>
      line.map {
        case '#' => "#"   // mine
        case '-' => "-"   // normal field
        case '!' => "!"   // mine and flagged
        case '?' => "?"   // normal field flagged
        case '+' => "+"   // clicked field
        case other => throw new IllegalArgumentException(
          s"Invalid character '$other' in level file"
        )
      }.toList.map(_.toString)
    }.toList

    (new Board(layout), seconds, moves)
  }
  def loadScores(filename: String): List[(String, Int)] = {
    val file = new File(filename)
    if (!file.exists()) return List()
    Source.fromFile(file).getLines().flatMap { line =>
      line.split(",") match {
        case Array(name, scoreStr) =>
          try Some((name, scoreStr.toInt))
          catch {
            case _: Exception => None
          }
        case _ => None
      }
    }.toList.sortBy(-_._2) // descending
  }

  /** Save a new score */
  def saveScore(name: String, score: Int, filename: String): Unit = {
    val file = new java.io.File(filename)
    val writer = new java.io.FileWriter(file, true) // `true` for append mode
    writer.write(s"$name,$score\n")
    writer.close()
  }

  def saveLevelToFile(board: Board, difficulty: String, filename: String): File = {
    val dir = new java.io.File(s"levels/$difficulty")
    if (!dir.exists()) dir.mkdirs()

    val file = new java.io.File(dir, s"$filename.txt")
    val writer = new java.io.PrintWriter(file)

    try {
      for (row <- board.fields) {
        val line = row.map(f => if (f.getIsMine) "#" else "-").mkString
        writer.println(line)
      }
    } finally {
      writer.close()
    }
    file
  }

  def saveLevel(parent: Window, board: Board, difficulty: String): Unit = {
    try {
      val filenameOpt = Dialog.showInput(
        parent,
        message = "Enter level name",
        initial = "Level name",
        title = "Save Level"
      )

      filenameOpt.foreach { filename =>
        saveLevelToFile(board, difficulty, filename)
        Dialog.showMessage(parent, "Level saved successfully!", "Save")
      }
    } catch {
      case _: Exception => println("Saved Level Cancelled")
    }
  }




}
