package com.vivant.telemedicine.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.vivant.telemedicine.model.base.BaseRequest

class GetRequestDetailsModel(
    @SerializedName("JSONData")
    @Expose
    private val jsonData: String,
    private val authToken: String) : BaseRequest(Header(authTicket = authToken)) {

    data class JSONDataRequest(
        @SerializedName("Criteria")
        val criteria: Criteria = Criteria(),
        @SerializedName("RequestedDate")
        val requestedDate: String = ""
    )

    data class Criteria(
        @SerializedName("DoctorID")
        val doctorID: String = "",
        @SerializedName("AccountID")
        val accountID: Any? = Any(),
        @SerializedName("RequestNumber")
        val requestNumber: String = ""
    )

    data class GetRequestDetailsResponse(
        @SerializedName("RequestDetails")
        val requestDetails: RequestDetails = RequestDetails()
    )

    data class RequestDetails(
        @SerializedName("AccountID")
        val accountID: String = "",
        @SerializedName("Age")
        val age: String = "",
        @SerializedName("AlternateContactNo")
        val alternateContactNo: String = "",
        @SerializedName("AppointmentDuration")
        val appointmentDuration: String = "",
        @SerializedName("Availability")
        val availability: Availability = Availability(),
        @SerializedName("ConsultationModeDetails")
        val consultationModeDetails: List<ConsultationModeDetail> = listOf(),
        @SerializedName("DateOfBirth")
        val dateOfBirth: String = "",
        @SerializedName("DefaultSupplierID")
        val defaultSupplierID: String = "",
        @SerializedName("DegreeDetails")
        val degreeDetails: DegreeDetails = DegreeDetails(),
        @SerializedName("Description")
        val description: String = "",
        @SerializedName("DoctorDocuments")
        val doctorDocuments: List<DoctorDocument> = listOf(),
        @SerializedName("DoctorID")
        val doctorID: String = "",
        @SerializedName("EmailAddress")
        val emailAddress: String = "",
        @SerializedName("Empanelments")
        val empanelments: Any? = Any(),
        @SerializedName("Experience")
        val experience: String = "",
        @SerializedName("Fellowships")
        val fellowships: Any? = Any(),
        @SerializedName("Gender")
        val gender: String = "",
        @SerializedName("ID")
        val iD: String = "",
        @SerializedName("IsOptForListing")
        val isOptForListing: String = "",
        @SerializedName("IsOptForOnboard")
        val isOptForOnboard: String = "",
        @SerializedName("LanguageSpoken")
        val languageSpoken: String = "",
        @SerializedName("Name")
        val name: String = "",
        @SerializedName("PrimaryContactNo")
        val primaryContactNo: String = "",
        @SerializedName("ProfileImage")
        val profileImage: ProfileImage = ProfileImage(),
        @SerializedName("ProfileSetting")
        val profileSetting: ProfileSetting = ProfileSetting(),
        @SerializedName("Reason")
        val reason: String = "",
        @SerializedName("RegistrationCouncil")
        val registrationCouncil: String = "",
        @SerializedName("RegistrationCouncilID")
        val registrationCouncilID: String = "",
        @SerializedName("RegistrationNo")
        val registrationNo: String = "",
        @SerializedName("RegistrationYear")
        val registrationYear: String = "",
        @SerializedName("RejectionReason")
        val rejectionReason: String = "",
        @SerializedName("RequestNumber")
        val requestNumber: String = "",
        @SerializedName("SpecialAwards")
        val specialAwards: SpecialAwards = SpecialAwards(),
        @SerializedName("Specialization")
        val specialization: String = "",
        @SerializedName("SpecializationID")
        val specializationID: String = "",
        @SerializedName("StatusCode")
        val statusCode: String = "",
        @SerializedName("StatusDescription")
        val statusDescription: String = "",
        @SerializedName("SupplierDetails")
        val supplierDetails: SupplierDetails = SupplierDetails()
    )

    data class Availability(
        @SerializedName("IsAvailableToday")
        val isAvailableToday: String = "",
        @SerializedName("IsAvailbaleTomorrow")
        val isAvailbaleTomorrow: String = ""
    )

    data class ConsultationModeDetail(
        @SerializedName("ConsultationCharges")
        val consultationCharges: String = "",
        @SerializedName("ConsultationMode")
        val consultationMode: String = ""
    )

    data class DegreeDetails(
        @SerializedName("PrimaryDegree")
        val primaryDegree: String = "",
        @SerializedName("PrimaryDegreeID")
        val primaryDegreeID: String = ""
    )

    data class DoctorDocument(
        @SerializedName("DoctorID")
        val doctorID: String = "",
        @SerializedName("DocumentID")
        val documentID: String = "",
        @SerializedName("DocumentReferanceID")
        val documentReferanceID: String = "",
        @SerializedName("DocumentTypeCode")
        val documentTypeCode: String = "",
        @SerializedName("FileName")
        val fileName: String = "",
        @SerializedName("FileType")
        val fileType: String = "",
        @SerializedName("FileURL")
        val fileURL: String = "",
        @SerializedName("ID")
        val iD: String = "",
        @SerializedName("IsActive")
        val isActive: Boolean = false
    )

    data class ProfileImage(
        @SerializedName("CreatedBy")
        val createdBy: Any? = Any(),
        @SerializedName("CreatedDate")
        val createdDate: Any? = Any(),
        @SerializedName("DoctorName")
        val doctorName: Any? = Any(),
        @SerializedName("DocumentType")
        val documentType: Any? = Any(),
        @SerializedName("DocumentTypeCode")
        val documentTypeCode: String = "",
        @SerializedName("FileBytes")
        val fileBytes: String = "",
        @SerializedName("FileName")
        val fileName: String = "",
        @SerializedName("FilePath")
        val filePath: Any? = Any(),
        @SerializedName("ID")
        val iD: Int = 0,
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
        @SerializedName("Title")
        val title: Any? = Any(),
        @SerializedName("UID")
        val uID: Any? = Any()
    )

    data class ProfileSetting(
        @SerializedName("IsTeleconsultation")
        val isTeleconsultation: String = "",
        @SerializedName("OnlineStatus")
        val onlineStatus: String = "",
        @SerializedName("OptedForAppListing")
        val optedForAppListing: String = "",
        @SerializedName("OptedForConsultNow")
        val optedForConsultNow: String = ""
    )

    data class SpecialAwards(
        @SerializedName("AwardIssuer")
        val awardIssuer: String = "",
        @SerializedName("AwardTitle")
        val awardTitle: String = "",
        @SerializedName("DateAwarded")
        val dateAwarded: String = "",
        @SerializedName("ID")
        val iD: String = ""
    )

    data class SupplierDetails(
        @SerializedName("AddressLine1")
        val addressLine1: String = "",
        @SerializedName("AddressLine2")
        val addressLine2: String = "",
        @SerializedName("ClinicDocuments")
        val clinicDocuments: List<ClinicDocument> = listOf(),
        @SerializedName("ConsultationFees")
        val consultationFees: String = "",
        @SerializedName("DL_SupplierID")
        val dLSupplierID: String = "",
        @SerializedName("DoctorID")
        val doctorID: String = "",
        @SerializedName("EmailAddress")
        val emailAddress: String = "",
        @SerializedName("EstablishmentCertificate")
        val establishmentCertificate: String = "",
        @SerializedName("ID")
        val iD: String = "",
        @SerializedName("IsDefault")
        val isDefault: String = "",
        @SerializedName("Latitude")
        val latitude: String = "",
        @SerializedName("Location")
        val location: String = "",
        @SerializedName("Longitude")
        val longitude: String = "",
        @SerializedName("PrimaryContactNo")
        val primaryContactNo: String = "",
        @SerializedName("RegistrationNumber")
        val registrationNumber: String = "",
        @SerializedName("ScheduleDetails")
        val scheduleDetails: List<ScheduleDetail> = listOf(),
        @SerializedName("ScopeOfServices")
        val scopeOfServices: String = "",
        @SerializedName("SessionData")
        val sessionData: List<SessionData> = listOf(),
        @SerializedName("SupplierID")
        val supplierID: String = "",
        @SerializedName("SupplierName")
        val supplierName: String = ""
    )

    data class ScheduleDetail(
        @SerializedName("EndTime")
        val endTime: String = "",
        @SerializedName("ID")
        val iD: String = "",
        @SerializedName("StartTime")
        val startTime: String = "",
        @SerializedName("SupplierID")
        val supplierID: String = "",
        @SerializedName("WeekDayCode")
        val weekDayCode: String = ""
    )

    data class ClinicDocument(
        @SerializedName("DoctorID")
        val doctorID: String = "",
        @SerializedName("DocumentID")
        val documentID: String = "",
        @SerializedName("DocumentTypeCode")
        val documentTypeCode: String = "",
        @SerializedName("FileName")
        val fileName: String = "",
        @SerializedName("FileType")
        val fileType: String = "",
        @SerializedName("FileURL")
        val fileURL: String = "",
        @SerializedName("ID")
        val iD: String = "",
        @SerializedName("IsActive")
        val isActive: Boolean = false,
        @SerializedName("SupplierID")
        val supplierID: String = ""
    )

    data class SessionData(
        @SerializedName("AppointmentDuration")
        val appointmentDuration: String = "",
        @SerializedName("ConsultationMode")
        val consultationMode: List<String> = listOf(),
        @SerializedName("DoctorID")
        val doctorID: String = "",
        @SerializedName("EndTime")
        val endTime: String = "",
        @SerializedName("ID")
        val iD: String = "",
        @SerializedName("SessionID")
        val sessionID: String = "",
        @SerializedName("StartTime")
        val startTime: String = "",
        @SerializedName("SupplierID")
        val supplierID: String = "",
        @SerializedName("TotalAvailableSlots")
        val totalAvailableSlots: String = "",
        @SerializedName("WeekDayCode")
        val weekDayCode: String = ""
    )

}
