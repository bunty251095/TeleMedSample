package com.vivant.telemedicine.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.vivant.telemedicine.model.base.BaseRequest

class ShareDocumentModel(
    @SerializedName("JSONData")
    @Expose
    private val jsonData: String,
    private val authToken: String) : BaseRequest(Header(authTicket = authToken)) {

    data class JSONDataRequest(
        @SerializedName("AppointmentID")
        val appointmentID: Int = 0,
        @SerializedName("DoctorID")
        val doctorID: Int = 0,
        @SerializedName("CustomerID")
        val customerID: Int = 0,
        @SerializedName("DocDetails")
        val docDetails: DocDetails = DocDetails()
    )

    data class DocDetails(
        @SerializedName("EntityID")
        val entityID: Int = 0,
        @SerializedName("ExpiryDate")
        val expiryDate: String = "",
        @SerializedName("Comments")
        val comments: String = "",
        @SerializedName("SharedWith")
        val sharedWith: List<SharedWith> = listOf(),
        @SerializedName("DocumentList")
        val documentList: List<Document> = listOf()
    )

    data class SharedWith(
        @SerializedName("ID")
        val id: Int = 0,
        @SerializedName("Name")
        val name: String = "",
        @SerializedName("Email")
        val email: String = "",
        @SerializedName("PhoneNumber")
        val phoneNumber: String = ""
    )

    data class Document(
        @SerializedName("LogID")
        val logID: Int = 0,
        @SerializedName("Title")
        val title: String = "",
        @SerializedName("DocumentID")
        val documentID: Int = 0,
        @SerializedName("DocumentTypeCode")
        val documentTypeCode: String = "",
        @SerializedName("RelatedTo")
        val relatedTo: String = ""
    )

    data class ShareDocumentResponse(
        @SerializedName("DocumentSharingRecord")
        val documentSharingRecord: DocumentSharingRecord = DocumentSharingRecord()
    )

    data class DocumentSharingRecord(
        @SerializedName("ID")
        val id: Int = 0,
        @SerializedName("AccountID")
        val accountID: Int = 0,
        @SerializedName("OwnerID")
        val ownerID: Int = 0,
        @SerializedName("EntityID")
        val entityID: Int = 0,
        @SerializedName("EntityType")
        val entityType: Any? = Any(),
        @SerializedName("AccessKey")
        val accessKey: Any? = Any(),
        @SerializedName("Hash")
        val hash: Any? = Any(),
        @SerializedName("ExpiryDate")
        val expiryDate: String = "",
        @SerializedName("Comments")
        val comments: String = "",
        @SerializedName("SharedWith")
        val sharedWith: List<SharedWithResp> = listOf(),
        @SerializedName("DocumentList")
        val documentList: List<DocumentResp> = listOf(),
        @SerializedName("CreatedBy")
        val createdBy: Any? = Any(),
        @SerializedName("CreatedDate")
        val createdDate: Any? = Any(),
        @SerializedName("ModifiedBy")
        val modifiedBy: Any? = Any(),
        @SerializedName("ModifiedDate")
        val modifiedDate: Any? = Any(),
    )

    data class SharedWithResp(
        @SerializedName("LogID")
        val logId: Int = 0,
        @SerializedName("ID")
        val id: Int = 0,
        @SerializedName("Name")
        val name: String = "",
        @SerializedName("Email")
        val email: String = "",
        @SerializedName("PhoneNumber")
        val phoneNumber: String = ""
    )

    data class DocumentResp(
        @SerializedName("LogID")
        val logID: Int = 0,
        @SerializedName("DocumentID")
        val documentID: Int = 0,
        @SerializedName("Title")
        val title: String = "",
        @SerializedName("DocumentTypeCode")
        val documentTypeCode: String = "",
        @SerializedName("RelatedTo")
        val relatedTo: String = "",
        @SerializedName("ID")
        val id: Int = 0,
    )

}
