package com.misterioesf.finance.data.entity

enum class Currencies {
    BYN, USD, EU;

    companion object {
        fun getCurrencyByName(name: String): Int {
            return when (name) {
                "BYN" -> 0
                "USD" -> 1
                "EU" -> 2
                else -> 0
            }
        }
    }
}