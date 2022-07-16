package com.vivant.telemedicine.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.vivant.telemedicine.model.base.BaseRequest

class DownloadRecordModel(
    @SerializedName("JSONData")
    @Expose
    private val jsonData: String,
    private val authToken: String) : BaseRequest(Header(authTicket = authToken)) {

    data class JSONDataRequest(
/*        @SerializedName("PersonID")
        val personID: Int = 0,*/
        @SerializedName("DocumentID")
        val documentID: Int = 0
    )

    data class DownloadRecordResponse(
        @SerializedName("HealthRelatedDocument")
        val healthRelatedDocument: HealthRelatedDocument = HealthRelatedDocument()
    )

    data class HealthRelatedDocument(
        @SerializedName("Comments")
        val comments: String = "",
        @SerializedName("CreatedBy")
        val createdBy: Int = 0,
        @SerializedName("CreatedDate")
        val createdDate: String = "",
        @SerializedName("DoctorName")
        val doctorName: String = "",
        @SerializedName("DocumentID")
        val documentID: Int = 0,
        @SerializedName("DocumentType")
        val documentType: DocumentType = DocumentType(),
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
        @SerializedName("Keywords")
        val keywords: List<Any> = listOf(),
        @SerializedName("ModifiedBy")
        val modifiedBy: Int = 0,
        @SerializedName("ModifiedDate")
        val modifiedDate: String = "",
        @SerializedName("OwnerCode")
        val ownerCode: Any? = Any(),
        @SerializedName("PersonID")
        val personID: Int = 0,
        @SerializedName("PersonName")
        val personName: Any? = Any(),
        @SerializedName("RecordDate")
        val recordDate: String = "",
        @SerializedName("Title")
        val title: String = "",
        @SerializedName("UID")
        val uID: Any? = Any()
    )

    data class DocumentType(
        @SerializedName("Code")
        val code: String = "",
        @SerializedName("CreatedBy")
        val createdBy: Any? = Any(),
        @SerializedName("CreatedDate")
        val createdDate: Any? = Any(),
        @SerializedName("Description")
        val description: String = "",
        @SerializedName("ID")
        val iD: Int = 0,
        @SerializedName("ModifiedBy")
        val modifiedBy: Any? = Any(),
        @SerializedName("ModifiedDate")
        val modifiedDate: Any? = Any()
    )

}
