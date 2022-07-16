package com.vivant.telemedicine.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.vivant.telemedicine.model.base.BaseRequest

class ListAppointmentsModel(
    @SerializedName("JSONData")
    @Expose
    private val jsonData: String,
    private val authToken: String) : BaseRequest(Header(authTicket = authToken)) {

    data class JSONDataRequest(
        @SerializedName("Criteria")
        val criteria: Criteria = Criteria(),
        @SerializedName("PagingCriteria")
        val pagingCriteria: PagingCriteria = PagingCriteria()
    )

    data class Criteria(
        @SerializedName("AccountID")
        val accountID: String = "",
        @SerializedName("AppointmentDate")
        val appointmentDate: String = "",
        @SerializedName("AppointmentStatus")
        val appointmentStatus: String = "",
        @SerializedName("CustomerID")
        val customerID: Int = 0,
        @SerializedName("DoctorID")
        val doctorID: String = "",
        @SerializedName("EndDate")
        val endDate: String = "",
        @SerializedName("OpMode")
        val opMode: String = "",
        @SerializedName("RequestDate")
        val requestDate: String = "",
        @SerializedName("RequestTime")
        val requestTime: String = "",
        @SerializedName("SortBy")
        val sortBy: String = "",
        @SerializedName("StartDate")
        val startDate: String = "",
        @SerializedName("SupplierID")
        val supplierID: String = ""
    )

    data class PagingCriteria(
        @SerializedName("CurrentPage")
        val currentPage: Int = 0,
        @SerializedName("PageSize")
        val pageSize: Int = 0,
        @SerializedName("StartIndex")
        val startIndex: Int = 0
    )

    data class ListAppointmentsResponse(
        @SerializedName("AppointmentList")
        val appointmentList: AppointmentList = AppointmentList()
    )

    data class AppointmentList(
        @SerializedName("TodaysAppointment")
        val todaysAppointment: MutableList<UpcomingAppointment> = mutableListOf(),
        @SerializedName("UpcomingAppointment")
        val upcomingAppointment: MutableList<UpcomingAppointment> = mutableListOf(),
        @SerializedName("PastAppointment")
        val pastAppointment: MutableList<UpcomingAppointment> = mutableListOf(),
        @SerializedName("PageAttributes")
        val pageAttributes: PageAttributes = PageAttributes()
    )

    data class UpcomingAppointment(
        @SerializedName("ID")
        val iD: Int = 0,
        @SerializedName("AccountID")
        val accountID: Int = 0,
        @SerializedName("Age")
        val age: String = "",
        @SerializedName("AlternateContactNo")
        val alternateContactNo: String = "",
        @SerializedName("AppointmentCategory")
        val appointmentCategory: String = "",
        @SerializedName("AppointmentDate")
        val appointmentDate: String = "",
        @SerializedName("AppointmentEndTime")
        val appointmentEndTime: String = "",
        @SerializedName("AppointmentFor")
        val appointmentFor: Any? = Any(),
        @SerializedName("AppointmentMode")
        val appointmentMode: String = "",
        @SerializedName("AppointmentSource")
        val appointmentSource: String = "",
        @SerializedName("AppointmentStartTime")
        val appointmentStartTime: String? = "",
        @SerializedName("AppointmentStatus")
        val appointmentStatus: String = "",
        @SerializedName("AppointmentType")
        val appointmentType: String = "",
        @SerializedName("BookedForFirstName")
        val bookedForFirstName: String = "",
        @SerializedName("BookedForLastName")
        val bookedForLastName: String = "",
        @SerializedName("BookedForName")
        val bookedForName: String = "",
        @SerializedName("BookingFor")
        val bookingFor: String = "",
        @SerializedName("CancelReasonCode")
        val cancelReasonCode: Any? = Any(),
        @SerializedName("CancelReasonTxt")
        val cancelReasonTxt: Any? = Any(),
        @SerializedName("ChatExpiryinDays")
        val chatExpiryinDays: Int = 0,
        @SerializedName("ChatExpiryinHours")
        val chatExpiryinHours: Int = 0,
        @SerializedName("ConsultationFees")
        val consultationFees: Double = 0.0,
        @SerializedName("CustomerID")
        val customerID: Int = 0,
        @SerializedName("DateOfBirth")
        val dateOfBirth: String = "",
        @SerializedName("DisplayOrder")
        val displayOrder: Int = 0,
        @SerializedName("DoctorAccountID")
        val doctorAccountID: Int = 0,
        @SerializedName("DoctorGender")
        val doctorGender: String = "",
        @SerializedName("DoctorID")
        val doctorID: Int = 0,
        @SerializedName("DoctorListingID")
        val doctorListingID: Any? = Any(),
        @SerializedName("DocumentID")
        val documentID: Any? = Any(),
        @SerializedName("Duration")
        val duration: Int = 0,
        @SerializedName("EmailAddress")
        val emailAddress: String = "",
        @SerializedName("FirstName")
        val firstName: String = "",
        @SerializedName("Gender")
        val gender: String = "",
        @SerializedName("IsActive")
        val isActive: Boolean = false,
        @SerializedName("IsConsentAccepted")
        val isConsentAccepted: String = "",
        @SerializedName("IsPrescriptionUploaded")
        val isPrescriptionUploaded: Boolean = false,
        @SerializedName("IsRefundApplicable")
        val isRefundApplicable: Any? = Any(),
        @SerializedName("IsRefundDone")
        val isRefundDone: Any? = Any(),
        @SerializedName("IsRefundProcess")
        val isRefundProcess: Any? = Any(),
        @SerializedName("LastName")
        val lastName: Any? = Any(),
        @SerializedName("ModifiedDate")
        val modifiedDate: String = "",
        @SerializedName("ProblemStatement")
        val problemStatement: String? = "",
        @SerializedName("OngoingMedication")
        val ongoingMedication: String? = "",
        @SerializedName("OrderID")
        val orderID: Int = 0,
        @SerializedName("PaymentStatus")
        val paymentStatus: String = "",
        @SerializedName("PhoneNumber")
        val phoneNumber: String = "",
        @SerializedName("ProblemStartDate")
        val problemStartDate: String = "",
        @SerializedName("RecordExpiryinDays")
        val recordExpiryinDays: Int = 0,
        @SerializedName("RoomID")
        val roomID: Any? = Any(),
        @SerializedName("RoomName")
        val roomName: String = "",
        @SerializedName("RowNumber")
        val rowNumber: Int = 0,
        @SerializedName("SellerOrderID")
        val sellerOrderID: Int = 0,
        @SerializedName("SessionEndTime")
        val sessionEndTime: Any? = Any(),
        @SerializedName("SessionStartTime")
        val sessionStartTime: Any? = Any(),
        @SerializedName("Severity")
        val severity: String = "",
        @SerializedName("SharedDocuments")
        val sharedDocuments: MutableList<SharedDocument> = mutableListOf(),
        @SerializedName("Specialization")
        val specialization: String = "",
        @SerializedName("SupplierAddress")
        val supplierAddress: String = "",
        @SerializedName("SupplierID")
        val supplierID: Int = 0,
        @SerializedName("SupplierListingID")
        val supplierListingID: Any? = Any(),
        @SerializedName("SupplierName")
        val supplierName: String = "",
        @SerializedName("TokenNumber")
        val tokenNumber: String = "",
        @SerializedName("UHID")
        val uHID: String = "",
        @SerializedName("UPID")
        val uPID: Any? = Any(),
        @SerializedName("WaitingEndTime")
        val waitingEndTime: Any? = Any(),
        @SerializedName("WaitingStartTime")
        val waitingStartTime: Any? = Any(),
        @SerializedName("YearsOfPractice")
        val yearsOfPractice: String = "",
        @SerializedName("ProfileImage")
        val profileImage: ProfileImage = ProfileImage()
    )

    data class SharedDocument(
        @SerializedName("AppointmentID")
        val appointmentID: Int = 0,
        @SerializedName("CreatedBy")
        val createdBy: Int = 0,
        @SerializedName("CreatedDate")
        val createdDate: String = "",
        @SerializedName("CustomerID")
        val customerID: Int = 0,
        @SerializedName("DoctorID")
        val doctorID: Int = 0,
        @SerializedName("DocumentID")
        val documentID: Int = 0,
        @SerializedName("DocumentTypeCode")
        val documentTypeCode: String = "",
        @SerializedName("FileName")
        val fileName: String = "",
        @SerializedName("ID")
        val iD: Int = 0,
        @SerializedName("IsActive")
        val isActive: Boolean = false,
        @SerializedName("ModifiedBy")
        val modifiedBy: Int = 0,
        @SerializedName("ModifiedDate")
        val modifiedDate: String = "",
        @SerializedName("PersonID")
        val personID: Int = 0,
        @SerializedName("RelatedTo")
        val relatedTo: String = "",
        @SerializedName("SharedDate")
        val sharedDate: String = ""
    )

    data class ProfileImage(
        @SerializedName("ID")
        val id: Int = 0,
        @SerializedName("DocumentTypeCode")
        val documentTypeCode: String = "",
        @SerializedName("FileName")
        val fileName: String = "",
        @SerializedName("FileBytes")
        val fileBytes: String = "",
        @SerializedName("Title")
        val title: Any? = Any(),
        @SerializedName("DocumentType")
        val documentType: Any? = Any(),
        @SerializedName("CreatedBy")
        val createdBy: Any? = Any(),
        @SerializedName("CreatedDate")
        val createdDate: Any? = Any(),
        @SerializedName("DoctorName")
        val doctorName: Any? = Any(),
        @SerializedName("FilePath")
        val filePath: Any? = Any(),
        @SerializedName("IsActive")
        val isActive: Boolean = false,
        @SerializedName("ModifiedBy")
        val modifiedBy: Any? = Any(),
        @SerializedName("ModifiedDate")
        val modifiedDate: Any? = Any(),
        @SerializedName("OwnerCode")
        val ownerCode: Any? = Any(),
        @SerializedName("RecordDate")
        val recordDate: String = "",
        @SerializedName("UID")
        val uID: Any? = Any()
    )

    data class PageAttributes(
        @SerializedName("CurrentPage")
        val currentPage: Int = 0,
        @SerializedName("PageSize")
        val pageSize: Int = 0,
        @SerializedName("SortOrder")
        val sortOrder: Int = 0,
        @SerializedName("TotalNumberOfPages")
        val totalNumberOfPages: Int = 0,
        @SerializedName("TotalRecords")
        val totalRecords: Int = 0
    )

}
