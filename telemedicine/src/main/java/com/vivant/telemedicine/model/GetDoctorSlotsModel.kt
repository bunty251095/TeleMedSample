package com.vivant.telemedicine.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.vivant.telemedicine.model.base.BaseRequest

class GetDoctorSlotsModel(
    @SerializedName("JSONData")
    @Expose
    private val jsonData: String,
    private val authToken: String) : BaseRequest(Header(authTicket = authToken)) {

    data class JSONDataRequest(
        @SerializedName("DoctorID")
        val doctorID: Int,
        @SerializedName("SupplierID")
        val supplierID: Int,
        @SerializedName("WeekDayCode")
        val weekDayCode: Int,
        @SerializedName("AppointmentDate")
        val appointmentDate: String,
        @SerializedName("AppointmentMode")
        val appointmentMode: String
    )

    data class GetDoctorSlotsResponse(
        @SerializedName("DoctorSessionSlots")
        var doctorSessionSlots: List<DoctorSessionSlots> = listOf(),
        @SerializedName("IsDoctorOnLeave")
        val isDoctorOnLeave: String = ""
    )

    data class DoctorSessionSlots(
        @SerializedName("header")
        val header: String = "",
        @SerializedName("TimeSlot")
        var timeSlot: MutableList<TimeSlot> = mutableListOf()
    )

    data class TimeSlot(
        @SerializedName("Slot")
        val slot: String = "",
        @SerializedName("ConsultationMode")
        val consultationMode: MutableList<String> = mutableListOf(),
        @SerializedName("Duration")
        val duration: String = "",
    )

}
