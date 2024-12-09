package com.misterioesf.finance.ui

import android.content.Context
import android.content.res.Configuration
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.misterioesf.finance.Utils
import com.misterioesf.finance.data.entity.Segment
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings
import java.util.*
import javax.inject.Inject
import kotlin.math.sqrt
import kotlin.properties.Delegates

const val TAG = "CIRCLE"

@AndroidEntryPoint
@WithFragmentBindings
open class CircleDiagramView @Inject constructor(
    context: Context,
    attributeSet: AttributeSet?
) : View(context, attributeSet) {
    protected lateinit var oval: RectF
    private lateinit var oval2: RectF

    private val radius by lazy { sqrt(((viewSize - ((viewSize + startX) / 2)) * (viewSize - ((viewSize + startX) / 2))).toDouble()) }
    protected val paint = Paint()
    private val linePaint = Paint()

    private var viewWidth by Delegates.notNull<Int>()
    private var viewHeight by Delegates.notNull<Int>()
    private var viewSize by Delegates.notNull<Float>()
    private var startX = 200F
    private var startY = 200F
    private var diagramElements: TreeMap<String, Segment> = TreeMap()
    private var themeColor: Int = 0
    protected var themeColorSecond: Int = 0
    protected var textColor: Int = 0
    protected var totalAmount = 0.00
    protected var totalEurAmount = 0.00
    protected var totalBynAmount = 0.00
    protected var currentCurrencyIndex = 0

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

    protected open fun drawColorDiagram(
        canvas: Canvas?
    ) {
        var sum = diagramElements.values.sumOf { it.getValue() }
        var startAngle = 0F
        var size = diagramElements.size
        paint.style = Paint.Style.FILL
        paint.strokeWidth = Utils.dpToPixel(context, 1f)

        //  draw circle segments
        diagramElements.forEach {
            var degree = (360F * it.value.getValue()) / sum
            val endAngle = startAngle + degree

            if (size == 1) degree += 360 - endAngle

            paint.color = it.value.getSegmentColor()
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
    }

    protected open fun drawIteratorCircle(
        paint: Paint,
        canvas: Canvas?,
        x: Float,
        y: Float,
        isCurrent: Boolean
    ) {
        if (isCurrent) {
            paint.color = Color.GREEN
            paint.style = Paint.Style.FILL

            canvas?.drawCircle(x, y, Utils.dpToPixel(context, 10f), paint)
        }
    }

    fun setMap(diagramElements: TreeMap<String, Segment>) {
        this.diagramElements = diagramElements
        invalidate()
    }

    fun setTotalAmount(valueUsd: Double, valueEur: Double, valueByn: Double) {
        totalAmount = Utils.roundOffDecimal(valueUsd)
        totalEurAmount = Utils.roundOffDecimal(valueEur)
        totalBynAmount = Utils.roundOffDecimal(valueByn)
        invalidate()
    }

    fun redraw() {
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                if (currentCurrencyIndex == 2) {
                    currentCurrencyIndex = 0
                    invalidate()
                    return true
                }
                currentCurrencyIndex++
                invalidate()
            }
        }
        return super.onTouchEvent(event)
    }
}