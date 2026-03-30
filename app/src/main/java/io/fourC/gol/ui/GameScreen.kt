package io.fourC.gol.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.fourC.gol.model.PatternType
import io.fourC.gol.viewmodel.GameViewModel

@Composable
fun GameScreen(viewModel: GameViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    var showPatternPicker by remember { mutableStateOf(false) }
    var longPressX by remember { mutableIntStateOf(0) }
    var longPressY by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Game grid takes remaining space
        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            GameCanvas(
                liveCells = state.grid,
                gridWidth = state.gridWidth,
                gridHeight = state.gridHeight,
                onCellTap = { x, y -> viewModel.toggleCell(x, y) },
                onCellLongPress = { x, y ->
                    longPressX = x
                    longPressY = y
                    showPatternPicker = true
                }
            )
        }

        // Control bar at bottom
        ControlBar(
            running = state.running,
            generation = state.generation,
            population = state.grid.size,
            speedMs = state.speedMs,
            onPlayPause = viewModel::toggleRunning,
            onStep = viewModel::step,
            onClear = viewModel::clear,
            onRandom = viewModel::randomSeed,
            onSpeedChange = viewModel::setSpeed
        )
    }

    // Pattern picker bottom sheet
    if (showPatternPicker) {
        PatternPicker(
            onPatternSelected = { pattern ->
                viewModel.placePattern(pattern, longPressX, longPressY)
            },
            onDismiss = { showPatternPicker = false }
        )
    }
}
