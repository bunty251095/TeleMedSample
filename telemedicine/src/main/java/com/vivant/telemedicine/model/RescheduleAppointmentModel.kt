package com.vivant.telemedicine.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.vivant.telemedicine.model.base.BaseRequest

class RescheduleAppointmentModel(
    @SerializedName("JSONData")
    @Expose
    private val jsonData: String,
    private val authToken: String) : BaseRequest(Header(authTicket = authToken)) {

    data class JSONDataRequest(
        @SerializedName("CustomerID")
        val customerID: Int = 0,
        @SerializedName("AppointmentDate")
        val appointmentDate: String = "",
        @SerializedName("StartTime")
        val startTime: String = "",
        @SerializedName("EndTime")
        val endTime: String = "",
        @SerializedName("IsFollowupAppointment")
        val isFollowupAppointment: Boolean = false,
        @SerializedName("AppointmentID")
        val appointmentID: Int = 0,
        @SerializedName("AppointmentCategory")
        val appointmentCategory: String = "",
        @SerializedName("AppointmentMode")
        val appointmentMode: String = "",
        @SerializedName("CancelReasonCode")
        val cancelReasonCode: String = "",
        @SerializedName("CancelReasonTxt")
        val cancelReasonTxt: String = "",
        @SerializedName("AppIdentifier")
        val appIdentifier: String = "",
    )

    data class RescheduleAppointmentResponse(
        @SerializedName("IStatusUpdated")
        val iStatusUpdated: String = ""
    )

}