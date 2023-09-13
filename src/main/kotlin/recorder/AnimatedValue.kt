package recorder

data class AnimatedValue(
    val absoluteValue: Long,
    val localValue: Long,
    val absoluteProgress: Float,
    val localProgress: Float,
    val absoluteDuration: Long,
    val localDuration: Long,
)
