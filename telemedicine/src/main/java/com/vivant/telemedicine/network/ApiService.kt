package com.vivant.telemedicine.network

import com.vivant.telemedicine.common.Constants
import com.vivant.telemedicine.model.*
import com.vivant.telemedicine.model.base.BaseHandlerResponse
import com.vivant.telemedicine.model.base.BaseResponse
import okhttp3.RequestBody
import retrofit2.http.*
import retrofit2.http.Body

interface ApiService {

    @POST(Constants.strLoginUrl)
    suspend fun ssoLogin(@Body request: SSOLoginModel): BaseResponse<SSOLoginModel.SSOLoginResponse>

    @POST(Constants.GET_CONSULTATION_TEMPLATE_API)
    suspend fun getConsultationTemplate(@Body request: GetConsultationTemplateModel): BaseResponse<GetConsultationTemplateModel.GetConsultationTemplateResponse>

    @POST(Constants.LIST_NOTIFICATIONS_API)
    suspend fun listNotifications(@Body request: ListNotificationsModel): BaseResponse<ListNotificationsModel.ListNotificationsResponse>

    @POST(Constants.UPDATE_NOTIFICATION_API)
    suspend fun updateNotification(@Body request: UpdateNotificationModel): BaseResponse<UpdateNotificationModel.UpdateNotificationResponse>


    @POST(Constants.LIST_DOCUMENT_API)
    suspend fun listDocuments(@Body request: ListDocumentModel): BaseResponse<ListDocumentModel.ListDocumentResponse>

    @POST(Constants.DOWNLOAD_DOCUMENT_API)
    suspend fun downloadDocument(@Body request: DownloadRecordModel): BaseResponse<DownloadRecordModel.DownloadRecordResponse>

    @POST(Constants.DELETE_DOCUMENT_API)
    suspend fun deleteDocument(@Body request: DeleteDocumentModel): BaseResponse<DeleteDocumentModel.DeleteDocumentResponse>

    @POST(Constants.SAVE_DOCUMENT_API)
    suspend fun saveDocuments(@Body request: SaveDocumentModel): BaseResponse<SaveDocumentModel.SaveDocumentResponse>

    @POST(Constants.SHARE_DOCUMENT_API)
    suspend fun shareDocuments(@Body request: ShareDocumentModel): BaseResponse<ShareDocumentModel.ShareDocumentResponse>

    @POST(Constants.REMOVE_SHARED_DOCUMENT_API)
    suspend fun removeSharedDocument(@Body request: RemoveSharedDocumentModel): BaseResponse<RemoveSharedDocumentModel.RemoveSharedDocumentResponse>


    @POST(Constants.LIST_SEARCHED_DOCTORS_API)
    suspend fun listSearchedDoctors(@Body request: ListSearchedDoctorsModel): BaseResponse<ListSearchedDoctorsModel.ListSearchedDoctorsResponse>

    @POST(Constants.GET_DOCTOR_SLOTS_API)
    suspend fun getDoctorSlots(@Body request: GetDoctorSlotsModel): BaseResponse<GetDoctorSlotsModel.GetDoctorSlotsResponse>

    @POST(Constants.GET_WALLET_API)
    suspend fun getWallet(@Body request: GetWalletModel): BaseResponse<GetWalletModel.GetWalletResponse>

    @POST(Constants.UPDATE_WALLET_API)
    suspend fun updateWallet(@Body request: UpdateWalletModel): BaseResponse<UpdateWalletModel.UpdateWalletResponse>

    @POST(Constants.BOOK_APPOINTMENTS_API)
    suspend fun bookAppointment(@Body request: BookAppointmentModel): BaseResponse<BookAppointmentModel.BookAppointmentResponse>

    @POST(Constants.GENERATE_ON_BOOKING_API)
    suspend fun generateOnBooking(@Body request: GenerateOnBookingModel): BaseResponse<GenerateOnBookingModel.GenerateOnBookingResponse>

