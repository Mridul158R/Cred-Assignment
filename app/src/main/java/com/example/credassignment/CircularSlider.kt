package com.example.credassignment

import android.graphics.Paint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.credassignment.ui.theme.Purple80

import kotlin.math.*

/**
 * A Circular slider for Tracking progress
 *
 * @param maxNum to provide maximum number for the slider
 * @param radiusCircle radius of the circular slider
 * @param percentageFontSize font size of the percentage text
 * @param percentageColor color of the percentage text
 * @param progressWidth width of the Progress
 * @param animDuration to set duration for the sliding animation
 * @param animDelay to set delay for the sliding animation
 * @param strokeCap to set strokes of the ends
 * @param thumbRadius to set the radius of the thumb
 * @param tickColor to set the color of the minute-like clock arms
 * @param tickhighlightedColor to set the color of the hour-like clock arms
 * @param dialColor  dial color
 * @param progressColor color of the progress bar
 * @param startThumbCircleColor Initial thumb color
 * @param thumbColor Thumb color
 * @param trackColor track color
 * @param trackOpacity Opacity of the track
 * @param trackWidth width of the track
 * @param isDisabled Flag to set enabled/disabled circular slider
 * @param staticProgress Static progress in case if isDisabled is true
 */

@Composable
fun CircularProgressBar(
    totalBalance: Int = 487891,
    maxNum: Int = 50,
    radiusCircle: Dp = 150.dp,
    percentageFontSize: TextUnit = 28.sp,
    percentageColor: Color = Color.Black,
    progressWidth: Float = 50f,
    animDuration: Int = 0,
    animDelay: Int = 0,
    strokeCap: StrokeCap = StrokeCap.Round,
    thumbRadius: Float = 40f,
    tickColor: Color = Color.Cyan,
    tickhighlightedColor: Color = Color.White,
    dialColor: Color = Color.White,
    progressColor: Color = Color(0xFFCD8B73),
    startThumbCircleColor: List<Color> = listOf(Color.Cyan, Color.Cyan),
    thumbColor: List<Color> = listOf(Color.Black, Color.Black),
    trackColor: Color = Color(0xFFFAE6DC),
    trackOpacity: Float = 1f,
    trackWidth: Float = 50f,
    isDisabled: Boolean = false,
    staticProgress: Float = 0f,
    currentProgressToBeReturned: (Float) -> Unit,  //Float type value to be returned..to it's parent composable using State hoisting - we're passing currentPercentage to the parent composable
    currentUpdatedValue: String = "",
    onTouchEnabled: Boolean = true,
    onDragEnabled: Boolean = true,
) {
    var radius by remember {
        mutableStateOf(0f)
    }

    var shapeCenter by remember {   //Center of the shape
        mutableStateOf(Offset.Zero)
    }

    var handleCenter by remember {  //Current position
        mutableStateOf(Offset.Zero)
    }

    var angle by remember { //Thumb and progress moves based on this (0 - 360)
        mutableStateOf(0.0)
    }

    var animationDuration by remember {
        mutableStateOf(1000)
    }

    animationDuration = animDuration

    val animatedAngle by animateFloatAsState(
        targetValue = angle.toFloat(),
        animationSpec = tween(
            durationMillis = animationDuration,
            delayMillis = animDelay
        )
    )

    var currentValue by remember { mutableStateOf(0f) }
    LaunchedEffect(key1 = animatedAngle) {
        currentValue =
            if (angle < 1.5) 0f else ((animatedAngle + 3f) * maxNum / 360)     // + 3f to show 100% in the slider
    }

    var currentPercentage by remember { mutableStateOf(0f) }
    if (isDisabled) {
        currentPercentage = staticProgress
        angle = (currentPercentage * 360f / 100f).toDouble()
    } else {
        currentPercentage = currentValue * 100 / maxNum
    }
    //Returning percentage value to the parent composable
    if (currentPercentage in 0.0..100.0) {
        currentProgressToBeReturned(currentPercentage)
    }

    //Progressbar
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(radiusCircle * 2f)
            .rotate(-90f)
    ) {
        Canvas(
            modifier = Modifier
                .size(radiusCircle * 2f)
                .pointerInput(Unit) {
                    if (onDragEnabled && !onTouchEnabled) {
                        detectDragGestures { change, dragAmount ->
                            handleCenter += dragAmount
                            angle = getRotationAngle(handleCenter, shapeCenter)
                            change.consume()
                        }
                    }
                    if (onTouchEnabled && !onDragEnabled) {

                        detectTapGestures(
                            onTap = { offset ->
                                handleCenter = Offset.Zero + offset
                                angle = getRotationAngle(handleCenter, shapeCenter)
                            },
                        )
                    }
                }
                .pointerInput(Unit) {
                    if (onTouchEnabled && onDragEnabled) {

                        detectTapGestures(
                            onTap = { offset ->
                                handleCenter = Offset.Zero + offset
                                angle = getRotationAngle(handleCenter, shapeCenter)
                            }
                        )
                    }
                }
                .pointerInput(Unit) {
                    if (onTouchEnabled && onDragEnabled) {

                        detectDragGestures { change, dragAmount ->
                            handleCenter += dragAmount
                            angle = getRotationAngle(handleCenter, shapeCenter)
                            change.consume()
                        }
                    }
                }
        ) {
            shapeCenter = center
            radius = size.minDimension / 2

            val x = (shapeCenter.x + cos(Math.toRadians(angle)) * radius).toFloat()
            val y = (shapeCenter.y + sin(Math.toRadians(angle)) * radius).toFloat()

            handleCenter = Offset(x, y)

            //track
            drawCircle(
                color = trackColor.copy(alpha = trackOpacity),
                style = Stroke(trackWidth),
                radius = radius
            )

            //Clock arms
//            drawContext.canvas.nativeCanvas.apply {
//                drawCircle(
//                    center.x,
//                    center.y,
//                    radiusCircle.toPx(),
//                    Paint().apply {
//                        color = android.graphics.Color.TRANSPARENT
//                        style = Paint.Style.FILL
//                        setShadowLayer(
//                            70f,
//                            0f,
//                            0f,
//                            android.graphics.Color.argb(50, 0, 0, 0)
//                        )
//                    }
//                )
//            }



            //Progress
            if (angle.toFloat() >= 359) {
                angle = 0.0
            }

            drawArc(
                color = progressColor,
                startAngle = 0f,
                sweepAngle = if (isDisabled) angle.toFloat() else animatedAngle,
                useCenter = false,
                style = Stroke(progressWidth)
            )

//            drawArc(
//                Color = progressColor,
//                startAngle = 0f,
//                sweepAngle = if (isDisabled) angle.toFloat() else animatedAngle,
//                useCenter = false,
//                style = Stroke(progressWidth)
//            )

            //Dial circle - Purple
//            drawCircle(
//                color = dialColor,
//                radius = (radiusCircle * 0.8f).toPx()
//            )
            //Inner Dial circle - 1
//            drawCircle(
//                color = Color.White,
//                radius = (radiusCircle * 0.6f).toPx(),
//                blendMode = BlendMode.Screen
//            )
            //Inner Dial boarder
//            drawCircle(
//                color = Color.Gray,
//                radius = (radiusCircle * 0.5f).toPx(),
//                style = Stroke(
//                    width = 1f,
//                )
//            )
            //Initial thumb circle at (0,0) coordinate
//            drawCircle(
//                brush = Brush.radialGradient(
//                    colors = startThumbCircleColor.onEach { it.copy(alpha = 0.10f) },
//                    center = Offset(size.width, size.height / 2)
//                ),
//                center = Offset(size.width, size.height / 2),
//                radius = thumbRadius + 5f
//            )
            //Inner initial circle
//            drawCircle(
//                color = thumbColor[0],
//                center = Offset(size.width, size.height / 2),
//                radius = thumbRadius - 10f
//            )
            //Thumb
//            drawCircle(
////                brush = Brush.radialGradient(colors = thumbColor, center = handleCenter),
//                color = Color.Black,
//                center = handleCenter,
//                radius = thumbRadius
//            )
            //Inner Thumb
            drawCircle(
                color = Color.Black,
                center = handleCenter,
                radius = thumbRadius - 10f
            )
            //Outer Shadow
            drawCircle(
                color = Color(0xFFE6C6BC),
                center = handleCenter,
                radius = thumbRadius + 20f
            )
            drawCircle(
//                brush = Brush.radialGradient(colors = thumbColor, center = handleCenter),
                color = Color.Black.copy(alpha = 0.65f),
                center = handleCenter,
                radius = thumbRadius + 5f
            )
            //Extra layer to increase touch area
            drawCircle(
                color = Color.Transparent,
                center = handleCenter,
                radius = thumbRadius + 40f
            )
        }
        /**
         * Extra canvas layer to intercept touch event
         */
        Canvas(modifier = Modifier
            .clip(CircleShape)
            .size((radiusCircle) * 1.5f)
            .clickable(
                enabled = false,
                onClick = {}
            )) {
            drawCircle(
                color = Color.Transparent,
                radius = (radiusCircle - 16.dp).toPx(),
                blendMode = BlendMode.Screen
            )
        }

        Box(modifier = Modifier
            .rotate(0f)
//            .background(Color.Cyan)
            .rotate(90f)
            ,
            contentAlignment = Alignment.Center

        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(text = "credit amount",
                    color = Color.Black.copy(alpha = 0.4f),
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(5.dp)
//                    .rotate(90f)
                )
                var money =((currentPercentage/100)*totalBalance).roundToInt();

                Text(
                    text = if (currentUpdatedValue == "") "₹${formatNumber(money)} " else currentUpdatedValue,
                    color = percentageColor,
                    fontSize = percentageFontSize,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
//                    .rotate(90f)
                        .padding(5.dp)
                        .drawUnderline(Color.Black, 25f)
                )
                Text(text = "@1.04% monthly",
                    color = Color(0xFF9CB474),
                    modifier = Modifier
                        .padding(5.dp)
//                    .rotate(90f)
                )
            }


        }


    }
}

