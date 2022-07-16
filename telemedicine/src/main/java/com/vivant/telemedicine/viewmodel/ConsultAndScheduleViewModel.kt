package com.vivant.telemedicine.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Base64
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.vivant.telemedicine.base.BaseViewModel
import com.vivant.telemedicine.common.*
import com.vivant.telemedicine.model.*
import com.vivant.telemedicine.network.AppDispatchers
import com.vivant.telemedicine.network.domain.AppointmentDetailsUseCase
import com.vivant.telemedicine.network.domain.ConsultAndScheduleUseCase
import com.vivant.telemedicine.network.utils.Resource
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream

@SuppressLint("StaticFieldLeak")
class ConsultAndScheduleViewModel(private val dispatchers: AppDispatchers,
                                  private val consultAndScheduleUseCase: ConsultAndScheduleUseCase,
                                  private val appointmentDetailsUseCase: AppointmentDetailsUseCase,
                                  private val preferenceUtils: PreferenceUtils,
                                  private val context: Context) : BaseViewModel() {

    val firstName = preferenceUtils.getPreference(PreferenceConstants.FIRSTNAME)
    val email = preferenceUtils.getPreference(PreferenceConstants.EMAIL)

    val token = preferenceUtils.getPreference(PreferenceConstants.TOKEN)
    //val token = Constants.TOKEN

    private val consultAndScheduleSingleton = ConsultAndScheduleSingleton.instance!!

    var getConsultationTemplateSource: LiveData<Resource<GetConsultationTemplateModel.GetConsultationTemplateResponse>> = MutableLiveData()
    val _getConsultationTemplate = MediatorLiveData<GetConsultationTemplateModel.GetConsultationTemplateResponse?>()
    val getConsultationTemplate: LiveData<GetConsultationTemplateModel.GetConsultationTemplateResponse?> get() = _getConsultationTemplate

    private var listSearchedDoctorsSource: LiveData<Resource<ListSearchedDoctorsModel.ListSearchedDoctorsResponse>> = MutableLiveData()
    private val _listSearchedDoctors = MediatorLiveData<ListSearchedDoctorsModel.ListSearchedDoctorsResponse?>()
    val listSearchedDoctors: LiveData<ListSearchedDoctorsModel.ListSearchedDoctorsResponse?> get() = _listSearchedDoctors

    private var getDoctorSlotsSource: LiveData<Resource<GetDoctorSlotsModel.GetDoctorSlotsResponse>> = MutableLiveData()
    private val _getDoctorSlots = MediatorLiveData<GetDoctorSlotsModel.GetDoctorSlotsResponse?>()
    val getDoctorSlots: LiveData<GetDoctorSlotsModel.GetDoctorSlotsResponse?> get() = _getDoctorSlots

    private var getWalletSource: LiveData<Resource<GetWalletModel.GetWalletResponse>> = MutableLiveData()
    private val _getWallet = MediatorLiveData<GetWalletModel.GetWalletResponse?>()
    val getWallet: LiveData<GetWalletModel.GetWalletResponse?> get() = _getWallet

    private var updateWalletSource: LiveData<Resource<UpdateWalletModel.UpdateWalletResponse>> = MutableLiveData()
    private val _updateWallet = MediatorLiveData<UpdateWalletModel.UpdateWalletResponse?>()
    val updateWallet: LiveData<UpdateWalletModel.UpdateWalletResponse?> get() = _updateWallet

    private var bookAppointmentSource: LiveData<Resource<BookAppointmentModel.BookAppointmentResponse>> = MutableLiveData()
    private val _bookAppointment = MediatorLiveData<BookAppointmentModel.BookAppointmentResponse?>()
    val bookAppointment: LiveData<BookAppointmentModel.BookAppointmentResponse?> get() = _bookAppointment

    private var generateOnBookingSource: LiveData<Resource<GenerateOnBookingModel.GenerateOnBookingResponse>> = MutableLiveData()
    private val _generateOnBooking = MediatorLiveData<GenerateOnBookingModel.GenerateOnBookingResponse?>()
    val generateOnBooking: LiveData<GenerateOnBookingModel.GenerateOnBookingResponse?> get() = _generateOnBooking

    private var saveConsultationRequestSource: LiveData<Resource<SaveConsultationRequestModel.SaveConsultationRequestResponse>> = MutableLiveData()
    private val _saveConsultationRequest = MediatorLiveData<SaveConsultationRequestModel.SaveConsultationRequestResponse?>()
    val saveConsultationRequest: LiveData<SaveConsultationRequestModel.SaveConsultationRequestResponse?> get() = _saveConsultationRequest

    private var checkoutAppointmentSource: LiveData<Resource<CheckoutAppointmentModel.CheckoutAppointmentResponse>> = MutableLiveData()
    private val _checkoutAppointment = MediatorLiveData<CheckoutAppointmentModel.CheckoutAppointmentResponse?>()
    val checkoutAppointment: LiveData<CheckoutAppointmentModel.CheckoutAppointmentResponse?> get() = _checkoutAppointment

    private var getConsultationRequestSource: LiveData<Resource<GetConsultationRequestModel.GetConsultationRequestResponse>> = MutableLiveData()
    private val _getConsultationRequest = MediatorLiveData<GetConsultationRequestModel.GetConsultationRequestResponse?>()
    val getConsultationRequest: LiveData<GetConsultationRequestModel.GetConsultationRequestResponse?> get() = _getConsultationRequest

    private var joinRoomSource: LiveData<Resource<JoinRoomModel.JoinRoomResponse>> = MutableLiveData()
    private val _joinRoom = MediatorLiveData<JoinRoomModel.JoinRoomResponse?>()
    val joinRoom: LiveData<JoinRoomModel.JoinRoomResponse?> get() = _joinRoom

    private var updateConsultationRequestSource: LiveData<Resource<UpdateConsultationRequestModel.UpdateConsultationRequestResponse>> = MutableLiveData()
    private val _updateConsultationRequest = MediatorLiveData<UpdateConsultationRequestModel.UpdateConsultationRequestResponse?>()
    val updateConsultationRequest: LiveData<UpdateConsultationRequestModel.UpdateConsultationRequestResponse?> get() = _updateConsultationRequest

    private var saveDocumentsSource: LiveData<Resource<SaveDocumentModel.SaveDocumentResponse>> = MutableLiveData()
    private val _saveDocuments = MediatorLiveData<SaveDocumentModel.SaveDocumentResponse?>()
    val saveDocuments: LiveData<SaveDocumentModel.SaveDocumentResponse?> get() = _saveDocuments

    private var shareDocumentsSource: LiveData<Resource<ShareDocumentModel.ShareDocumentResponse>> = MutableLiveData()
    private val _shareDocuments = MediatorLiveData<ShareDocumentModel.ShareDocumentResponse?>()
    val shareDocuments: LiveData<ShareDocumentModel.ShareDocumentResponse?> get() = _shareDocuments

    private var getRequestDetailsSource: LiveData<Resource<GetRequestDetailsModel.GetRequestDetailsResponse>> = MutableLiveData()
    private val _getRequestDetails = MediatorLiveData<GetRequestDetailsModel.GetRequestDetailsResponse?>()
    val getRequestDetails: LiveData<GetRequestDetailsModel.GetRequestDetailsResponse?> get() = _getRequestDetails

    fun callGetConsultationTemplateApi() = viewModelScope.launch(dispatchers.main) {

        val requestData = GetConsultationTemplateModel(Gson().toJson(GetConsultationTemplateModel.JSONDataRequest(
            templateID = 0,
            isDefault = "true"),GetConsultationTemplateModel.JSONDataRequest::class.java),token)

        _progressBar.value = Event("")
        _getConsultationTemplate.removeSource(getConsultationTemplateSource)
        withContext(dispatchers.io) {
            getConsultationTemplateSource = consultAndScheduleUseCase.invokeGetConsultationTemplate(isForceRefresh = true, data = requestData)
        }
        _getConsultationTemplate.addSource(getConsultationTemplateSource) {
            _getConsultationTemplate.value = it.data

            if (it.status == Resource.Status.SUCCESS) {
                _progressBar.value = Event(Event.HIDE_PROGRESS)
                //if (it.data != null) { }
            }
            if (it.status == Resource.Status.ERROR) {
                _progressBar.value = Event(Event.HIDE_PROGRESS)
                if (it.errorNumber.equals("1100014", true)) {
                    _sessionError.value = Event(true)
                } else {
                    toastMessage(it.errorMessage)
                }
            }
        }

    }

    fun callListSearchedDoctorsApi(speciality : String , mode : String) = viewModelScope.launch(dispatchers.main) {

        val requestData = ListSearchedDoctorsModel(
            Gson().toJson(ListSearchedDoctorsModel.JSONDataRequest(
                criteria = ListSearchedDoctorsModel.Criteria(
                    //sortBy = "NAME",
                    speciality = listOf(speciality),
                    consultationModes = listOf(Helper.getModeForApi(mode)),
                    pagingCriteria = ListSearchedDoctorsModel.PagingCriteria(
                        currentPage = 1,
                        pageSize = 0
                    )),
                requestedDate = DateHelper.currentUTCDatetimeInMillisecAsString ),
                ListSearchedDoctorsModel.JSONDataRequest::class.java), token)

        //_progressBar.value = Event("")
        _listSearchedDoctors.removeSource(listSearchedDoctorsSource)
        withContext(dispatchers.io) {
            listSearchedDoctorsSource = consultAndScheduleUseCase.invokeListSearchedDoctors( data = requestData)
        }
        _listSearchedDoctors.addSource(listSearchedDoctorsSource) {
            _listSearchedDoctors.value = it.data

            if (it.status == Resource.Status.SUCCESS) {
                if (it.data != null) {
                    _progressBar.value = Event(Event.HIDE_PROGRESS)
                    //Utilities.printData("Response",it.data,true)
                }
            }
            if (it.status == Resource.Status.ERROR) {
                _progressBar.value = Event(Event.HIDE_PROGRESS)
                if (it.errorNumber.equals("1100014", true)) {
                    _sessionError.value = Event(true)
                } else {
                    toastMessage(it.errorMessage)
                }
            }
        }
    }

    fun callGetDoctorSlotsApi(doctorId:Int,supplierId:Int,date:String,mode:String) = viewModelScope.launch(dispatchers.main) {

        val requestData = GetDoctorSlotsModel(
            Gson().toJson(GetDoctorSlotsModel.JSONDataRequest(
            doctorID = doctorId ,
            supplierID = supplierId ,
            weekDayCode = Helper.getWeekdayCodeForApi(date) ,
            appointmentDate = date,
            appointmentMode = Helper.getModeForApi(mode)),GetDoctorSlotsModel.JSONDataRequest::class.java),token)

        _progressBar.value = Event("")
        _getDoctorSlots.removeSource(getDoctorSlotsSource)
        withContext(dispatchers.io) {
            getDoctorSlotsSource = consultAndScheduleUseCase.invokeGetDoctorSlots( data = requestData)
        }
        _getDoctorSlots.addSource(getDoctorSlotsSource) {
            _getDoctorSlots.value = it.data

            if (it.status == Resource.Status.SUCCESS) {
                if (it.data != null) {
                    _progressBar.value = Event(Event.HIDE_PROGRESS)
                    //Utilities.printData("Response",it.data,true)
                }
            }
            if (it.status == Resource.Status.ERROR) {
                _progressBar.value = Event(Event.HIDE_PROGRESS)
                if (it.errorNumber.equals("1100014", true)) {
                    _sessionError.value = Event(true)
                } else {
                    //toastMessage(it.errorMessage)
                }
            }
        }
    }

    fun callGetWalletApi() = viewModelScope.launch(dispatchers.main) {

        val requestData = GetWalletModel(
            Gson().toJson(GetWalletModel.JSONDataRequest(
                customerID = preferenceUtils.getPreference(PreferenceConstants.CUSTOMERID).toInt() ,
                familyMemberID = 1 ),GetWalletModel.JSONDataRequest::class.java),token)

        _progressBar.value = Event("")
        _getWallet.removeSource(getWalletSource)
        withContext(dispatchers.io) {
            getWalletSource = consultAndScheduleUseCase.invokeGetWallet( data = requestData)
        }
        _getWallet.addSource(getWalletSource) {
            _getWallet.value = it.data

            if (it.status == Resource.Status.SUCCESS) {
                if (it.data != null) {
                    _progressBar.value = Event(Event.HIDE_PROGRESS)
                    //Utilities.printData("Response",it.data,true)
                }
            }
            if (it.status == Resource.Status.ERROR) {
                _progressBar.value = Event(Event.HIDE_PROGRESS)
                if (it.errorNumber.equals("1100014", true)) {
                    _sessionError.value = Event(true)
                } else {
                    toastMessage(it.errorMessage)
                }
            }
        }
    }

    fun callUpdateWalletApi(updatedBalance:Double) = viewModelScope.launch(dispatchers.main) {

        val requestData = UpdateWalletModel(
            Gson().toJson(UpdateWalletModel.JSONDataRequest(
                customerID = preferenceUtils.getPreference(PreferenceConstants.CUSTOMERID).toInt() ,
                wallet = UpdateWalletModel.Wallet(
                    fmid = 1,
                    crd = updatedBalance)),UpdateWalletModel.JSONDataRequest::class.java),token)

        _progressBar.value = Event("")
        _updateWallet.removeSource(updateWalletSource)
        withContext(dispatchers.io) {
            updateWalletSource = consultAndScheduleUseCase.invokeUpdateWallet( data = requestData)
        }
        _updateWallet.addSource(updateWalletSource) {
            _updateWallet.value = it.data

            if (it.status == Resource.Status.SUCCESS) {
                if (it.data != null) {
                    _progressBar.value = Event(Event.HIDE_PROGRESS)
                    //Utilities.printData("Response",it.data,true)
                }
            }
            if (it.status == Resource.Status.ERROR) {
                _progressBar.value = Event(Event.HIDE_PROGRESS)
                if (it.errorNumber.equals("1100014", true)) {
                    _sessionError.value = Event(true)
                } else {
                    toastMessage(it.errorMessage)
                }
            }
        }
    }

    fun callBookAppointmentApi( type:String,walletAmount:String ) = viewModelScope.launch(dispatchers.main) {
        try {
            var requestData = BookAppointmentModel("","")
            when(type) {
                Constants.SCHEDULE_APPOINTMENT -> {
                    requestData = getScheduleAppointmentRequest(walletAmount)
                }
                Constants.BOOK_FOLLOW_UP -> {
                    requestData = getFollowupAppointmentRequest(walletAmount)
                }
            }

        _progressBar.value = Event("")
        _bookAppointment.removeSource(bookAppointmentSource)
        withContext(dispatchers.io) {
            bookAppointmentSource = consultAndScheduleUseCase.invokeBookAppointment( data = requestData)
        }
        _bookAppointment.addSource(bookAppointmentSource) {
            _bookAppointment.value = it.data

            if (it.status == Resource.Status.SUCCESS) {
                if (it.data != null) {
                    _progressBar.value = Event(Event.HIDE_PROGRESS)
                    //Utilities.printData("Response",it.data,true)
                }
            }
            if (it.status == Resource.Status.ERROR) {
                _progressBar.value = Event(Event.HIDE_PROGRESS)
                if (it.errorNumber.equals("1100014", true)) {
                    _sessionError.value = Event(true)
                } else {
                    toastMessage(it.errorMessage)
                }
            }
        }
        } catch ( e : Exception ) {
            e.printStackTrace()
        }
    }

    private fun getScheduleAppointmentRequest(walletAmount:String) : BookAppointmentModel {
        val appDate = DateHelper.convertDateSourceToDestination(consultAndScheduleSingleton.appointmentDate,"yyyy-MM-dd","dd MMM yyyy")
        val requestData = BookAppointmentModel(
            Gson().toJson(BookAppointmentModel.JSONDataRequest(
                appointment = BookAppointmentModel.Appointment(
                    id = 1 ,
                    customerID = preferenceUtils.getPreference(PreferenceConstants.CUSTOMERID).toInt() ,
                    personID = preferenceUtils.getPreference(PreferenceConstants.PERSONID).toInt() ,
                    doctorID = consultAndScheduleSingleton.doctorId ,
                    supplierID = consultAndScheduleSingleton.supplierId ,
                    appointmentDate = appDate ,
                    appointmentStartTime = consultAndScheduleSingleton.appointmentTime ,
                    appointmentEndTime = DateHelper.getTimeBeforeOrAfter(consultAndScheduleSingleton.appointmentTime,
                        "HH:mm:ss",
                        "HH:mm:ss", (consultAndScheduleSingleton.appointmentDuration.toInt()*60000)) ,
                    duration = consultAndScheduleSingleton.appointmentDuration.toInt() ,
                    appointmentMode = consultAndScheduleSingleton.consultationMode ,
                    problemStatement = consultAndScheduleSingleton.currentMedicalHistory ,
                    ongoingMedication = consultAndScheduleSingleton.currentMedication ,
                    companyCoveredAmount = walletAmount ,
                    selfPayAmount = 0 ,
                    price = consultAndScheduleSingleton.totalPrice ,
                    isPaymentDone = true ,
                    problemStartDate = "Not Mentioned" ,
                    isFollowupAppointment = false ,
                    appointmentType = "NEW" ,
                    appointmentStatus = "PENDING" ,
                    appointmentCategory = "TELE" ,
                    isOPDConsumption = false,
                    severity = "Not Mentioned"
                ),
                customer = BookAppointmentModel.Customer(
                    partnerCode = Configuration.PartnerCode,
                    name = preferenceUtils.getPreference(PreferenceConstants.FIRSTNAME),
                    firstName = preferenceUtils.getPreference(PreferenceConstants.FIRSTNAME),
                    lastName = "",
                    emailAddress = preferenceUtils.getPreference(PreferenceConstants.EMAIL),
                    phoneNumber = preferenceUtils.getPreference(PreferenceConstants.PHONE),
                    alternateContactNo = "",
                    gender = preferenceUtils.getPreference(PreferenceConstants.GENDER),
                    //gender = Helper.getGenderDisplayValue(preferenceUtils.getPreference(PreferenceConstants.GENDER),context),
                    dateOfBirth = preferenceUtils.getPreference(PreferenceConstants.DOB),
                    address = "",
                    //address = "Hadapsar, Pune, Maharashtra",
                    latitude = 0,
                    longitude = 0,
                    reference = ""
                )
            ),BookAppointmentModel.JSONDataRequest::class.java),token)
        //Utilities.printData("requestData",requestData,true)
        return requestData
    }

    private fun getFollowupAppointmentRequest(walletAmount:String) : BookAppointmentModel {
        val appDate = DateHelper.convertDateSourceToDestination(consultAndScheduleSingleton.appointmentDate,"yyyy-MM-dd","dd MMM yyyy")
        val requestData = BookAppointmentModel(
            Gson().toJson(BookAppointmentModel.JSONDataRequestFollowup(
                appointment = BookAppointmentModel.AppointmentFollowup(
                    id = preferenceUtils.getPreference(PreferenceConstants.CUSTOMERID).toInt() ,
                    customerID = preferenceUtils.getPreference(PreferenceConstants.CUSTOMERID).toInt() ,
                    personID = preferenceUtils.getPreference(PreferenceConstants.PERSONID).toInt() ,
                    doctorID = consultAndScheduleSingleton.doctorId.toString() ,
                    supplierID = consultAndScheduleSingleton.supplierId ,
                    appointmentDate = appDate ,
                    appointmentStartTime = consultAndScheduleSingleton.appointmentTime ,
                    appointmentEndTime = DateHelper.getTimeBeforeOrAfter(consultAndScheduleSingleton.appointmentTime,
                        "HH:mm:ss",
                        "HH:mm:ss", (consultAndScheduleSingleton.appointmentDuration.toInt()*60000)) ,
                    duration = consultAndScheduleSingleton.appointmentDuration ,
                    appointmentMode = consultAndScheduleSingleton.consultationMode ,
                    companyCoveredAmount = walletAmount ,
                    selfPayAmount = 0 ,
                    price = consultAndScheduleSingleton.totalPrice ,
                    isPaymentDone = true ,
                    isFollowupAppointment = true ,
                    appointmentType = "FLP" ,
                    appointmentStatus = "PENDING" ,
                    appointmentCategory = "TELE" ,
                    isOPDConsumption = false
                ),
                customer = BookAppointmentModel.CustomerFollowup(
                    doctorID = consultAndScheduleSingleton.doctorId ,
                    supplierID = consultAndScheduleSingleton.supplierId ,
                    partnerCode = Configuration.PartnerCode,
                    name = preferenceUtils.getPreference(PreferenceConstants.FIRSTNAME),
                    firstName = preferenceUtils.getPreference(PreferenceConstants.FIRSTNAME),
                    lastName = "",
                    emailAddress = preferenceUtils.getPreference(PreferenceConstants.EMAIL),
                    phoneNumber = preferenceUtils.getPreference(PreferenceConstants.PHONE),
                    alternateContactNo = "",
                    gender = preferenceUtils.getPreference(PreferenceConstants.GENDER),
                    //gender = Helper.getGenderDisplayValue(preferenceUtils.getPreference(PreferenceConstants.GENDER),context),
                    dateOfBirth = preferenceUtils.getPreference(PreferenceConstants.DOB),
                    address = "",
                    //address = "Hadapsar, Pune, Maharashtra",
                    latitude = 0,
                    longitude = 0,
                    reference = ""
                )
            ),BookAppointmentModel.JSONDataRequestFollowup::class.java),token)
        //Utilities.printData("requestData",requestData,true)
        return requestData
    }

    fun callGenerateOnBookingApi(appointmentID:Int) = viewModelScope.launch(dispatchers.main) {

        val requestData = GenerateOnBookingModel(
            Gson().toJson(GenerateOnBookingModel.JSONDataRequest(
                orderDetails = GenerateOnBookingModel.OrderDetails(
                    doctorID = consultAndScheduleSingleton.doctorId,
                    supplierID = consultAndScheduleSingleton.supplierId ,
                    appointmentID = appointmentID,
                    orderDate = DateHelper.currentDateAsStringddMMMyyyy + " " + DateHelper.currentTimeAs_hh_mm_ss,
                    appointmentMode = consultAndScheduleSingleton.consultationMode ,
                    finalPrice = consultAndScheduleSingleton.totalPrice ,
                    status = "NEW")),GenerateOnBookingModel.JSONDataRequest::class.java),token)

        _progressBar.value = Event("")
        _generateOnBooking.removeSource(generateOnBookingSource)
        withContext(dispatchers.io) {
            generateOnBookingSource = consultAndScheduleUseCase.invokeGenerateOnBooking( data = requestData)
        }
        _generateOnBooking.addSource(generateOnBookingSource) {
            _generateOnBooking.value = it.data

            if (it.status == Resource.Status.SUCCESS) {
                if (it.data != null) {
                    _progressBar.value = Event(Event.HIDE_PROGRESS)
                    //Utilities.printData("Response",it.data,true)
                }
            }
            if (it.status == Resource.Status.ERROR) {
                _progressBar.value = Event(Event.HIDE_PROGRESS)
                if (it.errorNumber.equals("1100014", true)) {
                    _sessionError.value = Event(true)
                } else {
                    toastMessage(it.errorMessage)
                }
            }
        }
    }

    fun callSaveConsultationRequestApi() = viewModelScope.launch(dispatchers.main) {

        val time = DateHelper.currentTimeAs_hh_mm_ss
        val requestData = SaveConsultationRequestModel(
            Gson().toJson(SaveConsultationRequestModel.JSONDataRequest(
                request = SaveConsultationRequestModel.Request(
                    customerID = preferenceUtils.getPreference(PreferenceConstants.CUSTOMERID).toInt() ,
                    personID = preferenceUtils.getPreference(PreferenceConstants.PERSONID).toInt() ,
                    appointmentType = "CON",
                    appointmentStatus = "NEW",
                    appointmentCategory = "CONSULTATION",
                    appointmentDate = DateHelper.currentDateAsStringyyyyMMdd + " " + time,
                    appointmentStartTime = time,
                    appointmentEndTime = "",
                    consultationStatus = "NEW",
                    speciality = consultAndScheduleSingleton.speciality,
                    appointmentMode = consultAndScheduleSingleton.consultationMode,
                    problemStatement = consultAndScheduleSingleton.currentMedicalHistory,
                    ongoingMedication = consultAndScheduleSingleton.currentMedication,
                    severity = "Not Mentioned",
                    problemStartDate = "Not Mentioned",
                    companyCoveredAmount = consultAndScheduleSingleton.walletAmount,
                    selfPayAmount = 0,
                    //selfPayAmount = consultAndScheduleSingleton.selfPay,
                    price = consultAndScheduleSingleton.totalPrice)),SaveConsultationRequestModel.JSONDataRequest::class.java),token)

        //_progressBar.value = Event("")
        _saveConsultationRequest.removeSource(saveConsultationRequestSource)
        withContext(dispatchers.io) {
            saveConsultationRequestSource = consultAndScheduleUseCase.invokeSaveConsultationRequest( data = requestData)
        }
        _saveConsultationRequest.addSource(saveConsultationRequestSource) {
            _saveConsultationRequest.value = it.data

            if (it.status == Resource.Status.SUCCESS) {
                if (it.data != null) {
                    _progressBar.value = Event(Event.HIDE_PROGRESS)
                    //Utilities.printData("Response",it.data,true)
                }
            }
            if (it.status == Resource.Status.ERROR) {
                _progressBar.value = Event(Event.HIDE_PROGRESS)
                if (it.errorNumber.equals("1100014", true)) {
                    _sessionError.value = Event(true)
                } else {
                    toastMessage(it.errorMessage)
                }
            }
        }
    }

    fun callCheckoutAppointmentApi(consultationID:Int) = viewModelScope.launch(dispatchers.main) {

        val requestData = CheckoutAppointmentModel(
            Gson().toJson(CheckoutAppointmentModel.JSONDataRequest(
                paymentDetails = CheckoutAppointmentModel.PaymentDetails(
                    fpxDebitAuthCode = "00",
                    fpxSellerOrderNo = consultationID,
                    fpxFpxTxnId = "",
                    fpxFpxTxnTime = "",
                    fpxTxnAmount = "0",
                    buyerbankname = "",
                    appointmentCategory = "CON")),CheckoutAppointmentModel.JSONDataRequest::class.java),token)

        //_progressBar.value = Event("")
        _checkoutAppointment.removeSource(checkoutAppointmentSource)
        withContext(dispatchers.io) {
            checkoutAppointmentSource = consultAndScheduleUseCase.invokeCheckoutAppointment( data = requestData)
        }
        _checkoutAppointment.addSource(checkoutAppointmentSource) {
            _checkoutAppointment.value = it.data

            if (it.status == Resource.Status.SUCCESS) {
                if (it.data != null) {
                    _progressBar.value = Event(Event.HIDE_PROGRESS)
                    //Utilities.printData("Response",it.data,true)
                }
            }
            if (it.status == Resource.Status.ERROR) {
                _progressBar.value = Event(Event.HIDE_PROGRESS)
                if (it.errorNumber.equals("1100014", true)) {
                    _sessionError.value = Event(true)
                } else {
                    toastMessage(it.errorMessage)
                }
            }
        }
    }

    fun callGetConsultationRequestApi(consultationID:Int) = viewModelScope.launch(dispatchers.main) {

        val requestData = GetConsultationRequestModel(
            Gson().toJson(GetConsultationRequestModel.JSONDataRequest(
                consultationID = consultationID.toString()),GetConsultationRequestModel.JSONDataRequest::class.java),token)

        //_progressBar.value = Event("")
        _getConsultationRequest.removeSource(getConsultationRequestSource)
        withContext(dispatchers.io) {
            getConsultationRequestSource = consultAndScheduleUseCase.invokeGetConsultationRequest( data = requestData)
        }
        _getConsultationRequest.addSource(getConsultationRequestSource) {
            _getConsultationRequest.value = it.data

            if (it.status == Resource.Status.SUCCESS) {
                if (it.data != null) {
                    _progressBar.value = Event(Event.HIDE_PROGRESS)
                    //Utilities.printData("Response",it.data,true)
                }
            }
            if (it.status == Resource.Status.ERROR) {
                _progressBar.value = Event(Event.HIDE_PROGRESS)
                if (it.errorNumber.equals("1100014", true)) {
                    _sessionError.value = Event(true)
                } else {
                    toastMessage(it.errorMessage)
                }
            }
        }
    }

    fun callJoinRoomApi(appointmentId:Int) = viewModelScope.launch(dispatchers.main) {

        val requestData = JoinRoomModel(
            Gson().toJson(JoinRoomModel.JSONDataRequest(
                criteria = JoinRoomModel.Criteria(
                    appointmentID = appointmentId.toString(),
                    //appIdentifier = "user_app",
                    appIdentifier = "USERPORTAL"
                )),JoinRoomModel.JSONDataRequest::class.java),token)

        _progressBar.value = Event("")
        _joinRoom.removeSource(joinRoomSource)
        withContext(dispatchers.io) {
            joinRoomSource = consultAndScheduleUseCase.invokeJoinRoom( data = requestData)
        }
        _joinRoom.addSource(joinRoomSource) {
            _joinRoom.value = it.data

            if (it.status == Resource.Status.SUCCESS) {
                if (it.data != null) {
                    _progressBar.value = Event(Event.HIDE_PROGRESS)
                    //Utilities.printData("Response",it.data,true)
                }
            }
            if (it.status == Resource.Status.ERROR) {
                _progressBar.value = Event(Event.HIDE_PROGRESS)
                if (it.errorNumber.equals("1100014", true)) {
                    _sessionError.value = Event(true)
                } else {
                    toastMessage(it.errorMessage)
                }
            }
        }
    }

    fun callUpdateConsultationRequestApi(consultationID:Int) = viewModelScope.launch(dispatchers.main) {

        val requestData = UpdateConsultationRequestModel(
            Gson().toJson(UpdateConsultationRequestModel.JSONDataRequest(
                consultationID = consultationID.toString(),
                consultationStatus = "CAN",
                actionBy = "USER",
                doctorID = null),UpdateConsultationRequestModel.JSONDataRequest::class.java),token)

        _progressBar.value = Event("")
        _updateConsultationRequest.removeSource(updateConsultationRequestSource)
        withContext(dispatchers.io) {
            updateConsultationRequestSource = consultAndScheduleUseCase.invokeUpdateConsultationRequest( data = requestData)
        }
        _updateConsultationRequest.addSource(updateConsultationRequestSource) {
            _updateConsultationRequest.value = it.data

            if (it.status == Resource.Status.SUCCESS) {
                if (it.data != null) {
                    _progressBar.value = Event(Event.HIDE_PROGRESS)
                    //Utilities.printData("Response",it.data,true)
                }
            }
            if (it.status == Resource.Status.ERROR) {
                _progressBar.value = Event(Event.HIDE_PROGRESS)
                if (it.errorNumber.equals("1100014", true)) {
                    _sessionError.value = Event(true)
                } else {
                    toastMessage(it.errorMessage)
                }
            }
        }
    }

    fun callSaveDocumentsApi(appointmentId:Int,doctorId:Int,doctorName:String) = viewModelScope.launch(dispatchers.main) {
        try {
            val healthDocumentsList: MutableList<SaveDocumentModel.HealthDocument> = mutableListOf()
            var encodedFile =""
            for ( i in consultAndScheduleSingleton.recordsList ) {
                val file = File(i.Path,i.OriginalFileName)
                if (file.exists()) {
                    try {
                        val bytesFile = ByteArray(file.length().toInt())
                        context.contentResolver.openFileDescriptor(Uri.fromFile(file), "r")?.use { parcelFileDescriptor ->
                            FileInputStream(parcelFileDescriptor.fileDescriptor).use { inStream ->
                                inStream.read(bytesFile)
                                encodedFile = Base64.encodeToString(bytesFile, Base64.DEFAULT)
                            }
                        }
                    } catch (e: Exception) {
                        Utilities.printLogError("Error retrieving document to upload")
                        e.printStackTrace()
                    }
                } else {
                    Utilities.printLogError("${i.OriginalFileName} : File not found")
                }

                healthDocumentsList.add( SaveDocumentModel.HealthDocument(
                    personID = preferenceUtils.getPreference(PreferenceConstants.PERSONID).toInt(),
                    documentTypeCode = i.Code,
                    recordDate = DateHelper.currentDateAsStringyyyyMMdd + "T" + DateHelper.currentTimeAs_hh_mm_ss,
                    title = i.OriginalFileName,
                    consultationID = 0,
                    comments = "health record",
                    commaKeywords = i.OriginalFileName,
                    doctorName = doctorName,
                    fileName = i.OriginalFileName,
                    fileBytes = encodedFile))
            }

            val requestData = SaveDocumentModel(Gson().toJson(SaveDocumentModel.JSONDataRequest(
                doctorID = doctorId ,
                customerID = preferenceUtils.getPreference(PreferenceConstants.CUSTOMERID).toInt() ,
                appointmentId = appointmentId,
                clevertapSource = "",
                healthDocuments = healthDocumentsList),SaveDocumentModel.JSONDataRequest::class.java),token)

            //Utilities.printData("requestData",requestData)

            _progressBar.value = Event("")
            _saveDocuments.removeSource(saveDocumentsSource)
            withContext(dispatchers.io) {
                saveDocumentsSource = appointmentDetailsUseCase.invokeSaveDocuments( data = requestData)
            }
            _saveDocuments.addSource(saveDocumentsSource) {
                _saveDocuments.value = it.data

                if (it.status == Resource.Status.SUCCESS) {
                    if (it.data != null) {
                        _progressBar.value = Event(Event.HIDE_PROGRESS)
                        //Utilities.printData("Response",it.data,true)
                    }
                }
                if (it.status == Resource.Status.ERROR) {
                    _progressBar.value = Event(Event.HIDE_PROGRESS)
                    if (it.errorNumber.equals("1100014", true)) {
                        _sessionError.value = Event(true)
                    } else {
                        toastMessage(it.errorMessage)
                    }
                }
            }
        } catch ( e : Exception ) {

        }
    }

    fun callShareDocumentsApi(appointmentId:Int,doctorId:Int,doctorName:String) = viewModelScope.launch(dispatchers.main) {

        val sharedWithList : MutableList<ShareDocumentModel.SharedWith> = mutableListOf()
        sharedWithList.add(ShareDocumentModel.SharedWith(
            id = 0,
            name = doctorName,
            email = preferenceUtils.getPreference(PreferenceConstants.EMAIL),
            phoneNumber = preferenceUtils.getPreference(PreferenceConstants.PHONE),
        ))

        val documentList : MutableList<ShareDocumentModel.Document> = mutableListOf()
        for ( i in consultAndScheduleSingleton.recordsList ) {
            documentList.add(ShareDocumentModel.Document(
                title = i.OriginalFileName,
                documentID = i.Id.toInt(),
                documentTypeCode = i.Code,
                relatedTo = Helper.getDocumentTypeDescForApi(i.Code),
            ))

        }
        val requestData = ShareDocumentModel(Gson().toJson(ShareDocumentModel.JSONDataRequest(
            appointmentID = appointmentId,
            doctorID = doctorId ,
            customerID = preferenceUtils.getPreference(PreferenceConstants.CUSTOMERID).toInt() ,
            docDetails = ShareDocumentModel.DocDetails(
                entityID = preferenceUtils.getPreference(PreferenceConstants.PERSONID).toInt(),
                expiryDate = "1970-01-01 00:00:00.000",
                comments = "",
                sharedWith = sharedWithList,
                documentList = documentList,
            )),ShareDocumentModel.JSONDataRequest::class.java),token)

        _progressBar.value = Event("")
        _shareDocuments.removeSource(shareDocumentsSource)
        withContext(dispatchers.io) {
            shareDocumentsSource = appointmentDetailsUseCase.invokeShareDocuments( data = requestData)
        }
        _shareDocuments.addSource(shareDocumentsSource) {
            _shareDocuments.value = it.data

            if (it.status == Resource.Status.SUCCESS) {
                if (it.data != null) {
                    _progressBar.value = Event(Event.HIDE_PROGRESS)
                    //Utilities.printData("Response",it.data,true)
                }
            }
            if (it.status == Resource.Status.ERROR) {
                _progressBar.value = Event(Event.HIDE_PROGRESS)
                if (it.errorNumber.equals("1100014", true)) {
                    _sessionError.value = Event(true)
                } else {
                    toastMessage(it.errorMessage)
                }
            }
        }
    }

    fun callGetRequestDetailsApi( doctorId:String,date:String ) = viewModelScope.launch(dispatchers.main) {

        val requestData = GetRequestDetailsModel(Gson().toJson(GetRequestDetailsModel.JSONDataRequest(
            criteria = GetRequestDetailsModel.Criteria(
                doctorID = doctorId ,
                accountID = null ,
                requestNumber = doctorId),
            requestedDate = date), GetRequestDetailsModel.JSONDataRequest::class.java),token)

        //Utilities.printData("requestData",requestData,true)

        _progressBar.value = Event("")
        _getRequestDetails.removeSource(getRequestDetailsSource)
        withContext(dispatchers.io) {
            getRequestDetailsSource = appointmentDetailsUseCase.invokeGetRequestDetails( data = requestData)
        }
        _getRequestDetails.addSource(getRequestDetailsSource) {
            _getRequestDetails.value = it.data

            if (it.status == Resource.Status.SUCCESS) {
                if (it.data != null) {
                    _progressBar.value = Event(Event.HIDE_PROGRESS)
                    //Utilities.printData("Response",it.data,true)
                }
            }
            if (it.status == Resource.Status.ERROR) {
                _progressBar.value = Event(Event.HIDE_PROGRESS)
                if (it.errorNumber.equals("1100014", true)) {
                    _sessionError.value = Event(true)
                } else {
                    //toastMessage(it.errorMessage)
                }
            }
        }
    }


/*    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text*/

}