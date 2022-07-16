package com.vivant.telemedicine.network.datasource

import com.vivant.telemedicine.model.*
import com.vivant.telemedicine.network.ApiService

class HomeDatasource(private val defaultService: ApiService,
                     private val encryptedService: ApiService) {

    suspend fun ssoLogin(data: SSOLoginModel) = encryptedService.ssoLogin(data)

    suspend fun getConsultationTemplate(data: GetConsultationTemplateModel) = encryptedService.getConsultationTemplate(data)

    suspend fun listAppointments(data: ListAppointmentsModel) = encryptedService.listAppointments(data)
    suspend fun listConsultation(data: ListConsultationModel) = encryptedService.listConsultation(data)

    suspend fun listDocuments(data: ListDocumentModel) = encryptedService.listDocuments(data)
    suspend fun downloadDocument(data: DownloadRecordModel) = encryptedService.downloadDocument(data)
    suspend fun deleteDocument(data: DeleteDocumentModel) = encryptedService.deleteDocument(data)

    suspend fun listNotifications(data: ListNotificationsModel) = encryptedService.listNotifications(data)
    suspend fun updateNotification(data: UpdateNotificationModel) = encryptedService.updateNotification(data)
}