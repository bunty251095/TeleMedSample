package com.vivant.telemedicine.model.base

//import com.caressa.model.blogs.BlogsCategoryModel
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.vivant.telemedicine.model.PatientStateListModel


data class BaseHandlerResponse(
    @SerializedName(value = "JSONData", alternate = ["jsonData"])
    @Expose
    var jsonData: Any? = null,
    @SerializedName("JObject")
    @Expose
    var jObject: Any? = null,
    @SerializedName("StatusCode")
    @Expose
    var statusCode: String? = "",
    @SerializedName("ErrorNumber")
    @Expose
    var errorNumber: String? = "",
    @SerializedName("Message")
    @Expose
    var message: String? = "",
    @SerializedName("ProfileImageID")
    @Expose
    var profileImageID: String? = "",
    @SerializedName("FileData")
    @Expose
    var fileData: String? = "",
    @SerializedName("FileName")
    @Expose
    var fileName: String? = "",
    @SerializedName("StateList")
    val stateList: List<PatientStateListModel.State?>? = listOf(),
    @SerializedName("Success")
    @Expose
    var success: Boolean = false
)