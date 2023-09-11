package scenes

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

@Composable
fun TitledImage(
    animatedValue: Long,
    image: @Composable (Modifier) -> Unit,
    title: @Composable (Modifier) -> Unit,
    subTitle: @Composable (Modifier) -> Unit,
    modifier: Modifier
) {
    val scaledAnimatedValue = (1000 - animatedValue.toInt()).coerceAtLeast(0) * 64 / 1000
    Row(modifier.fillMaxSize()) {
        image(
            Modifier
                .align(Alignment.CenterVertically)
                .padding(16.dp)
                .size(50.dp)
                .offset { IntOffset(-scaledAnimatedValue, 0) }
        )
        Column(
            Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        ) {
            title(Modifier
                .offset { IntOffset(0, -scaledAnimatedValue) })
            subTitle(Modifier
                .offset { IntOffset(0, scaledAnimatedValue) })
        }
    }
}

@Preview
@Composable
fun TitledImagePreview() {
    TitledImage(
        animatedValue = 25L,
        image = { modifier -> Box(modifier.background(Color.Gray)) },
        title = { modifier -> Text("Titre", modifier = modifier, style = MaterialTheme.typography.body1) },
        subTitle = { modifier -> Text("Sub-title", modifier = modifier, style = MaterialTheme.typography.caption) },
        modifier = Modifier
    )
}
