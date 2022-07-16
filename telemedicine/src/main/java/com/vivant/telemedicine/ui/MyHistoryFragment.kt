package com.vivant.telemedicine.ui

import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import com.vivant.telemedicine.R
import com.vivant.telemedicine.adapter.ConsultationAdapter
import com.vivant.telemedicine.adapter.MyHistoryAdapter
import com.vivant.telemedicine.base.BaseFragment
import com.vivant.telemedicine.base.BaseViewModel
import com.vivant.telemedicine.common.*
import com.vivant.telemedicine.databinding.FragmentMyHistoryBinding
import com.vivant.telemedicine.model.*
import com.vivant.telemedicine.ui.appointmentDetails.AppointmentOptionsBottomSheet
import com.vivant.telemedicine.ui.appointmentDetails.FeedbackDialog
import com.vivant.telemedicine.viewmodel.AppointmentDetailsViewModel
import com.vivant.telemedicine.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyHistoryFragment : BaseFragment(),

    AppointmentOptionsBottomSheet.OnAppointmentOptionClickListener,
    FeedbackDialog.OnSubmitListener,
    MyHistoryAdapter.OnPastAppointmentClickListener,
    ConsultationAdapter.OnConsultationClickListener {

    private var _binding: FragmentMyHistoryBinding? = null
    private val binding get() = _binding!!

    val viewModel: HomeViewModel by viewModel()
    private val appointmentDetailsViewModel: AppointmentDetailsViewModel by viewModel()

    private val appointmentDetailsSingleton = AppointmentDetailsSingleton.instance!!

    private var pos = -1
    private val fileUtils = FileUtils
    private var recordInSession = RecordInSession()
    private var appointment = ListAppointmentsModel.UpcomingAppointment()
    private var consultation = ListConsultationModel.Consultation()

    private var myHistoryAdapter: MyHistoryAdapter? = null
    private var consultationAdapter: ConsultationAdapter? = null

    private var appointmentsList: MutableList<ListAppointmentsModel.UpcomingAppointment> = mutableListOf()
    private var consultationList: MutableList<ListConsultationModel.Consultation> = mutableListOf()

    private var animation: LayoutAnimationController? = null

    override fun getViewModel(): BaseViewModel = appointmentDetailsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMyHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        try {
            initialise()
            registerObserver()
        } catch ( e : Exception ) {
            e.printStackTrace()
        }
        return root
    }

    private fun initialise() {
        animation = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_slide_from_bottom)

        consultationAdapter = ConsultationAdapter(requireContext(),this)
        binding.rvConsultations.adapter = consultationAdapter

        myHistoryAdapter = MyHistoryAdapter(requireContext(),this)
        binding.rvPastAppointments.adapter = myHistoryAdapter

        startShimmer()
        appointmentDetailsViewModel.callListAppointmentsApi(Constants.MY_HISTORY)
    }

    private fun registerObserver() {

        appointmentDetailsViewModel.listAppointments.observe(viewLifecycleOwner) {
            if (it != null) {
                appointmentsList.clear()
                appointmentsList.addAll(it.appointmentList.pastAppointment)
                Utilities.printLogError("AppointmentsList--->${appointmentsList.size}")
                viewModel.callListConsultationApi()
            }
        }

        viewModel.listConsultation.observe(viewLifecycleOwner) {
            if (it != null) {
                consultationList.clear()
                consultationList.addAll(it.consultationDetails.consultationList)
                Utilities.printLogError("ConsultationList--->${consultationList.size}")
                Utilities.printLogError("AppointmentsList--->${appointmentsList.size}")
                updateConsultationList(consultationList)
                updateHistoryList(appointmentsList)
                if ( appointmentsList.isEmpty() && consultationList.isEmpty() ) {
                    binding.layoutPastAppointments.visibility = View.GONE
                    binding.layoutNoData.visibility = View.VISIBLE
                }
                stopShimmer()
            }
        }

        appointmentDetailsViewModel.downloadInvoice.observe(viewLifecycleOwner) {
            if (it != null) {
                saveInvoice(it)
            }
        }

        appointmentDetailsViewModel.downloadPrescription.observe(viewLifecycleOwner) {
            if (it != null) {
                savePrescription(it)
            }
        }

        appointmentDetailsViewModel.saveFeedback.observe(viewLifecycleOwner) {
            if (it != null) {
                if ( it.isProcessed.equals(Constants.TRUE,ignoreCase = true) ) {
                    consultationList[pos].isFeedbackAdded = true
                    updateConsultationList(consultationList)
                    updateHistoryList(appointmentsList)
                    Utilities.toastMessageShort(requireContext(),resources.getString(R.string.FEEDBACK_SHARED_SUCCESSFULLY))
                    pos = -1
                }
            }
        }

    }

    private fun updateConsultationList(list2: MutableList<ListConsultationModel.Consultation>) {
        if ( !list2.isNullOrEmpty() ) {
            //list2.sortByDescending { it.appointmentDate }
            consultationAdapter!!.updateList(list2)
        }
    }

    private fun updateHistoryList(list1: MutableList<ListAppointmentsModel.UpcomingAppointment>) {
        if ( !list1.isNullOrEmpty() ) {
            //list1.sortByDescending { it.appointmentDate }
            myHistoryAdapter!!.updateList(list1)
        }
    }

    private fun saveInvoice( invoice: DownloadInvoiceResponse) {
        try {
            val byteArray = invoice.fileData
            val name = fileUtils.generateUniqueFileName( invoice.fileName ,".pdf" )
            recordInSession = createRecordInSession(name,name,Utilities.getAppFolderLocation(requireContext()),"PDF")
            val decodedString = Base64.decode(byteArray, Base64.DEFAULT)
            if (decodedString != null) {
                val saveRecord = fileUtils.saveByteArrayToExternalStorage(requireContext(),decodedString,name)
                if ( saveRecord != null ) {
                    Helper.viewRecord(requireContext(),recordInSession)
                }
            }
        } catch ( e : Exception) {
            e.printStackTrace()
        }
    }

    private fun savePrescription( invoice: DownloadPrescriptionResponse) {
        try {
            val byteArray = invoice.fileData
            val name = fileUtils.generateUniqueFileName( consultation.bookedForFirstName + "_" ,".pdf" )
            recordInSession = createRecordInSession(name,name,Utilities.getAppFolderLocation(requireContext()),"PDF")
            val decodedString = Base64.decode(byteArray, Base64.DEFAULT)
            if (decodedString != null) {
                val saveRecord = fileUtils.saveByteArrayToExternalStorage(requireContext(),decodedString,name)
                if ( saveRecord != null ) {
                    Helper.viewRecord(requireContext(),recordInSession)
                }
            }
        } catch ( e : Exception) {
            e.printStackTrace()
        }
    }

    private fun createRecordInSession( OriginalFileName:String,Name:String,Path:String,ImageType:String ) : RecordInSession {
        //val id = (0..100000).random().toString()
        return RecordInSession(
            Name = Name ,
            Id = "0" ,
            OriginalFileName = OriginalFileName ,
            Path = Path ,
            Type = ImageType)
    }

    override fun onConsultationSelection(position: Int, action: String, con: ListConsultationModel.Consultation) {
        pos = position
        consultation = con
        appointmentDetailsSingleton.consultation = con
        Utilities.printLogError("Action--->$action")
        Utilities.printData("Consultation",consultation,true)
        Utilities.printLogError("Feedback--->${consultation.isFeedbackAdded}")
        when(action) {
            Constants.CLICK-> {
                if ( consultation.doctorID != null && consultation.doctorID != "" ) {
                    openAnotherActivity(destination = NavigationConstants.APPOINTMENT_DETAILS) {
                        putString(Constants.FROM,Constants.CONSULTATION)
                    }
                } else {
                    Utilities.toastMessageShort(requireContext(),resources.getString(R.string.DETAILS_NOT_AVAILABLE))
                }
            }
            Constants.OPTION-> {
                val bottomSheet = AppointmentOptionsBottomSheet( this,appointment.appointmentStatus,true,consultation.isFeedbackAdded)
                bottomSheet.show(childFragmentManager, AppointmentOptionsBottomSheet.TAG)
            }
            Constants.PRESCRIPTION-> {
                appointmentDetailsViewModel.callDownloadPrescriptionApi(consultation.appointmentID,consultation.iD,consultation.doctorID)
            }
            Constants.BOOK_FOLLOW_UP-> {
                openAnotherActivity(destination = NavigationConstants.APPOINTMENT_DETAILS) {
                    putString(Constants.FROM,Constants.BOOK_FOLLOW_UP)
                }
            }
        }
    }

    override fun onPastAppointmentSelection(position: Int, action: String, appt: ListAppointmentsModel.UpcomingAppointment) {
        Utilities.printLogError("Action--->$action")
        appointment = appt
        appointmentDetailsSingleton.appointment = appt
        Utilities.printData("PastAppointment",appt,true)
        when(action) {
            Constants.CLICK-> {
                if ( appointment.doctorID != null &&  appointment.doctorID != 0 ) {
                    openAnotherActivity(destination = NavigationConstants.APPOINTMENT_DETAILS) {
                        putString(Constants.FROM,Constants.PAST)
                    }
                } else {
                    Utilities.toastMessageShort(requireContext(),resources.getString(R.string.DETAILS_NOT_AVAILABLE))
                }
            }
            Constants.VIEW_INVOICE-> {
                if ( appointment.doctorID != null && appointment.doctorID != 0 ) {
                    appointmentDetailsViewModel.callDownloadInvoiceApi(appointment.iD,appointment.orderID,appointment.doctorID,appointment.supplierID)
                } else {
                    Utilities.toastMessageShort(requireContext(),resources.getString(R.string.DETAILS_NOT_AVAILABLE))
                }
            }
        }
    }

    override fun onAppointmentOptionClick(action: String) {
        Utilities.printLogError("Action--->$action")
        try {
            when(action) {
                Constants.VIEW_INVOICE-> {
                    appointmentDetailsViewModel.callDownloadInvoiceApi(consultation.appointmentID.toInt(),consultation.orderID,consultation.doctorID.toInt(),consultation.supplierID)
                }
                Constants.SAVE_FEEDBACK-> {
                    FeedbackDialog(requireContext(),this).show()
                }
            }
        } catch ( e : Exception ) {
            e.printStackTrace()
        }
    }

    override fun onSubmitClickListener(rating: Int, comment: String) {
        Utilities.printLogError("rating--->$rating")
        Utilities.printLogError("comment--->$comment")
        appointmentDetailsViewModel.callSaveFeedbackApi(consultation.appointmentID,rating,comment)
    }

    private fun startShimmer() {
        binding.layoutPastAppointmentsShimmer.startShimmer()
        binding.layoutPastAppointmentsShimmer.visibility = View.VISIBLE
    }

    private fun stopShimmer() {
        binding.layoutPastAppointmentsShimmer.stopShimmer()
        binding.layoutPastAppointmentsShimmer.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /*    override fun onUpcomingAppointmentSelection(position: Int,action:String,appt:ListAppointmentsModel.UpcomingAppointment) {
        appointment = appt
        Utilities.printData("Appointment",appointment,true)
        val bottomSheet = AppointmentOptionsBottomSheet( this,"PAST",true)
        bottomSheet.show(childFragmentManager, AppointmentOptionsBottomSheet.TAG)
    }*/

}