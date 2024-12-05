package com.misterioesf.finance.dao.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
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
): java.io.Serializable
