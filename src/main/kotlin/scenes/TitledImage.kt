package scenes

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import recorder.AnimatedValue
import recorder.Direction
import recorder.RecorderScope

@Composable
fun RecorderScope.AvatarTitleSubTitle(
    image: @Composable (Modifier) -> Unit,
    title: @Composable (Modifier) -> Unit,
    subTitle: @Composable (Modifier) -> Unit,
    modifier: Modifier
) {
    Row(modifier.fillMaxSize()) {
        image(
            Modifier
                .fadeInSliding(millis = 400L, direction = Direction.TOWARD_RIGHT)
                .align(Alignment.CenterVertically)
                .padding(16.dp)
                .size(128.dp)
        )
        Column(
            Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        ) {
            title(Modifier.fadeInSliding(millis = 600L, direction = Direction.TOWARD_DOWN))
            subTitle(Modifier.fadeInSliding(millis = 600L, direction = Direction.TOWARD_UP))
        }
    }
}

@Preview
@Composable
fun TitledImagePreview() {
    with(RecorderScope(AnimatedValue(25, 25, .25f, .25f, 1, 1))) {
        AvatarTitleSubTitle(
            image = { Box(Modifier.background(Color.Gray)) },
            title = { Text("Titre", style = MaterialTheme.typography.body1) },
            subTitle = { Text("Sub-title", style = MaterialTheme.typography.caption) },
            modifier = Modifier
        )
    }
}
