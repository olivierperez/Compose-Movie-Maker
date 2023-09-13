package scenes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import recorder.RecorderScope

@Composable
fun RecorderScope.Debug(
    title: String,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.h3,
        )
        Text(
            text = "Absolute - ${animatedValue.absoluteValue} - ${animatedValue.absoluteProgress} - ${animatedValue.absoluteDuration}"
        )
        Text(
            text = "Local - ${animatedValue.localValue} - ${animatedValue.localProgress} - ${animatedValue.localDuration}"
        )
    }
}
