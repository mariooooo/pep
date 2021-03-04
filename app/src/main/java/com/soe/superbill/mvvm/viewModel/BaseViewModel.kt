package com.soe.superbill.mvvm.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soe.superbill.mvvm.Event
import com.soe.superbill.data.ResponseWrapper
import com.soe.superbill.data.model.Error
import com.soe.superbill.data.network.ApiMain
import com.soe.superbill.data.network.NetworkService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseViewModel:ViewModel() {
    var api: ApiMain = NetworkService.retrofitService()
    fun <T> requestWithLiveData(
            liveData: MutableLiveData<Event<T>>,
            request: suspend () -> ResponseWrapper<T>) {

        liveData.postValue(Event.loading())

        this.viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = request.invoke()
                if (response.data != null) {
                    println("DATA")
                    liveData.postValue(Event.success(response.data))
                } else if (response.error != null) {
                    println("ERROR")
                    liveData.postValue(Event.error(response.error))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                println("EXCEPTION")
                liveData.postValue(Event.error(Error(e.message)))
            }
        }
    }
}