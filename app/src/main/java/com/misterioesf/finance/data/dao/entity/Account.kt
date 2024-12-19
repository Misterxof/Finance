package com.misterioesf.finance.data.dao.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.misterioesf.finance.domain.model.Segment

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