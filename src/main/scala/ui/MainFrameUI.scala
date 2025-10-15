package ui

import scala.swing._
import controller.FileController
import model.Board

import java.io.File

class MainFrameUI extends MainFrame {

  private val menuPanel: MenuPanel = new MenuPanel(this)
  private val difficultyChooserPanel : DifficultyChooserPanel = new DifficultyChooserPanel(this)
  private var gamePanel: GamePanel = _


  font = new Font("Arial", java.awt.Font.BOLD, 40)
  title = "Minesweeper"
  //preferredSize = new Dimension(500, 500)
  contents = menuPanel

  def chooseDifficulty(): Unit = {
    contents = difficultyChooserPanel
    validate()
    repaint()
  }
  def startGame(file: File): Unit = {
    try{
      val (board, seconds, moves) = FileController.loadLevelFromFile(file)
      gamePanel = new GamePanel(this, board, seconds, moves)
      contents = gamePanel
      pack() // adjust frame size to fit new preferredSizex
    }catch {
      case e: IllegalArgumentException => println(e.getMessage)
    }
  }

  def showMenu(): Unit = {
    contents = menuPanel
    validate()
    repaint()
  }

  def loadLevel():Unit = {
    try{
      val file = FileController.loadFile(this)
      startGame(file)
    }catch {
      case e: IllegalStateException => println(e.getMessage)
    }
  }

  def openHighscores(): Unit = {
    contents = new HighscorePanel(this)
  }

  def createLevel(): Unit = {
    contents = new LevelCreatorPanel(this)
  }


}

