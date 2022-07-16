package com.vivant.telemedicine.network.datasource

import com.vivant.telemedicine.model.*
import com.vivant.telemedicine.network.ApiService
import okhttp3.RequestBody

class AppointmentDetailsDatasource(private val defaultService: ApiService,
                                   private val encryptedService: ApiService) {

    suspend fun saveDocuments(data: SaveDocumentModel) = encryptedService.saveDocuments(data)

    suspend fun shareDocuments(data: ShareDocumentModel) = encryptedService.shareDocuments(data)

    suspend fun removeSharedDocument(data: RemoveSharedDocumentModel) = encryptedService.removeSharedDocument(data)

    suspend fun rescheduleAppointment(data: RescheduleAppointmentModel) = encryptedService.rescheduleAppointment(data)

    suspend fun cancelAppointment(data: CancelAppointmentModel) = encryptedService.cancelAppointment(data)

    suspend fun downloadInvoice(cid: String,
                                appid: String,
                                oid: String,
                                docid: String,
                                sid: String,
                                isPrint: Boolean = true) = defaultService.downloadInvoice(cid,appid,oid,docid,sid,isPrint)

    suspend fun downloadPrescription(cid: String,
                                     appid: String,
                                     conid: String,
                                     docid: String) = defaultService.downloadPrescription(cid,appid,conid,docid)

    suspend fun getConsultationHealthDocument(data: GetConsultationHealthDocumentModel) = encryptedService.getConsultationHealthDocument(data)

    suspend fun saveFeedback(data: SaveFeedbackModel) = encryptedService.saveFeedback(data)

    suspend fun getRequestDetails(data: GetRequestDetailsModel) = encryptedService.getRequestDetails(data)

    suspend fun patientStateList(data: PatientStateListModel) = encryptedService.patientStateList(data)

    suspend fun sendEpharmaDetails(data: RequestBody) = encryptedService.sendEpharmaDetails(data)
}