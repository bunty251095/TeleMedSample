package com.vivant.telemedicine.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.vivant.telemedicine.model.base.BaseRequest

class DeleteDocumentModel(
    @SerializedName("JSONData")
    @Expose
    private val jsonData: String,
    private val authToken: String) : BaseRequest(Header(authTicket = authToken)) {

    data class JSONDataRequest(
/*        @SerializedName("PersonID")
        val personID: Int = 0,*/
        @SerializedName("DocumentIDS")
        val documentIDS: MutableList<Int> = mutableListOf()
    )

    data class DeleteDocumentResponse(
        @SerializedName("IsProcessed")
        val isProcessed: String = ""
    )

}
