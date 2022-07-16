package com.vivant.telemedicine.model

import com.google.gson.annotations.SerializedName

data class DownloadPrescriptionResponse(
    @SerializedName("FileData")
    val fileData: String = "",
/*    @SerializedName("FileName")
    val fileName: String = ""*/
)