package com.misterioesf.finance.dao.entity

import androidx.room.*
import com.misterioesf.finance.data.entity.Segment
import java.util.*

@Entity(foreignKeys = [
    ForeignKey(
        entity = Account::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("account_id"),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )
])
data class Transfer(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var sum: Double,
    var description: String,
    var currency: String,
    @ColumnInfo(name = "is_bill")var isBill: Boolean,
    var date: Date,
    @ColumnInfo(name = "account_id") var accountId: Int
): java.io.Serializable, Segment {
    //  later will be gone (in TAGS update)
    @Ignore
    var color : Int = 0xFF000000.toInt()
    override fun getValue(): Double {
        return sum
    }

    override fun getSegmentColor(): Int {
        return color
    }
}
