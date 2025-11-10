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
    cells = List(
      List(
        Cell(Some(1), Direction.START),
        Cell(None, Direction.E),
        Cell(None, Direction.S),
        Cell(None, Direction.SW),
        Cell(None, Direction.S)
      ),
      List(
        Cell(None, Direction.N),
        Cell(None, Direction.E),
        Cell(None, Direction.SE),
        Cell(None, Direction.W),
        Cell(None, Direction.SW)
      ),
      List(
        Cell(None, Direction.NE),
        Cell(None, Direction.S),
        Cell(None, Direction.E),
        Cell(None, Direction.W),
        Cell(None, Direction.N)
      ),
      List(
        Cell(None, Direction.E),
        Cell(None, Direction.NE),
        Cell(None, Direction.N),
        Cell(None, Direction.W),
        Cell(None, Direction.NW)
      ),
      List(
        Cell(None, Direction.E),
        Cell(None, Direction.N),
        Cell(None, Direction.NE),
        Cell(None, Direction.N),
        Cell(Some(25), Direction.STOP)
      )
    )
  )
}
