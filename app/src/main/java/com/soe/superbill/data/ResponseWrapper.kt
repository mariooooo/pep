package com.soe.superbill.data

import com.google.gson.annotations.SerializedName
import com.soe.superbill.data.model.Error
import java.io.Serializable

class ResponseWrapper<T> : Serializable {
    @SerializedName("data")
    val data: T? = null
    @SerializedName("error")
    val error: Error? = null
}