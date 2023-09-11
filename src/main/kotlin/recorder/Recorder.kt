package recorder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import kotlinx.coroutines.delay
import java.awt.Dimension

@Composable
fun rememberRecorder(
    fps: Int,
    dimension: Dimension,
    modifier: Modifier,
    content: @Composable (currentValue: Long) -> Unit
): Recorder = remember { Recorder(fps, dimension, modifier, content) }

class Recorder(
    private val fps: Int,
    dimension: Dimension,
    modifier: Modifier,
    content: @Composable (currentValue: Long) -> Unit
) {
    private var animationValue by mutableStateOf(0L)

    private val composeWindow = ComposeWindow()
        .apply {
            setContent {
                CaptureBox(
                    animationValue,
                    modifier,
                    ::onTimeComputed,
                    content
                )
            }
            this.size = dimension
        }

    private val screenshoter = Screenshoter(
        rootPane = composeWindow.rootPane,
        capturePath = "captures"
    )

    private var totalTime: Long? = null

    private fun onTimeComputed(starts: List<Long>) {
        if (this.totalTime == null) {
            this.totalTime = starts.last()
        }
    }

    suspend fun record() {
        composeWindow.isVisible = true
        composeWindow.toFront()
        screenshoter.init()

        delay(500)

        val totalTime = totalTime ?: error("Total time hasn't be computed!")
        var i = 0
        while (animationValue <= totalTime) {
            animationValue += 1_000L / fps
            delay(100)
            screenshoter.capture(i++)
        }

        animationValue = 0
        composeWindow.isVisible = false
    }
}
