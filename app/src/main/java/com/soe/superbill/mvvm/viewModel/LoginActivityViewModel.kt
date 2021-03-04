package com.soe.superbill.mvvm.viewModel

import androidx.lifecycle.MutableLiveData
import com.soe.superbill.data.model.Universal
import com.soe.superbill.mvvm.Event

class LoginActivityViewModel : BaseViewModel() {
    val simpleLiveData = MutableLiveData<Event<Universal>>()
    val restoreLiveData = MutableLiveData<Event<Universal>>()

    fun login(login: String, password: String) {
        requestWithLiveData(simpleLiveData) {

            api.login(
                    login = login,
                    password = password
            )
        }
    }

    fun restore(login: String) {
        requestWithLiveData(restoreLiveData) {
            api.recoveryRequest(
                    login = login
            )
        }
    }

}