package recorder

import java.awt.Rectangle
import java.awt.Robot
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO
import javax.swing.JRootPane

class Screenshoter(
    private val rootPane: JRootPane,
    private val capturePath: String
) {

    private val robot = Robot()

    fun init() {
        File(capturePath).run {
            deleteRecursively()
            mkdirs()
        }
    }

    fun capture(imageNumber: Int) {
        val bounds = Rectangle(rootPane.size)
        bounds.location = rootPane.locationOnScreen

        val bufferedImage = robot.createScreenCapture(bounds)

        try {
            val temp = File(capturePath, "screenshot-$imageNumber.png")
            ImageIO.write(bufferedImage, "png", temp)

            println("File created: ${temp.absolutePath}")
        } catch (ioe: IOException) {
            ioe.printStackTrace()
        }
    }
}
