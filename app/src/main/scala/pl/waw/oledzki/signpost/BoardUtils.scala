package pl.waw.oledzki.signpost

object BoardUtils {
  def directionToVector(direction: Direction): (Int, Int) = {
    direction match {
      case Direction.N => (0, -1)
      case Direction.S => (0, 1)
      case Direction.E => (1, 0)
      case Direction.W => (-1, 0)
      case Direction.NE => (1, -1)
      case Direction.NW => (-1, -1)
      case Direction.SE => (1, 1)
      case Direction.SW => (-1, 1)
      case Direction.START | Direction.STOP => (0, 0)
    }
  }

  def findAcceptingCellsIndicatedByArrow(board: Board, x: Int, y: Int): List[((Int, Int), Cell)] = {
    val cell = board.cells.get((x, y))

    cell match {
      case None => List.empty
      case Some(c) =>
        val (dx, dy) = directionToVector(c.arrow)

        // If no direction (START/STOP), return empty list
        if (dx == 0 && dy == 0) {
          List.empty
        } else {
          // Generate all cells in that direction, excluding the current one
          val cellsInDirection = LazyList.from(1).map { step =>
            val newX = x + dx * step
            val newY = y + dy * step
            ((newX, newY), board.cells.get((newX, newY)))
          }.takeWhile { case ((newX, newY), _) =>
            // Stop when we go out of bounds
            newX >= 0 && newX < board.size && newY >= 0 && newY < board.size
          }.collect {
            // Only keep cells that exist in the board
            case (coords, Some(cell)) => (coords, cell)
          }.toList

          cellsInDirection
            .filter { case((x, y), cell) =>
              ! board.connections.exists{connection => (x, y) == connection._2}
            }
        }
    }
  }
}
