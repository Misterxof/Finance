package com.misterioesf.finance.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import com.misterioesf.finance.Utils
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings
import javax.inject.Inject

@AndroidEntryPoint
@WithFragmentBindings
class HomeCircleDiagram @Inject constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : CircleDiagramView(context, attributeSet) {

    override fun drawColorDiagram(canvas: Canvas?) {
        super.drawColorDiagram(canvas)

        paint.color = textColor
        paint.textSize = 80f

        canvas?.drawText(
            "Total:",
            oval.centerX() - Utils.dpToPixel(context, 35f),
            oval.centerY() - Utils.dpToPixel(context, 20f),
            paint
        )

        drawTotalAmountView(paint, canvas)
    }

    private fun drawTotalAmountView(paint: Paint, canvas: Canvas?) {
        var totalAmountText = "$totalAmount$"

        when (currentCurrencyIndex) {
            0 -> {
                totalAmountText = "$totalAmount$"
                drawIteratorCircle(
                    paint,
                    canvas,
                    oval.centerX() - Utils.dpToPixel(context, 30f),
                    oval.centerY() + Utils.dpToPixel(context, 60f),
                    true
                )
            }
            1 -> {
                totalAmountText = "${totalEurAmount}€"
                drawIteratorCircle(
                    paint,
                    canvas,
                    oval.centerX(),
                    oval.centerY() + Utils.dpToPixel(context, 60f),
                    true
                )
            }
            2 -> {
                totalAmountText = "${totalBynAmount}Br"
                drawIteratorCircle(
                    paint,
                    canvas,
                    oval.centerX() + Utils.dpToPixel(context, 30f),
                    oval.centerY() + Utils.dpToPixel(context, 60f),
                    true
                )
            }
        }

        paint.color = themeColorSecond
        canvas?.drawText(
            totalAmountText,
            oval.centerX() - Utils.dpToPixel(context, Utils.setTextX(totalAmountText)),
            oval.centerY() + Utils.dpToPixel(context, 10f),
            paint
        )

        paint.color = Color.GRAY
        paint.style = Paint.Style.STROKE
        canvas?.drawCircle(
            oval.centerX() - Utils.dpToPixel(context, 30f),
            oval.centerY() + Utils.dpToPixel(context, 60f), Utils.dpToPixel(context, 10f), paint
        )
        canvas?.drawCircle(
            oval.centerX(),
            oval.centerY() + Utils.dpToPixel(context, 60f), Utils.dpToPixel(context, 10f), paint
        )
        canvas?.drawCircle(
            oval.centerX() + Utils.dpToPixel(context, 30f),
            oval.centerY() + Utils.dpToPixel(context, 60f), Utils.dpToPixel(context, 10f), paint
        )
    }
}