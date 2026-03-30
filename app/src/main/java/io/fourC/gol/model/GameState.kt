package io.fourC.gol.model

data class GameState(
    val grid: Set<Cell> = emptySet(),
    val generation: Long = 0,
    val running: Boolean = false,
    val speedMs: Long = 200L,
    val gridWidth: Int = 100,
    val gridHeight: Int = 100
)

data class Cell(val x: Int, val y: Int)

enum class PatternType(val label: String, val cells: List<Cell>) {
    GLIDER(
        "Glider",
        listOf(Cell(0, -1), Cell(1, 0), Cell(-1, 1), Cell(0, 1), Cell(1, 1))
    ),
    BLINKER(
        "Blinker",
        listOf(Cell(-1, 0), Cell(0, 0), Cell(1, 0))
    ),
    TOAD(
        "Toad",
        listOf(Cell(0, 0), Cell(1, 0), Cell(2, 0), Cell(-1, 1), Cell(0, 1), Cell(1, 1))
    ),
    BEACON(
        "Beacon",
        listOf(
            Cell(0, 0), Cell(1, 0), Cell(0, 1), Cell(1, 1),
            Cell(2, 2), Cell(3, 2), Cell(2, 3), Cell(3, 3)
        )
    ),
    BLOCK(
        "Block",
        listOf(Cell(0, 0), Cell(1, 0), Cell(0, 1), Cell(1, 1))
    ),
    LWSS(
        "LWSS",
        listOf(
            Cell(0, 0), Cell(3, 0),
            Cell(4, 1),
            Cell(0, 2), Cell(4, 2),
            Cell(1, 3), Cell(2, 3), Cell(3, 3), Cell(4, 3)
        )
    ),
    PULSAR(
        "Pulsar",
        listOf(
            // Top-left quadrant pattern (symmetric, full pulsar)
            Cell(-6, -4), Cell(-6, -3), Cell(-6, -2),
            Cell(-4, -6), Cell(-3, -6), Cell(-2, -6),
            Cell(-1, -4), Cell(-1, -3), Cell(-1, -2),
            Cell(-4, -1), Cell(-3, -1), Cell(-2, -1),
            // Top-right
            Cell(1, -4), Cell(1, -3), Cell(1, -2),
            Cell(2, -6), Cell(3, -6), Cell(4, -6),
            Cell(6, -4), Cell(6, -3), Cell(6, -2),
            Cell(2, -1), Cell(3, -1), Cell(4, -1),
            // Bottom-left
            Cell(-6, 2), Cell(-6, 3), Cell(-6, 4),
            Cell(-4, 1), Cell(-3, 1), Cell(-2, 1),
            Cell(-1, 2), Cell(-1, 3), Cell(-1, 4),
            Cell(-4, 6), Cell(-3, 6), Cell(-2, 6),
            // Bottom-right
            Cell(1, 2), Cell(1, 3), Cell(1, 4),
            Cell(2, 1), Cell(3, 1), Cell(4, 1),
            Cell(6, 2), Cell(6, 3), Cell(6, 4),
            Cell(2, 6), Cell(3, 6), Cell(4, 6)
        )
    ),
    GLIDER_GUN(
        "Gosper Gun",
        listOf(
            // Left block
            Cell(0, 4), Cell(0, 5), Cell(1, 4), Cell(1, 5),
            // Left part
            Cell(10, 4), Cell(10, 5), Cell(10, 6),
            Cell(11, 3), Cell(11, 7),
            Cell(12, 2), Cell(12, 8),
            Cell(13, 2), Cell(13, 8),
            Cell(14, 5),
            Cell(15, 3), Cell(15, 7),
            Cell(16, 4), Cell(16, 5), Cell(16, 6),
            Cell(17, 5),
            // Right part
            Cell(20, 2), Cell(20, 3), Cell(20, 4),
            Cell(21, 2), Cell(21, 3), Cell(21, 4),
            Cell(22, 1), Cell(22, 5),
            Cell(24, 0), Cell(24, 1), Cell(24, 5), Cell(24, 6),
            // Right block
            Cell(34, 2), Cell(34, 3), Cell(35, 2), Cell(35, 3)
        )
    )
}
