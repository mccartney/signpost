package pl.waw.oledzki.signpost

case class Board(
                  size: Int,
                  cells: Map[(Int, Int), Cell],
                  connections: Set[((Int, Int), (Int, Int))] = Set()
                )

enum Direction {
  case N, S, E, W, SE, NE, SW, NW,
  STOP, START
}

case class Cell(
               visitingNumber: Option[Int],
               arrow: Direction
               )
