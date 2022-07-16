package com.vivant.telemedicine.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.vivant.telemedicine.model.base.BaseRequest

class ListConsultationModel(
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
        @SerializedName("CustomerID")
        val customerID: Int = 0,
        @SerializedName("DoctorID")
        val doctorID: Any? = null,
        @SerializedName("AppointmentID")
        val appointmentID: Any? = null,
        @SerializedName("ConsultationIDs")
        val consultationIDs: Any? = null
    )

    data class PagingCriteria(
        @SerializedName("CurrentPage")
        val currentPage: Int = 1,
        @SerializedName("PageDirection")
        val pageDirection: Int = 0,
        @SerializedName("PageSize")
        val pageSize: Int = 0,
        @SerializedName("SortColumn")
        val sortColumn: Any? = null,
        @SerializedName("SortOrder")
        val sortOrder: Int = 0
    )

    data class ListConsultationResponse(
        @SerializedName("ConsultationDetails")
        val consultationDetails: ConsultationDetails = ConsultationDetails()
    )

    data class ConsultationDetails(
        @SerializedName("ConsultationList")
        val consultationList: List<Consultation> = listOf(),
        @SerializedName("PageAttributes")
        val pageAttributes: PageAttributes = PageAttributes()
    )

    data class Consultation(
        @SerializedName("ID")
        val iD: String = "",
        @SerializedName("AppointmentID")
        val appointmentID: String = "",
        @SerializedName("OrderID")
        val orderID: Int = 0,
        @SerializedName("DoctorID")
        val doctorID: String = "",
        @SerializedName("CustomerID")
        val customerID: String = "",
        @SerializedName("PersonID")
        val personID: String = "",
        @SerializedName("IsFeedbackAdded")
        var isFeedbackAdded: Boolean = false,
        @SerializedName("IsRefundDone")
        val isRefundDone: String = "",
        @SerializedName("IsRefundProcess")
        val isRefundProcess: String = "",
        @SerializedName("Status")
        val status: String = "",
        @SerializedName("AppointmentStatus")
        val appointmentStatus: String = "",
        @SerializedName("AppointmentMode")
        val appointmentMode: String = "",
        @SerializedName("AppointmentCategory")
        val appointmentCategory: String = "",
        @SerializedName("ConsultationDate")
        val consultationDate: String = "",
        @SerializedName("AppointmentDate")
        val appointmentDate: String = "",
        @SerializedName("AppointmentStartTime")
        val appointmentStartTime: String = "",
        @SerializedName("AppointmentEndTime")
        val appointmentEndTime: String = "",
        @SerializedName("BookedForName")
        val bookedForName: String = "",
        @SerializedName("BookedForFirstName")
        val bookedForFirstName: String = "",
        @SerializedName("BookedForLastName")
        val bookedForLastName: String = "",
        @SerializedName("EmailAddress")
        val emailAddress: String = "",
        @SerializedName("PhoneNumber")
        val phoneNumber: String = "",
        @SerializedName("Gender")
        val gender: String = "",
        @SerializedName("Age")
        val age: String = "",
        @SerializedName("SharedDocuments")
        val sharedDocuments: MutableList<SharedDocument> = mutableListOf(),
        @SerializedName("Attachments")
        val attachments: MutableList<Attachments>? = mutableListOf(),
        @SerializedName("Symptoms")
        val symptoms: MutableList<Symptom>? = mutableListOf(),
        @SerializedName("Medicines")
        val medicines: MutableList<Medicine>? = mutableListOf(),
        @SerializedName("Advice")
        val advice: MutableList<Advice>? = mutableListOf(),
        @SerializedName("Investigation")
        val investigation: MutableList<Investigation>? = mutableListOf(),
        /*@SerializedName("Attachments")
        val attachments: Any? = Any(),*/
        /*@SerializedName("Symptoms")
        val symptoms: Any? = Any(),*/
/*        @SerializedName("Medicines")
        val medicines: Any? = Any(),*/
        /*@SerializedName("Advice")
        val advice: Any? = Any(),*/
        /*@SerializedName("Investigation")
        val investigation: Any ?= null,*/
        @SerializedName("AppointmentDetails")
        val appointmentDetails: AppointmentDetails = AppointmentDetails(),
        @SerializedName("DoctorDetails")
        val doctorDetails: DoctorDetails = DoctorDetails(),
        @SerializedName("Diseases")
        val diseases: Any? = Any(),
        @SerializedName("Examination")
        val examination: Examination = Examination(),
        @SerializedName("ExaminationNote")
        val examinationNote: Any? = Any(),
        @SerializedName("FormatedDate")
        val formatedDate: String = "",
        @SerializedName("IsTeleConsultation")
        val isTeleConsultation: String = "",
        @SerializedName("LastUpdatedDate")
        val lastUpdatedDate: String = "",
        @SerializedName("Procedures")
        val procedures: Any? = Any(),
        @SerializedName("ReferredDoctors")
        val referredDoctors: Any? = Any(),
        @SerializedName("SupplierID")
        val supplierID: Int = 0,
        @SerializedName("UHID")
        val uHID: String = ""
    )

    data class Symptom(
        @SerializedName("ConsultationID")
        val consultationID: String = "",
        @SerializedName("DoctorID")
        val doctorID: String = "",
        @SerializedName("ID")
        val iD: String = "",
        @SerializedName("MediusID")
        val mediusID: String = "",
        @SerializedName("SMCTID")
        val sMCTID: String = "",
        @SerializedName("Status")
        val status: String = "",
        @SerializedName("Symptom")
        val symptom: String = "",
        @SerializedName("SymptomID")
        val symptomID: String = ""
    )

    data class Advice(
        @SerializedName("Advice")
        val advice: String = "",
        @SerializedName("ConsultationID")
        val consultationID: Int = 0,
        @SerializedName("DoctorID")
        val doctorID: Int = 0,
        @SerializedName("FollowUpDate")
        val followUpDate: Any? = Any(),
        @SerializedName("ID")
        val iD: Int = 0,
        @SerializedName("Status")
        val status: String = ""
    )

    data class Investigation(
        @SerializedName("ID")
        val iD: Int = 0,
        @SerializedName("DoctorID")
        val doctorID: Int = 0,
        @SerializedName("ConsultationID")
        val consultationID: Int = 0,
        @SerializedName("LabTestID")
        val labTestID: Int = 0,
        @SerializedName("Instructions")
        val instructions: String = "",
        @SerializedName("Status")
        val status: String = "",
        @SerializedName("Name")
        val name: String = ""
/*        @SerializedName("Name")
        val name: String = "",
        @SerializedName("MediusID")
        val mediusID: Any? = Any(),
        @SerializedName("OrdersetID")
        val ordersetID: Any? = Any()*/
    )

    data class Medicine(
        @SerializedName("Medicines")
        val medicinesDetail: Any? = Any(),
        @SerializedName("DrugName")
        val drugName: String? = "",
        @SerializedName("ID")
        val iD: Int? = 0,
        @SerializedName("DoctorID")
        val doctorID: Int? = 0,
        @SerializedName("ConsultationID")
        val consultationID: Int? = 0,
        @SerializedName("Frequency")
        val frequency: String? = "",
        @SerializedName("Duration")
        val duration: String? = "",
        @SerializedName("DurationIn")
        val durationIn: String? = "",
        @SerializedName("Instruction")
        val instruction: String? = "",
        @SerializedName("Instructions")
        val instructions: String? = "",
        @SerializedName("Quantity")
        val quantity: String? = "",
        @SerializedName("Status")
        val status: String? = "",

/*        @SerializedName("Doses")
        val doses: String? = "",
        @SerializedName("DosesUnit")
        val dosesUnit: String? = "",
        @SerializedName("DrugID")
        val drugID: Any? = Any(),
        @SerializedName("DrugIndexID")
        val drugIndexID: Any? = Any(),
        @SerializedName("EMED_ID")
        val eMEDID: Any? = Any(),
        @SerializedName("EntryType")
        val entryType: String? = "",
        @SerializedName("FrequencyPattern")
        val frequencyPattern: String? = "",
        @SerializedName("GenericName")
        val genericName: Any? = Any(),
        @SerializedName("OrdersetID")
        val ordersetID: Any? = Any(),
        @SerializedName("ScheduleType")
        val scheduleType: Any? = Any(),
        @SerializedName("Strength")
        val strength: String? = "",
        @SerializedName("Type")
        val type: String? = "",
        @SerializedName("TypeID")
        val typeID: Any? = Any()*/
    )

    data class AppointmentDetails(
        @SerializedName("Age")
        val age: String = "",
        @SerializedName("AlternateContactNo")
        val alternateContactNo: String = "",
        @SerializedName("AppointmentCategory")
        val appointmentCategory: String = "",
        @SerializedName("AppointmentDate1")
        val appointmentDate1: String = "",
        @SerializedName("AppointmentDate2")
        val appointmentDate2: String = "",
        @SerializedName("AppointmentEndTime")
        val appointmentEndTime: String = "",
        @SerializedName("AppointmentID")
        val appointmentID: String = "",
        @SerializedName("AppointmentMode")
        val appointmentMode: String = "",
        @SerializedName("AppointmentStartTime")
        val appointmentStartTime: String = "",
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
        @SerializedName("CustomerEmail")
        val customerEmail: String = "",
        @SerializedName("CustomerID")
        val customerID: String = "",
        @SerializedName("DateOfBirth")
        val dateOfBirth: String = "",
        @SerializedName("DoctorName")
        val doctorName: String = "",
        @SerializedName("Duration")
        val duration: String = "",
        @SerializedName("EmailAddress")
        val emailAddress: String = "",
        @SerializedName("EmployeeID")
        val employeeID: String = "",
        @SerializedName("FinalPrice")
        val finalPrice: String = "",
        @SerializedName("Gender")
        val gender: String = "",
        @SerializedName("IsFeedbackAdded")
        val isFeedbackAdded: Boolean = false,
        @SerializedName("IsOrderMedicine")
        var isOrderMedicine: Boolean = false,
        @SerializedName("IsRefundDone")
        val isRefundDone: String = "",
        @SerializedName("IsRefundProcess")
        val isRefundProcess: String = "",
        @SerializedName("LabContactNumber")
        val labContactNumber: String = "",
        @SerializedName("LabEmail")
        val labEmail: String = "",
        @SerializedName("LabName")
        val labName: String = "",
        @SerializedName("LabPhone")
        val labPhone: String = "",
        @SerializedName("Location")
        val location: String = "",
        @SerializedName("ProblemStatement")
        val problemStatement: String? = "",
        @SerializedName("OngoingMedication")
        val ongoingMedication: String? = "",
        @SerializedName("OrderID")
        val orderID: String = "",
        @SerializedName("PartnerCode")
        val partnerCode: String = "",
        @SerializedName("PartnerName")
        val partnerName: String = "",
        @SerializedName("PatientID")
        val patientID: String = "",
        @SerializedName("PhoneNumber")
        val phoneNumber: String = "",
        @SerializedName("ProblemStartDate")
        val problemStartDate: String = "",
        @SerializedName("RegistrationNo")
        val registrationNo: String = "",
        @SerializedName("RoomName")
        val roomName: String = "",
        @SerializedName("Severity")
        val severity: String = "",
        @SerializedName("SignaturePath")
        val signaturePath: String = "",
        @SerializedName("Specialization")
        val specialization: String = "",
        @SerializedName("SupplierID")
        val supplierID: String = "",
        @SerializedName("TokenNumber")
        val tokenNumber: String = "",
        @SerializedName("UHID")
        val uHID: String = "",
        @SerializedName("WorkingDays")
        val workingDays: String = ""
    )

    data class DoctorDetails(
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
        val clinicDocuments: Any? = Any(),
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

    data class Examination(
        @SerializedName("BMI")
        val bMI: String = "",
        @SerializedName("ConsultationID")
        val consultationID: String = "",
        @SerializedName("DiastolicBP")
        val diastolicBP: String = "",
        @SerializedName("DoctorID")
        val doctorID: String = "",
        @SerializedName("Examinations")
        val examinations: String = "",
        @SerializedName("Height")
        val height: String = "",
        @SerializedName("HeightUnit")
        val heightUnit: String = "",
        @SerializedName("ID")
        val iD: String = "",
        @SerializedName("Pulse")
        val pulse: String = "",
        @SerializedName("RespiratoryRate")
        val respiratoryRate: String = "",
        @SerializedName("Status")
        val status: String = "",
        @SerializedName("SystolicBP")
        val systolicBP: String = "",
        @SerializedName("Temperature")
        val temperature: String = "",
        @SerializedName("Weight")
        val weight: String = "",
        @SerializedName("WeightUnit")
        val weightUnit: String = ""
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

    data class AttachmentsList(
        val attachmentsList: MutableList<Attachments> = mutableListOf(),
    )

    data class Attachments(
        @SerializedName("ID")
        val iD: Int = 0,
        @SerializedName("HealthDocumentID")
        val healthDocumentID: Int = 0,
        @SerializedName("DocumentTypeCode")
        val documentTypeCode: String = "",
        @SerializedName("FileName")
        val fileName: String = "",
        @SerializedName("ConsultationDate")
        val consultationDate: String = "",
        @SerializedName("RelatedTo")
        val relatedTo: String = "",
        @SerializedName("DoctorID")
        val doctorID: Int = 0,
        @SerializedName("ConsultationID")
        val consultationID: Int = 0,
        @SerializedName("ConsultationDocID")
        val consultationDocID: Int = 0
/*        @SerializedName("UploadedDate")
        val uploadedDate: String = "",
        @SerializedName("Extension")
        val extension: Any? = Any(),
        @SerializedName("FileURL")
        val fileURL: Any? = Any(),
        @SerializedName("Status")
        val status: String = "",
        @SerializedName("UploadedBy")
        val uploadedBy: Int = 0*/
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

    data class PageAttributes(
        @SerializedName("TotalRecords")
        val totalRecords: Int = 0,
        @SerializedName("CurrentPage")
        val currentPage: Int = 0,
        @SerializedName("TotalNumberOfPages")
        val totalNumberOfPages: Int = 0,
        @SerializedName("PageSize")
        val pageSize: Int = 0,
        @SerializedName("SortOrder")
        val sortOrder: Int = 0
    )

}
