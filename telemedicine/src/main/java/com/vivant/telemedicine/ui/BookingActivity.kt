package com.vivant.telemedicine.ui

import com.vivant.telemedicine.R
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.vivant.telemedicine.base.BaseActivity
import com.vivant.telemedicine.base.BaseViewModel
import com.vivant.telemedicine.common.*
import com.vivant.telemedicine.databinding.ActivityBookingBinding
import com.vivant.telemedicine.extension.showDialog
import com.vivant.telemedicine.viewmodel.ConsultAndScheduleViewModel
import com.vivant.telemedicine.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class BookingActivity : BaseActivity(),DefaultNotificationDialog.OnDialogValueListener {

    private lateinit var binding: ActivityBookingBinding
    private val consultAndScheduleSingleton = ConsultAndScheduleSingleton.instance!!

    private var from = ""
    private var consultationID = 0
    private var appointmentID = 0
    private var doctorID = 0
    private var doctorName = ""

    private var isWaiting = true
    private var isConfirmed = false

    private lateinit var dialog : DefaultNotificationDialog

    val viewModel: ConsultAndScheduleViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)
    }

    override fun getViewModel(): BaseViewModel = viewModel

    override fun onCreateEvent(savedInstanceState: Bundle?) {
        try {
            binding = ActivityBookingBinding.inflate(layoutInflater)
            setContentView(binding.root)
            initialise()
            registerObserver()
        } catch ( e : Exception ) {
            e.printStackTrace()
        }
    }

    private fun initialise() {
        supportActionBar!!.hide()

        if (intent.hasExtra(Constants.FROM)) {
            from = intent.getStringExtra(Constants.FROM)!!
            Utilities.printLogError("from--->$from")
        }

        when(from) {
            Constants.SCHEDULE_APPOINTMENT -> {
                dialog = showDialog(
                    listener = this,
                    title = resources.getString(R.string.CONGRATULATIONS),
                    message = resources.getString(R.string.CONGRATULATIONS_DESC) ,
                    showLeftBtn = false,
                    showDismiss = false,
                    showImage = true,
                    imgId = R.drawable.img_congratulation,
                    rightText = resources.getString(R.string.GO_TO_HOME_PAGE))
            }
            Constants.CONSULT_NOW -> {
                //viewModel.callUpdateWalletApi(1000.0)
                viewModel.callSaveConsultationRequestApi()
                dialog = showDialog(
                    listener = this,
                    title = "${resources.getString(R.string.SEARCHING_FOR)}\n${resources.getString(R.string.GENERAL_PRACTITIONER)}",
                    message = resources.getString(R.string.SEARCHING_FOR_DESC) ,
                    showLeftBtn = false,
                    showDismiss = false,
                    showImage = true,
                    imgId = R.drawable.loader_petal,
                    animate = true,
                    rightText = resources.getString(R.string.CANCEL))
            }
        }

    }

    private fun registerObserver() {

        viewModel.saveConsultationRequest.observe(this) {
            if (it != null) {
                consultationID = it.request.id
                Utilities.printLogError("ConsultationId----->$consultationID")
                if ( !Utilities.isNullOrEmptyOrZero(consultationID.toString()) ) {
                    viewModel.callCheckoutAppointmentApi(consultationID)
                }
            }

        }

        viewModel.checkoutAppointment.observe(this) {
            if (it != null) {
                if ( it.result.isStatusUpdated.toString().equals(Constants.TRUE,ignoreCase = true) ) {
                    viewModel.callGetConsultationRequestApi(consultationID)
                }
            }
        }

        viewModel.getConsultationRequest.observe(this) {
            if (it != null) {
                Utilities.printLogError("AppointmentID----->${it.consultation.appointmentID}")
                if ( !Utilities.isNullOrEmptyOrZero(it.consultation.appointmentID) ) {
                    appointmentID = it.consultation.appointmentID.toInt()
                    doctorID = it.consultation.doctorID.toInt()
                    doctorName = it.consultation.doctorName
                    isConfirmed = true
                    dialog.dismiss()
                    dialog = showDialog(
                        listener = this,
                        title = resources.getString(R.string.APPOINTMENT_CONFORMATION),
                        message = "${resources.getString(R.string.APPOINTMENT_CONFORMATION_DESC)} ${resources.getString(R.string.DR)} $doctorName",
                        showLeftBtn = false,
                        rightText = Helper.getDisplayMode(it.consultation.appointmentMode,this))
                } else {
                    //viewModel.callGetConsultationRequestApi(consultationID)
                    Handler(Looper.getMainLooper()).postDelayed({
                        viewModel.callGetConsultationRequestApi(consultationID)
                    }, 5000)
                }
            }
        }

        viewModel.updateWallet.observe(this) {
            if (it != null) {
                if ( it.isSuccess.equals(Constants.TRUE,ignoreCase = true) ) {
                    //routeToHomeScreen()
                    if ( consultAndScheduleSingleton.recordsList.isNotEmpty() ) {
                        viewModel.callSaveDocumentsApi(appointmentID,doctorID,doctorName)
                    } else {
                        viewModel.callJoinRoomApi(appointmentID)
                    }
                }
            }
        }

        viewModel.joinRoom.observe(this) {
            if (it != null) {
                if ( !Utilities.isNullOrEmpty(it.response.roomName) ) {
                    routeToJitsiScreen(it.response.roomName,it.response.appointmentMode)
                }
            }
        }

        viewModel.saveDocuments.observe(this) {
            if (it != null) {
                val document = it.healthDocuments
                if ( document.isNotEmpty()  ) {
                    for ( i in consultAndScheduleSingleton.recordsList ) {
                        for ( j in document) {
                            if ( i.OriginalFileName.equals(j.fileName,ignoreCase = true) ) {
                                i.Id = j.documentID.toString()
                                i.Code = j.documentTypeCode
                            }
                        }
                    }
                    viewModel.callShareDocumentsApi(appointmentID,doctorID,doctorName)
                }
            }
        }

        viewModel.shareDocuments.observe(this) {
            if (it != null) {
                Utilities.printLogError("LogID----->${it.documentSharingRecord.id}")
                if ( !it.documentSharingRecord.documentList.isNullOrEmpty()
                    && !Utilities.isNullOrEmptyOrZero(it.documentSharingRecord.id.toString()) ) {
                    viewModel.callJoinRoomApi(appointmentID)
                }
            }
        }

        viewModel.updateConsultationRequest.observe(this) {
            if (it != null) {
                if ( it.isUpdated.equals(Constants.TRUE,ignoreCase = true) ) {
                    Utilities.toastMessageShort(this,resources.getString(R.string.APPOINTMENT_CANCELLATION_DONE))
                    routeToHomeScreen()
                }
            }
        }

    }

    override fun onDialogClickListener(isButtonLeft: Boolean, isButtonRight: Boolean) {
        when(from) {
            Constants.SCHEDULE_APPOINTMENT -> {
                if ( isButtonRight ) {
                    routeToHomeScreen()
                }
            }
            Constants.CONSULT_NOW -> {
                when {
                    isButtonRight && isConfirmed -> {
                        Utilities.printLogError("Case----->*****1*****")
                        //viewModel.callUpdateWalletApi(600.0)
                        viewModel.callUpdateWalletApi(consultAndScheduleSingleton.updatedBalance)
                    }

                    isButtonRight && isWaiting -> {
                        Utilities.printLogError("Case----->*****2*****")
                        isWaiting = false
                        dialog.dismiss()
                        dialog =showDialog(
                            listener = this,
                            title = resources.getString(R.string.APPOINTMENT_CANCELLATION),
                            message = resources.getString(R.string.APPOINTMENT_CANCELLATION_DESC),
                            leftText = resources.getString(R.string.NO),
                            rightText = resources.getString(R.string.YES))
                    }

                    isButtonRight && !isWaiting -> {
                        Utilities.printLogError("Case----->*****3*****")
                        viewModel.callUpdateConsultationRequestApi(consultationID)
                    }

                    isButtonLeft -> {
                        Utilities.printLogError("Case----->*****4*****")
                        isWaiting = true
                        dialog.dismiss()
                        dialog =showDialog(
                            listener = this,
                            title = "${resources.getString(R.string.SEARCHING_FOR)}\n${resources.getString(R.string.GENERAL_PRACTITIONER)}",
                            message = resources.getString(R.string.SEARCHING_FOR_DESC) ,
                            showLeftBtn = false,
                            showDismiss = false,
                            showImage = true,
                            imgId = R.drawable.loader_petal,
                            animate = true,
                            rightText = resources.getString(R.string.CANCEL))
                    }
                }

            }
        }
    }

    private fun routeToHomeScreen() {
        consultAndScheduleSingleton.clearData()
        openAnotherActivity(destination = NavigationConstants.HOME, clearTop = true)
    }

    private fun routeToJitsiScreen(roomName:String,mode:String) {
        consultAndScheduleSingleton.clearData()
        openAnotherActivity(destination = NavigationConstants.JITSI) {
            putString(Constants.MODE,mode)
            putString(Constants.ROOM_NAME,roomName)
        }
    }

}