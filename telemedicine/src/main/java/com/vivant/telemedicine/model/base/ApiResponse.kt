package com.vivant.telemedicine.model.base

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("Data") val data: T,
    @SerializedName("StatusCode") val statusCode: String = "0",
    @SerializedName("ErrorNumber") val errorNumber: String = "0",
    @SerializedName("Message") val message: String
)