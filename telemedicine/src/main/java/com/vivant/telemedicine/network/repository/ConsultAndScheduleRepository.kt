package com.vivant.telemedicine.network.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.vivant.telemedicine.model.*
import com.vivant.telemedicine.model.base.BaseResponse
import com.vivant.telemedicine.network.datasource.AppointmentDetailsDatasource
import com.vivant.telemedicine.network.datasource.ConsultAndScheduleDatasource
import com.vivant.telemedicine.network.utils.NetworkBoundResource
import com.vivant.telemedicine.network.utils.Resource

interface ConsultAndScheduleRepository {

    suspend fun getConsultationTemplate(forceRefresh: Boolean = false, data: GetConsultationTemplateModel): LiveData<Resource<GetConsultationTemplateModel.GetConsultationTemplateResponse>>
    suspend fun getDoctorSlots(data: GetDoctorSlotsModel): LiveData<Resource<GetDoctorSlotsModel.GetDoctorSlotsResponse>>
    suspend fun listSearchedDoctors(data: ListSearchedDoctorsModel): LiveData<Resource<ListSearchedDoctorsModel.ListSearchedDoctorsResponse>>
    suspend fun getWallet(data: GetWalletModel): LiveData<Resource<GetWalletModel.GetWalletResponse>>
    suspend fun updateWallet(data: UpdateWalletModel): LiveData<Resource<UpdateWalletModel.UpdateWalletResponse>>
    suspend fun bookAppointment(data: BookAppointmentModel): LiveData<Resource<BookAppointmentModel.BookAppointmentResponse>>
    suspend fun generateOnBooking(data: GenerateOnBookingModel): LiveData<Resource<GenerateOnBookingModel.GenerateOnBookingResponse>>

    suspend fun saveConsultationRequest(data: SaveConsultationRequestModel): LiveData<Resource<SaveConsultationRequestModel.SaveConsultationRequestResponse>>
    suspend fun checkoutAppointment(data: CheckoutAppointmentModel): LiveData<Resource<CheckoutAppointmentModel.CheckoutAppointmentResponse>>
    suspend fun getConsultationRequest(data: GetConsultationRequestModel): LiveData<Resource<GetConsultationRequestModel.GetConsultationRequestResponse>>
    suspend fun joinRoom(data: JoinRoomModel): LiveData<Resource<JoinRoomModel.JoinRoomResponse>>
    suspend fun updateConsultationRequest(data: UpdateConsultationRequestModel): LiveData<Resource<UpdateConsultationRequestModel.UpdateConsultationRequestResponse>>
}

