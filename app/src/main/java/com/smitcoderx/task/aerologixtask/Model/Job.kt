package com.smitcoderx.task.aerologixtask.Model


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

data class Job(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @Json(name = "exp")
    var exp: Int?,
    @Json(name = "organization")
    var organization: String?,
    @Json(name = "role")
    var role: String?
)