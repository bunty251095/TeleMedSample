package com.vivant.telemedicine.ui.consultAndSchedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.vivant.telemedicine.R
import com.vivant.telemedicine.adapter.ChooseDoctorAdapter
import com.vivant.telemedicine.base.BaseFragment
import com.vivant.telemedicine.base.BaseViewModel
import com.vivant.telemedicine.common.Constants
import com.vivant.telemedicine.common.ConsultAndScheduleSingleton
import com.vivant.telemedicine.common.Utilities
import com.vivant.telemedicine.databinding.FragmentChooseDoctorBinding
import com.vivant.telemedicine.model.ListSearchedDoctorsModel
import com.vivant.telemedicine.viewmodel.ConsultAndScheduleViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChooseDoctorFragment : BaseFragment() , ChooseDoctorAdapter.OnDoctorClickListener {

    private var _binding: FragmentChooseDoctorBinding? = null
    private val binding get() = _binding!!

    private val consultAndScheduleSingleton = ConsultAndScheduleSingleton.instance!!
    private var chooseDoctorAdapter: ChooseDoctorAdapter? = null
    private var animation: LayoutAnimationController? = null

    val viewModel: ConsultAndScheduleViewModel by viewModel()

    override fun getViewModel(): BaseViewModel = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            // callback to Handle back button event
            val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    (activity as ConsultAndScheduleActivity).setStepPositionScheduleAppointment(Constants.STEP_TWO)
                    findNavController().navigate(R.id.action_chooseDoctorFragment_to_chooseSpecializationFragment)
                }
            }
            requireActivity().onBackPressedDispatcher.addCallback(this, callback)

        } catch ( e : Exception ) {
            e.printStackTrace()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentChooseDoctorBinding.inflate(inflater, container, false)
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
        chooseDoctorAdapter = ChooseDoctorAdapter(requireContext(),this,consultAndScheduleSingleton.consultationMode)
        //binding.rvDoctorsList.layoutAnimation = animation
        binding.rvDoctorsList.adapter = chooseDoctorAdapter

        startShimmer()
        viewModel.callListSearchedDoctorsApi(consultAndScheduleSingleton.speciality,consultAndScheduleSingleton.consultationMode)

/*        val list: MutableList<DoctorModel> = mutableListOf()
        list.add(DoctorModel("1","Dr Adam","General Practitioner","27","Male","RM 40.80"))
        list.add(DoctorModel("2","Dr Alicia","General Practitioner","11","Female","RM 40.80"))
        list.add(DoctorModel("3","Dr Patrick","General Practitioner","18","Male","RM 40.80"))
        list.add(DoctorModel("4","Dr Michael","General Practitioner","22","Female","RM 40.80"))
        list.add(DoctorModel("5","Dr Steve","General Practitioner","15","Male","RM 40.80"))*/

/*        Handler(Looper.getMainLooper()).postDelayed({
            binding.rvDoctorsList.layoutAnimation = animation
            chooseDoctorAdapter!!.updateList(list)
        }, 300)*/

    }

    private fun registerObserver() {
        viewModel.listSearchedDoctors.observe(viewLifecycleOwner) {
            if (it != null) {
                val doctors = it.doctors.list
                if ( doctors.toString() != "" && !doctors.isNullOrEmpty() ) {
                    //chooseDoctorAdapter!!.updateList(doctors)
                    updateList(doctors)
                } else {
                    binding.rvDoctorsList.visibility = View.GONE
                    binding.layoutNoData.visibility = View.VISIBLE
                }
                stopShimmer()
            }
        }
    }

    private fun updateList(list: MutableList<ListSearchedDoctorsModel.DoctorDetail>) {
        if ( !list.isNullOrEmpty() ) {
            binding.rvDoctorsList.visibility = View.VISIBLE
            binding.layoutNoData.visibility = View.GONE
            //binding.rvNotifications.layoutAnimation = animation
            chooseDoctorAdapter!!.updateList(list)
            binding.rvDoctorsList.scheduleLayoutAnimation()
        } else {
            binding.rvDoctorsList.visibility = View.GONE
            binding.layoutNoData.visibility = View.VISIBLE
        }
    }

    override fun onDoctorSelection(position: Int, doctor: ListSearchedDoctorsModel.DoctorDetail) {
        Utilities.printData("DoctorDetails",doctor,true)
        consultAndScheduleSingleton.doctorImage = doctor.profileImage.fileBytes
        consultAndScheduleSingleton.doctorId = doctor.id
        consultAndScheduleSingleton.supplierId = doctor.supplierID
        consultAndScheduleSingleton.doctorName = doctor.firstName
        consultAndScheduleSingleton.doctorSpeciality = doctor.speciality
        consultAndScheduleSingleton.doctorExperince = doctor.yearsOfPractice
        consultAndScheduleSingleton.doctorGender = doctor.gender
        when(consultAndScheduleSingleton.consultationMode) {
            Constants.VIDEO_CALL -> consultAndScheduleSingleton.totalPrice = doctor.videoPrice
            Constants.AUDIO_CALL -> consultAndScheduleSingleton.totalPrice = doctor.audioPrice
            Constants.TEXT_CHAT -> consultAndScheduleSingleton.totalPrice = doctor.chatPrice
        }
        (activity as ConsultAndScheduleActivity).setStepPositionScheduleAppointment(Constants.STEP_FOUR)
        findNavController().navigate(R.id.action_chooseDoctorFragment_chooseSlotFragment)
    }

    private fun startShimmer() {
        binding.layoutDoctorsListShimmer.startShimmer()
        binding.layoutDoctorsListShimmer.visibility = View.VISIBLE
    }

    private fun stopShimmer() {
        binding.layoutDoctorsListShimmer.stopShimmer()
        binding.layoutDoctorsListShimmer.visibility = View.GONE
    }

}