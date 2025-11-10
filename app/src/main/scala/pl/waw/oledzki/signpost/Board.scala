package pl.waw.oledzki.signpost

case class Board(
                  size: Int,
                  cells: Map[(Int, Int), Cell]
                )

enum Direction {
  case N, S, E, W, SE, NE, SW, NW,
  STOP, START
}

case class Cell(
               visitingNumber: Option[Int],
               arrow: Direction,
               follower: Option[Cell] = None
               )
