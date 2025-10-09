package ui

import controller.FileController

import scala.swing._
import scala.swing.event.ButtonClicked

class HighscorePanel(frame: MainFrameUI) extends BorderPanel {
  // Top label
  private val title = new Label("Highscores") {
    font = new java.awt.Font("Arial", java.awt.Font.BOLD, 18)
  }

  private val backButton = new Button("Back")

  // Scores container
  private val scoresPanel = new BoxPanel(Orientation.Vertical)

  layout(title) = BorderPanel.Position.North
  layout(scoresPanel) = BorderPanel.Position.Center
  layout(backButton) = BorderPanel.Position.South

  // Refresh leaderboard whenever the panel is shown
  updateScores()

  /** Refresh leaderboard view */
  private def updateScores(): Unit = {
    scoresPanel.contents.clear()

    val scores = FileController.loadScores().take(10) // top 10 scores
    if (scores.isEmpty) {
      scoresPanel.contents += new Label("No scores yet.")
    } else {
      for ((entry, idx) <- scores.zipWithIndex) {
        scoresPanel.contents += new Label(s"${idx + 1}. ${entry._1} - ${entry._2}")
      }
    }
    revalidate()
    repaint()
  }

  listenTo(backButton)
  reactions += { case ButtonClicked(`backButton`) => frame.showMenu() }
}

