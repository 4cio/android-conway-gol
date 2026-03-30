package io.fourC.gol.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.fourC.gol.model.Cell
import io.fourC.gol.model.GameRules
import io.fourC.gol.model.GameState
import io.fourC.gol.model.PatternType
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameViewModel : ViewModel() {

    private val _state = MutableStateFlow(GameState())
    val state: StateFlow<GameState> = _state.asStateFlow()

    private var tickJob: Job? = null

    fun toggleRunning() {
        _state.update { it.copy(running = !it.running) }
        if (_state.value.running) {
            startTicking()
        } else {
            stopTicking()
        }
    }

    fun step() {
        if (_state.value.running) return
        advanceGeneration()
    }

    fun clear() {
        stopTicking()
        _state.update {
            it.copy(grid = emptySet(), generation = 0, running = false)
        }
    }

    fun randomSeed(density: Float = 0.15f) {
        stopTicking()
        val s = _state.value
        val cells = HashSet<Cell>()
        for (x in 0 until s.gridWidth) {
            for (y in 0 until s.gridHeight) {
                if (Random.nextFloat() < density) {
                    cells.add(Cell(x, y))
                }
            }
        }
        _state.update {
            it.copy(grid = cells, generation = 0, running = false)
        }
    }

    fun toggleCell(x: Int, y: Int) {
        val s = _state.value
        val wx = ((x % s.gridWidth) + s.gridWidth) % s.gridWidth
        val wy = ((y % s.gridHeight) + s.gridHeight) % s.gridHeight
        val cell = Cell(wx, wy)
        _state.update {
            val newGrid = it.grid.toMutableSet()
            if (cell in newGrid) {
                newGrid.remove(cell)
            } else {
                newGrid.add(cell)
            }
            it.copy(grid = newGrid)
        }
    }

    fun placePattern(pattern: PatternType, centerX: Int, centerY: Int) {
        val s = _state.value
        _state.update {
            it.copy(
                grid = GameRules.placePattern(
                    it.grid, pattern, centerX, centerY, s.gridWidth, s.gridHeight
                )
            )
        }
    }

    fun setSpeed(ms: Long) {
        _state.update { it.copy(speedMs = ms.coerceIn(16L, 1000L)) }
        if (_state.value.running) {
            stopTicking()
            startTicking()
        }
    }

    private fun advanceGeneration() {
        _state.update { s ->
            s.copy(
                grid = GameRules.nextGeneration(s.grid, s.gridWidth, s.gridHeight),
                generation = s.generation + 1
            )
        }
    }

    private fun startTicking() {
        tickJob?.cancel()
        tickJob = viewModelScope.launch {
            while (_state.value.running) {
                delay(_state.value.speedMs)
                advanceGeneration()
            }
        }
    }

    private fun stopTicking() {
        tickJob?.cancel()
        tickJob = null
        _state.update { it.copy(running = false) }
    }
}
