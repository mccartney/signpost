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

  def applyRules(board: Board): Board = {
    val rules = List[Rule](
      new ConnectedCellsHaveConsecutiveNumber(),
      new OnlyOneCellToGoTo(),
    )
    Function.chain(rules.map(r => r.evaluate))(board)
  }
}

abstract class Rule {
  def evaluate(board: Board): Board
}

class ConnectedCellsHaveConsecutiveNumber extends Rule {
  override def evaluate(board: Board): Board = {
    board.connections.collect { case ((x1, y1), (x2, y2)) =>
        val cell1 = board.cells((x1, y1))
        val cell2 = board.cells((x2, y2))
        (cell1.visitingNumber, cell2.visitingNumber) match {
          case (Some(num1), None) =>
            Some(board.copy(cells = board.cells + ((x2, y2) -> cell2.copy(visitingNumber = Some(num1 + 1)))))
          case (None, Some(num2)) =>
            Some(board.copy(cells = board.cells + ((x1, y1) -> cell1.copy(visitingNumber = Some(num2 - 1)))))
          case _ =>
            None
        }
      }.flatten.headOption.getOrElse(board)
  }
}

class OnlyOneCellToGoTo extends Rule {
  override def evaluate(board: Board): Board = {
    board.cells.collect { case ((x: Int, y: Int), cell: Cell) =>
      val potentialTargets = BoardUtils.findAcceptingCellsIndicatedByArrow(board, x, y)
      if (potentialTargets.size == 1) {
        val target = potentialTargets.head
        val connection = ((x, y), target._1)
        Some(board.copy(connections = board.connections + connection))
      } else {
        None
      }
    }.flatten.headOption.getOrElse(board)
  }
}
