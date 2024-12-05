package com.misterioesf.finance.ui

import android.content.Context
import android.content.res.Configuration
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import com.misterioesf.finance.R
import com.misterioesf.finance.dao.entity.Account
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.properties.Delegates

const val TAG = "CIRCLE"

class CircleDiagramView constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : View(context, attributeSet) {

    private var viewWidth by Delegates.notNull<Int>()
    private var viewHeight by Delegates.notNull<Int>()
    private var viewSize by Delegates.notNull<Float>()
//    private val radius by lazy { (viewSize + startX) / 2}
    private val radius by lazy { sqrt(((viewSize - ((viewSize + startX) / 2))*(viewSize - ((viewSize + startX) / 2))).toDouble()) }
    private lateinit var oval: RectF
    private lateinit var oval2: RectF
    private val paint = Paint()
    private val linePaint = Paint()
    private var startX = 200F
    private var startY = 200F
    private var diagramElements: TreeMap<String, Account> = TreeMap()
    private var themeColor: Int = 0
    private var themeColorSecond: Int = 0
    private var textColor: Int = 0
    private var totalAmount = 0.00

    init {
        attributeSet?.let {
            val attrs = context.obtainStyledAttributes(attributeSet, R.styleable.CircleDiagram)

            attrs?.let {
                Log.e(TAG, "Attrs ${it.getDimension(R.styleable.CircleDiagram_sem, 100f)}")
            }
        }

        linePaint.color = Color.BLACK
        linePaint.strokeWidth = 5F

        themeColor = getColorFromTheme(com.google.android.material.R.attr.colorOnPrimary)
        themeColorSecond = getColorFromTheme(com.google.android.material.R.attr.colorPrimary)
        textColor = getColorFromTheme(com.google.android.material.R.attr.editTextColor)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        if (context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            viewSize = viewWidth.toFloat() * 0.8F
            oval2 = RectF(0F, 0F, viewWidth.toFloat(), viewWidth.toFloat())
        } else {
            viewSize = viewHeight.toFloat() * 0.8F
            oval2 = RectF(0F, 0F, viewHeight.toFloat(), viewHeight.toFloat())
        }
        oval = RectF(startX, startY, viewSize, viewSize)

        paint.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas?) {
        if (diagramElements.isNotEmpty()) drawColorDiagram(canvas)

        Log.e(TAG, "onDraw ${oval.toString()}")
    }

    private fun drawColorDiagram(
        canvas: Canvas?
    ) {
        // test subj
        var sum = diagramElements.values.sumOf { it.sum }
        var startAngle = 0F
        var size = diagramElements.size
        var a = 255
        var r = 0
        var g = 255
        var b = 0
        var color = Color.argb(a, r, g, b)

        diagramElements.forEach {
            var degree = (360F * it.value.sum) / sum
            val endAngle = startAngle + degree
            val middleAngle = (startAngle + endAngle) / 2

            if (size == 1) degree += 360 - endAngle

            paint.color = color
            g -= 30
            b += 30
//            color = Color.argb(a, r, g, b)
            color = it.value.color
            Log.e(TAG, it.value.toString())
            Log.e(TAG, "onDraw ${it.value.color}")
            canvas?.let { canv ->
//                val x = (radius + radius * cos(Math.toRadians(middleAngle.toDouble()))) + startX
//                val y = (radius + radius * sin(Math.toRadians(middleAngle.toDouble()))) + startX
                //canv.drawArc(oval2, startAngle, degree, true, paint)
                canv.drawArc(oval, startAngle, degree.toFloat(), true, paint)

//                Log.e(TAG, "s=$startAngle e=$endAngle degree=$degree x=$x y=$y")
//                if (middleAngle >= 270 || middleAngle <= 90) {
//                    canv.drawLine(x.toFloat(), y.toFloat(), x.toFloat() + 50F, y.toFloat(), linePaint)
//                } else {
//                    canv.drawLine(x.toFloat(), y.toFloat(), x.toFloat() - 50F, y.toFloat(), linePaint)
//                }
            }
            startAngle = endAngle.toFloat()
            size--
        }

        paint.color = themeColor
        canvas?.drawCircle(oval.centerX(), oval.centerY(), radius.toFloat() - dpToPixel(10f), paint)
        paint.color = textColor
        paint.textSize = 80f
        val totalAmountText = "$totalAmount$"
        canvas?.drawText("Total:", oval.centerX() - dpToPixel(35f), oval.centerY() - dpToPixel(20f), paint)
        paint.color = themeColorSecond
        canvas?.drawText(totalAmountText, oval.centerX() - dpToPixel(setTextX(totalAmountText)), oval.centerY() + dpToPixel(10f), paint)
       // canvas?.let { drawSquare(it) }
    }

    fun setTextX(string: String): Float {
        return when(string.length) {
            1 -> 15f
            else -> (string.length - 2) * 10f
        }
    }

    fun dpToPixel(dp: Float): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return (dp * metrics.density) + 0.5f
    }

    fun setMap(diagramElements: TreeMap<String, Account>) {
        this.diagramElements = diagramElements
        invalidate()
    }

    fun setTotalAmount(value: Double) {
        totalAmount = roundOffDecimal(value)
        invalidate()
    }

    fun redraw() { invalidate() }

    private fun roundOffDecimal(number: Double): Double {
        val df = DecimalFormat("#.##", DecimalFormatSymbols(Locale.ENGLISH))
        df.roundingMode = RoundingMode.CEILING
        return df.format(number).toDouble()
    }

    private fun getColorFromTheme(resId: Int): Int {
        var typedValue = TypedValue()
        context.theme.resolveAttribute(resId, typedValue, true)
        return ContextCompat.getColor(context, typedValue.resourceId)
    }
}