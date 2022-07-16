package com.vivant.telemedicine.ui.appointmentDetails

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vivant.telemedicine.R
import com.vivant.telemedicine.common.Constants
import com.vivant.telemedicine.databinding.BottomSheetAppointmentOptionsBinding

class AppointmentOptionsBottomSheet(var listener: OnAppointmentOptionClickListener,
                                    val appStatus:String,
                                    val isHistory: Boolean,
                                    val isFeedback: Boolean = false ) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetAppointmentOptionsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = BottomSheetAppointmentOptionsBinding.inflate(inflater, container, false)
        initialise()
        setClickable()
        return binding.root
    }

    private fun initialise() {

        if ( isHistory ) {
            binding.layoutRescheduleAppointment.visibility = View.GONE
            binding.layoutCancelAppointment.visibility = View.GONE
            if ( isFeedback ) {
                binding.layoutFeedback.visibility = View.GONE
            } else {
                binding.layoutFeedback.visibility = View.VISIBLE
            }
        } else {
            binding.layoutRescheduleAppointment.visibility = View.VISIBLE
            binding.layoutCancelAppointment.visibility = View.VISIBLE
            binding.layoutFeedback.visibility = View.GONE
        }

/*        when(appStatus) {
            "CAN","PAST" -> {
                binding.layoutRescheduleAppointment.visibility = View.GONE
                binding.layoutCancelAppointment.visibility = View.GONE
            }
        }*/
    }

    private fun setClickable() {

        binding.layoutRescheduleAppointment.setOnClickListener {
            dismiss()
            listener.onAppointmentOptionClick(Constants.RESCHEDULE)
        }

        binding.layoutCancelAppointment.setOnClickListener {
            dismiss()
            listener.onAppointmentOptionClick(Constants.CANCEL)
        }

        binding.layoutViewInvoice.setOnClickListener {
            dismiss()
            listener.onAppointmentOptionClick(Constants.VIEW_INVOICE)
        }

        binding.layoutFeedback.setOnClickListener {
            dismiss()
            listener.onAppointmentOptionClick(Constants.SAVE_FEEDBACK)
        }

    }

    override fun getTheme(): Int {
        //return super.getTheme();
        return R.style.BottomSheetDialog
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //return super.onCreateDialog(savedInstanceState);
        return BottomSheetDialog(requireContext(), theme)
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }


    interface OnAppointmentOptionClickListener {
        fun onAppointmentOptionClick(action: String)
    }

}