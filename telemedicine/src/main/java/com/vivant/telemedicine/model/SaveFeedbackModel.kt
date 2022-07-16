package com.vivant.telemedicine.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.vivant.telemedicine.model.base.BaseRequest

class SaveFeedbackModel(
    @SerializedName("JSONData")
    @Expose
    private val jsonData: String,
    private val authToken: String) : BaseRequest(Header(authTicket = authToken)) {

    data class JSONDataRequest(
        @SerializedName("Feedback")
        val feedback: Feedback = Feedback(),
    )

    data class Feedback(
        @SerializedName("AppointmentID")
        val appointmentID: String = "",
        @SerializedName("UserRating")
        val userRating: Int = 0,
        @SerializedName("Comments")
        val comments: String = "",
        @SerializedName("RecordDate")
        val recordDate: String = "")

    data class SaveFeedbackResponse(
        @SerializedName("isProcessed")
        val isProcessed: String = ""
    )

}
