package old

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.Dimension
import java.awt.Rectangle
import java.awt.Robot
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

@Composable
fun ScreenshotBox(
    dimension: Dimension = Dimension(300, 300),
    frames: Int,
    content: @Composable (modifier: Modifier, animationValue: Int) -> Unit
) {
    var animationValue by remember { mutableStateOf(0) }

    val screenshot = remember {
        val composeWindow = ComposeWindow()
            .apply {
                setContent {
                    content(
                        Modifier,
                        animationValue
                    )
                }
                this.size = dimension
            }

        Screenshot(composeWindow, "captures")
    }


    val scope = rememberCoroutineScope()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(onClick = {
            scope.launch {
                screenshot.start()
                repeat(frames) { currentMs ->
                    animationValue = currentMs
                    delay(50)
                    screenshot.capture(currentMs)
                }

                screenshot.end()
            }
        }) {
            Text("Capture")
        }
        content(
            modifier = Modifier
                .size(dimension.width.dp, dimension.height.dp)
                .border(1.dp, Color.Gray)
                .clip(AbsoluteCutCornerShape(0.dp)),
            animationValue = 0
        )
    }
}

class Screenshot(
    private val composeWindow: ComposeWindow,
    private val capturePath: String
) {

    private val robot = Robot()

    fun capture(imageNumber: Int) {
        val rootPane = composeWindow.rootPane
        val bounds = Rectangle(rootPane.size)
        bounds.location = rootPane.locationOnScreen

        val bufferedImage = robot.createScreenCapture(bounds)

        try {
            val temp = File(capturePath, "screenshot-$imageNumber.png")
            ImageIO.write(bufferedImage, "png", temp)

            println("File created: ${temp.absolutePath}")
        } catch (ioe: IOException) {
            ioe.printStackTrace()
        }
    }

    suspend fun start() {
        composeWindow.isVisible = true
        composeWindow.toFront()
        delay(500)
    }

    fun end() {
        composeWindow.isVisible = false
    }
}
