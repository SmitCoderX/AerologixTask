package com.smitcoderx.task.aerologixtask.UI

import androidx.lifecycle.*
import com.smitcoderx.task.aerologixtask.Model.Data
import com.smitcoderx.task.aerologixtask.Repository.AerologixRepository
import com.smitcoderx.task.aerologixtask.Utils.AerologixApplication
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val app: AerologixApplication,
    private val repository: AerologixRepository
) : AndroidViewModel(app) {

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()
    private val _apiData = MutableLiveData<List<Data>>()
    val apiData: LiveData<List<Data>> = _apiData

    fun refreshed() = viewModelScope.launch {
        delay(1000L)
        _isLoading.value = false
        val data = repository.response()
        _apiData.postValue(data.data)
    }

    val data = repository.getData().asLiveData()
}