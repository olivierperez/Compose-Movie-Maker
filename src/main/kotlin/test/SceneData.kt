package test

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.unit.Density

fun Modifier.scene(millis: Long): Modifier =
    this.then(SceneData(millis = millis))

class SceneData(
    val millis: Long
) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?) = this@SceneData
}
