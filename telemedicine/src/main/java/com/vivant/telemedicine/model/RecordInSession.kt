package com.vivant.telemedicine.model

import com.google.gson.annotations.SerializedName

data class RecordInSession(
    var Id: String = "",
    var Name: String = "",
    var OriginalFileName: String = "",
    var Path: String = "",
    var Type: String = "",
    val FileUri: String = "" ,
    var Sync: String = "",
    var Code: String = ""
)