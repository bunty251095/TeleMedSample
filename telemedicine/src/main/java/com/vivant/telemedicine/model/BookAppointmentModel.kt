package com.vivant.telemedicine.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.vivant.telemedicine.model.base.BaseRequest

class BookAppointmentModel(
    @SerializedName("JSONData")
    @Expose
    private val jsonData: String,
    private val authToken: String) : BaseRequest(Header(authTicket = authToken)) {

    data class JSONDataRequest(
        @SerializedName("Appointment")
        val appointment: Appointment = Appointment(),
        @SerializedName("Customer")
        val customer: Customer = Customer()
    )

    data class JSONDataRequestFollowup(
        @SerializedName("Appointment")
        val appointment: AppointmentFollowup = AppointmentFollowup(),
        @SerializedName("Customer")
        val customer: CustomerFollowup = CustomerFollowup()
    )

    data class Appointment(
        @SerializedName("ID")
        val id: Int = 0,
        @SerializedName("CustomerID")
        val customerID: Int = 0,
        @SerializedName("PersonID")
        val personID: Int = 0,
        @SerializedName("DoctorID")
        val doctorID: Int = 0,
        @SerializedName("SupplierID")
        val supplierID: Int = 0,
        @SerializedName("AppointmentDate")
        val appointmentDate: String = "",
        @SerializedName("AppointmentStartTime")
        val appointmentStartTime: String = "",
        @SerializedName("AppointmentEndTime")
        val appointmentEndTime: String = "",
        @SerializedName("Duration")
        val duration: Int = 0,
        @SerializedName("AppointmentMode")
        val appointmentMode: String = "",
        @SerializedName("ProblemStatement")
        val problemStatement: String = "",
        @SerializedName("OngoingMedication")
        val ongoingMedication: String = "",
        @SerializedName("CompanyCoveredAmount")
        val companyCoveredAmount: String = "",
        @SerializedName("SelfPayAmount")
        val selfPayAmount: Int = 0,
        @SerializedName("Price")
        val price: String = "",
        @SerializedName("IsPaymentDone")
        val isPaymentDone: Boolean = false,
        @SerializedName("AppointmentType")
        val appointmentType: String = "",
        @SerializedName("ProblemStartDate")
        val problemStartDate: String = "",
        @SerializedName("IsFollowupAppointment")
        val isFollowupAppointment: Boolean = false,
        @SerializedName("AppointmentCategory")
        val appointmentCategory: String = "",
        @SerializedName("AppointmentStatus")
        val appointmentStatus: String = "",
        @SerializedName("IsOPDConsumption")
        val isOPDConsumption: Boolean = false,
        @SerializedName("Severity")
        val severity: String = ""
    )

    data class AppointmentFollowup(
        @SerializedName("ID")
        val id: Int = 0,
        @SerializedName("CustomerID")
        val customerID: Int = 0,
        @SerializedName("PersonID")
        val personID: Int = 0,
        @SerializedName("DoctorID")
        val doctorID: String = "",
        @SerializedName("SupplierID")
        val supplierID: Int = 0,
        @SerializedName("AppointmentDate")
        val appointmentDate: String = "",
        @SerializedName("AppointmentStartTime")
        val appointmentStartTime: String = "",
        @SerializedName("AppointmentEndTime")
        val appointmentEndTime: String = "",
        @SerializedName("Duration")
        val duration: String = "",
        @SerializedName("AppointmentMode")
        val appointmentMode: String = "",
        @SerializedName("CompanyCoveredAmount")
        val companyCoveredAmount: String = "",
        @SerializedName("SelfPayAmount")
        val selfPayAmount: Int = 0,
        @SerializedName("Price")
        val price: String = "",
        @SerializedName("IsPaymentDone")
        val isPaymentDone: Boolean = false,
        @SerializedName("IsFollowupAppointment")
        val isFollowupAppointment: Boolean = false,
        @SerializedName("AppointmentType")
        val appointmentType: String = "",
        @SerializedName("AppointmentCategory")
        val appointmentCategory: String = "",
        @SerializedName("AppointmentStatus")
        val appointmentStatus: String = "",
        @SerializedName("IsOPDConsumption")
        val isOPDConsumption: Boolean = false
    )

    data class Customer(
        @SerializedName("PartnerCode")
        val partnerCode: String = "",
        @SerializedName("Name")
        val name: String = "",
        @SerializedName("FirstName")
        val firstName: String = "",
        @SerializedName("LastName")
        val lastName: String = "",
        @SerializedName("EmailAddress")
        val emailAddress: String = "",
        @SerializedName("PhoneNumber")
        val phoneNumber: String = "",
        @SerializedName("AlternateContactNo")
        val alternateContactNo: String = "",
        @SerializedName("Gender")
        val gender: String = "",
        @SerializedName("DateOfBirth")
        val dateOfBirth: String = "",
        @SerializedName("Address")
        val address: String = "",
        @SerializedName("Latitude")
        val latitude: Int = 0,
        @SerializedName("Longitude")
        val longitude: Int = 0,
        @SerializedName("Reference")
        val reference: String = ""
    )

    data class CustomerFollowup(
        @SerializedName("DoctorID")
        val doctorID: Int = 0,
        @SerializedName("SupplierID")
        val supplierID: Int = 0,
        @SerializedName("PartnerCode")
        val partnerCode: String = "",
        @SerializedName("Name")
        val name: String = "",
        @SerializedName("FirstName")
        val firstName: String = "",
        @SerializedName("LastName")
        val lastName: String = "",
        @SerializedName("EmailAddress")
        val emailAddress: String = "",
        @SerializedName("PhoneNumber")
        val phoneNumber: String = "",
        @SerializedName("AlternateContactNo")
        val alternateContactNo: String = "",
        @SerializedName("Gender")
        val gender: String = "",
        @SerializedName("DateOfBirth")
        val dateOfBirth: String = "",
        @SerializedName("Address")
        val address: String = "",
        @SerializedName("Latitude")
        val latitude: Int = 0,
        @SerializedName("Longitude")
        val longitude: Int = 0,
        @SerializedName("Reference")
        val reference: String = ""
    )

    data class BookAppointmentResponse(
        @SerializedName("Response")
        val response: Response = Response()
    )

    data class Response(
        @SerializedName("AppointmentDetails")
        val appointmentDetails: AppointmentDetails = AppointmentDetails()
    )

    data class AppointmentDetails(
        @SerializedName("ID")
        val id: Int = 0,
        @SerializedName("DoctorName")
        val doctorName: String = "",
        @SerializedName("RoomName")
        val roomName: String = "",
        @SerializedName("CustomerID")
        val customerID: Int = 0,
        @SerializedName("PersonID")
        val personID: Int = 0,
        @SerializedName("DoctorID")
        val doctorID: Int = 0,
        @SerializedName("SupplierID")
        val supplierID: Int = 0,
        @SerializedName("AppointmentDate")
        val appointmentDate: String = "",
        @SerializedName("AppointmentStartTime")
        val appointmentStartTime: String = "",
        @SerializedName("AppointmentEndTime")
        val appointmentEndTime: String = "",
        @SerializedName("Duration")
        val duration: Int = 0,
        @SerializedName("AppointmentMode")
        val appointmentMode: String = "",
        @SerializedName("ProblemStatement")
        val problemStatement: String = "",
        @SerializedName("OngoingMedication")
        val ongoingMedication: String = "",
        @SerializedName("CompanyCoveredAmount")
        val companyCoveredAmount: String = "",
        @SerializedName("SelfPayAmount")
        val selfPayAmount: Int = 0,
        @SerializedName("Price")
        val price: String = "",
        @SerializedName("IsPaymentDone")
        val isPaymentDone: Boolean = false,
        @SerializedName("AppointmentType")
        val appointmentType: String = "",
        @SerializedName("AppointmentStatus")
        val appointmentStatus: String = "",
        @SerializedName("AppointmentCategory")
        val appointmentCategory: String = "",
        @SerializedName("BookingFor")
        val bookingFor: String = "",
        @SerializedName("BookedForName")
        val bookedForName: String = "",
        @SerializedName("BookedForFirstName")
        val bookedForFirstName: String = "",
        @SerializedName("BookedForLastName")
        val bookedForLastName: String = "",
        @SerializedName("ProblemStartDate")
        val problemStartDate: String = "",
        @SerializedName("IsFollowupAppointment")
        val isFollowupAppointment: Boolean = false,
        @SerializedName("IsOPDConsumption")
        val isOPDConsumption: Boolean = false,
        @SerializedName("Severity")
        val severity: String = "",
        @SerializedName("EmailAddress")
        val emailAddress: String = "",
        @SerializedName("PhoneNumber")
        val phoneNumber: String = "",
        @SerializedName("AlternateContactNo")
        val alternateContactNo: Any? = Any(),
        @SerializedName("Name")
        val name: String = "",
        @SerializedName("UHID")
        val uHID: String = "",
        @SerializedName("Gender")
        val gender: String = "",
        @SerializedName("DateOfBirth")
        val dateOfBirth: String = ""
    )

}
