package com.soe.superbill.mvvm.viewModel

import androidx.lifecycle.MutableLiveData
import com.soe.superbill.data.model.ShortList
import com.soe.superbill.data.model.Universal
import com.soe.superbill.mvvm.Event

class ListAccountViewModel: BaseViewModel() {
    val simpleLiveData = MutableLiveData<Event<ShortList>>()
    val deleteLiveData = MutableLiveData<Event<Universal>>()

    fun getList() {
        requestWithLiveData(simpleLiveData) {
            api.getShortList()
        }
    }
    fun delete(a:String) {
        requestWithLiveData(deleteLiveData) {
            api.delete(a)
        }
    }
}