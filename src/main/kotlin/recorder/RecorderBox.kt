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
    content: @Composable RecorderScope.() -> Unit
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
        var endsComputed by remember { mutableStateOf(listOf(0L)) }

        val demoAnimatedValue by remember(demoAnimatedPercent, endsComputed) {
            mutableStateOf((demoAnimatedPercent * endsComputed.last()).toLong())
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
                onEndsComputed = { endsComputed = it },
                content = {
                    with(RecorderScope(buildAnimatedValue(endsComputed, demoAnimatedValue))) {
                        content()
                    }
                }
            )
        }
        Text("${demoAnimatedValue / 1000f} sec")
        Slider(
            value = demoAnimatedPercent,
            onValueChange = { demoAnimatedPercent = it },
            steps = ((endsComputed.last() / 1000).toInt() - 1).coerceAtLeast(0)
        )
    }
}

@Composable
fun CaptureBox(
    animationValue: Long,
    modifier: Modifier = Modifier,
    onEndsComputed: (List<Long>) -> Unit,
    content: @Composable RecorderScope.() -> Unit
) {
    var computedEnds by remember { mutableStateOf(listOf(0L)) }
    Layout(
        modifier = modifier,
        content = {
            with(RecorderScope(buildAnimatedValue(computedEnds, animationValue))) {
                content()
            }
        }
    ) { measurables, constraints ->
        val (placeables, ends) = measurables.fold(
            Pair(listOf<Placeable>(), listOf<Long>())
        ) { (currentPlaceables, currentEnds), measurable ->
            val sceneData = measurable.parentData as? SceneData
                ?: error(".scene(...) Modifier need to be called on each scene")

            val nextEnd = (currentEnds.lastOrNull() ?: 0L) + sceneData.millis
            if (animationValue < (currentEnds.lastOrNull() ?: 0L)) {
                Pair(currentPlaceables, currentEnds + nextEnd)
            } else {
                val placeable = measurable.measure(constraints)
                Pair(currentPlaceables + placeable, currentEnds + nextEnd)
            }
        }

        computedEnds = ends
        onEndsComputed(ends)

        layout(constraints.maxWidth, constraints.maxHeight) {
            if (animationValue < computedEnds.last()) {
                placeables.last().place(0, 0)
            }
        }
    }
}

private fun buildAnimatedValue(
    ends: List<Long>,
    animatedValue: Long
): AnimatedValue {
    val (previousEnd, nextEnd) = ends.findPreviousAndNextOf(animatedValue)
    val absoluteProgress = ends.computeProgressionOf(animatedValue)
    val localAnimationValue = animatedValue - previousEnd
    val localDuration = nextEnd - previousEnd
    val localProgress = localAnimationValue.toFloat() / localDuration

    return AnimatedValue(
        absoluteValue = animatedValue,
        absoluteProgress = absoluteProgress,
        absoluteDuration = ends.last(),
        localValue = localAnimationValue,
        localProgress = localProgress,
        localDuration = localDuration
    )
}

private fun List<Long>.findPreviousAndNextOf(animationValue: Long): Pair<Long, Long> {
    val previousTime = this.lastOrNull { it <= animationValue } ?: 0L
    val nextTime = this.firstOrNull { it > animationValue } ?: this.last()
    return Pair(previousTime, nextTime)
}

private fun List<Long>.computeProgressionOf(animationValue: Long): Float {
    val lastValue = this.lastOrNull() ?: 0L
    return if (lastValue == 0L) {
        -1f
    } else {
        animationValue.toFloat() / lastValue
    }
}
