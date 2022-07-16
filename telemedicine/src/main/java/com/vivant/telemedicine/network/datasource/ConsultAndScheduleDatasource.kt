package com.vivant.telemedicine.network.datasource

import com.vivant.telemedicine.model.*
import com.vivant.telemedicine.network.ApiService

class ConsultAndScheduleDatasource(private val defaultService: ApiService,
                                   private val encryptedService: ApiService) {

    suspend fun getConsultationTemplate(data: GetConsultationTemplateModel) = encryptedService.getConsultationTemplate(data)

    suspend fun listSearchedDoctors(data: ListSearchedDoctorsModel) = encryptedService.listSearchedDoctors(data)

    suspend fun getDoctorSlots(data: GetDoctorSlotsModel) = encryptedService.getDoctorSlots(data)

    suspend fun getWallet(data: GetWalletModel) = encryptedService.getWallet(data)

    suspend fun updateWallet(data: UpdateWalletModel) = encryptedService.updateWallet(data)

    suspend fun bookAppointment(data: BookAppointmentModel) = encryptedService.bookAppointment(data)

    suspend fun generateOnBooking(data: GenerateOnBookingModel) = encryptedService.generateOnBooking(data)

    suspend fun saveConsultationRequest(data: SaveConsultationRequestModel) = encryptedService.saveConsultationRequest(data)

    suspend fun checkoutAppointment(data: CheckoutAppointmentModel) = encryptedService.checkoutAppointment(data)

    suspend fun getConsultationRequest(data: GetConsultationRequestModel) = encryptedService.getConsultationRequest(data)

    suspend fun joinRoom(data: JoinRoomModel) = encryptedService.joinRoom(data)

    suspend fun updateConsultationRequest(data: UpdateConsultationRequestModel) = encryptedService.updateConsultationRequest(data)
}