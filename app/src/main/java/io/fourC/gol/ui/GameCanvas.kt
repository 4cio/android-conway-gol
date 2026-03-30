package io.fourC.gol.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import io.fourC.gol.model.Cell

@Composable
fun GameCanvas(
    liveCells: Set<Cell>,
    gridWidth: Int,
    gridHeight: Int,
    onCellTap: (Int, Int) -> Unit,
    onCellLongPress: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    val cellColor = MaterialTheme.colorScheme.primary
    val gridLineColor = MaterialTheme.colorScheme.outlineVariant
    val backgroundColor = MaterialTheme.colorScheme.surface

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale = (scale * zoom).coerceIn(0.2f, 10f)
                    offsetX += pan.x
                    offsetY += pan.y
                }
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { offset ->
                        val cellSize = (minOf(size.width.toFloat(), size.height.toFloat()) / minOf(gridWidth, gridHeight)) * scale
                        val gridPixelWidth = gridWidth * cellSize
                        val gridPixelHeight = gridHeight * cellSize
                        val startX = (size.width - gridPixelWidth) / 2f + offsetX
                        val startY = (size.height - gridPixelHeight) / 2f + offsetY

                        val cellX = ((offset.x - startX) / cellSize).toInt()
                        val cellY = ((offset.y - startY) / cellSize).toInt()

                        if (cellX in 0 until gridWidth && cellY in 0 until gridHeight) {
                            onCellTap(cellX, cellY)
                        }
                    },
                    onLongPress = { offset ->
                        val cellSize = (minOf(size.width.toFloat(), size.height.toFloat()) / minOf(gridWidth, gridHeight)) * scale
                        val gridPixelWidth = gridWidth * cellSize
                        val gridPixelHeight = gridHeight * cellSize
                        val startX = (size.width - gridPixelWidth) / 2f + offsetX
                        val startY = (size.height - gridPixelHeight) / 2f + offsetY

                        val cellX = ((offset.x - startX) / cellSize).toInt()
                        val cellY = ((offset.y - startY) / cellSize).toInt()

                        if (cellX in 0 until gridWidth && cellY in 0 until gridHeight) {
                            onCellLongPress(cellX, cellY)
                        }
                    }
                )
            }
    ) {
        // Background
        drawRect(color = backgroundColor, size = size)

        val cellSize = (minOf(size.width.toFloat(), size.height.toFloat()) / minOf(gridWidth, gridHeight)) * scale
        val gridPixelWidth = gridWidth * cellSize
        val gridPixelHeight = gridHeight * cellSize
        val startX = (size.width - gridPixelWidth) / 2f + offsetX
        val startY = (size.height - gridPixelHeight) / 2f + offsetY

        // Grid lines (only draw if cells are large enough to see)
        if (cellSize > 4f) {
            val lineAlpha = ((cellSize - 4f) / 12f).coerceIn(0f, 0.3f)
            val lineColor = gridLineColor.copy(alpha = lineAlpha)

            for (x in 0..gridWidth) {
                val px = startX + x * cellSize
                drawLine(
                    color = lineColor,
                    start = Offset(px, startY),
                    end = Offset(px, startY + gridPixelHeight),
                    strokeWidth = 0.5f
                )
            }
            for (y in 0..gridHeight) {
                val py = startY + y * cellSize
                drawLine(
                    color = lineColor,
                    start = Offset(startX, py),
                    end = Offset(startX + gridPixelWidth, py),
                    strokeWidth = 0.5f
                )
            }
        }

        // Live cells
        val padding = if (cellSize > 6f) 0.5f else 0f
        for (cell in liveCells) {
            val px = startX + cell.x * cellSize + padding
            val py = startY + cell.y * cellSize + padding
            val cs = cellSize - padding * 2
            if (px + cs >= 0 && py + cs >= 0 && px <= size.width && py <= size.height) {
                drawRect(
                    color = cellColor,
                    topLeft = Offset(px, py),
                    size = Size(cs, cs)
                )
            }
        }
    }
}
