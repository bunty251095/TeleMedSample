package com.vivant.telemedicine.ui.consultAndSchedule

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.vivant.telemedicine.R
import com.vivant.telemedicine.base.BaseFragment
import com.vivant.telemedicine.base.BaseViewModel
import com.vivant.telemedicine.common.*
import com.vivant.telemedicine.databinding.FragmentConfirmAppointmentBinding
import com.vivant.telemedicine.model.BookAppointmentModel
import com.vivant.telemedicine.model.ListAppointmentsModel
import com.vivant.telemedicine.viewmodel.ConsultAndScheduleViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConfirmAppointmentFragment : BaseFragment() {

    private var _binding: FragmentConfirmAppointmentBinding? = null
    private val binding get() = _binding!!

    val viewModel: ConsultAndScheduleViewModel by viewModel()

    private var from = ""
    private var isConsent = true
    private var totalPrice = 0.0
    private var walletAmount = 0.0
    private var updatedBalance = 0.0
    private val consultAndScheduleSingleton = ConsultAndScheduleSingleton.instance!!
    private var appointment = BookAppointmentModel.AppointmentDetails()

    override fun getViewModel(): BaseViewModel = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            // callback to Handle back button event
            val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    when(from) {
                        Constants.CONSULT_NOW -> {
                            (activity as ConsultAndScheduleActivity).setStepPositionConsultNow(Constants.STEP_THREE)
                        }
                        Constants.SCHEDULE_APPOINTMENT -> {
                            (activity as ConsultAndScheduleActivity).setStepPositionScheduleAppointment(Constants.STEP_FIVE)
                        }
                    }
                    findNavController().navigate(R.id.action_confirmAppointmentFragment_to_describeSymptomsFragment)
                }
            }
            requireActivity().onBackPressedDispatcher.addCallback(this, callback)

        } catch ( e : Exception ) {
            e.printStackTrace()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentConfirmAppointmentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        try {
            from = (activity as ConsultAndScheduleActivity).from
            initialise()
            setClickable()
            registerObserver()
        } catch ( e : Exception ) {
            e.printStackTrace()
        }
        return root
    }

    private fun initialise() {

        viewModel.callGetWalletApi()
        binding.txtBookFor.text = viewModel.firstName
        binding.txtEmail.text = viewModel.email

        if ( !Utilities.isNullOrEmpty(consultAndScheduleSingleton.appointmentDate) ) {
            binding.txtAppointmentDate.text = DateHelper.convertDateSourceToDestination(consultAndScheduleSingleton.appointmentDate,DateHelper.SERVER_DATE_YYYYMMDD,DateHelper.DATEFORMAT_DDMMMYYYY_NEW)
        } else {
            binding.txtAppointmentDate.text = DateHelper.currentDateAsStringddMMMyyyyNew
        }
        //binding.txtAppointmentDate.text = "${DateHelper.convertDateSourceToDestination(consultAndScheduleSingleton.appointmentDate,DateHelper.SERVER_DATE_YYYYMMDD,DateHelper.DATEFORMAT_DDMMMYYYY_NEW)} at ${DateHelper.getTimeIn12HrFormatAmOrPm(consultAndScheduleSingleton.appointmentTime)}"

        binding.txtSpeciality.text = consultAndScheduleSingleton.speciality
        binding.txtConsultationMode.text = Helper.getDisplayMode(consultAndScheduleSingleton.consultationMode,requireContext())

        if ( !Utilities.isNullOrEmpty(consultAndScheduleSingleton.currentMedicalHistory) ) {
            binding.txtCurrentMedicalHistory.text = consultAndScheduleSingleton.currentMedicalHistory
        } else {
            binding.txtCurrentMedicalHistory.text = resources.getString(R.string.NOT_MENTIONED)
        }

        if ( !Utilities.isNullOrEmpty(consultAndScheduleSingleton.currentMedication) ) {
            binding.txtCurrentMedication.text = consultAndScheduleSingleton.currentMedication
        } else {
            binding.txtCurrentMedication.text = resources.getString(R.string.NOT_MENTIONED)
        }
    }

    private fun setClickable() {

        binding.imgConcent.setOnClickListener {
            isConsent = !isConsent
            if ( isConsent ) {
                binding.imgConcent.setImageResource(R.drawable.ic_check)
            } else {
                binding.imgConcent.setImageResource(R.drawable.ic_uncheck)
            }
        }

        binding.btnBook.setOnClickListener {
            when(from) {
                Constants.SCHEDULE_APPOINTMENT -> {
                    if ( isConsent ) {
                        viewModel.callBookAppointmentApi(Constants.SCHEDULE_APPOINTMENT,walletAmount.toString())
                        //viewModel.callSaveDocumentsApi(appointment.id,appointment.doctorID,appointment.doctorName)
                        //viewModel.callSaveDocumentsApi(40070,1,"John Yeo")
                    } else {
                        Utilities.toastMessageShort(requireContext(),resources.getString(R.string.PLEASE_PROVIDE_YOUR_CONCENT))
                    }
                }
                Constants.CONSULT_NOW -> {
                    openAnotherActivity(destination = NavigationConstants.BOOKING) {
                        putString(Constants.FROM,Constants.CONSULT_NOW)
                    }
                    //viewModel.callUpdateWalletApi(600.0)
                }
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun registerObserver() {

        viewModel.getWallet.observe(viewLifecycleOwner) {
            if (it != null) {
                Utilities.printData("getWallet",it,true)
                if ( !it.report.isNullOrEmpty() ) {
                    totalPrice = consultAndScheduleSingleton.totalPrice.toDouble()
                    walletAmount = it.report[0].walletAmount.toDouble()
                    consultAndScheduleSingleton.walletAmount = walletAmount.toString()

                    binding.txtTotal.text = "RM $totalPrice"
                    binding.txtCompanyCover.text = "RM $walletAmount"

                    if ( walletAmount >= totalPrice ) {
                        binding.txtSelfPay.text = "RM 0"
                        updatedBalance = walletAmount - totalPrice
                        consultAndScheduleSingleton.selfPay = "0"
                        consultAndScheduleSingleton.updatedBalance = updatedBalance
                    } else {
                        binding.txtSelfPay.text = "RM ${totalPrice - walletAmount}"
                        updatedBalance = 0.0
                        consultAndScheduleSingleton.selfPay = (totalPrice - walletAmount).toString()
                        consultAndScheduleSingleton.updatedBalance = updatedBalance
                    }
                }
            }
        }

        viewModel.bookAppointment.observe(viewLifecycleOwner) {
            if (it != null) {
                appointment = it.response.appointmentDetails
                Utilities.printLogError("AppointmentId----->${appointment.id}")
                if ( !Utilities.isNullOrEmptyOrZero(it.response.appointmentDetails.id.toString()) ) {
                    viewModel.callGenerateOnBookingApi(it.response.appointmentDetails.id)
                }
            }
        }

        viewModel.generateOnBooking.observe(viewLifecycleOwner) {
            if (it != null) {
                Utilities.printLogError("OrderID----->${it.orderDetails.orderID}")
                if ( !Utilities.isNullOrEmptyOrZero(it.orderDetails.orderID.toString()) ) {
                    //viewModel.callUpdateWalletApi(600.0)
                    viewModel.callUpdateWalletApi(updatedBalance)
                }
            }
        }

        viewModel.updateWallet.observe(viewLifecycleOwner) {
            if (it != null) {
                if ( it.isSuccess.equals(Constants.TRUE,ignoreCase = true) ) {
                    if ( consultAndScheduleSingleton.recordsList.isNotEmpty() ) {
                        viewModel.callSaveDocumentsApi(appointment.id,appointment.doctorID,appointment.doctorName)
                    } else {
                        openAnotherActivity(destination = NavigationConstants.BOOKING, clearTop = true) {
                            putString(Constants.FROM,Constants.SCHEDULE_APPOINTMENT)
                        }
                    }
                    //openAnotherActivity(destination = NavigationConstants.HOME, clearTop = true)
                }
            }
        }

        viewModel.saveDocuments.observe(viewLifecycleOwner) {
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
                    viewModel.callShareDocumentsApi(appointment.id,appointment.doctorID,appointment.doctorName)
                }
            }
        }

        viewModel.shareDocuments.observe(viewLifecycleOwner) {
            if (it != null) {
                Utilities.printLogError("LogID----->${it.documentSharingRecord.id}")
                if ( !it.documentSharingRecord.documentList.isNullOrEmpty()
                    && !Utilities.isNullOrEmptyOrZero(it.documentSharingRecord.id.toString()) ) {
                    openAnotherActivity(destination = NavigationConstants.BOOKING, clearTop = true) {
                        putString(Constants.FROM,Constants.SCHEDULE_APPOINTMENT)
                    }
                }
            }
        }

    }

}