private fun getRotationAngle(currentPosition: Offset, center: Offset): Double {
    val (dx, dy) = currentPosition - center
    val theta = atan2(dy, dx).toDouble()

    var angle = Math.toDegrees(theta)

    if (angle < 0) {
        angle += 360.0
    }
    return angle
}

//

//fun Modifier.drawUnderline(color: Color): Modifier = this.then(
//    Modifier.drawBehind {
//        val pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 10f), 0f)
//        val lineWidth = 2.dp.toPx() // Width of the underline
//        val startY = size.height - lineWidth / 2 // Position of the underline at the bottom of the text
//
//        drawLine(
//            color = color,
//            start = Offset(0f, startY),
//            end = Offset(size.width, startY),
//            strokeWidth = lineWidth,
//            pathEffect = pathEffect
//        )
//    }
//)

fun Modifier.drawUnderline(
    color: Color,
    underlinePadding: Float = 5f // Padding for underline on both sides
): Modifier = this.then(
    Modifier.drawBehind {
        val pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 10f), 0f)
        val lineWidth = 2.dp.toPx()
        val startY = size.height - lineWidth / 2

        // Use the measured text width with padding for exact underline length
        val textWidth = size.width - underlinePadding * 2

        drawLine(
            color = color,
            start = Offset(underlinePadding, startY),
            end = Offset(textWidth + underlinePadding, startY),
            strokeWidth = lineWidth,
            pathEffect = pathEffect
        )
    }
)

fun formatNumber(number: Int): String {
    val numStr = number.toString()
    val length = numStr.length
    val result = StringBuilder()

    // Start from the end of the string and work backwards
    for (i in numStr.indices) {
        result.append(numStr[length - 1 - i])

        // Add a comma after every 2 digits following the first 3 digits from the right
        if ((i + 1 > 3) && (i - 2) % 2 == 0 && i + 1 != length) {
            result.append(',')
        } else if (i == 2 && length > 3) { // First comma after 3 digits
            result.append(',')
        }
    }

    // Reverse the result to get the correct order
    return result.reverse().toString()
}
