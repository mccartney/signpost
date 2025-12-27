package pl.waw.oledzki.signpost

import java.nio.file.{Files, Paths}

object App {
  def main(args: Array[String]): Unit = {
    val board = exampleBoard
    val solvedBoard = new Solver().solve(exampleBoard)
    System.out.println(s"${solvedBoard.connections.size} connections: ${solvedBoard.connections}")
    val renderer = Renderer()
    val html = renderer.boardToHtml(solvedBoard)
    Files.writeString(Paths.get("board.html"), html)
    println("Board rendered to board.html")
  }

  val exampleBoard: Board = Board(
    size = 6,
    cells = Map(
      (0, 0) -> Cell(Some(1), Direction.S),
      (1, 0) -> Cell(None, Direction.S),
      (2, 0) -> Cell(None, Direction.SE),
      (3, 0) -> Cell(None, Direction.S),
      (4, 0) -> Cell(None, Direction.S),
      (5, 0) -> Cell(Some(31), Direction.SW),

      (0, 1) -> Cell(Some(2), Direction.E),
      (1, 1) -> Cell(None, Direction.N),
      (2, 1) -> Cell(None, Direction.S),
      (3, 1) -> Cell(None, Direction.NW),
      (4, 1) -> Cell(None, Direction.NW),
      (5, 1) -> Cell(None, Direction.W),

      (0, 2) -> Cell(None, Direction.E),
      (1, 2) -> Cell(None, Direction.E),
      (2, 2) -> Cell(None, Direction.W),
      (3, 2) -> Cell(None, Direction.NE),
      (4, 2) -> Cell(None, Direction.S),
      (5, 2) -> Cell(None, Direction.SW),

      (0, 3) -> Cell(None, Direction.SE),
      (1, 3) -> Cell(None, Direction.E),
      (2, 3) -> Cell(None, Direction.NE),
      (3, 3) -> Cell(None, Direction.SE),
      (4, 3) -> Cell(None, Direction.SE),
      (5, 3) -> Cell(None, Direction.N),

      (0, 4) -> Cell(None, Direction.S),
      (1, 4) -> Cell(Some(6), Direction.N),
      (2, 4) -> Cell(None, Direction.SW),
      (3, 4) -> Cell(Some(29), Direction.NW),
      (4, 4) -> Cell(Some(20), Direction.NE),
      (5, 4) -> Cell(None, Direction.W),

      (0, 5) -> Cell(None, Direction.N),
      (1, 5) -> Cell(None, Direction.N),
      (2, 5) -> Cell(None, Direction.E),
      (3, 5) -> Cell(None, Direction.N),
      (4, 5) -> Cell(None, Direction.N),
      (5, 5) -> Cell(Some(36), Direction.STOP)
    )
  )
}
