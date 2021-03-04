package com.soe.superbill.mvvm.viewModel

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.soe.superbill.mvvm.Event
import com.soe.superbill.data.model.Registration
import com.soe.superbill.data.model.ShortList
import com.soe.superbill.data.model.Universal
import java.lang.reflect.Type
import com.soe.superbill.Globals


class RegistrationActivityViewModel: BaseViewModel() {
    val simpleLiveData = MutableLiveData<Event<Universal>>()

    fun registration(map:Map<String,String>) {
        requestWithLiveData(simpleLiveData) {
            api.registration(
                    map = map
            )
        }
    }
}