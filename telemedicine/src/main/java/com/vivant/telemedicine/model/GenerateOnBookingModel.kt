package com.vivant.telemedicine.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.vivant.telemedicine.model.base.BaseRequest

class GenerateOnBookingModel(
    @SerializedName("JSONData")
    @Expose
    private val jsonData: String,
    private val authToken: String) : BaseRequest(Header(authTicket = authToken)) {

    data class JSONDataRequest(
        @SerializedName("OrderDetails")
        val orderDetails: OrderDetails = OrderDetails()
    )

    data class OrderDetails(
        @SerializedName("OrderID")
        val orderID: Int = 0,
        @SerializedName("DoctorID")
        val doctorID: Int = 0,
        @SerializedName("SupplierID")
        val supplierID: Int = 0,
        @SerializedName("AppointmentID")
        val appointmentID: Int = 0,
        @SerializedName("OrderDate")
        val orderDate: String = "",
        @SerializedName("AppointmentMode")
        val appointmentMode: String = "",
        @SerializedName("FinalPrice")
        val finalPrice: String = "",
        @SerializedName("ServiceCharge")
        val serviceCharge: Int = 0,
        @SerializedName("TotalTax")
        val totalTax: Int = 0,
        @SerializedName("PartnerSellingPrice")
        val partnerSellingPrice: Int = 0,
        @SerializedName("Status")
        val status: String = "",
        @SerializedName("DiscountPrice")
        val discountPrice: Int = 0,
        @SerializedName("DiscountPercentage")
        val discountPercentage: Int = 0
    )

    data class GenerateOnBookingResponse(
        @SerializedName("OrderDetails")
        val orderDetails: OrderDetailsResp = OrderDetailsResp()
    )

    data class OrderDetailsResp(
        @SerializedName("OrderID")
        val orderID: Int = 0,
        @SerializedName("DoctorID")
        val doctorID: Int = 0,
        @SerializedName("SupplierID")
        val supplierID: Int = 0,
        @SerializedName("AppointmentID")
        val appointmentID: Int = 0,
        @SerializedName("OrderDate")
        val orderDate: String = "",
        @SerializedName("AppointmentMode")
        val appointmentMode: String = "",
        @SerializedName("FinalPrice")
        val finalPrice: String = "",
        @SerializedName("ServiceCharge")
        val serviceCharge: Int = 0,
        @SerializedName("TotalTax")
        val totalTax: Int = 0,
        @SerializedName("PartnerSellingPrice")
        val partnerSellingPrice: Int = 0,
        @SerializedName("Status")
        val status: String = "",
        @SerializedName("DiscountPrice")
        val discountPrice: Int = 0,
        @SerializedName("DiscountPercentage")
        val discountPercentage: Int = 0,
        @SerializedName("OrderNumber")
        val orderNumber: String = "",
        @SerializedName("InvoiceNumber")
        val invoiceNumber: String = ""
    )

}