    @POST(Constants.LIST_APPOINTMENTS_API)
    suspend fun listAppointments(@Body request: ListAppointmentsModel): BaseResponse<ListAppointmentsModel.ListAppointmentsResponse>

    @POST(Constants.LIST_CONSULTATION_API)
    suspend fun listConsultation(@Body request: ListConsultationModel): BaseResponse<ListConsultationModel.ListConsultationResponse>

    @POST(Constants.RESCHEDULE_APPOINTMENTS_API)
    suspend fun rescheduleAppointment(@Body request: RescheduleAppointmentModel): BaseResponse<RescheduleAppointmentModel.RescheduleAppointmentResponse>

    @POST(Constants.CANCEL_APPOINTMENTS_API)
    suspend fun cancelAppointment(@Body request: CancelAppointmentModel): BaseResponse<CancelAppointmentModel.CancelAppointmentResponse>

    @POST(Constants.DOWNLOAD_INVOICE_API)
    suspend fun downloadInvoice(
        @Query("cid") cid: String,
        @Query("appid") appid: String,
        @Query("oid") oid: String,
        @Query("docid") docid: String,
        @Query("sid") sid: String,
        @Query("isPrint") isPrint: Boolean = true) : BaseResponse<DownloadInvoiceResponse>

    @POST(Constants.DOWNLOAD_PRESCRIPTION_API)
    suspend fun downloadPrescription(
        @Query("cid") cid: String,
        @Query("appid") appid: String,
        @Query("conid") conid: String,
        @Query("docid") docid: String) : BaseResponse<DownloadPrescriptionResponse>

    @POST(Constants.GET_CONSULTATION_HEALTH_DOCUMENT_API)
    suspend fun getConsultationHealthDocument(@Body request: GetConsultationHealthDocumentModel): BaseResponse<GetConsultationHealthDocumentModel.GetConsultationHealthDocumentResponse>

    @POST(Constants.SAVE_FEEDBACK_API)
    suspend fun saveFeedback(@Body request: SaveFeedbackModel): BaseResponse<SaveFeedbackModel.SaveFeedbackResponse>

    @POST(Constants.SAVE_CONSULTATION_REQUEST_API)
    suspend fun saveConsultationRequest(@Body request: SaveConsultationRequestModel): BaseResponse<SaveConsultationRequestModel.SaveConsultationRequestResponse>

    @POST(Constants.CHECKOUT_APPOINTMENT_API)
    suspend fun checkoutAppointment(@Body request: CheckoutAppointmentModel): BaseResponse<CheckoutAppointmentModel.CheckoutAppointmentResponse>

    @POST(Constants.GET_CONSULTATION_REQUEST_API)
    suspend fun getConsultationRequest(@Body request: GetConsultationRequestModel): BaseResponse<GetConsultationRequestModel.GetConsultationRequestResponse>

    @POST(Constants.JOIN_ROOM_API)
    suspend fun joinRoom(@Body request: JoinRoomModel): BaseResponse<JoinRoomModel.JoinRoomResponse>

    @POST(Constants.UPDATE_CONSULTATION_REQUEST_API)
    suspend fun updateConsultationRequest(@Body request: UpdateConsultationRequestModel): BaseResponse<UpdateConsultationRequestModel.UpdateConsultationRequestResponse>

    @POST(Constants.GET_REQUEST_DETAILS_API)
    suspend fun getRequestDetails(@Body request: GetRequestDetailsModel): BaseResponse<GetRequestDetailsModel.GetRequestDetailsResponse>

    @POST(Constants.PATIENT_STATE_LIST_API)
    suspend fun patientStateList(@Body request: PatientStateListModel): BaseResponse<PatientStateListModel.PatientStateListResponse>

    @Multipart
    @POST(Constants.SEND_EPHARMA_DETAILS_API)
    suspend fun sendEpharmaDetails(
        @Part("Data") authTicket: RequestBody): BaseResponse<SendEpharmaDetailsModel.SendEpharmaDetailsResponse>
}