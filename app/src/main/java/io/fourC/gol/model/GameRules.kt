package io.fourC.gol.model

/**
 * Standard Conway's Game of Life rules (B3/S23) with toroidal wrapping.
 */
object GameRules {

    /**
     * Compute the next generation from the current set of live cells.
     * Uses toroidal wrapping on a [width] x [height] grid.
     */
    fun nextGeneration(liveCells: Set<Cell>, width: Int, height: Int): Set<Cell> {
        // Count neighbors for every cell adjacent to a live cell
        val neighborCounts = HashMap<Cell, Int>(liveCells.size * 4)

        for (cell in liveCells) {
            for (dx in -1..1) {
                for (dy in -1..1) {
                    if (dx == 0 && dy == 0) continue
                    val nx = (cell.x + dx + width) % width
                    val ny = (cell.y + dy + height) % height
                    val neighbor = Cell(nx, ny)
                    neighborCounts[neighbor] = (neighborCounts[neighbor] ?: 0) + 1
                }
            }
        }

        val nextGen = HashSet<Cell>(liveCells.size)

        for ((cell, count) in neighborCounts) {
            val alive = cell in liveCells
            // B3: dead cell with exactly 3 neighbors becomes alive
            // S23: live cell with 2 or 3 neighbors survives
            if (count == 3 || (count == 2 && alive)) {
                nextGen.add(cell)
            }
        }

        return nextGen
    }

    /**
     * Place a pattern at the given center position with toroidal wrapping.
     */
    fun placePattern(
        currentCells: Set<Cell>,
        pattern: PatternType,
        centerX: Int,
        centerY: Int,
        width: Int,
        height: Int
    ): Set<Cell> {
        val newCells = currentCells.toMutableSet()
        for (offset in pattern.cells) {
            val x = (centerX + offset.x + width) % width
            val y = (centerY + offset.y + height) % height
            newCells.add(Cell(x, y))
        }
        return newCells
    }
}
