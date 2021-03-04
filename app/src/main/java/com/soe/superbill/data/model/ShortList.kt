package com.soe.superbill.data.model

data class ShortList(val user_info: UserInfo,
                      val lst_ls: List<Item>) {
    data class UserInfo(val registr_from_telephone: String,
                        val phone: String,
                        val name: String)
    data class Item(val a: String,
                             val fio: String,
                             val ls: String,
                             val address: String)
}