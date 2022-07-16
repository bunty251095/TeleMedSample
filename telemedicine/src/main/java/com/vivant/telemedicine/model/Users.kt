package com.vivant.telemedicine.model

import com.google.gson.annotations.SerializedName

data class Users(
    @SerializedName("Context")
    val authToken: String = "",
    @SerializedName("PartnerCode")
    val partnerCode: String = "",
    @SerializedName("Name")
    var name: String = "",
    @SerializedName("EmailAddress")
    val emailAddress: String = "",
    @SerializedName("DateOfBirth")
    val dateOfBirth: String? = "",
    @SerializedName("Gender")
    val gender: String = "",
    @SerializedName("PersonID")
    val personId: Int = 0,
    @SerializedName("FirstName")
    val firstName: String = "",
    @SerializedName("LastName")
    val lastName: String = "",
    @SerializedName("ClusterID")
    val clusterId: String = "",
    @SerializedName("IsActive")
    val isActive: Boolean = false,
    @SerializedName("Age")
    val age: Int = 0,
    @SerializedName("ClusterAssociationNo")
    val clusterAssociationNo: String = "",
    @SerializedName("PartnerID")
    val partnerId: Int = 0,
    @SerializedName("RELATIVE_ID")
    val relativeId: String = "",
    @SerializedName("MaritalStatus")
    val maritalStatus: String = "",
    @SerializedName("NumberOfKids")
    val numberOfCode: Int = 0,
    @SerializedName("ProfileImageID")
    val profileImageID: Int = 0,
    @SerializedName("PhoneNumber")
    val phoneNumber: String = "",
    @SerializedName("AccountID")
    val accountId: Int = 0,
    @SerializedName("CustomerID")
    val customerID: Int = 0,
    @SerializedName("AccountStatus")
    val accountStatus: String = "",
    @SerializedName("AccountType")
    val accountType: String = "",
    @SerializedName("DialingCode")
    val dialingCode: String = "",
    @SerializedName("CountryName")
    val countryName: String = "",
    @SerializedName("IsAuthenticated")
    val isAuthenticated: Boolean = false,
    @SerializedName("PROFILE_IMG_PATH")
    val profileImgPath: String = "",
    @SerializedName("PATH")
    val path: String = "",
    @SerializedName("LoginID")
    val loginID: String = "",
    @SerializedName("IC")
    val ic: String = "",
    @SerializedName("Services")
    val services: Services = Services(),


    @SerializedName("CreatedDate")
    val createdDate: String? = ""
)

data class Services(
    @SerializedName("HRA")
    val hra: List<HRA> = listOf(),
    @SerializedName("PregnancyCare")
    val pregnancyCare: List<PregnancyCare> = listOf()
)

data class HRA(
    @SerializedName("Status")
    val status: String = ""
)

data class PregnancyCare(
    @SerializedName("PRA")
    val pra: String = "",
    @SerializedName("PRAHistoryID")
    val praHistoryID: String = "",
    @SerializedName("Registered")
    val registered: String = ""
)
