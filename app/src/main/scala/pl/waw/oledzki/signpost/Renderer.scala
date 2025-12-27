package pl.waw.oledzki.signpost

class Renderer(cellSize: Int = 80) {

  def boardToHtml(board: Board): String = {
    val svgSize = board.size * cellSize

    val cells = for {
      ((x, y), cell) <- board.cells
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

    val fillColor = cell.visitingNumber match {
      case Some(_) => "#c0c0c0"
      case None => "white"
    }
    val rect = s"""<rect x="${x * cellSize}" y="${y * cellSize}" width="$cellSize" height="$cellSize" fill="$fillColor" stroke="#333" stroke-width="2"/>"""

    val number = cell.visitingNumber match {
      case Some(n) => s"""<text x="$cx" y="${cy - 15}" text-anchor="middle" font-size="20" font-weight="bold">$n</text>"""
      case None => ""
    }

    // Position arrow in bottom-right corner
    val arrowAreaSize = (cellSize * 0.4).toInt
    val margin = (cellSize * 0.05).toInt
    val arrowCx = x * cellSize + cellSize - arrowAreaSize / 2 - margin
    val arrowCy = y * cellSize + cellSize - arrowAreaSize / 2 - margin

    val arrow = cell.arrow match {
      case Direction.START => s"""<circle cx="$cx" cy="$cy" r="8" fill="green"/>"""
      case Direction.STOP => s"""<circle cx="$cx" cy="$cy" r="8" fill="red"/>"""
      case _ => renderArrow(arrowCx, arrowCy, cell.arrow, arrowAreaSize)
    }

    rect + "\n    " + number + "\n    " + arrow
  }

  private def renderArrow(cx: Int, cy: Int, dir: Direction, arrowAreaSize: Int): String = {
    val arrowLength = (arrowAreaSize * 0.5).toInt

    // Calculate rotation angle based on direction
    val angle = dir match {
      case Direction.E => 0
      case Direction.SE => 45
      case Direction.S => 90
      case Direction.SW => 135
      case Direction.W => 180
      case Direction.NW => 225
      case Direction.N => 270
      case Direction.NE => 315
      case _ => 0
    }

    // Arrow always points to the right (0 degrees), then rotated
    val x1 = cx - arrowLength / 2
    val x2 = cx + arrowLength / 2

    s"""<g transform="rotate($angle $cx $cy)">
       |      <line x1="$x1" y1="$cy" x2="$x2" y2="$cy" stroke="black" stroke-width="3" marker-end="url(#arrowhead)"/>
       |    </g>
       |    <defs>
       |      <marker id="arrowhead" markerWidth="6" markerHeight="6" refX="3" refY="3" orient="auto">
       |        <polygon points="0 0, 6 3, 0 6" fill="black"/>
       |      </marker>
       |    </defs>""".stripMargin
  }
}
