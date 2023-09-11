package recorder

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
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import java.awt.Dimension

@Composable
fun RecorderBox(
    fps: Int,
    dimension: Dimension,
    modifier: Modifier = Modifier,
    content: @Composable (currentValue: Long) -> Unit
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
        var startsComputed by remember { mutableStateOf(listOf(0L)) }

        val demoAnimatedValue by remember(demoAnimatedPercent, startsComputed) {
            mutableStateOf((demoAnimatedPercent * startsComputed.last()).toLong())
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
                onStartsComputed = { startsComputed = it },
                content = {
                    val previousStart = startsComputed.lastOrNull { it <= demoAnimatedValue } ?: 0L
                    val localAnimatedValue = demoAnimatedValue - previousStart
                    content(localAnimatedValue)
                }
            )
        }
        Text("${demoAnimatedValue / 1000f} sec")
        Slider(
            value = demoAnimatedPercent,
            onValueChange = { demoAnimatedPercent = it },
            steps = ((startsComputed.last() / 1000).toInt() - 1).coerceAtLeast(0)
        )
    }
}

@Composable
fun CaptureBox(
    animationValue: Long,
    modifier: Modifier = Modifier,
    onStartsComputed: (List<Long>) -> Unit,
    content: @Composable (currentValue: Long) -> Unit
) {
    var computedStarts by remember { mutableStateOf(listOf(0L)) }
    Layout(
        modifier = modifier,
        content = {
            val previousStart = computedStarts.lastOrNull { it <= animationValue } ?: 0L
            val localAnimationValue = animationValue - previousStart
            content(localAnimationValue)
        }
    ) { measurables, constraints ->
        val (placeables, starts) = measurables.fold(
            Pair(listOf<Placeable>(), listOf<Long>())
        ) { (currentPlaceables, currentStarts), measurable ->
            val sceneData = measurable.parentData as? SceneData
                ?: error(".scene(...) Modifier need to be called on each scene")

            val nextStart = (currentStarts.lastOrNull() ?: 0L) + sceneData.millis
            if (animationValue < (currentStarts.lastOrNull() ?: 0L)) {
                Pair(currentPlaceables, currentStarts + nextStart)
            } else {
                val placeable = measurable.measure(constraints)
                Pair(currentPlaceables + placeable, currentStarts + nextStart)
            }
        }

        computedStarts = starts
        onStartsComputed(starts)

        layout(constraints.maxWidth, constraints.maxHeight) {
            if (animationValue < computedStarts.last()) {
                placeables.last().place(0, 0)
            }
        }
    }
}
