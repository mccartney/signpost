package pl.waw.oledzki.signpost

class Renderer(cellSize: Int = 80) {

  def boardToHtml(board: Board): String = {
    val svgSize = board.size * cellSize

    val cells = for {
      (row, y) <- board.cells.zipWithIndex
      (cell, x) <- row.zipWithIndex
    } yield renderCell(cell, x, y)

    s"""<!DOCTYPE html>
       |<html>
       |<head>
       |  <meta charset="UTF-8">
       |  <title>Signpost Board</title>
       |  <style>
       |    body { font-family: Arial, sans-serif; margin: 20px; }
       |    svg { border: 2px solid #333; }
       |  </style>
       |</head>
       |<body>
       |  <h1>Signpost Board</h1>
       |  <svg width="$svgSize" height="$svgSize" xmlns="http://www.w3.org/2000/svg">
       |    ${cells.mkString("\n    ")}
       |  </svg>
       |</body>
       |</html>""".stripMargin
  }

  private def renderCell(cell: Cell, x: Int, y: Int): String = {
    val cx = x * cellSize + cellSize / 2
    val cy = y * cellSize + cellSize / 2

    val rect = s"""<rect x="${x * cellSize}" y="${y * cellSize}" width="$cellSize" height="$cellSize" fill="white" stroke="#333" stroke-width="2"/>"""

    val number = cell.visitingNumber match {
      case Some(n) => s"""<text x="$cx" y="${cy - 15}" text-anchor="middle" font-size="20" font-weight="bold">$n</text>"""
      case None => ""
    }

    val arrow = cell.arrow match {
      case Direction.START => s"""<circle cx="$cx" cy="$cy" r="8" fill="green"/>"""
      case Direction.STOP => s"""<circle cx="$cx" cy="$cy" r="8" fill="red"/>"""
      case _ => renderArrow(cx, cy, cell.arrow)
    }

    rect + "\n    " + number + "\n    " + arrow
  }

  private def renderArrow(cx: Int, cy: Int, dir: Direction): String = {
    val arrowLength = 25
    val (dx, dy) = dir match {
      case Direction.N => (0, -1)
      case Direction.S => (0, 1)
      case Direction.E => (1, 0)
      case Direction.W => (-1, 0)
      case Direction.NE => (1, -1)
      case Direction.NW => (-1, -1)
      case Direction.SE => (1, 1)
      case Direction.SW => (-1, 1)
      case _ => (0, 0)
    }

    val normalize = math.sqrt(dx * dx + dy * dy)
    val ndx = dx / normalize
    val ndy = dy / normalize

    val x2 = cx + (ndx * arrowLength).toInt
    val y2 = cy + (ndy * arrowLength).toInt

    s"""<line x1="$cx" y1="$cy" x2="$x2" y2="$y2" stroke="black" stroke-width="2" marker-end="url(#arrowhead)"/>
       |    <defs>
       |      <marker id="arrowhead" markerWidth="10" markerHeight="10" refX="5" refY="3" orient="auto">
       |        <polygon points="0 0, 10 3, 0 6" fill="black"/>
       |      </marker>
       |    </defs>""".stripMargin
  }
}
