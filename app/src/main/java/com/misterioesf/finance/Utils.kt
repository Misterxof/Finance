package com.misterioesf.finance

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
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

        fun isInternetOn(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val network = connectivityManager.activeNetwork ?: return false
                val activeNetwork =
                    connectivityManager.getNetworkCapabilities(network) ?: return false

                return when {
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    else -> false
                }
            } else {
                @Suppress("DEPRECATION") val networkInfo =
                    connectivityManager.activeNetworkInfo ?: return false
                @Suppress("DEPRECATION")
                return networkInfo.isConnected
            }
        }
    }
}