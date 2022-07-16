package com.vivant.telemedicine.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.vivant.telemedicine.model.base.BaseRequest

class UpdateWalletModel(
    @SerializedName("JSONData")
    @Expose
    private val jsonData: String,
    private val authToken: String) : BaseRequest(Header(authTicket = authToken)) {

    data class JSONDataRequest(
        @SerializedName("CustomerID")
        val customerID: Int = 0,
        @SerializedName("Wallet")
        val wallet: Wallet = Wallet()
    )

    data class Wallet(
        @SerializedName("FMID")
        val fmid: Int = 0,
        @SerializedName("CRD")
        val crd: Double = 0.0
    )

    data class UpdateWalletResponse(
        @SerializedName("IsSuccess")
        val isSuccess: String = ""
    )

}