package com.misterioesf.finance.ui

import android.content.Context
import android.content.res.Configuration
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.misterioesf.finance.R
import com.misterioesf.finance.Utils
import com.misterioesf.finance.dao.entity.Account
import java.util.*
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

        themeColor = Utils.getColorFromTheme(getContext(), com.google.android.material.R.attr.colorOnPrimary)
        themeColorSecond = Utils.getColorFromTheme(getContext(), com.google.android.material.R.attr.colorPrimary)
        textColor = Utils.getColorFromTheme(getContext(), com.google.android.material.R.attr.editTextColor)
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

        diagramElements.forEach {
            var degree = (360F * it.value.sum) / sum
            val endAngle = startAngle + degree
            val middleAngle = (startAngle + endAngle) / 2

            if (size == 1) degree += 360 - endAngle

            paint.color = it.value.color
            canvas?.let { canv ->
                canv.drawArc(oval, startAngle, degree.toFloat(), true, paint)
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
        totalAmount = Utils.roundOffDecimal(value)
        invalidate()
    }

    fun redraw() { invalidate() }
}