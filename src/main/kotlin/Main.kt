import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import recorder.RecorderBox
import recorder.scene
import scenes.TitledImage
import java.awt.Dimension

@Preview
@Composable
fun Test() {
    RecorderBox(
        fps = 10,
        dimension = Dimension(500, 300),
        modifier = Modifier.fillMaxSize()
    ) { currentValue ->
        Text(
            text = "Scene n°1",
            style = MaterialTheme.typography.h1,
            modifier = Modifier.scene(millis = 2_000L).fillMaxSize()
        )
        Text(
            text = "Scene n°2 - $currentValue",
            style = MaterialTheme.typography.h2,
            modifier = Modifier.scene(millis = 3_000L).fillMaxSize()
        )
        TitledImage(
            animatedValue = currentValue,
            image = { modifier -> Box(modifier.background(Color.Gray)) },
            title = { modifier -> Text("Titre", modifier = modifier, style = MaterialTheme.typography.body1) },
            subTitle = { modifier -> Text("Sub-title", modifier = modifier, style = MaterialTheme.typography.caption) },
            modifier = Modifier.scene(3_000L)
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
