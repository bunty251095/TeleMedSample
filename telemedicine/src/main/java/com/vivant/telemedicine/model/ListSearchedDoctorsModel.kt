package com.vivant.telemedicine.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.vivant.telemedicine.model.base.BaseRequest

class ListSearchedDoctorsModel(
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
        @SerializedName("Speciality")
        val speciality: List<String> = listOf(),
        @SerializedName("Gender")
        val gender: List<Any> = listOf(),
        @SerializedName("Availability")
        val availability: List<Any> = listOf(),
        @SerializedName("ConsultationModes")
        val consultationModes: List<String> = listOf(),
        @SerializedName("ConsultationFees")
        val consultationFees: List<Any> = listOf(),
        @SerializedName("Location")
        val location: List<Any> = listOf(),
        @SerializedName("“SortBy”")
        val sortBy: String = "",
        @SerializedName("Symptoms")
        val symptoms: List<Any> = listOf(),
        @SerializedName("Supplier")
        val supplier: List<Any> = listOf(),
        @SerializedName("DoctorName")
        val doctorName: String = "",
        @SerializedName("PagingCriteria")
        val pagingCriteria: PagingCriteria = PagingCriteria()
    )

    data class PagingCriteria(
        @SerializedName("CurrentPage")
        val currentPage: Int = 1,
        @SerializedName("PageSize")
        val pageSize: Int = 0
    )

    data class ListSearchedDoctorsResponse(
        @SerializedName("Doctors")
        val doctors: Doctors = Doctors()
    )

    data class Doctors(
        @SerializedName("List")
        val list: MutableList<DoctorDetail> = mutableListOf(),
        @SerializedName("CurrentPage")
        val currentPage: Int = 0,
        @SerializedName("TotalRecords")
        val totalRecords: Int = 0,
        @SerializedName("PageSize")
        val pageSize: Int = 0
    )

    data class DoctorDetail (
        @SerializedName("AppointmentDuration")
        val appointmentDuration: Int = 0,
        @SerializedName("AudioActive")
        val audioActive: Boolean = false,
        @SerializedName("AudioCode")
        val audioCode: String = "",
        @SerializedName("AudioPrice")
        val audioPrice: String = "",
        @SerializedName("Availability")
        val availability: Availability = Availability(),
        @SerializedName("ChatActive")
        val chatActive: Boolean = false,
        @SerializedName("ChatCode")
        val chatCode: String = "",
        @SerializedName("ChatPrice")
        val chatPrice: String = "",
        @SerializedName("ClinicAddress")
        val clinicAddress: String = "",
        @SerializedName("ClinicName")
        val clinicName: String = "",
        @SerializedName("ConsultationFees")
        val consultationFees: Double = 0.0,
        @SerializedName("CreatedDate")
        val createdDate: String = "",
        @SerializedName("DocumentID")
        val documentID: Any? = Any(),
        @SerializedName("FirstName")
        val firstName: String = "",
        @SerializedName("Gender")
        val gender: String = "",
        @SerializedName("ID")
        val id: Int = 0,
        @SerializedName("IsAvailable")
        val isAvailable: Int = 0,
        @SerializedName("LanguageSpoken")
        val languageSpoken: String = "",
        @SerializedName("LastName")
        val lastName: Any? = Any(),
        @SerializedName("PracticingSince")
        val practicingSince: Any? = Any(),
/*        @SerializedName("ProfileImage")
        val profileImage: Any? = Any(),*/
        @SerializedName("ProfileImage")
        val profileImage: ProfileImage = ProfileImage(),
        @SerializedName("RegistrationNumber")
        val registrationNumber: Any? = Any(),
        @SerializedName("RowNumber")
        val rowNumber: Int = 0,
        @SerializedName("Speciality")
        val speciality: String = "",
        @SerializedName("SpecializationID")
        val specializationID: Int = 0,
        @SerializedName("SupplierID")
        val supplierID: Int = 0,
        @SerializedName("VideoActive")
        val videoActive: Boolean = false,
        @SerializedName("VideoCode")
        val videoCode: String = "",
        @SerializedName("VideoPrice")
        val videoPrice: String = "",
        @SerializedName("YearsOfPractice")
        val yearsOfPractice: String = ""
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

    data class Availability(
        @SerializedName("IsAvailableToday")
        val isAvailableToday: String = "",
        @SerializedName("IsAvailbaleTomorrow")
        val isAvailbaleTomorrow: String = ""
    )

}