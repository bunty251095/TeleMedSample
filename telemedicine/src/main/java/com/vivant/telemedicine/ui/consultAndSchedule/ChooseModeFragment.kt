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
import com.vivant.telemedicine.common.Constants
import com.vivant.telemedicine.common.ConsultAndScheduleSingleton
import com.vivant.telemedicine.common.Utilities
import com.vivant.telemedicine.databinding.FragmentChooseModeBinding
import com.vivant.telemedicine.viewmodel.ConsultAndScheduleViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChooseModeFragment : BaseFragment() {

    private var _binding: FragmentChooseModeBinding? = null
    private val binding get() = _binding!!

    val viewModel: ConsultAndScheduleViewModel by viewModel()

    var from = ""
    var videoPrice = ""
    var audioPrice = ""
    var chatPrice = ""
    private val consultAndScheduleSingleton = ConsultAndScheduleSingleton.instance!!

    override fun getViewModel(): BaseViewModel = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            // callback to Handle back button event
            val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    (activity as ConsultAndScheduleActivity).routeToHomeScreen()
                }
            }
            requireActivity().onBackPressedDispatcher.addCallback(this, callback)

        } catch ( e : Exception ) {
            e.printStackTrace()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentChooseModeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        try {
            from = (activity as ConsultAndScheduleActivity).from
            initialise()
            registerObserver()
        } catch ( e : Exception ) {
            e.printStackTrace()
        }
        return root
    }

    private fun initialise() {

        if ( from.equals(Constants.CONSULT_NOW,ignoreCase = true) ) {
            viewModel.callGetConsultationTemplateApi()
        } else {
            binding.txtVideoPrice.visibility = View.GONE
            binding.txtAudioPrice.visibility = View.GONE
            binding.txtTextChatPrice.visibility = View.GONE
        }

        binding.cardVideoCall.setOnClickListener {
            consultAndScheduleSingleton.consultationMode = Constants.VIDEO_CALL
            consultAndScheduleSingleton.totalPrice = videoPrice
            routeToNext()
        }

        binding.cardAudioCall.setOnClickListener {
            consultAndScheduleSingleton.consultationMode = Constants.AUDIO_CALL
            consultAndScheduleSingleton.totalPrice = audioPrice
            routeToNext()
        }

        binding.cardTextChat.setOnClickListener {
            consultAndScheduleSingleton.consultationMode = Constants.TEXT_CHAT
            consultAndScheduleSingleton.totalPrice = chatPrice
            routeToNext()
        }

    }

    @SuppressLint("SetTextI18n")
    private fun registerObserver() {

        viewModel.getConsultationTemplate.observe(viewLifecycleOwner) {
            if (it != null) {
                if ( !Utilities.isNullOrEmptyOrZero(it.template.videoFees)
                    && !Utilities.isNullOrEmptyOrZero(it.template.audioFees)
                    && !Utilities.isNullOrEmptyOrZero(it.template.chatFees) ) {
                    videoPrice = it.template.videoFees
                    audioPrice = it.template.audioFees
                    chatPrice = it.template.chatFees
                    binding.txtVideoPrice.text = "${resources.getString(R.string.FROM_RM)} $videoPrice"
                    binding.txtAudioPrice.text = "${resources.getString(R.string.FROM_RM)} $audioPrice"
                    binding.txtTextChatPrice.text = "${resources.getString(R.string.FROM_RM)} $chatPrice"
                }
            }
        }

    }

    private fun routeToNext() {
        when(from) {
            Constants.CONSULT_NOW -> {
                (activity as ConsultAndScheduleActivity).setStepPositionConsultNow(Constants.STEP_TWO)
            }
            Constants.SCHEDULE_APPOINTMENT -> {
                (activity as ConsultAndScheduleActivity).setStepPositionScheduleAppointment(Constants.STEP_TWO)
            }
        }
        findNavController().navigate(R.id.action_chooseModeFragment_chooseSpecializationFragment)
    }

}