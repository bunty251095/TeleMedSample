package com.vivant.telemedicine.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.vivant.telemedicine.model.base.BaseRequest

class UpdateNotificationModel(
    @SerializedName("JSONData")
    @Expose
    private val jsonData: String,
    private val authToken: String) : BaseRequest(Header(authTicket = authToken)) {

    data class JSONDataRequest(
        @SerializedName("Criteria")
        val criteria: Criteria = Criteria(),
    )

    data class Criteria(
        @SerializedName("Mode")
        val mode: String = "",
        @SerializedName("NotificationID")
        val notificationID: Int = 0,
        @SerializedName("UserID")
        val userID: Int = 0,
        @SerializedName("UserType")
        val UserType: String = "")

    data class UpdateNotificationResponse(
        @SerializedName("Success")
        val success: String = ""
    )

}