package com.soe.superbill.data.model

data class Add (val ls_name:String,
                val eic_name:String,
                val region_id_name:String,
                val viber_on:String,
                val telegram_on:String,
                val sms_on:String,
                val email_invoice_on:String,
                val osr_id:String,
                val provider_id:String
){
}