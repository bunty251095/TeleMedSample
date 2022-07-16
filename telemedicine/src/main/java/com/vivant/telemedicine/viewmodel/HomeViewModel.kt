package com.vivant.telemedicine.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.vivant.telemedicine.base.BaseViewModel
import com.vivant.telemedicine.common.*
import com.vivant.telemedicine.model.*
import com.vivant.telemedicine.network.AppDispatchers
import com.vivant.telemedicine.network.domain.HomeUseCase
import com.vivant.telemedicine.network.utils.Resource
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

@SuppressLint("StaticFieldLeak")
class HomeViewModel(private val dispatchers: AppDispatchers,
                    private val homeUseCase: HomeUseCase,
                    private val preferenceUtils: PreferenceUtils,
                    private val context: Context) : BaseViewModel() {

    val token = preferenceUtils.getPreference(PreferenceConstants.TOKEN)
    //val token = Constants.TOKEN

    val name = preferenceUtils.getPreference(PreferenceConstants.FIRSTNAME)
    val email = preferenceUtils.getPreference(PreferenceConstants.EMAIL)

    private lateinit var argsLogin: String

/*    private var loginUserSource: LiveData<Resource<Users>> = MutableLiveData()
    private val _user = MediatorLiveData<Users?>()
    val user: LiveData<Users?> get() = _user*/

    private var loginUserSource: LiveData<Resource<SSOLoginModel.SSOLoginResponse>> = MutableLiveData()
    private val _user = MediatorLiveData<SSOLoginModel.SSOLoginResponse?>()
    val user: LiveData<SSOLoginModel.SSOLoginResponse?> get() = _user

    var listAppointmentsSource: LiveData<Resource<ListAppointmentsModel.ListAppointmentsResponse>> = MutableLiveData()
    val _listAppointments = MediatorLiveData<ListAppointmentsModel.ListAppointmentsResponse?>()
    val listAppointments: LiveData<ListAppointmentsModel.ListAppointmentsResponse?> get() = _listAppointments

    var listConsultationSource: LiveData<Resource<ListConsultationModel.ListConsultationResponse>> = MutableLiveData()
    val _listConsultation = MediatorLiveData<ListConsultationModel.ListConsultationResponse?>()
    val listConsultation: LiveData<ListConsultationModel.ListConsultationResponse?> get() = _listConsultation

    var listDocumentSource: LiveData<Resource<ListDocumentModel.ListDocumentResponse>> = MutableLiveData()
    val _listDocument = MediatorLiveData<ListDocumentModel.ListDocumentResponse?>()
    val listDocument: LiveData<ListDocumentModel.ListDocumentResponse?> get() = _listDocument

    var downloadDocumentSource: LiveData<Resource<DownloadRecordModel.DownloadRecordResponse>> = MutableLiveData()
    val _downloadDocument = MediatorLiveData<DownloadRecordModel.DownloadRecordResponse?>()
    val downloadDocument: LiveData<DownloadRecordModel.DownloadRecordResponse?> get() = _downloadDocument

    var deleteDocumentSource: LiveData<Resource<DeleteDocumentModel.DeleteDocumentResponse>> = MutableLiveData()
    val _deleteDocument = MediatorLiveData<DeleteDocumentModel.DeleteDocumentResponse?>()
    val deleteDocument: LiveData<DeleteDocumentModel.DeleteDocumentResponse?> get() = _deleteDocument

    var listNotificationsSource: LiveData<Resource<ListNotificationsModel.ListNotificationsResponse>> = MutableLiveData()
    val _listNotifications = MediatorLiveData<ListNotificationsModel.ListNotificationsResponse?>()
    val listNotifications: LiveData<ListNotificationsModel.ListNotificationsResponse?> get() = _listNotifications

    var updateNotificationSource: LiveData<Resource<UpdateNotificationModel.UpdateNotificationResponse>> = MutableLiveData()
    val _updateNotification = MediatorLiveData<UpdateNotificationModel.UpdateNotificationResponse?>()
    val updateNotification: LiveData<UpdateNotificationModel.UpdateNotificationResponse?> get() = _updateNotification

    fun callLoginSSO( data : String) = viewModelScope.launch(dispatchers.main) {
        try {

            val requestData = SSOLoginModel(Gson().toJson(SSOLoginModel.JSONDataRequest(
                data = data ),SSOLoginModel.JSONDataRequest::class.java))

            //_progressBar.value = Event("")
            _user.removeSource(loginUserSource)
            withContext(dispatchers.io) {
                loginUserSource = homeUseCase.invokeSSOLogin(isForceRefresh = true, data = requestData)
            }
            _user.addSource(loginUserSource) {
                _user.value = it.data

                if (it.status == Resource.Status.SUCCESS) {
                    _progressBar.value = Event(Event.HIDE_PROGRESS)
                    //Utilities.printLogError("LoginResp--->${it.data}")
                    try {
                        val data = it.data?.response!!.data
                        Utilities.printLogError("PersonId => ${data.personId}")
                        Utilities.printLogError("TOKEN--->${data.context}")
                        preferenceUtils.storeBooleanPreference(PreferenceConstants.IS_LOGIN, true)
                        Constants.TOKEN = data.context
                        preferenceUtils.storePreference(PreferenceConstants.TOKEN,data.context)
                        preferenceUtils.storePreference(PreferenceConstants.PERSONID,data.personId.toString())
                        preferenceUtils.storePreference(PreferenceConstants.ACCOUNTID,data.accountId.toString())
                        preferenceUtils.storePreference(PreferenceConstants.CUSTOMERID,data.customerId.toString())
                        preferenceUtils.storePreference(PreferenceConstants.FIRSTNAME,data.firstName)
                        preferenceUtils.storePreference(PreferenceConstants.EMAIL,data.emailAddress)
                        preferenceUtils.storePreference(PreferenceConstants.PHONE,data.phoneNumber)

                        //preferenceUtils.storePreference(PreferenceConstants.DOB,data.dateOfBirth.split("T").toTypedArray()[0])
                        preferenceUtils.storePreference(PreferenceConstants.DOB,data.dateOfBirth)
                        preferenceUtils.storePreference(PreferenceConstants.GENDER,data.gender)
                    } catch ( e : Exception ) {
                        e.printStackTrace()
                    }
                }

                if (it.status == Resource.Status.ERROR) {
                    _progressBar.value = Event(Event.HIDE_PROGRESS)
                    toastMessage(it.errorMessage)
                    Utilities.printLogError(it.errorMessage)
                }
            }
        } catch ( e : Exception ) {
            e.printStackTrace()
        }

    }

    fun callLogin(
        name: String = "",
        dob: String = "",
        gender: String = "",
        mobileStr: String = "",
        emailStr: String = "") = viewModelScope.launch(dispatchers.main) {
        try {

            val requestData = SSOLoginModel(Gson().toJson(SSOLoginModel.JSONDataRequest(
                data = getDataAsString(name,dob,gender,mobileStr,emailStr)),SSOLoginModel.JSONDataRequest::class.java))

            //_progressBar.value = Event("")
            _user.removeSource(loginUserSource)
            withContext(dispatchers.io) {
                loginUserSource = homeUseCase.invokeSSOLogin(isForceRefresh = true, data = requestData)
            }
            _user.addSource(loginUserSource) {
                _user.value = it.data

                if (it.status == Resource.Status.SUCCESS) {
                    _progressBar.value = Event(Event.HIDE_PROGRESS)
                    //Utilities.printLogError("LoginResp--->${it.data}")
                    try {
                        val data = it.data?.response!!.data
                        Utilities.printLogError("PersonId => ${data.personId}")
                        Utilities.printLogError("TOKEN--->${data.context}")
                        preferenceUtils.storeBooleanPreference(PreferenceConstants.IS_LOGIN, true)
                        Constants.TOKEN = data.context
                        preferenceUtils.storePreference(PreferenceConstants.TOKEN,data.context)
                        preferenceUtils.storePreference(PreferenceConstants.PERSONID,data.personId.toString())
                        preferenceUtils.storePreference(PreferenceConstants.ACCOUNTID,data.accountId.toString())
                        preferenceUtils.storePreference(PreferenceConstants.CUSTOMERID,data.customerId.toString())
                        preferenceUtils.storePreference(PreferenceConstants.FIRSTNAME,data.firstName)
                        preferenceUtils.storePreference(PreferenceConstants.EMAIL,data.emailAddress)
                        preferenceUtils.storePreference(PreferenceConstants.PHONE,data.phoneNumber)

                        //preferenceUtils.storePreference(PreferenceConstants.DOB,data.dateOfBirth.split("T").toTypedArray()[0])
                        preferenceUtils.storePreference(PreferenceConstants.DOB,data.dateOfBirth)
                        preferenceUtils.storePreference(PreferenceConstants.GENDER,data.gender)
                    } catch ( e : Exception ) {
                        e.printStackTrace()
                    }
                }

                if (it.status == Resource.Status.ERROR) {
                    _progressBar.value = Event(Event.HIDE_PROGRESS)
                    toastMessage(it.errorMessage)
                    Utilities.printLogError(it.errorMessage)
                }
            }
        } catch ( e : Exception ) {
            e.printStackTrace()
        }

    }

    private fun getDataAsString(
        username: String,
        dob: String = "",
        gender: String = "",
        phoneNumber: String = "",
        email: String = ""): String {

        val registerObject = JSONObject()
        registerObject.put(Constants.UserConstants.NAME, username)
        registerObject.put(Constants.UserConstants.DOB, dob)
        registerObject.put(Constants.UserConstants.GENDER, gender)
        registerObject.put(Constants.UserConstants.PHONE_NUMBER, phoneNumber)
        registerObject.put(Constants.UserConstants.EMAIL_ADDRESS, email)
        registerObject.put(Constants.UserConstants.PARTNER_CODE, Configuration.PartnerCode)

        registerObject.put(Constants.UserConstants.MN, "VIV123-KEY")
        registerObject.put(Constants.UserConstants.IC, "VIV123-KEY")
        registerObject.put(Constants.UserConstants.LID, "VIV123-KEY")
        registerObject.put(Constants.UserConstants.EID, "VIV123-KEY")

        registerObject.put(Constants.UserConstants.SRC, "TELEMED")
        registerObject.put(Constants.UserConstants.PCD, "PMCARE")
        registerObject.put(Constants.UserConstants.CN, "PMCARE")
        registerObject.put(Constants.UserConstants.LOC, "IN")
        registerObject.put(Constants.UserConstants.BU, "001")
        registerObject.put(Constants.UserConstants.EN, "IT")
        registerObject.put(Constants.UserConstants.DEPT, "IT")
        registerObject.put(Constants.UserConstants.DEVICE_ID, "")
        registerObject.put(Constants.UserConstants.LAUNCH_TYPE, "DASHBOARD")

        val familyArray = JSONArray()
        val familyObject = JSONObject()
        familyObject.put(Constants.UserConstants.FMID, "1")
        familyObject.put(Constants.UserConstants.FMNA, username)
        familyObject.put(Constants.UserConstants.FMDOB, dob)
        familyObject.put(Constants.UserConstants.FMG, gender)
        familyObject.put(Constants.UserConstants.FMRE, "Self")
        familyArray.put(familyObject)

        val paymentArray = JSONArray()
        val paymentObject = JSONObject()
        paymentObject.put(Constants.UserConstants.FMID, "1")
        paymentObject.put(Constants.UserConstants.CRD, "600")
        paymentArray.put(paymentObject)

        registerObject.put(Constants.UserConstants.FAMILY, familyArray)
        registerObject.put(Constants.UserConstants.PAYMENT, paymentArray)

        Utilities.printLog("BEFORE--->$registerObject")
        return registerObject.toString()
    }

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
            listAppointmentsSource = homeUseCase.invokeListAppointments(isForceRefresh = true, data = requestData)
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

    fun callListConsultationApi() = viewModelScope.launch(dispatchers.main) {

        val requestData = ListConsultationModel(Gson().toJson(ListConsultationModel.JSONDataRequest(
            criteria = ListConsultationModel.Criteria(
                customerID = preferenceUtils.getPreference(PreferenceConstants.CUSTOMERID).toInt()),
            pagingCriteria = ListConsultationModel.PagingCriteria(
                currentPage = 1,
                pageDirection = 0,
                pageSize = 0,
                sortOrder = 0)),ListConsultationModel.JSONDataRequest::class.java),token)

        //_progressBar.value = Event("")
        _listConsultation.removeSource(listConsultationSource)
        withContext(dispatchers.io) {
            listConsultationSource = homeUseCase.invokeListConsultation(isForceRefresh = true, data = requestData)
        }
        _listConsultation.addSource(listConsultationSource) {
            _listConsultation.value = it.data

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

    fun callListDocumentApi() = viewModelScope.launch(dispatchers.main) {

        val requestData = ListDocumentModel(Gson().toJson(ListDocumentModel.JSONDataRequest(
                searchCriteria = ListDocumentModel.SearchCriteria(
                    personID = preferenceUtils.getPreference(PreferenceConstants.PERSONID).toInt(),
                    mode = "FM",
                    pagingCriteria = ListDocumentModel.PagingCriteria(
                        currentPage = 1,
                        pageSize = 0))),ListDocumentModel.JSONDataRequest::class.java),token)

        //_progressBar.value = Event("")
        _listDocument.removeSource(listDocumentSource)
        withContext(dispatchers.io) {
            listDocumentSource = homeUseCase.invokeListDocument(isForceRefresh = true, data = requestData)
        }
        _listDocument.addSource(listDocumentSource) {
            _listDocument.value = it.data

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

    fun callDownloadDocumentApi( id : Int ) = viewModelScope.launch(dispatchers.main) {

        val requestData = DownloadRecordModel(Gson().toJson(DownloadRecordModel.JSONDataRequest(
            documentID = id),DownloadRecordModel.JSONDataRequest::class.java),token)

        _progressBar.value = Event("")
        _downloadDocument.removeSource(downloadDocumentSource)
        withContext(dispatchers.io) {
            downloadDocumentSource = homeUseCase.invokeDownloadDocument(isForceRefresh = true, data = requestData)
        }
        _downloadDocument.addSource(downloadDocumentSource) {
            _downloadDocument.value = it.data

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

    fun callDeleteDocumentApi( id : Int ) = viewModelScope.launch(dispatchers.main) {

        val requestData = DeleteDocumentModel(Gson().toJson(DeleteDocumentModel.JSONDataRequest(
            documentIDS = mutableListOf(id)),DeleteDocumentModel.JSONDataRequest::class.java),token)

        _progressBar.value = Event("")
        _deleteDocument.removeSource(deleteDocumentSource)
        withContext(dispatchers.io) {
            deleteDocumentSource = homeUseCase.invokeDeleteDocument(isForceRefresh = true, data = requestData)
        }
        _deleteDocument.addSource(deleteDocumentSource) {
            _deleteDocument.value = it.data

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

    fun callListNotificationsApi() = viewModelScope.launch(dispatchers.main) {

        val requestData = ListNotificationsModel(Gson().toJson(ListNotificationsModel.JSONDataRequest(
            criteria = ListNotificationsModel.Criteria(
                userID = preferenceUtils.getPreference(PreferenceConstants.CUSTOMERID).toInt(),
                UserType = "USER")),ListNotificationsModel.JSONDataRequest::class.java),token)

        //_progressBar.value = Event("")
        _listNotifications.removeSource(listNotificationsSource)
        withContext(dispatchers.io) {
            listNotificationsSource = homeUseCase.invokeListNotifications(isForceRefresh = true, data = requestData)
        }
        _listNotifications.addSource(listNotificationsSource) {
            _listNotifications.value = it.data

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

    fun callUpdateNotificationApi(id:Int,isClearAll:Boolean) = viewModelScope.launch(dispatchers.main) {

        val requestData = UpdateNotificationModel(
            Gson().toJson(UpdateNotificationModel.JSONDataRequest(
                criteria = UpdateNotificationModel.Criteria(
                    mode = if ( isClearAll ) { "READ_ALL" } else { "MARK_AS_READ" },
                    notificationID = id,
                    userID = preferenceUtils.getPreference(PreferenceConstants.CUSTOMERID).toInt(),
                    UserType = "USER")),UpdateNotificationModel.JSONDataRequest::class.java),token)

        _progressBar.value = Event("")
        _updateNotification.removeSource(updateNotificationSource)
        withContext(dispatchers.io) {
            updateNotificationSource = homeUseCase.invokeUpdateNotification(isForceRefresh = true, data = requestData)
        }
        _updateNotification.addSource(updateNotificationSource) {
            _updateNotification.value = it.data

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

    fun logoutAndResetPreferences() {
        Utilities.resetPreferences(preferenceUtils)
    }

/*    private fun getDataAsString2(
        username: String,
        dob: String = "",
        gender: String = "",
        phoneNumber: String = "",
        email: String = ""): String {

        val registerObject = JSONObject()
        registerObject.put(Constants.UserConstants.NAME, username)
        registerObject.put(Constants.UserConstants.EMAIL_ADDRESS, email)
        registerObject.put(Constants.UserConstants.DOB, dob)
        registerObject.put(Constants.UserConstants.GENDER, gender)
        registerObject.put(Constants.UserConstants.PHONE_NUMBER, phoneNumber)
        registerObject.put(Constants.UserConstants.PARTNER_CODE, Configuration.PartnerCode)

        registerObject.put(Constants.UserConstants.MN, "PMCARE02")
        registerObject.put(Constants.UserConstants.SRC, "TELEMED")
        registerObject.put(Constants.UserConstants.LID, "100")
        registerObject.put(Constants.UserConstants.IC, "pmc11")

        val familyArray = JSONArray()
        val familyObject1 = JSONObject()
        familyObject1.put(Constants.UserConstants.FMID, "1")
        familyObject1.put(Constants.UserConstants.FMNA, "Sanket Bhoir")
        familyObject1.put(Constants.UserConstants.FMDOB, "1997-09-21")
        familyObject1.put(Constants.UserConstants.FMG, gender)
        familyObject1.put(Constants.UserConstants.FMRE, "Self")
        familyArray.put(familyObject1)

        val familyObject2 = JSONObject()
        familyObject2.put(Constants.UserConstants.FMID, "2")
        familyObject2.put(Constants.UserConstants.FMNA, "Sunita Bhoir")
        familyObject2.put(Constants.UserConstants.FMDOB, "1999-06-05")
        familyObject2.put(Constants.UserConstants.FMG, "Female")
        familyObject2.put(Constants.UserConstants.FMRE, "Spouse")
        familyArray.put(familyObject2)

        val paymentArray = JSONArray()
        val paymentObject1 = JSONObject()
        paymentObject1.put(Constants.UserConstants.FMID, "1")
        paymentObject1.put(Constants.UserConstants.CRD, "80")
        paymentArray.put(paymentObject1)

        val paymentObject2 = JSONObject()
        paymentObject2.put(Constants.UserConstants.FMID, "2")
        paymentObject2.put(Constants.UserConstants.CRD, "60")
        paymentArray.put(paymentObject2)

        registerObject.put(Constants.UserConstants.FAMILY, familyArray)
        registerObject.put(Constants.UserConstants.PAYMENT, paymentArray)

        Utilities.printLog("BEFORE--->$registerObject")
        return registerObject.toString()
    }*/

}