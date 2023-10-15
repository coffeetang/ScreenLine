import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.material.MaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import java.awt.Toolkit
import kotlin.math.cos
import kotlin.math.sin

val screensize = Toolkit.getDefaultToolkit().screenSize
val screenheight = screensize.size.height.toFloat()
val screenwidth = screensize.size.width.toFloat()

@Composable
fun LineLoading(modifier: Modifier) {
    val width = remember { mutableStateOf(800f) }
    val height = remember { mutableStateOf(800f) }
    val centerX = width.value / 2
    val centerY = height.value / 2
    val maxLength = centerX*3/4
    val minLength = centerX / 2
    val count = 30 * 6
    val unitAngle = 360f / count

    val colorList = listOf(
        Color(0xff0055FF), Color(0xff43B988),
        Color(0xffFF1D1D), Color(0xffFF8723),
        Color(0xffFFBA23), Color(0xff00D16E),
    )
    val transition = rememberInfiniteTransition()
    val baseX by transition.animateFloat(
        maxLength, minLength, animationSpec = InfiniteRepeatableSpec(
            tween(5000), repeatMode = RepeatMode.Reverse
        )
    )
    val baseY by transition.animateFloat(
        minLength, maxLength, animationSpec = InfiniteRepeatableSpec(
            tween(5000), repeatMode = RepeatMode.Reverse
        )
    )

    val colorIndex by transition.animateValue(
        0, colorList.size, Int.VectorConverter, animationSpec = InfiniteRepeatableSpec(
            tween(colorList.size * 1000), repeatMode = RepeatMode.Reverse
        )
    )
    val lineColor by animateColorAsState(
        colorList[colorIndex], animationSpec = TweenSpec(
            1000
        )
    )
    val angleDiff by transition.animateFloat(
        30f, 360f, animationSpec = InfiniteRepeatableSpec(
            tween(10000), repeatMode = RepeatMode.Reverse
        )
    )

    val xList1 = Array(count) {
        pointX(baseX, centerX, it * unitAngle)
    }
    val yList1 = Array(count) {
        pointY(baseY, centerY, it * unitAngle)
    }

    val xList2 = Array(count) {
        pointX(baseX, centerX, it * unitAngle + angleDiff * 6)
    }
    val yList2 = Array(count) {
        pointY(baseY, centerY, it * unitAngle + angleDiff * 3)
    }

    val xList3 = Array(count) {
        pointX(baseX, centerX, it * unitAngle + angleDiff / 5)
    }
    val yList3 = Array(count) {
        pointY(baseY, centerY, it * unitAngle + angleDiff * 3)
    }


    val xList4 = Array(count) {
        pointX(baseX, centerX, it * unitAngle + angleDiff * 5)
    }
    val yList4 = Array(count) {
        pointY(baseY, centerY, it * unitAngle + angleDiff / 3)
    }

    val xList5 = Array(count) {
        pointX(baseX, centerX, it * unitAngle + angleDiff / 4)
    }
    val yList5 = Array(count) {
        pointY(baseY, centerY, it * unitAngle + angleDiff * 3)
    }

    val xList6 = Array(count) {
        pointX(baseX, centerX, it * unitAngle + angleDiff / 3)
    }
    val yList6 = Array(count) {
        pointY(baseY, centerY, it * unitAngle + angleDiff / 5)
    }


    val pathList = Array(count) {
        val path = Path()
        if (it % 2 == 0) {
            path.moveTo(centerX, centerY)
            path.cubicTo(xList1[it], yList1[it], xList2[it], yList2[it], xList3[it], yList3[it])
        } else {
            path.moveTo(centerX, centerY)
            path.cubicTo(xList4[it], yList4[it], xList5[it], yList5[it], xList6[it], yList6[it])
        }
        path
    }

    val dash by transition.animateFloat(
        10f, 40f, animationSpec = InfiniteRepeatableSpec(
            tween(2000), repeatMode = RepeatMode.Reverse
        )
    )
    val rotate by transition.animateFloat(
        0f, 360f, animationSpec = InfiniteRepeatableSpec(
            tween(8000)
        )
    )
    Canvas(
        modifier = modifier.rotate(rotate).blur(5.dp),
    ) {
        width.value = size.width
        height.value = size.height
        for (pa in pathList) {
            drawPath(
                path = pa, brush = Brush.verticalGradient(
                    listOf(Color.White, lineColor), tileMode = TileMode.Mirror
                ),
                style = Stroke(
                    width = 5f,
                    cap = StrokeCap.Round,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, dash))
                )
            )
        }
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        state = WindowState(
            placement = WindowPlacement.Fullscreen,
            position = WindowPosition(Alignment.Center),
            size = DpSize(width = screenwidth.dp, height = screenheight.dp)
        ),
    ) {
        Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
            LineLoading(modifier = Modifier.fillMaxSize())
        }
    }
}

fun pointX(radius: Float, centerX: Float, angle: Float): Float {
    val res = Math.toRadians(angle.toDouble())
    return centerX + cos(res).toFloat() * (radius)
}

fun pointY(radius: Float, centerY: Float, angle: Float): Float {
    val res = Math.toRadians(angle.toDouble())
    return centerY + sin(res).toFloat() * (radius)
}
