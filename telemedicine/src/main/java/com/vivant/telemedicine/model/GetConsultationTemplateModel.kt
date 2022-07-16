package com.vivant.telemedicine.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.vivant.telemedicine.model.base.BaseRequest

class GetConsultationTemplateModel(
    @SerializedName("JSONData")
    @Expose
    private val jsonData: String,
    private val authToken: String) : BaseRequest(Header(authTicket = authToken)) {

    data class JSONDataRequest(
        @SerializedName("TemplateID")
        val templateID: Int = 0,
        @SerializedName("IsDefault")
        val isDefault: String = ""
    )

    data class GetConsultationTemplateResponse(
        @SerializedName("Template")
        val template: Template = Template()
    )

    data class Template(
        @SerializedName("AudioFees")
        val audioFees: String = "",
        @SerializedName("ChatFees")
        val chatFees: String = "",
        @SerializedName("CreatedDate")
        val createdDate: String = "",
        @SerializedName("Description")
        val description: String = "",
        @SerializedName("IsActive")
        val isActive: String = "",
        @SerializedName("IsAppliedToAll")
        val isAppliedToAll: String = "",
        @SerializedName("IsDefault")
        val isDefault: String = "",
        @SerializedName("IsEnabled")
        val isEnabled: String = "",
        @SerializedName("RequestedBy")
        val requestedBy: String = "",
        @SerializedName("TemplateID")
        val templateID: String = "",
        @SerializedName("TotalCount")
        val totalCount: String = "",
        @SerializedName("VideoFees")
        val videoFees: String = ""
    )

}
