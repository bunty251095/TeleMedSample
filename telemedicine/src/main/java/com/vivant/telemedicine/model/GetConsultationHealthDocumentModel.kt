package com.vivant.telemedicine.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.vivant.telemedicine.model.base.BaseRequest

class GetConsultationHealthDocumentModel(
    @SerializedName("JSONData")
    @Expose
    private val jsonData: String,
    private val authToken: String) : BaseRequest(Header(authTicket = authToken)) {

    data class JSONDataRequest(
        @SerializedName("DocumentID")
        val documentID: Int = 0
    )

    data class GetConsultationHealthDocumentResponse(
        @SerializedName("HealthRelatedDocument")
        val healthRelatedDocument: HealthRelatedDocument = HealthRelatedDocument()
    )

    data class HealthRelatedDocument(
        @SerializedName("ID")
        val iD: Int = 0,
        @SerializedName("DocumentID")
        val documentID: Int = 0,
        @SerializedName("FileName")
        val fileName: String = "",
        @SerializedName("Title")
        val title: String = "",
        @SerializedName("FileBytes")
        val fileBytes: String = "",
        @SerializedName("DocumentTypeCode")
        val documentTypeCode: String = "",
        @SerializedName("RecordDate")
        val recordDate: String = ""
/*        @SerializedName("Comments")
        val comments: String = "",
        @SerializedName("DoctorName")
        val doctorName: Any? = Any(),
        @SerializedName("DocumentType")
        val documentType: Any? = Any(),
        @SerializedName("FilePath")
        val filePath: Any? = Any(),
        @SerializedName("Keywords")
        val keywords: List<Any> = listOf(),
        @SerializedName("OwnerCode")
        val ownerCode: Any? = Any(),
        @SerializedName("PersonID")
        val personID: Int = 0,
        @SerializedName("PersonName")
        val personName: Any? = Any(),
        @SerializedName("UID")
        val uID: Any? = Any()*/
    )

}
