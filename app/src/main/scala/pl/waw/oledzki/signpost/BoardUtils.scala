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
        }.filterNot { case (_, (newX, newY), _) =>
          board.connections.contains((x, y), (newX, newY))
        }.filterNot { case (_, newTarget, _) =>
          board.connections.exists((source, target) => target == newTarget || source == (x,y))
        }.filterNot { case ((x, y), newTarget, _) =>
          hasIndirectConnection(board, newTarget, (x, y))
        }
        val notViolatingVisitingNumbers = cell.visitingNumber match {
          case Some(thisVisitingNumber) =>
            withinBoard
              .filter{
                case (_, _, Some(Cell(Some(targetVisitingNumber), _))) =>
                  targetVisitingNumber == thisVisitingNumber + 1
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
      .map { case (_, target) => (target, board.cells(target)) }
  }

  def findAllPotentialConnectionsToCell(board: Board, x: Int, y: Int): List[((Int, Int), Cell)] = {
    findAllPotentialConnections(board)
      .filter { connection => connection._2 == (x, y)}
      .map { case (source, _) => (source, board.cells(source)) }
  }

  private def hasIndirectConnection(board: Board, from: (Int, Int), to: (Int, Int)): Boolean = {
    @annotation.tailrec
    def follow(current: (Int, Int), visited: Set[(Int, Int)]): Boolean = {
      if (current == to) true
      else if (visited.contains(current)) false
      else {
        board.connections.find(_._1 == current) match {
          case Some((_, next)) => follow(next, visited + current)
          case None => false
        }
      }
    }
    follow(from, Set.empty)
  }

}
