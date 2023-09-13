package scenes

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import recorder.AnimatedValue
import recorder.RecorderScope


@Composable
fun RecorderScope.Headline(
    title: String,
    millisToShowFullLength: Long,
    modifier: Modifier
) {
    val progress = animatedValue.localValue.toFloat() / millisToShowFullLength
    val charCount = (title.length * progress).toInt()
    var currentHeadline = remember(charCount) { title.take(charCount) }

    LaunchedEffect(Unit) {
        title.forEach { char ->
            currentHeadline += char
            delay(100)
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = currentHeadline,
            style = MaterialTheme.typography.h2
        )
    }
}


@Preview
@Composable
private fun HeadlinePreview() {
    with(RecorderScope(AnimatedValue(25, 25, .25f, .25f, 1, 1))) {
        Headline(
            title = "Un super titre",
            millisToShowFullLength = 1_000L,
            modifier = Modifier
        )
    }
}
