package com.soe.superbill.data.model


import com.google.gson.annotations.SerializedName


data class Error(
        @SerializedName("err")
        val err: String?
) {
}
