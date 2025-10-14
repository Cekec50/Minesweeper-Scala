package controller

import model.Board
import model.Field
import ui.GamePanel

import java.io.File
import scala.io.Source
import scala.swing.{Dialog, Frame, Window}

class GameController(
        gameWon: () => Unit,
        gameLost: () => Unit,
        makeMove: () => Unit
                    ) {
  private var gameOver = false

  def getGameState = gameOver

  def changeGameState(): Unit = {
    gameOver = true
  }
  def playMove(click: String, board: Board, field: Field): Unit = {
    if (!gameOver){
      click match {
        case "left-click" =>
          board.openField(field) match {
            case Some(true) => // opened a mine
              gameLost()
              gameOver = true
            case Some(false) =>
              makeMove()
              checkGameWon(board)
            case None => () // flagged or already revealed → do nothing
          }
        case "right-click" =>
          field.flagField() match {
            case Some(true) => makeMove()
            case None => ()
          }
      }
    }

  }

  def suggestMove(board: Board): Field = {
    val unopenedSafe = scala.util.Random.shuffle(board.fields.flatten).find(f => !f.getIsMine && f.enabled)
    unopenedSafe.foreach(f => f.flashField())
    unopenedSafe.get
  }

  def checkGameWon(board: Board) = {
    val gameWonBool = board.fields.flatten.forall(field => field.getIsMine || !field.enabled)
    if(gameWonBool) {
      gameWon()
      gameOver = true
    }
  }

  def calculatePoints(seconds: Int, moves: Int): Int = {
    1000 - seconds - moves
  }
  def playMovesFromFile(frame: Frame, board: Board): Unit = {
    try {
      val file = FileController.loadFile(frame)
      playMovesFromGivenFile(file, board)
    } catch {
      case e: IllegalStateException => println(e.getMessage)
      case e: IllegalArgumentException => println(e.getMessage)
    }
  }
  def playMovesFromGivenFile(file: File,  board: Board): Unit = {

      val lines = Source.fromFile(file).getLines()

      for (line <- lines) {
        val move = line.trim
        if (move.nonEmpty) {
          move.head match {
            case 'L' =>
              val (x, y) = parseCoordinates(move)
              if(x < board.rows && y < board.cols && x >= 0 && y >= 0)
                playMove("left-click", board, board.fields(x)(y))
            case 'D' =>
              val (x, y) = parseCoordinates(move)
              if(x < board.rows && y < board.cols && x >= 0 && y >= 0)
                playMove("right-click", board, board.fields(x)(y))
            case _ =>
              throw new IllegalArgumentException(s"Invalid move format: $move")
          }
        }
      }
  }

  private def parseCoordinates(move: String): (Int, Int) = {

    val pattern = """[LD]\((\d+),(\d+)\)""".r
    move match {
      case pattern(xStr, yStr) =>
        val x = xStr.toInt - 1
        val y = yStr.toInt - 1
        (x, y)
      case _ =>
        throw new IllegalArgumentException(s"Invalid move format: $move")
    }
  }



}
