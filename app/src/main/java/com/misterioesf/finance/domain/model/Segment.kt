package com.misterioesf.finance.domain.model

interface Segment {
    fun getValue(): Double
    fun getSegmentColor(): Int
}