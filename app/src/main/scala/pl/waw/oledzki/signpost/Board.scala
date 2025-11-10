package pl.waw.oledzki.signpost

case class Board(
                  size: int,
                  cells: List[List[Cell]]
                )

enum Direction {
  case N, S, E, W, SE, NE, SW, NW,
  STOP, START
}

case class Cell(
                 visitingNumber: Option[int],
                 arrow: Direction,
               )
