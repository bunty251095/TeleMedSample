package com.vivant.telemedicine.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.vivant.telemedicine.model.base.BaseRequest

class ListDocumentModel(
    @SerializedName("JSONData")
    @Expose
    private val jsonData: String,
    private val authToken: String) : BaseRequest(Header(authTicket = authToken)) {

    data class JSONDataRequest(
        @SerializedName("SearchCriteria")
        val searchCriteria: SearchCriteria = SearchCriteria(),
    )

    data class SearchCriteria(
        @SerializedName("PersonID")
        val personID: Int = 0,
        @SerializedName("Mode")
        val mode: String = "FM",
        @SerializedName("PagingCriteria")
        val pagingCriteria: PagingCriteria = PagingCriteria()
    )

    data class PagingCriteria(
        @SerializedName("CurrentPage")
        val currentPage: Int = 1,
        @SerializedName("PageSize")
        val pageSize: Int = 0
    )

    data class ListDocumentResponse(
        @SerializedName("HealthRelatedDocument")
        val healthRelatedDocument: MutableList<HealthRelatedDocument> = mutableListOf(),
        @SerializedName("PageAttributes")
        val pageAttributes: PageAttributes = PageAttributes()
    )

    data class HealthRelatedDocument(
        @SerializedName("ID")
        val id: Int = 0,
        @SerializedName("DocumentID")
        val documentID: Int = 0,
        @SerializedName("PersonID")
        val personID: Int = 0,
        @SerializedName("PersonName")
        val personName: String = "",
        @SerializedName("DocumentTypeCode")
        val documentTypeCode: String = "",
        @SerializedName("FileName")
        val fileName: String = "",
        @SerializedName("Title")
        val title: String = "",
        @SerializedName("FileBytes")
        val fileBytes: Any? = Any(),
        @SerializedName("DocumentType")
        val documentType: DocumentType = DocumentType(),
        @SerializedName("Keywords")
        val keywords: MutableList<String> = mutableListOf(),
        @SerializedName("Comments")
        val comments: String = "",
        @SerializedName("RecordDate")
        val recordDate: String = "",
        @SerializedName("OwnerCode")
        val ownerCode: Any? = Any(),
        @SerializedName("FilePath")
        val filePath: Any? = Any(),
        @SerializedName("DoctorName")
        val doctorName: Any? = Any(),
        @SerializedName("UID")
        val uID: Any? = Any(),
        @SerializedName("CreatedBy")
        val createdBy: Int = 0,
        @SerializedName("CreatedDate")
        val createdDate: String = "",
        @SerializedName("ModifiedBy")
        val modifiedBy: Int = 0,
        @SerializedName("ModifiedDate")
        val modifiedDate: String = "",
    )

    data class DocumentType(
        @SerializedName("ID")
        val id: Int = 0,
        @SerializedName("Code")
        val code: String = "",
        @SerializedName("Description")
        val description: String = "",
        @SerializedName("CreatedBy")
        val createdBy: Any? = Any(),
        @SerializedName("CreatedDate")
        val createdDate: Any? = Any(),
        @SerializedName("ModifiedBy")
        val modifiedBy: Any? = Any(),
        @SerializedName("ModifiedDate")
        val modifiedDate: Any? = Any()
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
