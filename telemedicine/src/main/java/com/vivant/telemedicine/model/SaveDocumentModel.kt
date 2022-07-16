package com.vivant.telemedicine.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.vivant.telemedicine.model.base.BaseRequest

class SaveDocumentModel(
    @SerializedName("JSONData")
    @Expose
    private val jsonData: String,
    private val authToken: String) : BaseRequest(Header(authTicket = authToken)) {

    data class JSONDataRequest(
        @SerializedName("DoctorID")
        val doctorID: Int = 0,
        @SerializedName("AppointmentID")
        val appointmentId: Int = 0,
        @SerializedName("CustomerID")
        val customerID: Int = 0,
        @SerializedName("ClevertapSource")
        val clevertapSource: String = "",
        @SerializedName("HealthDocuments")
        val healthDocuments: List<HealthDocument> = listOf()
    )

    data class HealthDocument(
        @SerializedName("PersonID")
        val personID: Int = 0,
        @SerializedName("DocumentTypeCode")
        val documentTypeCode: String = "",
        @SerializedName("RecordDate")
        val recordDate: String = "",
        @SerializedName("Title")
        val title: String = "",
        @SerializedName("ConsultationID")
        val consultationID: Int = 0,
        @SerializedName("Comments")
        val comments: String = "",
        @SerializedName("CommaKeywords")
        val commaKeywords: String = "",
        @SerializedName("DoctorName")
        val doctorName: String = "",
        @SerializedName("FileName")
        val fileName: String = "",
        @SerializedName("FileBytes")
        val fileBytes: String = ""
    )

    data class SaveDocumentResponse(
        @SerializedName("HealthDocuments")
        val healthDocuments: List<HealthDocumentResp> = listOf()
    )

    data class HealthDocumentResp(
        @SerializedName("CommaKeywords")
        val commaKeywords: String = "",
        @SerializedName("Comments")
        val comments: String = "",
        @SerializedName("ConsultationID")
        val consultationID: Int = 0,
        @SerializedName("DoctorName")
        val doctorName: String = "",
        @SerializedName("DocumentID")
        val documentID: Int = 0,
        @SerializedName("DocumentTypeCode")
        val documentTypeCode: String = "",
        @SerializedName("FileBytes")
        val fileBytes: String = "",
        @SerializedName("FileName")
        val fileName: String = "",
        @SerializedName("HealthRelatedDocumentID")
        val healthRelatedDocumentID: Int = 0,
        @SerializedName("PersonID")
        val personID: Int = 0,
        @SerializedName("RecordDate")
        val recordDate: String = "",
        @SerializedName("Title")
        val title: String = ""
    )

}
