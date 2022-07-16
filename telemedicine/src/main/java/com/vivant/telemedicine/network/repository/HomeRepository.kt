package com.vivant.telemedicine.network.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.vivant.telemedicine.model.*
import com.vivant.telemedicine.model.base.ApiResponse
import com.vivant.telemedicine.model.base.BaseResponse
import com.vivant.telemedicine.network.datasource.HomeDatasource
import com.vivant.telemedicine.network.utils.NetworkBoundResource
import com.vivant.telemedicine.network.utils.Resource

interface HomeRepository {

    suspend fun ssoLogin(forceRefresh: Boolean = false, data: SSOLoginModel): LiveData<Resource<SSOLoginModel.SSOLoginResponse>>

    suspend fun listAppointments(forceRefresh: Boolean = false, data: ListAppointmentsModel): LiveData<Resource<ListAppointmentsModel.ListAppointmentsResponse>>
    suspend fun listConsultation(forceRefresh: Boolean = false, data: ListConsultationModel): LiveData<Resource<ListConsultationModel.ListConsultationResponse>>

    suspend fun listDocument(forceRefresh: Boolean = false, data: ListDocumentModel): LiveData<Resource<ListDocumentModel.ListDocumentResponse>>
    suspend fun downloadDocument(forceRefresh: Boolean = false, data: DownloadRecordModel): LiveData<Resource<DownloadRecordModel.DownloadRecordResponse>>
    suspend fun deleteDocument(forceRefresh: Boolean = false, data: DeleteDocumentModel): LiveData<Resource<DeleteDocumentModel.DeleteDocumentResponse>>

    suspend fun listNotifications(forceRefresh: Boolean = false, data: ListNotificationsModel): LiveData<Resource<ListNotificationsModel.ListNotificationsResponse>>
    suspend fun updateNotification(forceRefresh: Boolean = false, data: UpdateNotificationModel): LiveData<Resource<UpdateNotificationModel.UpdateNotificationResponse>>
}

