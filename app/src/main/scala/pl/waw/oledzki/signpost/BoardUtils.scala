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

  private def findAllPotentialConnections(board: Board): List[((Int, Int), (Int, Int))] = {
    val maxVisitingNumber = board.size * board.size

    board.cells.map { case ((x, y), cell) =>
      val (dx, dy) = directionToVector(cell.arrow)

      if (cell.visitingNumber.contains(maxVisitingNumber)) {
        List.empty
      } else {
        val withinBoard = LazyList.from(1).map { step =>
          val newX = x + dx * step
          val newY = y + dy * step
          ((x, y), (newX, newY), board.cells.get((newX, newY)))
        }.takeWhile { case (_, (newX, newY), _) =>
          newX >= 0 && newX < board.size && newY >= 0 && newY < board.size
        }
        val notViolatingVisitingNumbers = cell.visitingNumber match {
          case Some(thisVisitingNumber) =>
            withinBoard
              .filter{
                case (_, _, Some(Cell(Some(newVisitingNumber), _))) if newVisitingNumber < thisVisitingNumber => false
                case _ => true
              }
          case _ =>
            withinBoard
        }
        notViolatingVisitingNumbers.collect {
           case (source, target, Some(targetCell)) if !targetCell.visitingNumber.contains(1) => (source, target)
        }.toList
      }
    }.toList.flatten
  }

  def findPotentialConnectionsFromCell(board: Board, x: Int, y: Int): List[((Int, Int), Cell)] = {
    findAllPotentialConnections(board)
      .filter { connection => connection._1 == (x, y) }
      .filter { case (source, target) => ! board.connections.exists{connection => source == connection._1} }
      .filter { case (source, target) => ! board.connections.exists{connection => target == connection._2} }
      .map { case (_, target) => (target, board.cells((target._1, target._2))) }
  }

  def findAllPotentialConnectionsToCell(board: Board, x: Int, y: Int): List[((Int, Int), Cell)] = {
    findAllPotentialConnections(board)
      .filter { connection => connection._2 == (x, y)}
      .filter { case (source, target) => ! board.connections.exists{connection => source == connection._1} }
      .filter { case (source, target) => ! board.connections.exists{connection => target == connection._2} }
      .map { case (source, _) => (source, board.cells((source._1, source._2))) }
  }
}
