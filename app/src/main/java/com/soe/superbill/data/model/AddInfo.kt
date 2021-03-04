package com.soe.superbill.data.model

data class AddInfo(val res: List<Region>,
                   val list_co: List<Distribution>,
                   val list_cp: List<Provider>) {
    data class Region(override val id: Int,
                      override val name: String) : DataInfo

    data class Provider(override val id: Int,
                        override val name: String) : DataInfo

    data class Distribution(override val id: Int,
                            override val name: String) : DataInfo
}