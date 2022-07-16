package com.vivant.telemedicine.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.vivant.telemedicine.model.base.BaseRequest

class SaveConsultationRequestModel(
    @SerializedName("JSONData")
    @Expose
    private val jsonData: String,
    private val authToken: String) : BaseRequest(Header(authTicket = authToken)) {

    data class JSONDataRequest(
        @SerializedName("Request")
        val request: Request = Request()
    )

    data class Request(
        @SerializedName("CustomerID")
        val customerID: Int = 0,
        @SerializedName("PersonID")
        val personID: Int = 0,
        @SerializedName("AppointmentType")
        val appointmentType: String = "",
        @SerializedName("AppointmentStatus")
        val appointmentStatus: String = "",
        @SerializedName("AppointmentCategory")
        val appointmentCategory: String = "",
        @SerializedName("AppointmentDate")
        val appointmentDate: String = "",
        @SerializedName("AppointmentStartTime")
        val appointmentStartTime: String = "",
        @SerializedName("AppointmentEndTime")
        val appointmentEndTime: String = "",
        @SerializedName("ConsultationStatus")
        val consultationStatus: String = "",
        @SerializedName("Speciality")
        val speciality: String = "",
        @SerializedName("AppointmentMode")
        val appointmentMode: String = "",
        @SerializedName("ProblemStatement")
        val problemStatement: String = "",
        @SerializedName("OngoingMedication")
        val ongoingMedication: String = "",
        @SerializedName("Severity")
        val severity: String = "",
        @SerializedName("ProblemStartDate")
        val problemStartDate: String = "",
        @SerializedName("CompanyCoveredAmount")
        val companyCoveredAmount: String = "",
        @SerializedName("SelfPayAmount")
        val selfPayAmount: Int = 0,
        @SerializedName("Price")
        val price: String = ""
    )

    data class SaveConsultationRequestResponse(
        @SerializedName("Request")
        val request: RequestResp = RequestResp()
    )

    data class RequestResp(
        @SerializedName("ID")
        val id: Int = 0,
        @SerializedName("CustomerID")
        val customerID: Int = 0,
        @SerializedName("PersonID")
        val personID: Int = 0,
        @SerializedName("AppointmentType")
        val appointmentType: String = "",
        @SerializedName("AppointmentStatus")
        val appointmentStatus: String = "",
        @SerializedName("AppointmentCategory")
        val appointmentCategory: String = "",
        @SerializedName("AppointmentDate")
        val appointmentDate: String = "",
        @SerializedName("AppointmentStartTime")
        val appointmentStartTime: String = "",
        @SerializedName("AppointmentEndTime")
        val appointmentEndTime: String = "",
        @SerializedName("ConsultationStatus")
        val consultationStatus: String = "",
        @SerializedName("Speciality")
        val speciality: String = "",
        @SerializedName("AppointmentMode")
        val appointmentMode: String = "",
        @SerializedName("ProblemStatement")
        val problemStatement: String = "",
        @SerializedName("OngoingMedication")
        val ongoingMedication: String = "",
        @SerializedName("Severity")
        val severity: String = "",
        @SerializedName("ProblemStartDate")
        val problemStartDate: String = "",
        @SerializedName("CompanyCoveredAmount")
        val companyCoveredAmount: String = "",
        @SerializedName("SelfPayAmount")
        val selfPayAmount: Int = 0,
        @SerializedName("Price")
        val price: String = ""
    )

}
