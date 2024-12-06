package com.misterioesf.finance.data.entity

import com.google.gson.annotations.SerializedName

data class Course(
    @SerializedName("Cur_ID")
    var id: Int,
    @SerializedName("Cur_Abbreviation")
    var abbreviation : String,
    @SerializedName("Cur_Name")
    var name: String,
    @SerializedName("Cur_OfficialRate")
    var rate: Float
) : java.io.Serializable
