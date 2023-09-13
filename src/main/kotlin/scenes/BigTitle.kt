package scenes

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import recorder.Direction
import recorder.RecorderScope

@Composable
fun RecorderScope.BigTitle(
    modifier: Modifier = Modifier,
    title: @Composable (Modifier) -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.CenterStart
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
                .scaleIn(millis = animatedValue.localDuration),
            painter = painterResource("confetti.png"),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
        title(Modifier.fadeInSliding(1000L, direction = Direction.TOWARD_RIGHT))
    }
}
