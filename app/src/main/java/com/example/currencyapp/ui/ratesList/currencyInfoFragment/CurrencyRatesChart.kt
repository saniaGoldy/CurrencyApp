package com.example.currencyapp.ui.ratesList.currencyInfoFragment

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.currencyapp.domain.CurrentDateData
import java.math.RoundingMode


/**
 * call [TEXT_SIZE].sp.toPx()
 */
private const val TEXT_SIZE = 12
private const val SPACING = 100f

//constants for chart
private const val RATES_SCALE = 3
private const val GRAPH_GRADIENT_TOP_COLOR_ALPHA = 0.5f
private const val Y_AXIS_BOTTOM_MARGIN = 5
private const val X_AXIS_VALUES_NUMBER = 5
private const val X_AXIS_LEFT_MARGIN = 30f

/** call [CHART_STROKE_WIDTH].dp.toPx()*/
private const val CHART_STROKE_WIDTH = 3

@Composable
fun CurrencyRatesChart(
    rateStory: Map<String, Double> = mapOf(),
    modifier: Modifier,
    graphColor: Color = Color.Green
) {


    val transparentGraphColor = remember {
        graphColor.copy(alpha = GRAPH_GRADIENT_TOP_COLOR_ALPHA)
    }

    val upperValue = remember(rateStory) {
        rateStory.values.maxOrNull() ?: 0.0
    }

    val lowerValue = remember(rateStory) {
        rateStory.values.minOrNull() ?: 0.0
    }

    val calculator = Calculator(upperValue, lowerValue)

    val density = LocalDensity.current
    val textPaint = remember(density) {
        Paint().apply {
            color = android.graphics.Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = density.run { TEXT_SIZE.sp.toPx() }
        }
    }

    Canvas(modifier = modifier) {
        val chartBottom = size.height - SPACING

        //draw Y axis values(Dates)
        val spacePerDate = (size.width - SPACING) / rateStory.size
        rateStory.keys.forEachIndexed { id, date ->
            val simpleDate = CurrentDateData.formatToSimpleDate(date)
            //?.toBigDecimal()?.setScale(4, RoundingMode.HALF_EVEN)
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    simpleDate,
                    calculator.getYAxisValueXCoordinate(id, spacePerDate),
                    size.height - Y_AXIS_BOTTOM_MARGIN,
                    textPaint
                )
            }
        }

        //draw X axis values (rates)
        val rateStep = (upperValue - lowerValue) / X_AXIS_VALUES_NUMBER.toFloat()
        (0..X_AXIS_VALUES_NUMBER).forEach { i ->
            drawContext.canvas.nativeCanvas.apply {
                val rateString = (lowerValue + rateStep * i).toBigDecimal()
                    .setScale(RATES_SCALE, RoundingMode.HALF_EVEN)
                    .toEngineeringString()
                drawText(
                    rateString,
                    X_AXIS_LEFT_MARGIN + rateString.length,
                    chartBottom - i * size.height / X_AXIS_VALUES_NUMBER,
                    textPaint
                )
            }
        }

        //setup chart path
        var lastX = 0f
        val strokePath = Path().apply {
            val rates = rateStory.values.toList()
            for (i in rates.indices) {
                val rate = rates[i]
                val nextRate = rates.getOrNull(i + 1) ?: rates.last()

                val startPoint = ChartPoint(
                    calculator.getYAxisValueXCoordinate(i, spacePerDate),
                    calculator.pointYCoordinate(size.height, rate)
                )

                val endPoint = ChartPoint(
                    calculator.getYAxisValueXCoordinate(i + 1, spacePerDate),
                    calculator.pointYCoordinate(size.height, nextRate)
                )

                if (i == 0) {
                    moveTo(startPoint.x, startPoint.y)
                }

                val controlPoint = calculator.getControlPoint(startPoint, endPoint)
                lastX = controlPoint.x

                quadraticBezierTo(startPoint.x, startPoint.y, controlPoint.x, controlPoint.y)
            }
        }

        //setup chart gradient
        val fillPath = android.graphics.Path(strokePath.asAndroidPath()).asComposePath().apply {
            lineTo(lastX, chartBottom)
            //spacing here refers to x coordinate of the first point in chart
            lineTo(SPACING, chartBottom)
            close()
        }

        //draw chart
        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    transparentGraphColor,
                    Color.Transparent
                ),
                endY = chartBottom
            )
        )

        drawPath(
            strokePath,
            color = graphColor,
            style = Stroke(
                width = CHART_STROKE_WIDTH.dp.toPx(),
                cap = StrokeCap.Round
            )
        )
    }
}

private class Calculator(val upperValue: Double, val lowerValue: Double) {
    fun getRatio(rate: Double): Double = (rate - lowerValue) / (upperValue - lowerValue)

    fun getYAxisValueXCoordinate(id: Int, spacePerValue: Float) = SPACING + id * spacePerValue

    fun pointYCoordinate(
        height: Float,
        rate: Double
    ) = height - SPACING - (getRatio(rate) * height).toFloat()

    fun getControlPoint(start: ChartPoint, end: ChartPoint): ChartPoint {
        return ChartPoint((start.x + end.x) / 2f, (start.y + end.y) / 2f)
    }
}

private data class ChartPoint(val x: Float, val y: Float)