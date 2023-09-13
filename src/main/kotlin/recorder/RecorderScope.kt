package recorder

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize

class RecorderScope(
    val animatedValue: AnimatedValue
) {
    fun Modifier.fadeInSliding(
        millis: Long,
        direction: Direction,
        fadeEasing: Easing = FastOutSlowInEasing
    ): Modifier {
        var size = IntSize.Zero
        val progress = (animatedValue.localValue.toFloat() / millis).coerceIn(0f, 1f)

        return this
            .onSizeChanged { size = it }
            .then(offset {
                val yOffset = direction.verticalMultiplier * (size.height * (1 - progress)).toInt()
                val xOffset = direction.horizontalMultiplier * (size.width * (1 - progress)).toInt()
                IntOffset(xOffset, yOffset)
            })
            .then(graphicsLayer { alpha = fadeEasing.transform(progress) })
    }

    fun Modifier.scaleIn(
        millis: Long
    ): Modifier {
        return this
            .then(graphicsLayer {
                val progress = (animatedValue.localValue.toFloat() / millis).coerceIn(0f, 1f)
                scaleX = 1f + .05f * progress
                scaleY = 1f + .05f * progress
            })
    }
}

enum class Direction(
    val verticalMultiplier: Int,
    val horizontalMultiplier: Int
) {
    TOWARD_DOWN(-1, 0),
    TOWARD_UP(1, 0),
    TOWARD_RIGHT(0, -1),
    TOWARD_LEFT(0, 1),
}
