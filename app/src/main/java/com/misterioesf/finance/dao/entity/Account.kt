package com.misterioesf.finance.dao.entity

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.misterioesf.finance.data.entity.Segment
import java.util.Date

@Entity
data class Account(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var name: String,
    var sum: Double,
    var currency: String,
    ) : java.io.Serializable, Segment {
    @Ignore var color : Int = 0xFF000000.toInt()

    override fun getValue(): Double {
        return sum
    }

    override fun getSegmentColor(): Int {
        return color
    }
}