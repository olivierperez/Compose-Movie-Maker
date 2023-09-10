package test

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
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
        Box(
            modifier = Modifier
                .clip(CutCornerShape(0.dp))
                .border(1.dp, Color.Gray)
        ) {
            content()
        }
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
