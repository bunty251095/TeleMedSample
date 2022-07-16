package com.vivant.telemedicine.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.vivant.telemedicine.model.base.BaseRequest

class RemoveSharedDocumentModel(
    @SerializedName("JSONData")
    @Expose
    private val jsonData: String,
    private val authToken: String) : BaseRequest(Header(authTicket = authToken)) {

    data class JSONDataRequest(
        @SerializedName("CustomerID")
        val customerID: Int = 0,
        @SerializedName("DocumentID")
        val documentID: Int = 0
    )

    data class RemoveSharedDocumentResponse(
        @SerializedName("Document")
        val document: String = ""
    )

}
