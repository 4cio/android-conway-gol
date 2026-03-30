package io.fourC.gol.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ControlBar(
    running: Boolean,
    generation: Long,
    population: Int,
    speedMs: Long,
    onPlayPause: () -> Unit,
    onStep: () -> Unit,
    onClear: () -> Unit,
    onRandom: () -> Unit,
    onSpeedChange: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        tonalElevation = 2.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Stats row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Gen: $generation",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Pop: $population",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${speedMs}ms",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Speed slider
            Slider(
                value = speedMs.toFloat(),
                onValueChange = { onSpeedChange(it.toLong()) },
                valueRange = 16f..1000f,
                modifier = Modifier.fillMaxWidth()
            )

            // Control buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilledIconButton(onClick = onPlayPause) {
                    Icon(
                        imageVector = if (running) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                        contentDescription = if (running) "Pause" else "Play"
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = onStep, enabled = !running) {
                    Icon(
                        imageVector = Icons.Filled.SkipNext,
                        contentDescription = "Step"
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = onClear) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "Clear"
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = onRandom) {
                    Icon(
                        imageVector = Icons.Filled.Casino,
                        contentDescription = "Random"
                    )
                }
            }
        }
    }
}
