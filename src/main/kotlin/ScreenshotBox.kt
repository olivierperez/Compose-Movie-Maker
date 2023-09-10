import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
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
    ms: Int,
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
    Column {
        Button(onClick = {
            scope.launch {
                screenshot.start()
                repeat(ms) { currentMs ->
                    animationValue = currentMs
                    delay(100)
                    screenshot.capture(currentMs)
                }

                screenshot.end()
            }
        }) {
            Text("Capture")
        }
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
