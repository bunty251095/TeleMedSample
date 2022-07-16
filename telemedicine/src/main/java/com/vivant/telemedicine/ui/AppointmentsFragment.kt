package com.vivant.telemedicine.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.navigation.fragment.findNavController
import com.vivant.telemedicine.R
import com.vivant.telemedicine.adapter.UpcomingAppointmentsAdapter
import com.vivant.telemedicine.base.BaseFragment
import com.vivant.telemedicine.base.BaseViewModel
import com.vivant.telemedicine.common.*
import com.vivant.telemedicine.databinding.FragmentAppointmentsBinding
import com.vivant.telemedicine.model.DownloadInvoiceResponse
import com.vivant.telemedicine.model.ListAppointmentsModel
import com.vivant.telemedicine.model.RecordInSession
import com.vivant.telemedicine.ui.appointmentDetails.AppointmentOptionsBottomSheet
import com.vivant.telemedicine.ui.appointmentDetails.CancelAppointmentDialog
import com.vivant.telemedicine.viewmodel.AppointmentDetailsViewModel
import com.vivant.telemedicine.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AppointmentsFragment : BaseFragment(),
    UpcomingAppointmentsAdapter.OnUpcomingAppointmentClickListener,
    AppointmentOptionsBottomSheet.OnAppointmentOptionClickListener,
    CancelAppointmentDialog.OnCancelListener {

    private var _binding: FragmentAppointmentsBinding? = null
    private val binding get() = _binding!!

    //val viewModel: HomeViewModel by viewModel()
    private val appointmentDetailsViewModel: AppointmentDetailsViewModel by viewModel()

    var cancelAppointmentDialog: CancelAppointmentDialog? = null

    private val fileUtils = FileUtils
    private var recordInSession = RecordInSession()

    private val appointmentDetailsSingleton = AppointmentDetailsSingleton.instance!!
    private var appointment = ListAppointmentsModel.UpcomingAppointment()
    private var todayList: MutableList<ListAppointmentsModel.UpcomingAppointment> = mutableListOf()
    private var upcomingList: MutableList<ListAppointmentsModel.UpcomingAppointment> = mutableListOf()
    private var todayAppointmentsAdapter: UpcomingAppointmentsAdapter? = null
    private var upcomingAppointmentsAdapter: UpcomingAppointmentsAdapter? = null
    private var animation: LayoutAnimationController? = null

    override fun getViewModel(): BaseViewModel = appointmentDetailsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAppointmentsBinding.inflate(inflater, container, false)
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
        animation = AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_slide_from_bottom)

        todayAppointmentsAdapter = UpcomingAppointmentsAdapter(requireContext(),this,true)
        //binding.rvTodayAppointments.layoutAnimation = animation
        binding.rvTodayAppointments.adapter = todayAppointmentsAdapter

        upcomingAppointmentsAdapter = UpcomingAppointmentsAdapter(requireContext(),this,true)
        //binding.rvUpcomingAppointments.layoutAnimation = animation
        binding.rvUpcomingAppointments.adapter = upcomingAppointmentsAdapter

        startShimmer()
        appointmentDetailsViewModel.callListAppointmentsApi(Constants.MY_APPOINTMENTS)
    }

    private fun registerObserver() {

        appointmentDetailsViewModel.listAppointments.observe(viewLifecycleOwner) {
            if (it != null) {
                todayList.clear()
                upcomingList.clear()
                todayList.addAll(it.appointmentList.todaysAppointment)
                upcomingList.addAll(it.appointmentList.upcomingAppointment)
                updateTodayList(todayList)
                updateUpcomingList(upcomingList)

                if ( todayList.isNullOrEmpty() && upcomingList.isNullOrEmpty() ) {
                    binding.layoutTodayAppointments.visibility = View.GONE
                    binding.layoutUpcomingAppointments.visibility = View.GONE
                    binding.layoutNoData.visibility = View.VISIBLE
                }
                stopShimmer()
            }
        }

        appointmentDetailsViewModel.cancelAppointment.observe(viewLifecycleOwner) {
            if (it != null) {
                if ( it.iStatusUpdated.equals(Constants.TRUE,ignoreCase = true) ) {
                    Utilities.toastMessageShort(requireContext(),resources.getString(R.string.APPOINTMENT_CANCELLED_SUCCESSFULLY))
                    openAnotherActivity(destination = NavigationConstants.HOME, clearTop = true)
                }
            }
        }

        appointmentDetailsViewModel.downloadInvoice.observe(viewLifecycleOwner) {
            if (it != null) {
                saveDownloadedInvoice(it)
            }
        }

    }

    private fun saveDownloadedInvoice( invoice: DownloadInvoiceResponse) {
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

    private fun createRecordInSession( OriginalFileName:String,Name:String,Path:String,ImageType:String ) : RecordInSession {
        //val id = (0..100000).random().toString()
        return RecordInSession(
            Name = Name ,
            Id = "0" ,
            OriginalFileName = OriginalFileName ,
            Path = Path ,
            Type = ImageType)
    }

    private fun updateTodayList(list: MutableList<ListAppointmentsModel.UpcomingAppointment>) {
        if ( list.size > 0 ) {
            binding.layoutTodayAppointments.visibility = View.VISIBLE
            //binding.rvTodayAppointments.layoutAnimation = animation
            todayAppointmentsAdapter!!.updateList(list)
            binding.rvTodayAppointments.scheduleLayoutAnimation()
        } else {
            binding.layoutTodayAppointments.visibility = View.GONE
            binding.lblToday.visibility = View.GONE
        }
    }

    private fun updateUpcomingList(upcomingList: MutableList<ListAppointmentsModel.UpcomingAppointment>) {
        if ( upcomingList.size > 0 ) {
            binding.layoutUpcomingAppointments.visibility = View.VISIBLE
            //binding.rvUpcomingAppointments.layoutAnimation = animation
            upcomingAppointmentsAdapter!!.updateList(upcomingList)
            binding.rvUpcomingAppointments.scheduleLayoutAnimation()
        } else {
            binding.layoutUpcomingAppointments.visibility = View.GONE
            binding.lblUpcoming.visibility = View.GONE
        }
    }

    private fun showUpdateStatusDialog() {
        cancelAppointmentDialog = CancelAppointmentDialog(requireContext(),this)
        cancelAppointmentDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        cancelAppointmentDialog!!.show()
    }

    override fun onUpcomingAppointmentSelection(position: Int,action:String,appt:ListAppointmentsModel.UpcomingAppointment) {
        Utilities.printLogError("Action--->$action")
        appointment = appt
        appointmentDetailsSingleton.appointment = appt
        when(action) {
            Constants.CLICK-> {
                openAnotherActivity(destination = NavigationConstants.APPOINTMENT_DETAILS) {
                    putString(Constants.FROM,Constants.UPCOMING)
                }
            }
            Constants.OPTION-> {
                val bottomSheet = AppointmentOptionsBottomSheet( this,appointment.appointmentStatus,false)
                bottomSheet.show(childFragmentManager, AppointmentOptionsBottomSheet.TAG)
            }
            Constants.ATTACH_DOCUMENT-> {
                openAnotherActivity(destination = NavigationConstants.APPOINTMENT_DETAILS) {
                    putString(Constants.FROM,Constants.ATTACH_DOCUMENT)
                }
            }
            Constants.MODE-> {
                openAnotherActivity(destination = NavigationConstants.JITSI) {
                    putString(Constants.MODE,appt.appointmentMode)
                    putString(Constants.ROOM_NAME,appt.roomName)
                    //putString(Constants.MODE,Constants.VIDEO_CALL)
                    //putString(Constants.ROOM_NAME,"testJitsimy1")
                }
            }
        }
    }

    override fun onAppointmentOptionClick(action: String) {
        Utilities.printLogError("Action--->$action")
        when(action) {
            Constants.RESCHEDULE-> {
                openAnotherActivity(destination = NavigationConstants.APPOINTMENT_DETAILS) {
                    putString(Constants.FROM,Constants.RESCHEDULE)
                }
            }
            Constants.CANCEL-> {
                showUpdateStatusDialog()
            }
            Constants.VIEW_INVOICE-> {
                try {
                    appointmentDetailsViewModel.callDownloadInvoiceApi(appointment.iD,appointment.orderID,appointment.doctorID,appointment.supplierID)
                } catch ( e : Exception ) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onCancelClickListener(reason: String) {
        appointmentDetailsViewModel.callCancelAppointmentApi(appointment.iD,reason)
    }

    private fun startShimmer() {
        binding.layoutTodayAppointmentsShimmer.startShimmer()
        binding.layoutUpcomingAppointmentsShimmer.startShimmer()
        binding.layoutTodayAppointmentsShimmer.visibility = View.VISIBLE
        binding.layoutUpcomingAppointmentsShimmer.visibility = View.VISIBLE
    }

    private fun stopShimmer() {
        binding.layoutTodayAppointmentsShimmer.stopShimmer()
        binding.layoutUpcomingAppointmentsShimmer.stopShimmer()
        binding.layoutTodayAppointmentsShimmer.visibility = View.GONE
        binding.layoutUpcomingAppointmentsShimmer.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}