package com.smitcoderx.task.aerologixtask.Model


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json


@Entity(tableName = "Data")
data class Data(
    @Json(name = "age")
    var age: Int?,

    @Json(name = "education")
    var education: List<Education?>?,

    @PrimaryKey
    @Json(name = "firstname")
    var firstname: String,

    @Json(name = "gender")
    var gender: String?,

    @Json(name = "job")
    var job: List<Job?>?,

    @Json(name = "lastname")
    var lastname: String?,

    @Json(name = "picture")
    var picture: String?
)