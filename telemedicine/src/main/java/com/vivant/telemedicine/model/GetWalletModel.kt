package com.vivant.telemedicine.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.vivant.telemedicine.model.base.BaseRequest

class GetWalletModel(
    @SerializedName("JSONData")
    @Expose
    private val jsonData: String,
    private val authToken: String) : BaseRequest(Header(authTicket = authToken)) {

    data class JSONDataRequest(
        @SerializedName("CustomerID")
        val customerID: Int = 0,
        @SerializedName("FamilyMemberID")
        val familyMemberID: Int = 0,
    )

    data class GetWalletResponse(
        @SerializedName("Report")
        val report: List<Report> = listOf()
    )

    data class Report(
        @SerializedName("ID")
        val id: Int = 0,
        @SerializedName("CustomerID")
        val customerID: Int = 0,
        @SerializedName("FamilyMemberID")
        val familyMemberID: Int = 0,
        @SerializedName("WalletAmount")
        val walletAmount: String = "",
        @SerializedName("IsActive")
        val isActive: Boolean = false,
        @SerializedName("IsAlreadyApplied")
        val isAlreadyApplied: Any? = Any(),
        @SerializedName("PartnerCode")
        val partnerCode: Any? = Any(),
        @SerializedName("CreatedBy")
        val createdBy: Int = 0,
        @SerializedName("CreatedDate")
        val createdDate: String = "",
        @SerializedName("ModifiedBy")
        val modifiedBy: Int = 0,
        @SerializedName("ModifiedDate")
        val modifiedDate: String = ""
    )

}
