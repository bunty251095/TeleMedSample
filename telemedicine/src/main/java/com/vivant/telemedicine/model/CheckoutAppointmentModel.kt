package com.vivant.telemedicine.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.vivant.telemedicine.model.base.BaseRequest

class CheckoutAppointmentModel(
    @SerializedName("JSONData")
    @Expose
    private val jsonData: String,
    private val authToken: String) : BaseRequest(Header(authTicket = authToken)) {

    data class JSONDataRequest(
        @SerializedName("PaymentDetails")
        val paymentDetails: PaymentDetails = PaymentDetails()
    )

    data class PaymentDetails(
        @SerializedName("fpx_debitAuthCode")
        val fpxDebitAuthCode: String = "",
        @SerializedName("fpx_sellerOrderNo")
        val fpxSellerOrderNo: Int = 0,
        @SerializedName("fpx_fpxTxnId")
        val fpxFpxTxnId: String = "",
        @SerializedName("fpx_fpxTxnTime")
        val fpxFpxTxnTime: String = "",
        @SerializedName("fpx_txnAmount")
        val fpxTxnAmount: String = "",
        @SerializedName("buyerbankname")
        val buyerbankname: String = "",
        @SerializedName("AppointmentCategory")
        val appointmentCategory: String = ""
    )

    data class CheckoutAppointmentResponse(
        @SerializedName("Result")
        val result: Result = Result()
    )

    data class Result(
        @SerializedName("ID")
        val id: String = "",
        @SerializedName("CustomerID")
        val customerID: String = "",
        @SerializedName("PersonID")
        val personID: String = "",
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
        val selfPayAmount: String = "",
        @SerializedName("IsStatusUpdated")
        val isStatusUpdated: Boolean = false
    )

}
