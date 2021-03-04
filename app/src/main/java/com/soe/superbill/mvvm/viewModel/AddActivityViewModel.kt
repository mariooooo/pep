package com.soe.superbill.mvvm.viewModel

import androidx.lifecycle.MutableLiveData
import com.soe.superbill.data.model.AddInfo
import com.soe.superbill.data.model.Universal
import com.soe.superbill.mvvm.Event

class AddActivityViewModel : BaseViewModel() {

    val addLiveData = MutableLiveData<Event<Universal>>()
    val regionsLiveData = MutableLiveData<Event<AddInfo>>()

    fun add(map:Map<String,String>) {
        requestWithLiveData(addLiveData) {
            api.add(
                    map = map
            )
        }
    }
    fun getRegions(){
        requestWithLiveData(regionsLiveData) {
            api.getRegions()
        }
    }
}