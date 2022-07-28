package com.smitcoderx.task.aerologixtask.API

import com.smitcoderx.task.aerologixtask.Utils.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

object ApiClient {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
    }

    val retrofitService by lazy {
        retrofit.create(AerologixApi::class.java)
    }
}