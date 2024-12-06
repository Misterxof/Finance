package com.misterioesf.finance

import android.graphics.Color

class ColorSetter {
    companion object {
        private var a = 255
        private var r = 0
        private var g = 255
        private var b = 0
        private var color = Color.argb(a, r, g, b)

        fun getNextColor(): Int {
            if (g > 0) g -= 30
            else g = 255
            if (b < 255) b += 30
            else b = 0
            return Color.argb(a, r, g, b)
        }

        fun clear() {
            a = 255
            r = 0
            g = 255
            b = 0
        }
    }
}