class ConsultAndScheduleRepositoryImpl(private val dataSource: ConsultAndScheduleDatasource,
                                       val context: Context) : ConsultAndScheduleRepository {

    override suspend fun getConsultationTemplate(forceRefresh: Boolean, data: GetConsultationTemplateModel): LiveData<Resource<GetConsultationTemplateModel.GetConsultationTemplateResponse>> {

        return object : NetworkBoundResource<GetConsultationTemplateModel.GetConsultationTemplateResponse,BaseResponse<GetConsultationTemplateModel.GetConsultationTemplateResponse>>(context) {

            override fun shouldStoreInDb(): Boolean = false

            override suspend fun loadFromDb(): GetConsultationTemplateModel.GetConsultationTemplateResponse {
                return GetConsultationTemplateModel.GetConsultationTemplateResponse()
            }

            override suspend fun createCallAsync(): BaseResponse<GetConsultationTemplateModel.GetConsultationTemplateResponse> {
                return dataSource.getConsultationTemplate(data)
            }

            override fun processResponse(response: BaseResponse<GetConsultationTemplateModel.GetConsultationTemplateResponse>): GetConsultationTemplateModel.GetConsultationTemplateResponse {
                return response.jSONData
            }

            override suspend fun saveCallResults(items: GetConsultationTemplateModel.GetConsultationTemplateResponse) {

            }

            override fun shouldFetch(data: GetConsultationTemplateModel.GetConsultationTemplateResponse?): Boolean = true

        }.build().asLiveData()

    }

    override suspend fun getDoctorSlots(data: GetDoctorSlotsModel): LiveData<Resource<GetDoctorSlotsModel.GetDoctorSlotsResponse>> {

        return object : NetworkBoundResource<GetDoctorSlotsModel.GetDoctorSlotsResponse, BaseResponse<GetDoctorSlotsModel.GetDoctorSlotsResponse>>(context) {

            override fun shouldStoreInDb(): Boolean = false

            override suspend fun loadFromDb(): GetDoctorSlotsModel.GetDoctorSlotsResponse {
                return GetDoctorSlotsModel.GetDoctorSlotsResponse()
            }

            override suspend fun createCallAsync(): BaseResponse<GetDoctorSlotsModel.GetDoctorSlotsResponse> {
                return dataSource.getDoctorSlots(data)
            }

            override fun processResponse(response: BaseResponse<GetDoctorSlotsModel.GetDoctorSlotsResponse>): GetDoctorSlotsModel.GetDoctorSlotsResponse {
                return response.jSONData
            }

            override suspend fun saveCallResults(items: GetDoctorSlotsModel.GetDoctorSlotsResponse) {

            }

            override fun shouldFetch(data: GetDoctorSlotsModel.GetDoctorSlotsResponse?) : Boolean = true

        }.build().asLiveData()
    }

    override suspend fun listSearchedDoctors(data: ListSearchedDoctorsModel): LiveData<Resource<ListSearchedDoctorsModel.ListSearchedDoctorsResponse>> {

        return object : NetworkBoundResource<ListSearchedDoctorsModel.ListSearchedDoctorsResponse, BaseResponse<ListSearchedDoctorsModel.ListSearchedDoctorsResponse>>(context) {

            override fun shouldStoreInDb(): Boolean = false

            override suspend fun loadFromDb(): ListSearchedDoctorsModel.ListSearchedDoctorsResponse {
                return ListSearchedDoctorsModel.ListSearchedDoctorsResponse()
            }

            override suspend fun createCallAsync(): BaseResponse<ListSearchedDoctorsModel.ListSearchedDoctorsResponse> {
                return dataSource.listSearchedDoctors(data)
            }

            override fun processResponse(response: BaseResponse<ListSearchedDoctorsModel.ListSearchedDoctorsResponse>): ListSearchedDoctorsModel.ListSearchedDoctorsResponse {
                return response.jSONData
            }

            override suspend fun saveCallResults(items: ListSearchedDoctorsModel.ListSearchedDoctorsResponse) {

            }

            override fun shouldFetch(data: ListSearchedDoctorsModel.ListSearchedDoctorsResponse?) : Boolean = true

        }.build().asLiveData()

    }

    override suspend fun getWallet(data: GetWalletModel): LiveData<Resource<GetWalletModel.GetWalletResponse>> {

        return object : NetworkBoundResource<GetWalletModel.GetWalletResponse,BaseResponse<GetWalletModel.GetWalletResponse>>(context) {

            override fun shouldStoreInDb(): Boolean = false

            override suspend fun loadFromDb(): GetWalletModel.GetWalletResponse {
                return GetWalletModel.GetWalletResponse()
            }

            override suspend fun createCallAsync(): BaseResponse<GetWalletModel.GetWalletResponse> {
                return dataSource.getWallet(data)
            }

            override fun processResponse(response: BaseResponse<GetWalletModel.GetWalletResponse>): GetWalletModel.GetWalletResponse {
                return response.jSONData
            }

            override suspend fun saveCallResults(items: GetWalletModel.GetWalletResponse) {

            }

            override fun shouldFetch(data: GetWalletModel.GetWalletResponse?): Boolean = true

        }.build().asLiveData()

    }

    override suspend fun updateWallet(data: UpdateWalletModel): LiveData<Resource<UpdateWalletModel.UpdateWalletResponse>> {

        return object : NetworkBoundResource<UpdateWalletModel.UpdateWalletResponse,BaseResponse<UpdateWalletModel.UpdateWalletResponse>>(context) {

            override fun shouldStoreInDb(): Boolean = false

            override suspend fun loadFromDb(): UpdateWalletModel.UpdateWalletResponse {
                return UpdateWalletModel.UpdateWalletResponse()
            }

            override suspend fun createCallAsync(): BaseResponse<UpdateWalletModel.UpdateWalletResponse> {
                return dataSource.updateWallet(data)
            }

            override fun processResponse(response: BaseResponse<UpdateWalletModel.UpdateWalletResponse>): UpdateWalletModel.UpdateWalletResponse {
                return response.jSONData
            }

            override suspend fun saveCallResults(items: UpdateWalletModel.UpdateWalletResponse) {

            }

            override fun shouldFetch(data: UpdateWalletModel.UpdateWalletResponse?): Boolean = true

        }.build().asLiveData()

    }

    override suspend fun bookAppointment(data: BookAppointmentModel): LiveData<Resource<BookAppointmentModel.BookAppointmentResponse>> {

        return object : NetworkBoundResource<BookAppointmentModel.BookAppointmentResponse,BaseResponse<BookAppointmentModel.BookAppointmentResponse>>(context) {

            override fun shouldStoreInDb(): Boolean = false

            override suspend fun loadFromDb(): BookAppointmentModel.BookAppointmentResponse {
                return BookAppointmentModel.BookAppointmentResponse()
            }

            override suspend fun createCallAsync(): BaseResponse<BookAppointmentModel.BookAppointmentResponse> {
                return dataSource.bookAppointment(data)
            }

            override fun processResponse(response: BaseResponse<BookAppointmentModel.BookAppointmentResponse>): BookAppointmentModel.BookAppointmentResponse {
                return response.jSONData
            }

            override suspend fun saveCallResults(items: BookAppointmentModel.BookAppointmentResponse) {

            }

            override fun shouldFetch(data: BookAppointmentModel.BookAppointmentResponse?) : Boolean = true

        }.build().asLiveData()

    }

    override suspend fun generateOnBooking(data: GenerateOnBookingModel): LiveData<Resource<GenerateOnBookingModel.GenerateOnBookingResponse>> {

        return object : NetworkBoundResource<GenerateOnBookingModel.GenerateOnBookingResponse,BaseResponse<GenerateOnBookingModel.GenerateOnBookingResponse>>(context) {

            override fun shouldStoreInDb(): Boolean = false

            override suspend fun loadFromDb(): GenerateOnBookingModel.GenerateOnBookingResponse {
                return GenerateOnBookingModel.GenerateOnBookingResponse()
            }

            override suspend fun createCallAsync(): BaseResponse<GenerateOnBookingModel.GenerateOnBookingResponse> {
                return dataSource.generateOnBooking(data)
            }

            override fun processResponse(response: BaseResponse<GenerateOnBookingModel.GenerateOnBookingResponse>): GenerateOnBookingModel.GenerateOnBookingResponse {
                return response.jSONData
            }

            override suspend fun saveCallResults(items: GenerateOnBookingModel.GenerateOnBookingResponse) {

            }

            override fun shouldFetch(data: GenerateOnBookingModel.GenerateOnBookingResponse?): Boolean = true

        }.build().asLiveData()

    }

    override suspend fun saveConsultationRequest(data: SaveConsultationRequestModel): LiveData<Resource<SaveConsultationRequestModel.SaveConsultationRequestResponse>> {

        return object : NetworkBoundResource<SaveConsultationRequestModel.SaveConsultationRequestResponse,BaseResponse<SaveConsultationRequestModel.SaveConsultationRequestResponse>>(context) {

            override fun shouldStoreInDb(): Boolean = false

            override suspend fun loadFromDb(): SaveConsultationRequestModel.SaveConsultationRequestResponse {
                return SaveConsultationRequestModel.SaveConsultationRequestResponse()
            }

            override suspend fun createCallAsync(): BaseResponse<SaveConsultationRequestModel.SaveConsultationRequestResponse> {
                return dataSource.saveConsultationRequest(data)
            }

            override fun processResponse(response: BaseResponse<SaveConsultationRequestModel.SaveConsultationRequestResponse>): SaveConsultationRequestModel.SaveConsultationRequestResponse {
                return response.jSONData
            }

            override suspend fun saveCallResults(items: SaveConsultationRequestModel.SaveConsultationRequestResponse) {

            }

            override fun shouldFetch(data: SaveConsultationRequestModel.SaveConsultationRequestResponse?): Boolean = true

        }.build().asLiveData()

    }

    override suspend fun checkoutAppointment(data: CheckoutAppointmentModel): LiveData<Resource<CheckoutAppointmentModel.CheckoutAppointmentResponse>> {

        return object : NetworkBoundResource<CheckoutAppointmentModel.CheckoutAppointmentResponse,BaseResponse<CheckoutAppointmentModel.CheckoutAppointmentResponse>>(context) {

            override fun shouldStoreInDb(): Boolean = false

            override suspend fun loadFromDb(): CheckoutAppointmentModel.CheckoutAppointmentResponse {
                return CheckoutAppointmentModel.CheckoutAppointmentResponse()
            }

            override suspend fun createCallAsync(): BaseResponse<CheckoutAppointmentModel.CheckoutAppointmentResponse> {
                return dataSource.checkoutAppointment(data)
            }

            override fun processResponse(response: BaseResponse<CheckoutAppointmentModel.CheckoutAppointmentResponse>): CheckoutAppointmentModel.CheckoutAppointmentResponse {
                return response.jSONData
            }

            override suspend fun saveCallResults(items: CheckoutAppointmentModel.CheckoutAppointmentResponse) {

            }

            override fun shouldFetch(data: CheckoutAppointmentModel.CheckoutAppointmentResponse?): Boolean = true

        }.build().asLiveData()

    }

    override suspend fun getConsultationRequest(data: GetConsultationRequestModel): LiveData<Resource<GetConsultationRequestModel.GetConsultationRequestResponse>> {

        return object : NetworkBoundResource<GetConsultationRequestModel.GetConsultationRequestResponse,BaseResponse<GetConsultationRequestModel.GetConsultationRequestResponse>>(context) {

            override fun shouldStoreInDb(): Boolean = false

            override suspend fun loadFromDb(): GetConsultationRequestModel.GetConsultationRequestResponse {
                return GetConsultationRequestModel.GetConsultationRequestResponse()
            }

            override suspend fun createCallAsync(): BaseResponse<GetConsultationRequestModel.GetConsultationRequestResponse> {
                return dataSource.getConsultationRequest(data)
            }

            override fun processResponse(response: BaseResponse<GetConsultationRequestModel.GetConsultationRequestResponse>): GetConsultationRequestModel.GetConsultationRequestResponse {
                return response.jSONData
            }

            override suspend fun saveCallResults(items: GetConsultationRequestModel.GetConsultationRequestResponse) {

            }

            override fun shouldFetch(data: GetConsultationRequestModel.GetConsultationRequestResponse?): Boolean = true

        }.build().asLiveData()

    }

    override suspend fun joinRoom(data: JoinRoomModel): LiveData<Resource<JoinRoomModel.JoinRoomResponse>> {

        return object : NetworkBoundResource<JoinRoomModel.JoinRoomResponse,BaseResponse<JoinRoomModel.JoinRoomResponse>>(context) {

            override fun shouldStoreInDb(): Boolean = false

            override suspend fun loadFromDb(): JoinRoomModel.JoinRoomResponse {
                return JoinRoomModel.JoinRoomResponse()
            }

            override suspend fun createCallAsync(): BaseResponse<JoinRoomModel.JoinRoomResponse> {
                return dataSource.joinRoom(data)
            }

            override fun processResponse(response: BaseResponse<JoinRoomModel.JoinRoomResponse>): JoinRoomModel.JoinRoomResponse {
                return response.jSONData
            }

            override suspend fun saveCallResults(items: JoinRoomModel.JoinRoomResponse) {

            }

            override fun shouldFetch(data: JoinRoomModel.JoinRoomResponse?): Boolean = true

        }.build().asLiveData()

    }

    override suspend fun updateConsultationRequest(data: UpdateConsultationRequestModel): LiveData<Resource<UpdateConsultationRequestModel.UpdateConsultationRequestResponse>> {

        return object : NetworkBoundResource<UpdateConsultationRequestModel.UpdateConsultationRequestResponse,BaseResponse<UpdateConsultationRequestModel.UpdateConsultationRequestResponse>>(context) {

            override fun shouldStoreInDb(): Boolean = false

            override suspend fun loadFromDb(): UpdateConsultationRequestModel.UpdateConsultationRequestResponse {
                return UpdateConsultationRequestModel.UpdateConsultationRequestResponse()
            }

            override suspend fun createCallAsync(): BaseResponse<UpdateConsultationRequestModel.UpdateConsultationRequestResponse> {
                return dataSource.updateConsultationRequest(data)
            }

            override fun processResponse(response: BaseResponse<UpdateConsultationRequestModel.UpdateConsultationRequestResponse>): UpdateConsultationRequestModel.UpdateConsultationRequestResponse {
                return response.jSONData
            }

            override suspend fun saveCallResults(items: UpdateConsultationRequestModel.UpdateConsultationRequestResponse) {

            }

            override fun shouldFetch(data: UpdateConsultationRequestModel.UpdateConsultationRequestResponse?): Boolean = true

        }.build().asLiveData()

    }

}