package com.smitcoderx.task.aerologixtask.Utils

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.smitcoderx.task.aerologixtask.Db.AerologixDatabase
import com.smitcoderx.task.aerologixtask.Repository.AerologixRepository

class AerologixApplication : Application() {

    val database by lazy { AerologixDatabase.invoke(this) }
    val repository by lazy { AerologixRepository(database) }


}