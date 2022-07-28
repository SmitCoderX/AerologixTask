package com.smitcoderx.task.aerologixtask.UI

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import com.smitcoderx.task.aerologixtask.Repository.AerologixRepository
import com.smitcoderx.task.aerologixtask.Utils.AerologixApplication

class MainViewModel(
    private val app: AerologixApplication,
    private val repository: AerologixRepository
) : AndroidViewModel(app) {

    fun handle() = data

    val data = repository.getData().asLiveData()
}