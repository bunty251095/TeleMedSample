package com.vivant.telemedicine.ui.consultAndSchedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.vivant.telemedicine.R
import com.vivant.telemedicine.base.BaseFragment
import com.vivant.telemedicine.base.BaseViewModel
import com.vivant.telemedicine.common.Constants
import com.vivant.telemedicine.common.ConsultAndScheduleSingleton
import com.vivant.telemedicine.databinding.FragmentChooseSpecializationBinding
import com.vivant.telemedicine.viewmodel.ConsultAndScheduleViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChooseSpecializationFragment : BaseFragment() {

    private var _binding: FragmentChooseSpecializationBinding? = null
    private val binding get() = _binding!!

    var from = ""
    private val consultAndScheduleSingleton = ConsultAndScheduleSingleton.instance!!

    val viewModel: ConsultAndScheduleViewModel by viewModel()

    override fun getViewModel(): BaseViewModel = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            // callback to Handle back button event
            val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    when(from) {
                        Constants.CONSULT_NOW -> {
                            (activity as ConsultAndScheduleActivity).setStepPositionConsultNow(Constants.STEP_ONE,true)
                        }
                        Constants.SCHEDULE_APPOINTMENT -> {
                            (activity as ConsultAndScheduleActivity).setStepPositionScheduleAppointment(Constants.STEP_ONE)
                        }
                    }
                    findNavController().navigate(R.id.action_chooseSpecializationFragment_to_chooseModeFragment)
                }
            }
            requireActivity().onBackPressedDispatcher.addCallback(this, callback)

        } catch ( e : Exception ) {
            e.printStackTrace()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentChooseSpecializationBinding.inflate(inflater, container, false)
        val root: View = binding.root
        try {
            from = (activity as ConsultAndScheduleActivity).from
            initialise()
        } catch ( e : Exception ) {
            e.printStackTrace()
        }
        return root
    }

    private fun initialise() {

        binding.cardGeneralPractitioner.setOnClickListener {
            consultAndScheduleSingleton.speciality = Constants.GENERAL_PRACTITIONER
            when(from) {
                Constants.CONSULT_NOW -> {
                    (activity as ConsultAndScheduleActivity).setStepPositionConsultNow(Constants.STEP_THREE)
                    it.findNavController().navigate(R.id.action_chooseSpecializationFragment_describeSymptomsFragment)
                }
                Constants.SCHEDULE_APPOINTMENT -> {
                    (activity as ConsultAndScheduleActivity).setStepPositionScheduleAppointment(Constants.STEP_THREE)
                    it.findNavController().navigate(R.id.action_chooseSpecializationFragment_chooseDoctorFragment)
                }
            }

        }

    }

}