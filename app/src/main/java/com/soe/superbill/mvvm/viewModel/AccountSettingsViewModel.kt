package com.soe.superbill.mvvm.viewModel

import androidx.lifecycle.MutableLiveData
import com.soe.superbill.data.model.GeneralInformation
import com.soe.superbill.data.model.Universal
import com.soe.superbill.mvvm.Event

class AccountSettingsViewModel:BaseViewModel() {
    val simpleLiveData = MutableLiveData<Event<GeneralInformation>>()
    val sendLiveData = MutableLiveData<Event<Universal>>()

    fun getSettings(a:String) {
        requestWithLiveData(simpleLiveData) {
            api.getDetailInfo(a)
        }
    }
    fun send(map:Map<String,String>,a:String) {
        requestWithLiveData(sendLiveData) {
            api.sendSettingsNotification(map,a)
        }
    }
}