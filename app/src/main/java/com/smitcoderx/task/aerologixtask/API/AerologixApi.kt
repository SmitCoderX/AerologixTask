package com.smitcoderx.task.aerologixtask.API

import com.smitcoderx.task.aerologixtask.Model.ApiResponse
import com.smitcoderx.task.aerologixtask.Utils.Constants.GET_DEV_DATA
import com.smitcoderx.task.aerologixtask.Utils.Constants.GET_PROD_DATA
import retrofit2.http.GET

interface AerologixApi {

    // Change GET_DEV_DATA to GET_PROD_DATA
    @GET(GET_DEV_DATA)
    suspend fun getData(): ApiResponse

}