package com.smitcoderx.task.aerologixtask.UI

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.smitcoderx.task.aerologixtask.Repository.AerologixRepository
import com.smitcoderx.task.aerologixtask.Utils.AerologixApplication

class ViewModelProviderFactory(
    private val application: AerologixApplication,
    private val repository: AerologixRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(application, repository) as T
    }

}