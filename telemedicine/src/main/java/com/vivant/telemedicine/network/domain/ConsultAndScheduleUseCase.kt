package com.vivant.telemedicine.network.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.vivant.telemedicine.model.*
import com.vivant.telemedicine.network.repository.AppointmentDetailsRepository
import com.vivant.telemedicine.network.repository.ConsultAndScheduleRepository
import com.vivant.telemedicine.network.utils.Resource

class ConsultAndScheduleUseCase(private val repository: ConsultAndScheduleRepository) {

    suspend fun invokeGetConsultationTemplate(isForceRefresh: Boolean, data: GetConsultationTemplateModel): LiveData<Resource<GetConsultationTemplateModel.GetConsultationTemplateResponse>> {
        return Transformations.map(repository.getConsultationTemplate(isForceRefresh, data)) {
            it
        }
    }

    suspend fun invokeListSearchedDoctors(data: ListSearchedDoctorsModel): LiveData<Resource<ListSearchedDoctorsModel.ListSearchedDoctorsResponse>> {
        return Transformations.map(repository.listSearchedDoctors(data = data)) {
            it
        }
    }

    suspend fun invokeGetDoctorSlots(data: GetDoctorSlotsModel): LiveData<Resource<GetDoctorSlotsModel.GetDoctorSlotsResponse>> {
        return Transformations.map(repository.getDoctorSlots(data = data)) {
            it
        }
    }

    suspend fun invokeGetWallet(data: GetWalletModel): LiveData<Resource<GetWalletModel.GetWalletResponse>> {
        return Transformations.map(repository.getWallet(data = data)) {
            it
        }
    }

    suspend fun invokeUpdateWallet(data: UpdateWalletModel): LiveData<Resource<UpdateWalletModel.UpdateWalletResponse>> {
        return Transformations.map(repository.updateWallet(data = data)) {
            it
        }
    }

    suspend fun invokeBookAppointment(data: BookAppointmentModel): LiveData<Resource<BookAppointmentModel.BookAppointmentResponse>> {
        return Transformations.map(repository.bookAppointment(data = data)) {
            it
        }
    }

    suspend fun invokeGenerateOnBooking(data: GenerateOnBookingModel): LiveData<Resource<GenerateOnBookingModel.GenerateOnBookingResponse>> {
        return Transformations.map(repository.generateOnBooking(data = data)) {
            it
        }
    }


    suspend fun invokeSaveConsultationRequest(data: SaveConsultationRequestModel): LiveData<Resource<SaveConsultationRequestModel.SaveConsultationRequestResponse>> {
        return Transformations.map(repository.saveConsultationRequest(data = data)) {
            it
        }
    }

    suspend fun invokeCheckoutAppointment(data: CheckoutAppointmentModel): LiveData<Resource<CheckoutAppointmentModel.CheckoutAppointmentResponse>> {
        return Transformations.map(repository.checkoutAppointment(data = data)) {
            it
        }
    }

    suspend fun invokeGetConsultationRequest(data: GetConsultationRequestModel): LiveData<Resource<GetConsultationRequestModel.GetConsultationRequestResponse>> {
        return Transformations.map(repository.getConsultationRequest(data = data)) {
            it
        }
    }

    suspend fun invokeJoinRoom(data: JoinRoomModel): LiveData<Resource<JoinRoomModel.JoinRoomResponse>> {
        return Transformations.map(repository.joinRoom(data = data)) {
            it
        }
    }

    suspend fun invokeUpdateConsultationRequest(data: UpdateConsultationRequestModel): LiveData<Resource<UpdateConsultationRequestModel.UpdateConsultationRequestResponse>> {
        return Transformations.map(repository.updateConsultationRequest(data = data)) {
            it
        }
    }

}