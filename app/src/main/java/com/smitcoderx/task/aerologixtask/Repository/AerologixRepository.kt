package com.smitcoderx.task.aerologixtask.Repository

import android.util.Log
import androidx.room.withTransaction
import com.smitcoderx.task.aerologixtask.API.ApiClient
import com.smitcoderx.task.aerologixtask.Db.AerologixDatabase
import com.smitcoderx.task.aerologixtask.Utils.Constants.TAG
import com.smitcoderx.task.aerologixtask.Utils.networkBoundResource
import kotlinx.coroutines.delay

class AerologixRepository(private val db: AerologixDatabase) {

    fun getData() = networkBoundResource(
        query = {
            db.getAerologixDb().getAllDataFromDB()
        },
        fetch = {
            delay(2000)
            Log.d(TAG, "getData: ${ApiClient.retrofitService.getData()}")
            ApiClient.retrofitService.getData()
        },
        saveFetchResult = { data ->
            db.withTransaction {
                db.getAerologixDb().addData(data.data)
            }
        },
    )
}