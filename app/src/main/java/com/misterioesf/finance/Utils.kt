package com.misterioesf.finance

import android.content.Context
import android.util.TypedValue
import androidx.core.content.ContextCompat
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

class Utils {
    companion object {
        fun roundOffDecimal(number: Double): Double {
            val df = DecimalFormat("#.##", DecimalFormatSymbols(Locale.ENGLISH))
            df.roundingMode = RoundingMode.CEILING
            return df.format(number).toDouble()
        }

        fun getColorFromTheme(context: Context, resId: Int): Int {
            var typedValue = TypedValue()
            context.theme.resolveAttribute(resId, typedValue, true)
            return ContextCompat.getColor(context, typedValue.resourceId)
        }

        fun setTextX(string: String): Float {
            return when (string.length) {
                1 -> 15f
                else -> (string.length - 2) * 10f
            }
        }

        fun dpToPixel(context: Context, dp: Float): Float {
            val resources = context.resources
            val metrics = resources.displayMetrics
            return (dp * metrics.density) + 0.5f
        }
    }
}