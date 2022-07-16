package com.vivant.telemedicine.model.base

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.vivant.telemedicine.common.Configuration
import com.vivant.telemedicine.common.DateHelper
import java.text.SimpleDateFormat
import java.util.*


open class BaseRequest(
    @SerializedName("Header")
    @Expose
    val header: Header
) {

    data class Header(
        @SerializedName("ApplicationCode")
        @Expose
        private val applicationCode: String = Configuration.ApplicationCode,
        @SerializedName("APIAccessToken")
        @Expose
        private val apiAccessToken: String? = Configuration.APIAccessToken,
        @SerializedName("EntityType")
        @Expose
        //private val entityType: String? = null,
        private val entityType: String = Configuration.EntityType,
        @SerializedName("AuthTicket")
        @Expose
        //private val authTicket: String? = null,
        private val authTicket: String? = "",
        @SerializedName("DateTime")
        @Expose
        private val dateTime: String = DateHelper.currentDateAsStringddMMMyyyy,
        @SerializedName("RequestID")
        @Expose
        private val requestID: String = Configuration.RequestID,
        @SerializedName("PartnerCode")
        @Expose
        private val partnerCode: String = Configuration.PartnerCode,
    )

    companion object {
        fun replaceChars(strVal: String): String {
            return strVal.replace("\\u003d", "=")
        }

        /* Current Date : */ val currentDateAsString_yyyy_MM_dd: String
            get() {
                val calendar = Calendar.getInstance()
                val df = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                return df.format(calendar.time)
            }
    }


}
