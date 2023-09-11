package test

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import java.awt.Dimension

@Composable
fun RecorderBox(
    fps: Int,
    dimension: Dimension,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val recorder = rememberRecorder(fps, dimension, modifier, content)
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            scope.launch { recorder.record() }
        }) {
            Text("Capture")
        }

        var demoAnimatedPercent by remember { mutableStateOf(0f) }
        var maxTimeComputed by remember { mutableStateOf(0L) }

        val demoAnimatedValue by remember(demoAnimatedPercent, maxTimeComputed) {
            mutableStateOf((demoAnimatedPercent * maxTimeComputed).toLong())
        }

        Box(
            modifier = Modifier
                .size(dimension.width.dp, dimension.height.dp)
                .clip(CutCornerShape(0.dp))
                .border(1.dp, Color.Gray)
        ) {
            CaptureBox(
                animationValue = demoAnimatedValue,
                modifier = Modifier,
                onTimeComputed = { maxTimeComputed = it },
                content = { content() }
            )
        }
        Text("${demoAnimatedValue / 1000f} sec")
        Slider(
            value = demoAnimatedPercent,
            onValueChange = { demoAnimatedPercent = it },
            steps = ((maxTimeComputed / 1000).toInt() - 1).coerceAtLeast(0)
        )
    }
}

@Composable
fun CaptureBox(
    animationValue: Long,
    modifier: Modifier = Modifier,
    onTimeComputed: (Long) -> Unit,
    content: @Composable () -> Unit
) {
    Layout(
        content = content, modifier = modifier
    ) { measurables, constraints ->
        var currentStart = 0L

        val placeables = measurables.mapNotNull { measurable ->
            val sceneData =
                measurable.parentData as? SceneData ?: error(".scene(...) Modifier need to be called on each scene")

            if (animationValue < currentStart) {
                currentStart += sceneData.millis
                return@mapNotNull null
            }

            currentStart += sceneData.millis

            measurable.measure(constraints)
        }

        onTimeComputed(currentStart)

        layout(constraints.maxWidth, constraints.maxHeight) {
            placeables.last().place(0, 0)
        }
    }
}
