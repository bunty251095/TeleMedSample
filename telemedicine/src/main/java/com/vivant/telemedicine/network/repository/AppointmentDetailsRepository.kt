package com.vivant.telemedicine.network.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.vivant.telemedicine.model.*
import com.vivant.telemedicine.model.base.BaseResponse
import com.vivant.telemedicine.network.datasource.AppointmentDetailsDatasource
import com.vivant.telemedicine.network.utils.NetworkBoundResource
import com.vivant.telemedicine.network.utils.NetworkDataBoundResource
import com.vivant.telemedicine.network.utils.Resource
import okhttp3.RequestBody

interface AppointmentDetailsRepository {

    suspend fun saveDocuments(forceRefresh: Boolean = false, data: SaveDocumentModel): LiveData<Resource<SaveDocumentModel.SaveDocumentResponse>>
    suspend fun shareDocuments(forceRefresh: Boolean = false, data: ShareDocumentModel): LiveData<Resource<ShareDocumentModel.ShareDocumentResponse>>
    suspend fun removeSharedDocument(forceRefresh: Boolean = false, data: RemoveSharedDocumentModel): LiveData<Resource<RemoveSharedDocumentModel.RemoveSharedDocumentResponse>>
    suspend fun rescheduleAppointment(forceRefresh: Boolean = false, data: RescheduleAppointmentModel): LiveData<Resource<RescheduleAppointmentModel.RescheduleAppointmentResponse>>
    suspend fun cancelAppointment(forceRefresh: Boolean = false, data: CancelAppointmentModel): LiveData<Resource<CancelAppointmentModel.CancelAppointmentResponse>>
    suspend fun downloadInvoice(cid:String,appid:String,oid:String,docid:String,sid:String): LiveData<Resource<DownloadInvoiceResponse>>
    suspend fun downloadPrescription(cid: String, appid: String, conid: String, docid: String): LiveData<Resource<DownloadPrescriptionResponse>>
    suspend fun getConsultationHealthDocument(forceRefresh: Boolean = false, data: GetConsultationHealthDocumentModel): LiveData<Resource<GetConsultationHealthDocumentModel.GetConsultationHealthDocumentResponse>>
    suspend fun saveFeedback(forceRefresh: Boolean = false, data: SaveFeedbackModel): LiveData<Resource<SaveFeedbackModel.SaveFeedbackResponse>>
    suspend fun getRequestDetails(forceRefresh: Boolean = false, data: GetRequestDetailsModel): LiveData<Resource<GetRequestDetailsModel.GetRequestDetailsResponse>>

    suspend fun patientStateList(forceRefresh: Boolean = false, data: PatientStateListModel): LiveData<Resource<PatientStateListModel.PatientStateListResponse>>
    suspend fun sendEpharmaDetails(forceRefresh: Boolean = false, data: RequestBody): LiveData<Resource<SendEpharmaDetailsModel.SendEpharmaDetailsResponse>>
}

