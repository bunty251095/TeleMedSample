package com.vivant.telemedicine.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.vivant.telemedicine.model.base.BaseRequest

class PatientStateListModel(
    @SerializedName("JSONData")
    @Expose
    private val jsonData: String,
    private val authToken: String) : BaseRequest(Header(authTicket = authToken)) {

    data class PatientStateListResponse(
        @SerializedName("StateList")
        val stateList: MutableList<State>? = mutableListOf()
    )

    data class State(
        @SerializedName("stateName")
        val stateName: String? = ""
    )

}