class HomeRepositoryImpl(private val dataSource: HomeDatasource,
                                       val context: Context) : HomeRepository {

    override suspend fun ssoLogin(forceRefresh: Boolean, data: SSOLoginModel): LiveData<Resource<SSOLoginModel.SSOLoginResponse>> {

        return object : NetworkBoundResource<SSOLoginModel.SSOLoginResponse,BaseResponse<SSOLoginModel.SSOLoginResponse>>(context) {

            override fun shouldStoreInDb(): Boolean = false

            override suspend fun loadFromDb(): SSOLoginModel.SSOLoginResponse {
                return SSOLoginModel.SSOLoginResponse()
            }

            override suspend fun createCallAsync(): BaseResponse<SSOLoginModel.SSOLoginResponse> {
                return dataSource.ssoLogin(data)
            }

            override fun processResponse(response: BaseResponse<SSOLoginModel.SSOLoginResponse>): SSOLoginModel.SSOLoginResponse {
                return response.jSONData
            }

            override suspend fun saveCallResults(items: SSOLoginModel.SSOLoginResponse) {

            }

            override fun shouldFetch(data: SSOLoginModel.SSOLoginResponse?):  Boolean = true

        }.build().asLiveData()

    }

    override suspend fun listAppointments(forceRefresh: Boolean, data: ListAppointmentsModel): LiveData<Resource<ListAppointmentsModel.ListAppointmentsResponse>> {

        return object : NetworkBoundResource<ListAppointmentsModel.ListAppointmentsResponse,BaseResponse<ListAppointmentsModel.ListAppointmentsResponse>>(context) {

            override fun shouldStoreInDb(): Boolean = false

            override suspend fun loadFromDb(): ListAppointmentsModel.ListAppointmentsResponse {
                return ListAppointmentsModel.ListAppointmentsResponse()
            }

            override suspend fun createCallAsync(): BaseResponse<ListAppointmentsModel.ListAppointmentsResponse> {
                return dataSource.listAppointments(data)
            }

            override fun processResponse(response: BaseResponse<ListAppointmentsModel.ListAppointmentsResponse>): ListAppointmentsModel.ListAppointmentsResponse {
                return response.jSONData
            }

            override suspend fun saveCallResults(items: ListAppointmentsModel.ListAppointmentsResponse) {

            }

            override fun shouldFetch(data: ListAppointmentsModel.ListAppointmentsResponse?): Boolean = true

        }.build().asLiveData()

    }

    override suspend fun listConsultation(forceRefresh: Boolean, data: ListConsultationModel): LiveData<Resource<ListConsultationModel.ListConsultationResponse>> {

        return object : NetworkBoundResource<ListConsultationModel.ListConsultationResponse,BaseResponse<ListConsultationModel.ListConsultationResponse>>(context) {

            override fun shouldStoreInDb(): Boolean = false

            override suspend fun loadFromDb(): ListConsultationModel.ListConsultationResponse {
                return ListConsultationModel.ListConsultationResponse()
            }

            override suspend fun createCallAsync(): BaseResponse<ListConsultationModel.ListConsultationResponse> {
                return dataSource.listConsultation(data)
            }

            override fun processResponse(response: BaseResponse<ListConsultationModel.ListConsultationResponse>): ListConsultationModel.ListConsultationResponse {
                return response.jSONData
            }

            override suspend fun saveCallResults(items: ListConsultationModel.ListConsultationResponse) {

            }

            override fun shouldFetch(data: ListConsultationModel.ListConsultationResponse?): Boolean = true
        }.build().asLiveData()

    }

    override suspend fun listDocument(forceRefresh: Boolean, data: ListDocumentModel): LiveData<Resource<ListDocumentModel.ListDocumentResponse>> {

        return object : NetworkBoundResource<ListDocumentModel.ListDocumentResponse,BaseResponse<ListDocumentModel.ListDocumentResponse>>(context) {

            override fun shouldStoreInDb(): Boolean = false

            override suspend fun loadFromDb(): ListDocumentModel.ListDocumentResponse {
                return ListDocumentModel.ListDocumentResponse()
            }

            override suspend fun createCallAsync(): BaseResponse<ListDocumentModel.ListDocumentResponse> {
                return dataSource.listDocuments(data)
            }

            override fun processResponse(response: BaseResponse<ListDocumentModel.ListDocumentResponse>): ListDocumentModel.ListDocumentResponse {
                return response.jSONData
            }

            override suspend fun saveCallResults(items: ListDocumentModel.ListDocumentResponse) {

            }

            override fun shouldFetch(data: ListDocumentModel.ListDocumentResponse?): Boolean = true

        }.build().asLiveData()

    }

    override suspend fun downloadDocument(forceRefresh: Boolean, data: DownloadRecordModel): LiveData<Resource<DownloadRecordModel.DownloadRecordResponse>> {

        return object : NetworkBoundResource<DownloadRecordModel.DownloadRecordResponse,BaseResponse<DownloadRecordModel.DownloadRecordResponse>>(context) {

            override fun shouldStoreInDb(): Boolean = false

            override suspend fun loadFromDb(): DownloadRecordModel.DownloadRecordResponse {
                return DownloadRecordModel.DownloadRecordResponse()
            }

            override suspend fun createCallAsync(): BaseResponse<DownloadRecordModel.DownloadRecordResponse> {
                return dataSource.downloadDocument(data)
            }

            override fun processResponse(response: BaseResponse<DownloadRecordModel.DownloadRecordResponse>): DownloadRecordModel.DownloadRecordResponse {
                return response.jSONData
            }

            override suspend fun saveCallResults(items: DownloadRecordModel.DownloadRecordResponse) {

            }

            override fun shouldFetch(data: DownloadRecordModel.DownloadRecordResponse?): Boolean = true

        }.build().asLiveData()

    }

    override suspend fun deleteDocument(forceRefresh: Boolean, data: DeleteDocumentModel): LiveData<Resource<DeleteDocumentModel.DeleteDocumentResponse>> {

        return object : NetworkBoundResource<DeleteDocumentModel.DeleteDocumentResponse,BaseResponse<DeleteDocumentModel.DeleteDocumentResponse>>(context) {

            override fun shouldStoreInDb(): Boolean = false

            override suspend fun loadFromDb(): DeleteDocumentModel.DeleteDocumentResponse {
                return DeleteDocumentModel.DeleteDocumentResponse()
            }

            override fun processResponse(response: BaseResponse<DeleteDocumentModel.DeleteDocumentResponse>): DeleteDocumentModel.DeleteDocumentResponse {
                return response.jSONData
            }

            override suspend fun createCallAsync(): BaseResponse<DeleteDocumentModel.DeleteDocumentResponse> {
                return dataSource.deleteDocument(data)
            }

            override suspend fun saveCallResults(items: DeleteDocumentModel.DeleteDocumentResponse) {

            }

            override fun shouldFetch(data: DeleteDocumentModel.DeleteDocumentResponse?): Boolean = true


        }.build().asLiveData()

    }

    override suspend fun listNotifications(forceRefresh: Boolean, data: ListNotificationsModel): LiveData<Resource<ListNotificationsModel.ListNotificationsResponse>> {

        return object : NetworkBoundResource<ListNotificationsModel.ListNotificationsResponse, BaseResponse<ListNotificationsModel.ListNotificationsResponse>>(context) {

            override fun shouldStoreInDb(): Boolean = false

            override suspend fun loadFromDb(): ListNotificationsModel.ListNotificationsResponse {
                return ListNotificationsModel.ListNotificationsResponse()
            }

            override suspend fun createCallAsync(): BaseResponse<ListNotificationsModel.ListNotificationsResponse> {
                return dataSource.listNotifications(data)
            }

            override fun processResponse(response: BaseResponse<ListNotificationsModel.ListNotificationsResponse>): ListNotificationsModel.ListNotificationsResponse {
                return response.jSONData
            }

            override suspend fun saveCallResults(items: ListNotificationsModel.ListNotificationsResponse) {

            }

            override fun shouldFetch(data: ListNotificationsModel.ListNotificationsResponse?): Boolean = true

        }.build().asLiveData()

    }

    override suspend fun updateNotification(forceRefresh: Boolean, data: UpdateNotificationModel): LiveData<Resource<UpdateNotificationModel.UpdateNotificationResponse>> {

        return object : NetworkBoundResource<UpdateNotificationModel.UpdateNotificationResponse, BaseResponse<UpdateNotificationModel.UpdateNotificationResponse>>(context) {

            override fun shouldStoreInDb(): Boolean = false

            override suspend fun loadFromDb(): UpdateNotificationModel.UpdateNotificationResponse {
                return UpdateNotificationModel.UpdateNotificationResponse()
            }

            override suspend fun createCallAsync(): BaseResponse<UpdateNotificationModel.UpdateNotificationResponse> {
                return dataSource.updateNotification(data)
            }

            override fun processResponse(response: BaseResponse<UpdateNotificationModel.UpdateNotificationResponse>): UpdateNotificationModel.UpdateNotificationResponse {
                return response.jSONData
            }

            override suspend fun saveCallResults(items: UpdateNotificationModel.UpdateNotificationResponse) {

            }

            override fun shouldFetch(data: UpdateNotificationModel.UpdateNotificationResponse?): Boolean = true


        }.build().asLiveData()

    }

}