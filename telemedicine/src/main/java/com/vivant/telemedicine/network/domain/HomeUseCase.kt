package com.vivant.telemedicine.network.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.vivant.telemedicine.model.*
import com.vivant.telemedicine.network.repository.HomeRepository
import com.vivant.telemedicine.network.utils.Resource

class HomeUseCase(private val repository: HomeRepository) {

    suspend fun invokeSSOLogin(isForceRefresh: Boolean, data: SSOLoginModel): LiveData<Resource<SSOLoginModel.SSOLoginResponse>> {
        return Transformations.map(repository.ssoLogin(isForceRefresh, data)) {
            it
        }
    }

    suspend fun invokeListAppointments(isForceRefresh: Boolean, data: ListAppointmentsModel): LiveData<Resource<ListAppointmentsModel.ListAppointmentsResponse>> {
        return Transformations.map(repository.listAppointments(isForceRefresh, data)) {
            it
        }
    }

    suspend fun invokeListConsultation(isForceRefresh: Boolean, data: ListConsultationModel): LiveData<Resource<ListConsultationModel.ListConsultationResponse>> {
        return Transformations.map(repository.listConsultation(isForceRefresh, data)) {
            it
        }
    }

    suspend fun invokeListDocument(isForceRefresh: Boolean, data: ListDocumentModel): LiveData<Resource<ListDocumentModel.ListDocumentResponse>> {
        return Transformations.map(repository.listDocument(isForceRefresh, data)) {
            it
        }
    }

    suspend fun invokeDownloadDocument(isForceRefresh: Boolean, data: DownloadRecordModel): LiveData<Resource<DownloadRecordModel.DownloadRecordResponse>> {
        return Transformations.map(repository.downloadDocument(isForceRefresh, data)) {
            it
        }
    }

    suspend fun invokeDeleteDocument(isForceRefresh: Boolean, data: DeleteDocumentModel): LiveData<Resource<DeleteDocumentModel.DeleteDocumentResponse>> {
        return Transformations.map(repository.deleteDocument(isForceRefresh, data)) {
            it
        }
    }

    suspend fun invokeListNotifications(isForceRefresh: Boolean, data: ListNotificationsModel): LiveData<Resource<ListNotificationsModel.ListNotificationsResponse>> {
        return Transformations.map(repository.listNotifications(isForceRefresh, data)) {
            it
        }
    }

    suspend fun invokeUpdateNotification(isForceRefresh: Boolean, data: UpdateNotificationModel): LiveData<Resource<UpdateNotificationModel.UpdateNotificationResponse>> {
        return Transformations.map(repository.updateNotification(isForceRefresh, data)) {
            it
        }
    }

}
