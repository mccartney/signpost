package pl.waw.oledzki.signpost

import java.nio.file.{Files, Paths}

object App {
  def main(args: Array[String]): Unit = {
    val board = exampleBoard
    val solvedBoard = new Solver().solve(exampleBoard)
    val renderer = Renderer()
    val html = renderer.boardToHtml(solvedBoard)
    Files.writeString(Paths.get("board.html"), html)
    println("Board rendered to board.html")
  }

  val exampleBoard: Board = Board(
    size = 5,
    cells = Map(
      (0, 0) -> Cell(Some(1), Direction.START),
      (1, 0) -> Cell(None, Direction.E),
      (2, 0) -> Cell(None, Direction.S),
      (3, 0) -> Cell(None, Direction.SW),
      (4, 0) -> Cell(None, Direction.S),

      (0, 1) -> Cell(None, Direction.N),
      (1, 1) -> Cell(None, Direction.E),
      (2, 1) -> Cell(None, Direction.SE),
      (3, 1) -> Cell(None, Direction.W),
      (4, 1) -> Cell(None, Direction.SW),

      (0, 2) -> Cell(None, Direction.NE),
      (1, 2) -> Cell(None, Direction.S),
      (2, 2) -> Cell(None, Direction.E),
      (3, 2) -> Cell(None, Direction.W),
      (4, 2) -> Cell(None, Direction.N),

      (0, 3) -> Cell(None, Direction.E),
      (1, 3) -> Cell(None, Direction.NE),
      (2, 3) -> Cell(None, Direction.N),
      (3, 3) -> Cell(None, Direction.W),
      (4, 3) -> Cell(None, Direction.NW),

      (0, 4) -> Cell(None, Direction.E),
      (1, 4) -> Cell(None, Direction.N),
      (2, 4) -> Cell(None, Direction.NE),
      (3, 4) -> Cell(None, Direction.N),
      (4, 4) -> Cell(Some(25), Direction.STOP)
    )
  )
}
