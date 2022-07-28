package com.smitcoderx.task.aerologixtask.Model


import com.squareup.moshi.Json

data class ApiResponse(
    @Json(name = "data")
    var data: List<Data>
)