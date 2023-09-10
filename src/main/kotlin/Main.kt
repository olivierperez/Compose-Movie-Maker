import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import test.RecorderBox
import test.scene
import java.awt.Dimension

@Preview
@Composable
fun Test() {
    RecorderBox(
        fps = 10,
        dimension = Dimension(500, 300),
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Scene n°1",
            style = MaterialTheme.typography.h1,
            modifier = Modifier.scene(millis = 2_000L)
        )
        Text(
            text = "Scene n°2",
            style = MaterialTheme.typography.h2,
            modifier = Modifier.scene(millis = 3_000L)
        )
    }
}

fun main() = application {
    Window(
        title = "Recorder",
        onCloseRequest = ::exitApplication,
    ) {
        Test()
    }
}
