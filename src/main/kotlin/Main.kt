import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.awt.Dimension
import kotlin.math.roundToInt


@Composable
@Preview
fun App() {
    MaterialTheme {
        ScreenshotBox(
            dimension = Dimension(400, 300),
            frames = 60
        ) { modifier, animationValue ->
            Box(modifier.fillMaxSize()) {
                Text(
                    text = "Ceci est un text composé",
                    modifier = Modifier.graphicsLayer {
                        alpha = animationValue / 60f
                    }
                )
                Text(
                    text = "Image n°$animationValue",
                    modifier = Modifier.align(Alignment.BottomEnd)
                )

                var size by remember { mutableStateOf(IntSize.Zero) }
                Image(
                    painter = painterResource("avatar.png"),
                    contentDescription = null,
                    modifier = Modifier
                        .onGloballyPositioned { size = it.size }
                        .offset {
                            IntOffset((-size.width + size.width * (animationValue / 60f)).roundToInt(), 0)
                        }
                        .clip(CircleShape)
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

