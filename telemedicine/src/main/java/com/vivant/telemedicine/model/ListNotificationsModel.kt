package com.vivant.telemedicine.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.vivant.telemedicine.common.Configuration
import com.vivant.telemedicine.model.base.BaseRequest

class ListNotificationsModel(
    @SerializedName("JSONData")
    @Expose
    private val jsonData: String,
    private val authToken: String) : BaseRequest(Header(authTicket = authToken)) {

    data class JSONDataRequest(
        @SerializedName("Criteria")
        val criteria: Criteria = Criteria()
    )

    data class Criteria(
        @SerializedName("UserID")
        val userID: Int = 0,
        @SerializedName("UserType")
        val UserType: String = "")

    data class ListNotificationsResponse(
        @SerializedName("NotificationList")
        val notificationList: MutableList<Notification> = mutableListOf()
    )

    data class Notification(
        @SerializedName("NotificationID")
        val notificationID: Int = 0,
        @SerializedName("CustomerID")
        val customerID: Int = 0,
        @SerializedName("Type")
        val type: String = "",
        @SerializedName("Message")
        val message: Message = Message(),
        @SerializedName("SentDate")
        val sentDate: String = "",
        @SerializedName("AppointmentID")
        val appointmentID: Int = 0
    )

    data class Message(
        @SerializedName("AppointmentDate")
        val appointmentDate: String = "",
        @SerializedName("AppointmentMode")
        val appointmentMode: String = "",
        @SerializedName("AppointmentModeNo")
        val appointmentModeNo: Int = 0,
        @SerializedName("AppointmentTime")
        val appointmentTime: String = "",
        @SerializedName("BookedFor")
        val bookedFor: String = "",
        @SerializedName("DoctorName")
        val doctorName: String = "",
        @SerializedName("RoomName")
        val roomName: String = ""
    )

}
