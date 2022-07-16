package com.vivant.telemedicine.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.vivant.telemedicine.model.base.BaseRequest

class JoinRoomModel(
    @SerializedName("JSONData")
    @Expose
    private val jsonData: String,
    private val authToken: String) : BaseRequest(Header(authTicket = authToken)) {

    data class JSONDataRequest(
        @SerializedName("Criteria")
        val criteria: Criteria = Criteria()
    )

    data class Criteria(
        @SerializedName("AppointmentID")
        val appointmentID: String = "",
        @SerializedName("AppIdentifier")
        val appIdentifier: String = ""
    )

    data class JoinRoomResponse(
        @SerializedName("Response")
        val response: Response = Response()
    )

    data class Response(
        @SerializedName("AppointmentMode")
        val appointmentMode: String = "",
        @SerializedName("RoomName")
        val roomName: String = ""
    )

}
