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
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings
import java.util.*
import kotlin.math.sqrt
import kotlin.properties.Delegates

const val TAG = "CIRCLE"

@AndroidEntryPoint
@WithFragmentBindings
class CircleDiagramView constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : View(context, attributeSet) {
    private lateinit var oval: RectF
    private lateinit var oval2: RectF

    private val radius by lazy { sqrt(((viewSize - ((viewSize + startX) / 2)) * (viewSize - ((viewSize + startX) / 2))).toDouble()) }
    private val paint = Paint()
    private val linePaint = Paint()

    private var viewWidth by Delegates.notNull<Int>()
    private var viewHeight by Delegates.notNull<Int>()
    private var viewSize by Delegates.notNull<Float>()
    private var startX = 200F
    private var startY = 200F
    private var diagramElements: TreeMap<String, Account> = TreeMap()
    private var themeColor: Int = 0
    private var themeColorSecond: Int = 0
    private var textColor: Int = 0
    private var totalAmount = 0.00

    init {
        linePaint.color = Color.BLACK
        linePaint.strokeWidth = 5F

        themeColor =
            Utils.getColorFromTheme(getContext(), com.google.android.material.R.attr.colorOnPrimary)
        themeColorSecond =
            Utils.getColorFromTheme(getContext(), com.google.android.material.R.attr.colorPrimary)
        textColor =
            Utils.getColorFromTheme(getContext(), com.google.android.material.R.attr.editTextColor)
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
        var sum = diagramElements.values.sumOf { it.sum }
        var startAngle = 0F
        var size = diagramElements.size

        diagramElements.forEach {
            var degree = (360F * it.value.sum) / sum
            val endAngle = startAngle + degree

            if (size == 1) degree += 360 - endAngle

            paint.color = it.value.color
            canvas?.let { canv ->
                canv.drawArc(oval, startAngle, degree.toFloat(), true, paint)
            }
            startAngle = endAngle.toFloat()
            size--
        }

        paint.color = themeColor
        canvas?.drawCircle(
            oval.centerX(),
            oval.centerY(),
            radius.toFloat() - Utils.dpToPixel(context, 10f),
            paint
        )
        paint.color = textColor
        paint.textSize = 80f
        val totalAmountText = "$totalAmount$"
        canvas?.drawText(
            "Total:",
            oval.centerX() - Utils.dpToPixel(context, 35f),
            oval.centerY() - Utils.dpToPixel(context, 20f),
            paint
        )
        paint.color = themeColorSecond
        canvas?.drawText(
            totalAmountText,
            oval.centerX() - Utils.dpToPixel(context, Utils.setTextX(totalAmountText)),
            oval.centerY() + Utils.dpToPixel(context, 10f),
            paint
        )
    }

    fun setMap(diagramElements: TreeMap<String, Account>) {
        this.diagramElements = diagramElements
        invalidate()
    }

    fun setTotalAmount(value: Double) {
        totalAmount = Utils.roundOffDecimal(value)
        invalidate()
    }

    fun redraw() {
        invalidate()
    }
}