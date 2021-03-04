package com.soe.superbill.data.model

import com.google.gson.annotations.SerializedName

open class Wrapper {
    @SerializedName(value = "registr_user",
            alternate = ["login_user", "reset_password_user", "add_new_ls_user", "delete_ls_user",
                "api_main_reg", "update_pdf_temp_user", "insert_values_user",
                "insert_services_user", "check_mobile_code_user", "query_pass_phone_code_user",
                "universal"])

    private var universal: Universal? = null
}