package com.vivant.telemedicine.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.vivant.telemedicine.R
import com.vivant.telemedicine.base.BaseViewModel
import com.vivant.telemedicine.common.*
import com.vivant.telemedicine.model.*
import com.vivant.telemedicine.network.AppDispatchers
import com.vivant.telemedicine.network.domain.AppointmentDetailsUseCase
import com.vivant.telemedicine.network.utils.Resource
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject

@SuppressLint("StaticFieldLeak")
class AppointmentDetailsViewModel(private val dispatchers: AppDispatchers,
                                  private val appointmentDetailsUseCase: AppointmentDetailsUseCase,
                                  private val preferenceUtils: PreferenceUtils,
                                  private val context: Context ) : BaseViewModel() {

    val token = preferenceUtils.getPreference(PreferenceConstants.TOKEN)
    //val token = Constants.TOKEN
    val firstName = preferenceUtils.getPreference(PreferenceConstants.FIRSTNAME)

    var listAppointmentsSource: LiveData<Resource<ListAppointmentsModel.ListAppointmentsResponse>> = MutableLiveData()
    val _listAppointments = MediatorLiveData<ListAppointmentsModel.ListAppointmentsResponse?>()
    val listAppointments: LiveData<ListAppointmentsModel.ListAppointmentsResponse?> get() = _listAppointments

    private var saveDocumentsSource: LiveData<Resource<SaveDocumentModel.SaveDocumentResponse>> = MutableLiveData()
    private val _saveDocuments = MediatorLiveData<SaveDocumentModel.SaveDocumentResponse?>()
    val saveDocuments: LiveData<SaveDocumentModel.SaveDocumentResponse?> get() = _saveDocuments

    private var shareDocumentsSource: LiveData<Resource<ShareDocumentModel.ShareDocumentResponse>> = MutableLiveData()
    private val _shareDocuments = MediatorLiveData<ShareDocumentModel.ShareDocumentResponse?>()
    val shareDocuments: LiveData<ShareDocumentModel.ShareDocumentResponse?> get() = _shareDocuments

    private var removeSharedDocumentSource: LiveData<Resource<RemoveSharedDocumentModel.RemoveSharedDocumentResponse>> = MutableLiveData()
    private val _removeSharedDocument = MediatorLiveData<RemoveSharedDocumentModel.RemoveSharedDocumentResponse?>()
    val removeSharedDocument: LiveData<RemoveSharedDocumentModel.RemoveSharedDocumentResponse?> get() = _removeSharedDocument

    private var rescheduleAppointmentSource: LiveData<Resource<RescheduleAppointmentModel.RescheduleAppointmentResponse>> = MutableLiveData()
    private val _rescheduleAppointment = MediatorLiveData<RescheduleAppointmentModel.RescheduleAppointmentResponse?>()
    val rescheduleAppointment: LiveData<RescheduleAppointmentModel.RescheduleAppointmentResponse?> get() = _rescheduleAppointment

    private var cancelAppointmentSource: LiveData<Resource<CancelAppointmentModel.CancelAppointmentResponse>> = MutableLiveData()
    private val _cancelAppointment = MediatorLiveData<CancelAppointmentModel.CancelAppointmentResponse?>()
    val cancelAppointment: LiveData<CancelAppointmentModel.CancelAppointmentResponse?> get() = _cancelAppointment

    private var getDoctorSlotsSource: LiveData<Resource<GetDoctorSlotsModel.GetDoctorSlotsResponse>> = MutableLiveData()
    private val _getDoctorSlots = MediatorLiveData<GetDoctorSlotsModel.GetDoctorSlotsResponse?>()
    val getDoctorSlots: LiveData<GetDoctorSlotsModel.GetDoctorSlotsResponse?> get() = _getDoctorSlots

    private var downloadInvoiceSource: LiveData<Resource<DownloadInvoiceResponse>> = MutableLiveData()
    private val _downloadInvoice = MediatorLiveData<DownloadInvoiceResponse?>()
    val downloadInvoice: LiveData<DownloadInvoiceResponse?> get() = _downloadInvoice

    private var downloadPrescriptionSource: LiveData<Resource<DownloadPrescriptionResponse>> = MutableLiveData()
    private val _downloadPrescription = MediatorLiveData<DownloadPrescriptionResponse?>()
    val downloadPrescription: LiveData<DownloadPrescriptionResponse?> get() = _downloadPrescription

    private var saveFeedbackSource: LiveData<Resource<SaveFeedbackModel.SaveFeedbackResponse>> = MutableLiveData()
    private val _saveFeedback = MediatorLiveData<SaveFeedbackModel.SaveFeedbackResponse?>()
    val saveFeedback: LiveData<SaveFeedbackModel.SaveFeedbackResponse?> get() = _saveFeedback

    private var getConsultationHealthDocumentSource: LiveData<Resource<GetConsultationHealthDocumentModel.GetConsultationHealthDocumentResponse>> = MutableLiveData()
    private val _getConsultationHealthDocument= MediatorLiveData<GetConsultationHealthDocumentModel.GetConsultationHealthDocumentResponse?>()
    val getConsultationHealthDocument: LiveData<GetConsultationHealthDocumentModel.GetConsultationHealthDocumentResponse?> get() = _getConsultationHealthDocument

    private var patientStateListSource: LiveData<Resource<PatientStateListModel.PatientStateListResponse>> = MutableLiveData()
    private val _patientStateList = MediatorLiveData<PatientStateListModel.PatientStateListResponse?>()
    val patientStateList: LiveData<PatientStateListModel.PatientStateListResponse?> get() = _patientStateList

    private var sendEpharmaDetailsSource: LiveData<Resource<SendEpharmaDetailsModel.SendEpharmaDetailsResponse>> = MutableLiveData()
    private val _sendEpharmaDetails = MediatorLiveData<SendEpharmaDetailsModel.SendEpharmaDetailsResponse?>()
    val sendEpharmaDetails: LiveData<SendEpharmaDetailsModel.SendEpharmaDetailsResponse?> get() = _sendEpharmaDetails

    fun callListAppointmentsApi( from:String ) = viewModelScope.launch(dispatchers.main) {

        var sDate = ""
        var apptStatus = ""
        var mode = ""
        when(from) {
            Constants.MY_APPOINTMENTS -> {
                sDate = DateHelper.currentDateAsStringyyyyMMdd
                apptStatus = "BOOKED,PENDING,WIC,ACT,RES,CONFIRM,CAN"
                mode = "LISTUPCOMING"
            }
            Constants.MY_HISTORY -> {
                apptStatus = "CAN,PENDING,ACT,RES,CONFIRM,INPROGRESS,NOTACCEPTED,NOTFOUND"
                mode = "LISTPAST"
            }
        }

        val requestData = ListAppointmentsModel(Gson().toJson(ListAppointmentsModel.JSONDataRequest(
            criteria = ListAppointmentsModel.Criteria(
                customerID = preferenceUtils.getPreference(PreferenceConstants.CUSTOMERID).toInt() ,
                startDate = sDate,
                sortBy = "DEFAULT",
                appointmentStatus = apptStatus,
                requestDate = DateHelper.currentDateAsStringddMMMyyyyNew,
                requestTime = DateHelper.getTimeBeforeOrAfter(DateHelper.currentTimeAs_hh_mm,"HH:mm","hh:mm a",-1800000),
                //requestTime = DateHelper.currentTimeIn12HrFormatAmOrPm,
                opMode = mode),
            pagingCriteria = ListAppointmentsModel.PagingCriteria(
                currentPage = 1,
                startIndex = 1,
                pageSize = 0)),ListAppointmentsModel.JSONDataRequest::class.java),token)

        //_progressBar.value = Event("")
        _listAppointments.removeSource(listAppointmentsSource)
        withContext(dispatchers.io) {
            listAppointmentsSource = appointmentDetailsUseCase.invokeListAppointments(isForceRefresh = true, data = requestData)
        }
        _listAppointments.addSource(listAppointmentsSource) {
            _listAppointments.value = it.data

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

    fun callSaveDocumentsApi(appointmentId:Int,doctorId:Int,doctorName:String,docTypeCode:String,
                             recordDate:String,title:String,fileBytes:String) = viewModelScope.launch(dispatchers.main) {

        val healthDocumentsList: MutableList<SaveDocumentModel.HealthDocument> = mutableListOf()
        healthDocumentsList.add( SaveDocumentModel.HealthDocument(
            personID = preferenceUtils.getPreference(PreferenceConstants.PERSONID).toInt(),
            documentTypeCode = docTypeCode,
            recordDate = recordDate,
            title = title,
            consultationID = 0,
            comments = "health record",
            commaKeywords = title,
            doctorName = doctorName,
            fileName = title,
            fileBytes = fileBytes))

        val requestData = SaveDocumentModel(Gson().toJson(SaveDocumentModel.JSONDataRequest(
            doctorID = doctorId ,
            customerID = preferenceUtils.getPreference(PreferenceConstants.CUSTOMERID).toInt() ,
            appointmentId = appointmentId,
            clevertapSource = "From Patient View",
            healthDocuments = healthDocumentsList),SaveDocumentModel.JSONDataRequest::class.java),token)

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
    }

    fun callShareDocumentsApi(appointmentId:Int,doctorId:Int,doctorName:String,
                              documentId:Int,title:String,code:String) = viewModelScope.launch(dispatchers.main) {

        val sharedWithList : MutableList<ShareDocumentModel.SharedWith> = mutableListOf()
        sharedWithList.add(ShareDocumentModel.SharedWith(
            id = 0,
            name = doctorName,
            email = preferenceUtils.getPreference(PreferenceConstants.EMAIL),
            phoneNumber = preferenceUtils.getPreference(PreferenceConstants.PHONE),
        ))

        val documentList : MutableList<ShareDocumentModel.Document> = mutableListOf()
        documentList.add(ShareDocumentModel.Document(
            title = title,
            documentID = documentId,
            documentTypeCode = code,
            relatedTo = Helper.getDocumentTypeDescForApi(code),
        ))

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

    fun callRemoveSharedDocumentApi(id : Int) = viewModelScope.launch(dispatchers.main) {

        val requestData = RemoveSharedDocumentModel(Gson().toJson(RemoveSharedDocumentModel.JSONDataRequest(
            customerID = preferenceUtils.getPreference(PreferenceConstants.CUSTOMERID).toInt() ,
            documentID = id),RemoveSharedDocumentModel.JSONDataRequest::class.java),token)

        _progressBar.value = Event("")
        _removeSharedDocument.removeSource(removeSharedDocumentSource)
        withContext(dispatchers.io) {
            removeSharedDocumentSource = appointmentDetailsUseCase.invokeRemoveSharedDocument( data = requestData)
        }
        _removeSharedDocument.addSource(removeSharedDocumentSource) {
            _removeSharedDocument.value = it.data

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

    fun callRescheduleAppointmentApi(id:Int,date:String,startTime:String,duration:Int,mode:String,reason:String) = viewModelScope.launch(dispatchers.main) {

        val requestData = RescheduleAppointmentModel(Gson().toJson(RescheduleAppointmentModel.JSONDataRequest(
            customerID = preferenceUtils.getPreference(PreferenceConstants.CUSTOMERID).toInt() ,
            appointmentDate = date,
            startTime = startTime,
            endTime = DateHelper.getTimeBeforeOrAfter(startTime,
                "HH:mm:ss",
                "HH:mm:ss",(duration*60000)),
            isFollowupAppointment = false,
            appointmentID = id,
            appointmentCategory = "TELE",
            appointmentMode = mode,
            cancelReasonCode = "RES",
            cancelReasonTxt = reason,
            appIdentifier = "user_app"),RescheduleAppointmentModel.JSONDataRequest::class.java),token)

        _progressBar.value = Event("")
        _rescheduleAppointment.removeSource(rescheduleAppointmentSource)
        withContext(dispatchers.io) {
            rescheduleAppointmentSource = appointmentDetailsUseCase.invokeRescheduleAppointment( data = requestData)
        }
        _rescheduleAppointment.addSource(rescheduleAppointmentSource) {
            _rescheduleAppointment.value = it.data

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

    fun callCancelAppointmentApi(id:Int,reason:String) = viewModelScope.launch(dispatchers.main) {

        val requestData = CancelAppointmentModel(Gson().toJson(CancelAppointmentModel.JSONDataRequest(
            customerID = preferenceUtils.getPreference(PreferenceConstants.CUSTOMERID).toInt() ,
            appointmentID = id ,
            status = "CAN",
            cancelReasonCode = "CAN",
            cancelReasonTxt = reason,
            appIdentifier = "user_app"),CancelAppointmentModel.JSONDataRequest::class.java),token)

        _progressBar.value = Event("")
        _cancelAppointment.removeSource(cancelAppointmentSource)
        withContext(dispatchers.io) {
            cancelAppointmentSource = appointmentDetailsUseCase.invokeCancelAppointment( data = requestData)
        }
        _cancelAppointment.addSource(cancelAppointmentSource) {
            _cancelAppointment.value = it.data

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

    fun callDownloadInvoiceApi( appointmentId:Int,orderId:Int,doctorId:Int,supplierId:Int ) = viewModelScope.launch(dispatchers.main) {

        _progressBar.value = Event("")
        _downloadInvoice.removeSource(downloadInvoiceSource)
        withContext(dispatchers.io) {
            downloadInvoiceSource = appointmentDetailsUseCase.invokeDownloadInvoice(
                cid = preferenceUtils.getPreference(PreferenceConstants.CUSTOMERID),
                appid = appointmentId.toString(),
                oid = orderId.toString(),
                docid = doctorId.toString(),
                sid = supplierId.toString() )
        }
        _downloadInvoice.addSource(downloadInvoiceSource) {
            _downloadInvoice.value = it.data

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

    fun callDownloadPrescriptionApi( appointmentId:String,consultationId:String,doctorId:String ) = viewModelScope.launch(dispatchers.main) {

        _progressBar.value = Event("")
        _downloadPrescription.removeSource(downloadPrescriptionSource)
        withContext(dispatchers.io) {
            downloadPrescriptionSource = appointmentDetailsUseCase.invokeDownloadPrescription(
                cid = preferenceUtils.getPreference(PreferenceConstants.CUSTOMERID),
                appid = appointmentId,
                conid = consultationId,
                docid = doctorId )
        }
        _downloadPrescription.addSource(downloadPrescriptionSource) {
            _downloadPrescription.value = it.data

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

    fun callSaveFeedbackApi( appointmentId:String,rating:Int,comments:String ) = viewModelScope.launch(dispatchers.main) {

        val requestData = SaveFeedbackModel(Gson().toJson(SaveFeedbackModel.JSONDataRequest(
            feedback = SaveFeedbackModel.Feedback(
                appointmentID = appointmentId ,
                userRating = rating ,
                comments = comments,
                recordDate = DateHelper.currentUTCDatetimeInMillisecAsString)), SaveFeedbackModel.JSONDataRequest::class.java),token)

        Utilities.printData("SaveFeedback",requestData,true)
        _progressBar.value = Event("")
        _saveFeedback.removeSource(saveFeedbackSource)
        withContext(dispatchers.io) {
            saveFeedbackSource = appointmentDetailsUseCase.invokeSaveFeedback( data = requestData)
        }
        _saveFeedback.addSource(saveFeedbackSource) {
            _saveFeedback.value = it.data

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

    fun callGetConsultationHealthDocumentApi( docId:Int ) = viewModelScope.launch(dispatchers.main) {

        val requestData = GetConsultationHealthDocumentModel(Gson().toJson(GetConsultationHealthDocumentModel.JSONDataRequest(
            documentID = docId),GetConsultationHealthDocumentModel.JSONDataRequest::class.java),token)

        _progressBar.value = Event("")
        _getConsultationHealthDocument.removeSource(getConsultationHealthDocumentSource)
        withContext(dispatchers.io) {
            getConsultationHealthDocumentSource = appointmentDetailsUseCase.invokeGetConsultationHealthDocument( data = requestData)
        }
        _getConsultationHealthDocument.addSource(getConsultationHealthDocumentSource) {
            _getConsultationHealthDocument.value = it.data

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

    fun callGetDoctorSlotsApi(date:String,mode:String,doctorId:Int,supplierId:Int) = viewModelScope.launch(dispatchers.main) {

        val requestData = GetDoctorSlotsModel(Gson().toJson(GetDoctorSlotsModel.JSONDataRequest(
            doctorID = doctorId ,
            supplierID = supplierId,
            weekDayCode = Helper.getWeekdayCodeForApi(date) ,
            appointmentDate = date,
            appointmentMode = Helper.getModeForApi(mode)),GetDoctorSlotsModel.JSONDataRequest::class.java),token)

        _progressBar.value = Event("")
        _getDoctorSlots.removeSource(getDoctorSlotsSource)
        withContext(dispatchers.io) {
            getDoctorSlotsSource = appointmentDetailsUseCase.invokeGetDoctorSlots( data = requestData)
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

    fun callPatientStateListApi() = viewModelScope.launch(dispatchers.main) {

        val obj = JSONObject()
        val requestData = PatientStateListModel(obj.toString(),token)

        _progressBar.value = Event("")
        _patientStateList.removeSource(patientStateListSource)
        withContext(dispatchers.io) {
            patientStateListSource = appointmentDetailsUseCase.invokePatientStateList( data = requestData)
        }
        _patientStateList.addSource(patientStateListSource) {
            _patientStateList.value = it.data

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

    fun callSendEpharmaDetailsApi( con : ListConsultationModel.Consultation,address:String,city:String,state:String,postcode:String ) = viewModelScope.launch(dispatchers.main) {

        val requestObj = JsonObject()
        requestObj.addProperty("AuthTicket",token)
        requestObj.addProperty("LoginId",0)
        requestObj.addProperty("EmployeeId","VIV123-KEY")
        requestObj.addProperty("EmployeeName",preferenceUtils.getPreference(PreferenceConstants.FIRSTNAME))
        requestObj.addProperty("PatientId","1")
        requestObj.addProperty("PatientName",preferenceUtils.getPreference(PreferenceConstants.FIRSTNAME))
        requestObj.addProperty("PatientGender",Helper.getGenderValueApi(preferenceUtils.getPreference(PreferenceConstants.GENDER)))
        requestObj.addProperty("CompanyCode",Configuration.PartnerCode)
        requestObj.addProperty("CompanyName",Configuration.PartnerCode)
        requestObj.addProperty("Email",preferenceUtils.getPreference(PreferenceConstants.EMAIL))
        requestObj.addProperty("PhoneNo",preferenceUtils.getPreference(PreferenceConstants.PHONE))
        requestObj.addProperty("IC","")
        requestObj.addProperty("Address",address)
        requestObj.addProperty("City",city)
        requestObj.addProperty("State",state)
        requestObj.addProperty("Postcode",postcode)

        requestObj.addProperty("AppointmentID",con.appointmentID)
        requestObj.addProperty("CustomerID",preferenceUtils.getPreference(PreferenceConstants.CUSTOMERID))
        requestObj.addProperty("DoctorID",con.doctorID)
        requestObj.addProperty("ConsultationID",con.iD)
        requestObj.addProperty("Prescription_Duration","1")
        val reqData = EncryptionUtility.encrypt(Configuration.SecurityKey,requestObj.toString(), Configuration.SecurityKey)
        val data = RequestBody.create("text/plain".toMediaTypeOrNull(),reqData)

        Utilities.printData("requestObj",requestObj,false)
        Utilities.printLogError("data--->$reqData")

        _progressBar.value = Event("")
        _sendEpharmaDetails.removeSource(sendEpharmaDetailsSource)
        withContext(dispatchers.io) {
            sendEpharmaDetailsSource = appointmentDetailsUseCase.invokeSendEpharmaDetails( data = data)
        }
        _sendEpharmaDetails.addSource(sendEpharmaDetailsSource) {
            _sendEpharmaDetails.value = it.data

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

}