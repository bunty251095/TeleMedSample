package com.vivant.telemedicine.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.recyclerview.widget.LinearLayoutManager
import com.vivant.telemedicine.R
import com.vivant.telemedicine.adapter.UpcomingAppointmentsAdapter
import com.vivant.telemedicine.base.BaseFragment
import com.vivant.telemedicine.base.BaseViewModel
import com.vivant.telemedicine.common.*
import com.vivant.telemedicine.databinding.FragmentHomeScreenBinding
import com.vivant.telemedicine.model.ListAppointmentsModel
import com.vivant.telemedicine.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeScreenFragment : BaseFragment() ,UpcomingAppointmentsAdapter.OnUpcomingAppointmentClickListener {

    private var _binding: FragmentHomeScreenBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    val viewModel: HomeViewModel by viewModel()

    private val appointmentDetailsSingleton = AppointmentDetailsSingleton.instance!!
    private var appointmentsList: MutableList<ListAppointmentsModel.UpcomingAppointment> = mutableListOf()
    private var upcomingAppointmentsAdapter: UpcomingAppointmentsAdapter? = null
    private var animation: LayoutAnimationController? = null

    override fun getViewModel(): BaseViewModel = viewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeScreenBinding.inflate(inflater, container, false)
        val root: View = binding.root
        try {
            initialise()
            setClickable()
            registerObservers()
        } catch ( e : Exception ) {
            e.printStackTrace()
        }
        return root
    }

    private fun initialise() {

        animation = AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_slide_from_bottom)
        upcomingAppointmentsAdapter = UpcomingAppointmentsAdapter(requireContext(),this,false)
        binding.rvUpcomingAppointments.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        //binding.rvUpcomingAppointments.layoutAnimation = animation
        binding.rvUpcomingAppointments.adapter = upcomingAppointmentsAdapter

        startShimmer()
        viewModel.callListAppointmentsApi(Constants.MY_APPOINTMENTS)
        //updateList(appointmentsList)
        binding.swipeContainer.setColorSchemeResources(R.color.colorPrimary)
        //binding.swipeContainer.setColorSchemeResources(R.color.secondary_pink)
    }

    private fun setClickable() {

        binding.swipeContainer.setOnRefreshListener {
            startShimmer()
            binding.layoutNoRecords.visibility = View.GONE
            viewModel.callListAppointmentsApi(Constants.MY_APPOINTMENTS)
        }

        binding.cardScheduleAppointment.setOnClickListener {
            openAnotherActivity(destination = NavigationConstants.CONSULT_AND_SCHEDULE) {
                putString(Constants.FROM,Constants.SCHEDULE_APPOINTMENT)
            }
        }

        binding.cardConsultNow.setOnClickListener {
            openAnotherActivity(destination = NavigationConstants.CONSULT_AND_SCHEDULE) {
                putString(Constants.FROM,Constants.CONSULT_NOW)
            }
        }

    }

    private fun registerObservers() {

        viewModel.user.observe(viewLifecycleOwner) {}

        viewModel.listAppointments.observe(viewLifecycleOwner) {
            if (it != null) {
                appointmentsList.clear()
                appointmentsList.addAll(it.appointmentList.todaysAppointment)
                appointmentsList.addAll(it.appointmentList.upcomingAppointment)
                updateList(appointmentsList)
            }
        }
    }

    private fun updateList(notificationsList: MutableList<ListAppointmentsModel.UpcomingAppointment>) {
        if (notificationsList.size > 0) {
            binding.rvUpcomingAppointments.visibility = View.VISIBLE
            binding.layoutNoRecords.visibility = View.GONE
            //binding.rvUpcomingAppointments.layoutAnimation = animation
            upcomingAppointmentsAdapter!!.updateList(notificationsList)
            binding.rvUpcomingAppointments.scheduleLayoutAnimation()
        } else {
            binding.rvUpcomingAppointments.visibility = View.GONE
            binding.layoutNoRecords.visibility = View.VISIBLE
        }
        stopShimmer()
        binding.swipeContainer.isRefreshing = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onUpcomingAppointmentSelection(position: Int,action:String,appt:ListAppointmentsModel.UpcomingAppointment) {
        Utilities.printData("Appointment",appt,true)
        appointmentDetailsSingleton.appointment = appt
        when(action) {
            Constants.CLICK -> {
                openAnotherActivity(destination = NavigationConstants.APPOINTMENT_DETAILS) {
                    putString(Constants.FROM,Constants.DASHBOARD)
                }
            }
            Constants.ATTACH_DOCUMENT -> {
                openAnotherActivity(destination = NavigationConstants.APPOINTMENT_DETAILS) {
                    putString(Constants.FROM,Constants.ATTACH_DOCUMENT)
                }
            }
            Constants.MODE -> {
                openAnotherActivity(destination = NavigationConstants.JITSI) {
                    putString(Constants.MODE,appt.appointmentMode)
                    putString(Constants.ROOM_NAME,appt.roomName)
                    //putString(Constants.MODE,Constants.TEXT_CHAT)
                    //putString(Constants.ROOM_NAME,"testJitsimy1")
                }
            }
        }
    }

    private fun startShimmer() {
        binding.layoutAppointmentsShimmer.startShimmer()
        binding.layoutAppointmentsShimmer.visibility = View.VISIBLE
        binding.rvUpcomingAppointments.visibility = View.GONE
    }

    private fun stopShimmer() {
        binding.layoutAppointmentsShimmer.stopShimmer()
        binding.layoutAppointmentsShimmer.visibility = View.GONE
        binding.rvUpcomingAppointments.visibility = View.VISIBLE
    }

}