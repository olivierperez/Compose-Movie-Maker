import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.Dimension
import java.awt.Rectangle
import java.awt.Robot
import java.io.File
import java.io.IOException
import java.util.*
import javax.imageio.ImageIO


@Composable
@Preview
fun App() {
    MaterialTheme {
        ScreenshotBox(
            dimension = Dimension(400, 300),
            ms = 11
        ) { modifier, animationValue ->
            Column(modifier) {
                Text(
                    text = "Ceci est un text composé",
                    modifier = Modifier.graphicsLayer {
                        alpha = animationValue / 11f
                    }
                )
                Text(
                    text = "Image n°$animationValue."
                )
            }
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}

