package com.soe.superbill.mvvm.viewModel

import androidx.lifecycle.MutableLiveData
import com.soe.superbill.data.model.Universal
import com.soe.superbill.mvvm.Event

class ConfirmRecoveryViewModel:BaseViewModel(){
    val confirmOrRecoveryLiveData = MutableLiveData<Event<Universal>>()

    fun confirmOrRecovery(login:String,
                          code:String,
                          password:String,
                          password_repeat:String
    ) {
        requestWithLiveData(confirmOrRecoveryLiveData) {
            api.activationOrRecovery(
                    login = login,
                    code = code,
                    password = password,
                    password_repeat = password_repeat
            )
        }
    }
}