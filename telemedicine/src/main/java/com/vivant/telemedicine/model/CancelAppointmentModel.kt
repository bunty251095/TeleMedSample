package com.vivant.telemedicine.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.vivant.telemedicine.model.base.BaseRequest

class CancelAppointmentModel(
    @SerializedName("JSONData")
    @Expose
    private val jsonData: String,
    private val authToken: String) : BaseRequest(Header(authTicket = authToken)) {

    data class JSONDataRequest(
        @SerializedName("CustomerID")
        val customerID: Int = 0,
        @SerializedName("AppointmentID")
        val appointmentID: Int = 0,
        @SerializedName("Status")
        val status: String = "",
        @SerializedName("CancelReasonCode")
        val cancelReasonCode: String = "",
        @SerializedName("CancelReasonTxt")
        val cancelReasonTxt: String = "",
        @SerializedName("AppIdentifier")
        val appIdentifier: String = ""
    )

    data class CancelAppointmentResponse(
        @SerializedName("IStatusUpdated")
        val iStatusUpdated: String = ""
    )



}
