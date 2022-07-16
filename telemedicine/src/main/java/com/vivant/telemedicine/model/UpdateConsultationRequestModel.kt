package com.vivant.telemedicine.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.vivant.telemedicine.model.base.BaseRequest

class UpdateConsultationRequestModel(
    @SerializedName("JSONData")
    @Expose
    private val jsonData: String,
    private val authToken: String) : BaseRequest(Header(authTicket = authToken)) {

    data class JSONDataRequest(
        @SerializedName("ConsultationID")
        val consultationID: String = "",
        @SerializedName("ConsultationStatus")
        val consultationStatus: String = "",
        @SerializedName("ActionBy")
        val actionBy: String = "",
        @SerializedName("DoctorID")
        val doctorID: String? = ""
    )

    data class UpdateConsultationRequestResponse(
        @SerializedName("IsUpdated")
        val isUpdated: String = ""
    )

}