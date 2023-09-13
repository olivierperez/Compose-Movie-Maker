import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import recorder.RecorderBox
import recorder.scene
import scenes.AvatarTitleSubTitle
import scenes.BigTitle
import scenes.Debug
import java.awt.Dimension

@Preview
@Composable
fun MainScene() {
    RecorderBox(
        fps = 30,
        dimension = Dimension(1200, 675),
        modifier = Modifier.fillMaxSize()
    ) {
        BigTitle(
            modifier = Modifier.scene(2_000L),
            title = { modifier ->
                Text(
                    modifier = modifier.padding(16.dp),
                    text = "Inspired\nby Remotion",
                    style = MaterialTheme.typography.h1,
                    fontWeight = FontWeight.Bold
                )
            }
        )
        Debug(
            modifier = Modifier.scene(millis = 2_000L),
            title = "Debugging scene"
        )
        AvatarTitleSubTitle(
            modifier = Modifier.scene(2_000L),
            image = { modifier ->
                Image(
                    modifier = modifier.clip(CircleShape),
                    painter = painterResource("avatar.png"),
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )
            },
            title = { modifier ->
                Text("Olivier PEREZ", modifier = modifier, style = MaterialTheme.typography.h3)
            },
            subTitle = { modifier ->
                Text(
                    "Compose Movie Maker : le 7ème art à portée de composants web et d'API \uD83C\uDFAC ",
                    modifier = modifier,
                    style = MaterialTheme.typography.h5
                )
            }
        )
    }
}

fun main() = application {
    Window(
        title = "Recorder",
        onCloseRequest = ::exitApplication,
        state = rememberWindowState(width = 1400.dp, height = 800.dp)
    ) {
        MainScene()
    }
}
