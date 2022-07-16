package com.vivant.telemedicine.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.vivant.telemedicine.common.DateHelper
import com.vivant.telemedicine.model.base.BaseRequest

class CheckAppUpdateModel(
    @SerializedName("JSONData")
    @Expose
    private val jsonData: String, private val authToken: String) : BaseRequest(Header(authTicket = authToken)) {

    data class JSONDataRequest(
        @SerializedName("App")
        val app: String = "",
        @SerializedName("Device")
        val device: String = "",
        @SerializedName("AppVersion")
        val appVersion: String = ""
    )

    data class CheckAppUpdateResponse(
        @SerializedName("Result")
        var result: Result = Result()
    )

    data class Result(
        @SerializedName("AppVersion")
        val appVersion: List<AppVersion> = listOf()
    )

    data class AppVersion(
        @SerializedName("ID")
        val id: Int = 0,
        @SerializedName("CurrentVersion")
        var currentVersion: String? = "",
        @SerializedName("ForceUpdate")
        var forceUpdate: Boolean = false,
        @SerializedName("Description")
        val description: String? = "",
        @SerializedName("ImagePath")
        val imagePath: String? = "",
        @SerializedName("APICallInterval")
        val apiCallInterval: Int = 0,
        @SerializedName("LastUpdateDate")
        val lastUpdateDate: String? = DateHelper.currentDateAsStringyyyyMMdd,
        @SerializedName("Application")
        val application: String? = "",
        @SerializedName("DeviceType")
        val deviceType: String? = "",
        @SerializedName("Features")
        val features: String? = "",
        @SerializedName("ReleasedDate")
        val releasedDate: String? = ""
    )

}