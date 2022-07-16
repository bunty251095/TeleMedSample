package com.vivant.telemedicine.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.vivant.telemedicine.model.base.BaseRequest

class SSOLoginModel(
    @SerializedName("JSONData")
    @Expose
    private val jsonData: String,) : BaseRequest(Header()) {

    data class JSONDataRequest(
        @SerializedName("Mode")
        val mode: String = "LOGIN",
        @SerializedName("Data")
        var data: String = "",
        @SerializedName("Hash")
        val hash: String = "aa"
    )

    data class SSOLoginResponse(
        @SerializedName("Response")
        val response: Response = Response()
    )

    data class Response(
        @SerializedName("Data")
        val `data`: DataResp = DataResp(),
        @SerializedName("StatusCode")
        val statusCode: Int = 0
    )

    data class DataResp(
        @SerializedName("Context")
        val context: String = "",
        @SerializedName("PersonID")
        val personId: Int = 0,
        @SerializedName("AccountID")
        val accountId: Int = 0,
        @SerializedName("CustomerID")
        val customerId: Int = 0,
        @SerializedName("FirstName")
        val firstName: String = "",
        @SerializedName("LastName")
        val lastName: String = "",
        @SerializedName("EmailAddress")
        val emailAddress: String = "",
        @SerializedName("DateOfBirth")
        val dateOfBirth: String = "",
        @SerializedName("Gender")
        val gender: String = "",
        @SerializedName("PhoneNumber")
        val phoneNumber: String = "",
        @SerializedName("Age")
        val age: Int = 0,
        @SerializedName("PartnerCode")
        val partnerCode: String = "",
        @SerializedName("PartnerID")
        val partnerId: Int = 0,
        @SerializedName("LoginID")
        val loginId: String = "",
        @SerializedName("IC")
        val ic: String = "",
        @SerializedName("AccountStatus")
        val accountStatus: String = "",
        @SerializedName("AccountType")
        val accountType: String = "",
        @SerializedName("ClusterAssociationNo")
        val clusterAssociationNo: Any? = Any(),
        @SerializedName("ClusterID")
        val clusterId: Int = 0,
        @SerializedName("CountryName")
        val countryName: Any? = Any(),
        @SerializedName("DialingCode")
        val dialingCode: String = "",
        @SerializedName("IsActive")
        val isActive: Boolean = false,
        @SerializedName("IsAuthenticated")
        val isAuthenticated: Boolean = false,
        @SerializedName("MaritalStatus")
        val maritalStatus: String = "",
        @SerializedName("Name")
        val name: String = "",
        @SerializedName("NumberOfKids")
        val numberOfKids: Int = 0,
        @SerializedName("PATH")
        val path: Any? = Any(),
        @SerializedName("PROFILE_IMG_PATH")
        val profileImgPath: Any? = Any(),
        @SerializedName("ProfileImageID")
        val profileImageID: Int = 0,
        @SerializedName("RELATIVE_ID")
        val relativeId: Any? = Any(),
        @SerializedName("ROLES")
        val roles: ROLES = ROLES(),
        @SerializedName("Services")
        val services: Services = Services(),
        @SerializedName("TNCIsAccepted")
        val tncIsAccepted: Any? = Any(),
        @SerializedName("TNCDescription")
        val tncDescription: Any? = Any()
    )

    data class ROLES(
        @SerializedName("AIH_HRA_PORTAL")
        val aIHHRAPORTAL: List<AIHHRAPORTAL> = listOf()
    )

    data class AIHHRAPORTAL(
        @SerializedName("Role")
        val role: String = ""
    )

    data class Services(
        @SerializedName("HRA")
        val hRA: List<HRA> = listOf(),
        @SerializedName("PregnancyCare")
        val pregnancyCare: List<PregnancyCare> = listOf()
    )

    data class HRA(
        @SerializedName("Status")
        val status: String = ""
    )

    data class PregnancyCare(
        @SerializedName("PRA")
        val pRA: String = "",
        @SerializedName("PRAHistoryID")
        val pRAHistoryID: String = "",
        @SerializedName("Registered")
        val registered: String = ""
    )

    }