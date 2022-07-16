package com.vivant.telemedicine.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.vivant.telemedicine.model.base.BaseRequest

class GetConsultationRequestModel(
    @SerializedName("JSONData")
    @Expose
    private val jsonData: String,
    private val authToken: String) : BaseRequest(Header(authTicket = authToken)) {

    data class JSONDataRequest(
        @SerializedName("ConsultationID")
        val consultationID: String = ""
    )

    data class GetConsultationRequestResponse(
        @SerializedName("Consultation")
        val consultation: Consultation = Consultation()
    )

    data class Consultation(
        @SerializedName("ID")
        val id: String = "",
        @SerializedName("ConsultationID")
        val consultationID: String = "",
        @SerializedName("CustomerID")
        val customerID: String = "",
        @SerializedName("PersonID")
        val personID: String = "",
        @SerializedName("AppointmentType")
        val appointmentType: String = "",
        @SerializedName("AppointmentStatus")
        val appointmentStatus: String = "",
        @SerializedName("AppointmentDate")
        val appointmentDate: String = "",
        @SerializedName("AppointmentStartTime")
        val appointmentStartTime: String = "",
        @SerializedName("DoctorID")
        val doctorID: String = "",
        @SerializedName("ConsultationStatus")
        val consultationStatus: String = "",
        @SerializedName("Speciality")
        val speciality: String = "",
        @SerializedName("RoomID")
        val roomID: String = "",
        @SerializedName("DoctorName")
        val doctorName: String = "",
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
        @SerializedName("AppointmentID")
        val appointmentID: String = "",
        @SerializedName("DoctorEmail")
        val doctorEmail: String = "",
        @SerializedName("DoctorPhoneNumber")
        val doctorPhoneNumber: String = "",
        @SerializedName("CompanyCoveredAmount")
        val companyCoveredAmount: String = "",
        @SerializedName("SelfPayAmount")
        val selfPayAmount: String = "",
        @SerializedName("Price")
        val price: String = ""
    )

}