class AppointmentDetailsRepositoryImpl(private val dataSource: AppointmentDetailsDatasource,
                                       val context: Context) : AppointmentDetailsRepository {

    override suspend fun saveDocuments(forceRefresh: Boolean, data: SaveDocumentModel): LiveData<Resource<SaveDocumentModel.SaveDocumentResponse>> {

        return object : NetworkBoundResource<SaveDocumentModel.SaveDocumentResponse,BaseResponse<SaveDocumentModel.SaveDocumentResponse>>(context) {

            override fun shouldStoreInDb(): Boolean = false

            override suspend fun loadFromDb(): SaveDocumentModel.SaveDocumentResponse {
                return SaveDocumentModel.SaveDocumentResponse()
            }

            override suspend fun createCallAsync(): BaseResponse<SaveDocumentModel.SaveDocumentResponse> {
                return dataSource.saveDocuments(data)
            }

            override fun processResponse(response: BaseResponse<SaveDocumentModel.SaveDocumentResponse>): SaveDocumentModel.SaveDocumentResponse {
                return response.jSONData
            }

            override suspend fun saveCallResults(items: SaveDocumentModel.SaveDocumentResponse) {

            }

            override fun shouldFetch(data: SaveDocumentModel.SaveDocumentResponse?): Boolean = true

        }.build().asLiveData()

    }

    override suspend fun shareDocuments(forceRefresh: Boolean, data: ShareDocumentModel): LiveData<Resource<ShareDocumentModel.ShareDocumentResponse>> {

        return object : NetworkBoundResource<ShareDocumentModel.ShareDocumentResponse,BaseResponse<ShareDocumentModel.ShareDocumentResponse>>(context) {

            override fun shouldStoreInDb(): Boolean = false

            override suspend fun loadFromDb(): ShareDocumentModel.ShareDocumentResponse {
                return ShareDocumentModel.ShareDocumentResponse()
            }

            override suspend fun createCallAsync(): BaseResponse<ShareDocumentModel.ShareDocumentResponse> {
                return dataSource.shareDocuments(data)
            }

            override fun processResponse(response: BaseResponse<ShareDocumentModel.ShareDocumentResponse>): ShareDocumentModel.ShareDocumentResponse {
                return response.jSONData
            }

            override suspend fun saveCallResults(items: ShareDocumentModel.ShareDocumentResponse) {

            }

            override fun shouldFetch(data: ShareDocumentModel.ShareDocumentResponse?): Boolean = true

        }.build().asLiveData()

    }

    override suspend fun removeSharedDocument(forceRefresh: Boolean, data: RemoveSharedDocumentModel): LiveData<Resource<RemoveSharedDocumentModel.RemoveSharedDocumentResponse>> {

        return object : NetworkBoundResource<RemoveSharedDocumentModel.RemoveSharedDocumentResponse,BaseResponse<RemoveSharedDocumentModel.RemoveSharedDocumentResponse>>(context) {

            override fun shouldStoreInDb(): Boolean = false

            override suspend fun loadFromDb(): RemoveSharedDocumentModel.RemoveSharedDocumentResponse {
                return RemoveSharedDocumentModel.RemoveSharedDocumentResponse()
            }

            override fun processResponse(response: BaseResponse<RemoveSharedDocumentModel.RemoveSharedDocumentResponse>): RemoveSharedDocumentModel.RemoveSharedDocumentResponse {
                return response.jSONData
            }

            override suspend fun createCallAsync(): BaseResponse<RemoveSharedDocumentModel.RemoveSharedDocumentResponse> {
                return dataSource.removeSharedDocument(data)
            }

            override suspend fun saveCallResults(items: RemoveSharedDocumentModel.RemoveSharedDocumentResponse) {

            }

            override fun shouldFetch(data: RemoveSharedDocumentModel.RemoveSharedDocumentResponse?): Boolean = true

        }.build().asLiveData()

    }

    override suspend fun rescheduleAppointment(forceRefresh: Boolean, data: RescheduleAppointmentModel): LiveData<Resource<RescheduleAppointmentModel.RescheduleAppointmentResponse>> {

        return object : NetworkBoundResource<RescheduleAppointmentModel.RescheduleAppointmentResponse,BaseResponse<RescheduleAppointmentModel.RescheduleAppointmentResponse>>(context) {

            override fun shouldStoreInDb(): Boolean = false

            override suspend fun loadFromDb(): RescheduleAppointmentModel.RescheduleAppointmentResponse {
                return RescheduleAppointmentModel.RescheduleAppointmentResponse()
            }

            override suspend fun createCallAsync(): BaseResponse<RescheduleAppointmentModel.RescheduleAppointmentResponse> {
                return dataSource.rescheduleAppointment(data)
            }

            override fun processResponse(response: BaseResponse<RescheduleAppointmentModel.RescheduleAppointmentResponse>): RescheduleAppointmentModel.RescheduleAppointmentResponse {
                return response.jSONData
            }

            override suspend fun saveCallResults(items: RescheduleAppointmentModel.RescheduleAppointmentResponse) {

            }

            override fun shouldFetch(data: RescheduleAppointmentModel.RescheduleAppointmentResponse?): Boolean = true


        }.build().asLiveData()

    }

    override suspend fun cancelAppointment(forceRefresh: Boolean, data: CancelAppointmentModel): LiveData<Resource<CancelAppointmentModel.CancelAppointmentResponse>> {

        return object : NetworkBoundResource<CancelAppointmentModel.CancelAppointmentResponse,BaseResponse<CancelAppointmentModel.CancelAppointmentResponse>>(context) {

            override fun shouldStoreInDb(): Boolean = false

            override suspend fun loadFromDb(): CancelAppointmentModel.CancelAppointmentResponse {
                return CancelAppointmentModel.CancelAppointmentResponse()
            }

            override suspend fun createCallAsync(): BaseResponse<CancelAppointmentModel.CancelAppointmentResponse> {
                return dataSource.cancelAppointment(data)
            }

            override fun processResponse(response: BaseResponse<CancelAppointmentModel.CancelAppointmentResponse>): CancelAppointmentModel.CancelAppointmentResponse {
                return response.jSONData
            }

            override suspend fun saveCallResults(items: CancelAppointmentModel.CancelAppointmentResponse) {

            }

            override fun shouldFetch(data: CancelAppointmentModel.CancelAppointmentResponse?): Boolean = true

        }.build().asLiveData()

    }

    override suspend fun downloadInvoice(cid: String, appid: String, oid: String, docid: String, sid: String): LiveData<Resource<DownloadInvoiceResponse>> {

        return object : NetworkDataBoundResource<DownloadInvoiceResponse,BaseResponse<DownloadInvoiceResponse>>(context) {

            override fun processResponse(response: BaseResponse<DownloadInvoiceResponse>): DownloadInvoiceResponse {
                return response.jSONData
            }

            override suspend fun createCallAsync(): BaseResponse<DownloadInvoiceResponse> {
                return dataSource.downloadInvoice(cid,appid,oid,docid,sid)
            }

        }.build().asLiveData()

    }

    override suspend fun downloadPrescription(cid: String, appid: String, conid: String, docid: String): LiveData<Resource<DownloadPrescriptionResponse>> {

        return object : NetworkDataBoundResource<DownloadPrescriptionResponse,BaseResponse<DownloadPrescriptionResponse>>(context) {

            override fun processResponse(response: BaseResponse<DownloadPrescriptionResponse>): DownloadPrescriptionResponse {
                return response.jSONData
            }

            override suspend fun createCallAsync(): BaseResponse<DownloadPrescriptionResponse> {
                return dataSource.downloadPrescription(cid,appid,conid,docid)
            }

        }.build().asLiveData()

    }

    override suspend fun getConsultationHealthDocument(forceRefresh: Boolean, data: GetConsultationHealthDocumentModel): LiveData<Resource<GetConsultationHealthDocumentModel.GetConsultationHealthDocumentResponse>> {

        return object : NetworkBoundResource<GetConsultationHealthDocumentModel.GetConsultationHealthDocumentResponse,BaseResponse<GetConsultationHealthDocumentModel.GetConsultationHealthDocumentResponse>>(context) {

            override fun shouldStoreInDb(): Boolean = false

            override suspend fun loadFromDb(): GetConsultationHealthDocumentModel.GetConsultationHealthDocumentResponse {
                return GetConsultationHealthDocumentModel.GetConsultationHealthDocumentResponse()
            }

            override suspend fun createCallAsync(): BaseResponse<GetConsultationHealthDocumentModel.GetConsultationHealthDocumentResponse> {
                return dataSource.getConsultationHealthDocument(data)
            }

            override fun processResponse(response: BaseResponse<GetConsultationHealthDocumentModel.GetConsultationHealthDocumentResponse>): GetConsultationHealthDocumentModel.GetConsultationHealthDocumentResponse {
                return response.jSONData
            }

            override suspend fun saveCallResults(items: GetConsultationHealthDocumentModel.GetConsultationHealthDocumentResponse) {

            }

            override fun shouldFetch(data: GetConsultationHealthDocumentModel.GetConsultationHealthDocumentResponse?): Boolean = true

        }.build().asLiveData()

    }

    override suspend fun saveFeedback(forceRefresh: Boolean, data: SaveFeedbackModel): LiveData<Resource<SaveFeedbackModel.SaveFeedbackResponse>> {

        return object : NetworkBoundResource<SaveFeedbackModel.SaveFeedbackResponse,BaseResponse<SaveFeedbackModel.SaveFeedbackResponse>>(context) {

            override fun shouldStoreInDb(): Boolean = false

            override suspend fun loadFromDb(): SaveFeedbackModel.SaveFeedbackResponse {
                return SaveFeedbackModel.SaveFeedbackResponse()
            }

            override suspend fun createCallAsync(): BaseResponse<SaveFeedbackModel.SaveFeedbackResponse> {
                return dataSource.saveFeedback(data)
            }

            override fun processResponse(response: BaseResponse<SaveFeedbackModel.SaveFeedbackResponse>): SaveFeedbackModel.SaveFeedbackResponse {
                return response.jSONData
            }

            override suspend fun saveCallResults(items: SaveFeedbackModel.SaveFeedbackResponse) {

            }

            override fun shouldFetch(data: SaveFeedbackModel.SaveFeedbackResponse?): Boolean = true

        }.build().asLiveData()

    }

    override suspend fun getRequestDetails(forceRefresh: Boolean, data: GetRequestDetailsModel): LiveData<Resource<GetRequestDetailsModel.GetRequestDetailsResponse>> {

        return object : NetworkBoundResource<GetRequestDetailsModel.GetRequestDetailsResponse,BaseResponse<GetRequestDetailsModel.GetRequestDetailsResponse>>(context) {

            override fun shouldStoreInDb(): Boolean = false

            override suspend fun loadFromDb(): GetRequestDetailsModel.GetRequestDetailsResponse {
                return GetRequestDetailsModel.GetRequestDetailsResponse()
            }

            override suspend fun createCallAsync(): BaseResponse<GetRequestDetailsModel.GetRequestDetailsResponse> {
                return dataSource.getRequestDetails(data)
            }

            override fun processResponse(response: BaseResponse<GetRequestDetailsModel.GetRequestDetailsResponse>): GetRequestDetailsModel.GetRequestDetailsResponse {
                return response.jSONData
            }

            override suspend fun saveCallResults(items: GetRequestDetailsModel.GetRequestDetailsResponse) {

            }

            override fun shouldFetch(data: GetRequestDetailsModel.GetRequestDetailsResponse?): Boolean = true
        }.build().asLiveData()

    }

    override suspend fun patientStateList(forceRefresh: Boolean, data: PatientStateListModel): LiveData<Resource<PatientStateListModel.PatientStateListResponse>> {

        return object : NetworkDataBoundResource<PatientStateListModel.PatientStateListResponse,BaseResponse<PatientStateListModel.PatientStateListResponse>>(context) {

            override fun processResponse(response: BaseResponse<PatientStateListModel.PatientStateListResponse>): PatientStateListModel.PatientStateListResponse {
                return response.jSONData
            }

            override suspend fun createCallAsync(): BaseResponse<PatientStateListModel.PatientStateListResponse> {
                return dataSource.patientStateList(data)
            }


        }.build().asLiveData()

    }

    override suspend fun sendEpharmaDetails(forceRefresh: Boolean, data: RequestBody): LiveData<Resource<SendEpharmaDetailsModel.SendEpharmaDetailsResponse>> {

        return object : NetworkDataBoundResource<SendEpharmaDetailsModel.SendEpharmaDetailsResponse, BaseResponse<SendEpharmaDetailsModel.SendEpharmaDetailsResponse>>(context) {

            override fun processResponse(response: BaseResponse<SendEpharmaDetailsModel.SendEpharmaDetailsResponse>): SendEpharmaDetailsModel.SendEpharmaDetailsResponse {
                return response.jSONData
            }

            override suspend fun createCallAsync(): BaseResponse<SendEpharmaDetailsModel.SendEpharmaDetailsResponse> {
                return dataSource.sendEpharmaDetails(data)
            }


        }.build().asLiveData()

    }

}