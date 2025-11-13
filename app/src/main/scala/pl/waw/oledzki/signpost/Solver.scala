package pl.waw.oledzki.signpost

import scala.:+

class Solver {
  def solve(board: Board): Board = {
    val oneStep = applyRules(board)
    if (oneStep != board) {
      solve(oneStep)
    } else {
      board
    }
  }

  private def applyRules(board: Board): Board = {
    val rules = List[Rule](
      new ConnectedCellsHaveConsecutiveNumber(),
      new OnlyOnePossibleOutgoingConnection(),
      new OnlyOnePossibleIncomingConnection(),
      new CellsWithConsecutiveNumbersShouldBeConnected(),
    )
    Function.chain(rules.map(r => r.evaluate))(board)
  }
}

abstract class Rule {
  def evaluate(board: Board): Board
}

class ConnectedCellsHaveConsecutiveNumber extends Rule {
  override def evaluate(board: Board): Board = {
    board.connections.iterator.map { case (coord1, coord2) =>
        val cell1 = board.cells(coord1)
        val cell2 = board.cells(coord2)
        (cell1.visitingNumber, cell2.visitingNumber) match {
          case (Some(num1), None) =>
            System.out.println(s"New numbering ${num1}-${num1 + 1}")
            Some(board.copy(cells = board.cells + (coord2 -> cell2.copy(visitingNumber = Some(num1 + 1)))))
          case (None, Some(num2)) =>
            System.out.println(s"New numbering ${num2 - 1}-${num2}")
            Some(board.copy(cells = board.cells + (coord1 -> cell1.copy(visitingNumber = Some(num2 - 1)))))
          case _ =>
            None
        }
      }.collectFirst{case Some(b) => b}.getOrElse(board)
  }
}

class OnlyOnePossibleOutgoingConnection extends Rule {
  override def evaluate(board: Board): Board = {
    board.cells.iterator.map{ case (coord: (Int, Int), cell: Cell) =>
      val (x, y) = coord
      val potentialTargets = BoardUtils.findPotentialConnectionsFromCell(board, x, y)
      if (potentialTargets.size == 1) {
        val target = potentialTargets.head
        val connection = ((x, y), target._1)
        System.out.println("NEW CONNECTION_OUT " + connection)
        Some(board.copy(connections = board.connections + connection))
      } else {
        None
      }
    }.collectFirst{case Some(b) => b}.getOrElse(board)
  }
}

class OnlyOnePossibleIncomingConnection extends Rule {
  override def evaluate(board: Board): Board = {
    board.cells.iterator.map { case ((x: Int, y: Int), cell: Cell) =>
      val potentialSources = BoardUtils.findAllPotentialConnectionsToCell(board, x, y)
      if (potentialSources.size == 1) {
        val source = potentialSources.head
        val connection = (source._1, (x, y))
        System.out.println("NEW CONNECTION_IN " + connection)
        Some(board.copy(connections = board.connections + connection))
      } else {
        None
      }
    }.collectFirst{case Some(b) => b}.getOrElse(board)
  }
}

class CellsWithConsecutiveNumbersShouldBeConnected extends Rule {
  override def evaluate(board: Board): Board = {
    val byVisitingNumbers: Map[Int, (Int, Int)] =
      board.cells.collect {
        case ((x, y), Cell(Some(visitingNumber), _)) =>
          visitingNumber -> (x,y)
      }
    Range(1, board.size * board.size - 1).iterator.map{ n =>
      (byVisitingNumbers.get(n), byVisitingNumbers.get(n+1)) match {
        case (Some(cell1), Some(cell2)) if !board.connections.contains((cell1, cell2)) =>
            val connection = (cell1, cell2)
            Some(board.copy(connections = board.connections + connection))
        case _ => None
      }
    }.collectFirst{case Some(b) => b}.getOrElse(board)
  }
}

object Solver {
  def mapFirstDefined[A, B](xs: Iterable[A])(f: A => Option[B]): Option[B] =
    xs.iterator.map(f).collectFirst { case Some(v) => v }
}
