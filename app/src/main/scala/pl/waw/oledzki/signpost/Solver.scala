package pl.waw.oledzki.signpost

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
    board
  }
}
