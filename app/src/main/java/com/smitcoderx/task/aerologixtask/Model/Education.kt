package com.smitcoderx.task.aerologixtask.Model


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

data class Education(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @Json(name = "degree")
    var degree: String?,
    @Json(name = "institution")
    var institution: String?
)