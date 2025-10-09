package ui

import controller.LevelCreatorController
import model.Board

import scala.swing._
import scala.swing.BorderPanel.Position
import scala.swing.event.ButtonClicked

class LevelCreatorPanel(frame: MainFrameUI) extends BorderPanel {
  private var board = new Board(List.fill(10, 10)("-"))
  private val levelCreatorController = new LevelCreatorController()

  // === Title ===
  private val title = new Label("Level Creator") {
    font = new Font("Arial", java.awt.Font.BOLD, 20)
    horizontalAlignment = Alignment.Center
  }
  private val addRowButton = operationButton("Add Row")
  private val addColumnButton = operationButton("Add Column")
  private val deleteRowButton = operationButton("Delete Row")
  private val deleteColumnButton = operationButton("Delete Column")
  private val toggleMineButton = operationButton("Toggle mine")
  private val clearAreaButton = operationButton("Clear Area")

  private val applyButton = new Button("Apply")
  private val inverseButton = new Button("Inverse")

  private val saveLevelButton = new Button("Save Level")
  private val backButton = new Button("Back")

  // === Difficulty selection ===
  private val beginner     = new RadioButton("Beginner")
  private val intermediate = new RadioButton("Intermediate")
  private val expert       = new RadioButton("Expert")
  private val difficultyGroup = new ButtonGroup(beginner, intermediate, expert)

  private val difficultyBox = new BoxPanel(Orientation.Vertical) {
    contents += new Label("Choose Difficulty") {
      xAlignment = Alignment.Center
      border = Swing.EmptyBorder(5, 0, 5, 0)
    }
    contents ++= Seq(beginner, intermediate, expert)
    border = Swing.TitledBorder(Swing.EtchedBorder, "Difficulty")
  }

  // === Operations (uniform button size) ===
  private def operationButton(text: String): Button = new Button(text) {
    preferredSize = new Dimension(150, 35)
    maximumSize = preferredSize
    margin = new Insets(5, 10, 5, 10)
  }

  private val buttonBox = new BoxPanel(Orientation.Vertical) {
    contents += new Label("Operations") {
      xAlignment = Alignment.Center
      border = Swing.EmptyBorder(5, 0, 5, 0)
    }
    contents ++= Seq(addRowButton, addColumnButton, deleteRowButton, deleteColumnButton, toggleMineButton, clearAreaButton)
    border = Swing.TitledBorder(Swing.EtchedBorder, "Map Editing")
    border = Swing.EmptyBorder(10, 10, 10, 10)
  }

  // === Isometries ===
  private val levelList = new ListView[String](Seq.empty)
  private val scrollPanel = new ScrollPane(levelList) {
    preferredSize = new Dimension(200, 150)
  }

  private val isometryBox = new BoxPanel(Orientation.Vertical) {
    contents += new Label("Choose Isometry") {
      xAlignment = Alignment.Center
      border = Swing.EmptyBorder(5, 0, 5, 0)
    }
    contents += scrollPanel
    contents += new FlowPanel{
      contents ++= Seq(applyButton, inverseButton)
    }
    border = Swing.TitledBorder(Swing.EtchedBorder, "Isometries")
    border = Swing.EmptyBorder(10, 10, 10, 10)
  }

  private val westBox = new BoxPanel(Orientation.Vertical) {
    contents += difficultyBox
    contents += Swing.VStrut(15)
    contents += buttonBox
    contents += Swing.VStrut(15)
    contents += isometryBox
  }

  // === Bottom buttons ===
  private val buttonBar = new FlowPanel {
    contents ++= Seq(saveLevelButton, backButton)
  }
  private def setNewBoard(newBoard:Board): Unit = {
    layout -= board
    board = newBoard
    layout(board)   = Position.Center
    board.revalidate()
    board.repaint()
  }
  // === Layout ===
  layout(title)   = Position.North
  layout(westBox) = Position.West
  layout(buttonBar) = Position.South
  layout(board)   = Position.Center

  // === Events ===
  listenTo(addRowButton, addColumnButton, deleteRowButton, deleteColumnButton, toggleMineButton, clearAreaButton)
  listenTo(applyButton, inverseButton)
  listenTo(saveLevelButton, backButton)
  reactions +={
    case ButtonClicked(`addRowButton`) => setNewBoard(levelCreatorController.addRow(board))
    case ButtonClicked(`addColumnButton`) => setNewBoard(levelCreatorController.addColumn(board))
    case ButtonClicked(`deleteRowButton`) => setNewBoard(levelCreatorController.deleteRow(board))
    case ButtonClicked(`deleteColumnButton`) => setNewBoard(levelCreatorController.deleteColumn(board))
    case ButtonClicked(`toggleMineButton`) =>
    case ButtonClicked(`clearAreaButton`) =>

    case ButtonClicked(`applyButton`) =>
    case ButtonClicked(`inverseButton`) =>

    case ButtonClicked(`saveLevelButton`) =>
    case ButtonClicked(`backButton`) => frame.showMenu()
  }
}
