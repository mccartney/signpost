package pl.waw.oledzki.signpost

import java.nio.file.{Files, Paths}

object App {
  def main(args: Array[String]): Unit = {
    println(greeting())
    println(exampleBoard)

    val renderer = Renderer()
    val html = renderer.boardToHtml(exampleBoard)
    Files.writeString(Paths.get("board.html"), html)
    println("Board rendered to board.html")
  }

  def greeting(): String = "Hello, world!"

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
