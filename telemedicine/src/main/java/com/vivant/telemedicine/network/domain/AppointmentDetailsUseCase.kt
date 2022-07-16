package com.vivant.telemedicine.network.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.vivant.telemedicine.model.*
import com.vivant.telemedicine.network.repository.AppointmentDetailsRepository
import com.vivant.telemedicine.network.repository.ConsultAndScheduleRepository
import com.vivant.telemedicine.network.repository.HomeRepository
import com.vivant.telemedicine.network.utils.Resource
import okhttp3.RequestBody

class AppointmentDetailsUseCase(private val appointmentDetailsRepository: AppointmentDetailsRepository,private val consultAndScheduleRepository: ConsultAndScheduleRepository,private val repository: HomeRepository) {

    suspend fun invokeListAppointments(isForceRefresh: Boolean, data: ListAppointmentsModel): LiveData<Resource<ListAppointmentsModel.ListAppointmentsResponse>> {
        return Transformations.map(repository.listAppointments(isForceRefresh, data)) {
            it
        }
    }

    suspend fun invokeSaveDocuments(data: SaveDocumentModel): LiveData<Resource<SaveDocumentModel.SaveDocumentResponse>> {
        return Transformations.map(appointmentDetailsRepository.saveDocuments(data = data)) {
            it
        }
    }

    suspend fun invokeShareDocuments(data: ShareDocumentModel): LiveData<Resource<ShareDocumentModel.ShareDocumentResponse>> {
        return Transformations.map(appointmentDetailsRepository.shareDocuments(data = data)) {
            it
        }
    }

    suspend fun invokeRemoveSharedDocument(data: RemoveSharedDocumentModel): LiveData<Resource<RemoveSharedDocumentModel.RemoveSharedDocumentResponse>> {
        return Transformations.map(appointmentDetailsRepository.removeSharedDocument(data = data)) {
            it
        }
    }

    suspend fun invokeRescheduleAppointment(data: RescheduleAppointmentModel): LiveData<Resource<RescheduleAppointmentModel.RescheduleAppointmentResponse>> {
        return Transformations.map(appointmentDetailsRepository.rescheduleAppointment(data = data)) {
            it
        }
    }

    suspend fun invokeCancelAppointment(data: CancelAppointmentModel): LiveData<Resource<CancelAppointmentModel.CancelAppointmentResponse>> {
        return Transformations.map(appointmentDetailsRepository.cancelAppointment(data = data)) {
            it
        }
    }

    suspend fun invokeDownloadInvoice(cid:String,appid:String,oid:String,docid:String,sid:String): LiveData<Resource<DownloadInvoiceResponse>> {
        return Transformations.map(appointmentDetailsRepository.downloadInvoice(
            cid = cid,appid = appid,oid = oid,docid = docid,sid = sid)) {
            it
        }
    }

    suspend fun invokeDownloadPrescription(cid:String,appid:String,conid:String,docid:String): LiveData<Resource<DownloadPrescriptionResponse>> {
        return Transformations.map(appointmentDetailsRepository.downloadPrescription(
            cid = cid,appid = appid, conid = conid,docid = docid)) {
            it
        }
    }

    suspend fun invokeGetConsultationHealthDocument(data: GetConsultationHealthDocumentModel): LiveData<Resource<GetConsultationHealthDocumentModel.GetConsultationHealthDocumentResponse>> {
        return Transformations.map(appointmentDetailsRepository.getConsultationHealthDocument(data = data)) {
            it
        }
    }

    suspend fun invokeSaveFeedback(data: SaveFeedbackModel): LiveData<Resource<SaveFeedbackModel.SaveFeedbackResponse>> {
        return Transformations.map(appointmentDetailsRepository.saveFeedback(data = data)) {
            it
        }
    }

    suspend fun invokeGetRequestDetails(data: GetRequestDetailsModel): LiveData<Resource<GetRequestDetailsModel.GetRequestDetailsResponse>> {
        return Transformations.map(appointmentDetailsRepository.getRequestDetails(data = data)) {
            it
        }
    }

    suspend fun invokeGetDoctorSlots(data: GetDoctorSlotsModel): LiveData<Resource<GetDoctorSlotsModel.GetDoctorSlotsResponse>> {
        return Transformations.map(consultAndScheduleRepository.getDoctorSlots(data = data)) {
            it
        }
    }

    suspend fun invokePatientStateList(data: PatientStateListModel): LiveData<Resource<PatientStateListModel.PatientStateListResponse>> {
        return Transformations.map(appointmentDetailsRepository.patientStateList(data = data)) {
            it
        }
    }

    suspend fun invokeSendEpharmaDetails(data: RequestBody): LiveData<Resource<SendEpharmaDetailsModel.SendEpharmaDetailsResponse>> {
        return Transformations.map(appointmentDetailsRepository.sendEpharmaDetails(data = data)) {
            it
        }
    }

}
