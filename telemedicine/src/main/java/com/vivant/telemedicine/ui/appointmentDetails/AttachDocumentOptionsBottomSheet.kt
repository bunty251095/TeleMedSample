package com.vivant.telemedicine.ui.appointmentDetails

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vivant.telemedicine.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vivant.telemedicine.common.Constants
import com.vivant.telemedicine.databinding.BottomSheetAttachDocumentOptionsBinding

class AttachDocumentOptionsBottomSheet(var listener: OnAttachDocumentOptionClickListener) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetAttachDocumentOptionsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = BottomSheetAttachDocumentOptionsBinding.inflate(inflater, container, false)
        initialise()
        setClickable()
        return binding.root
    }

    private fun initialise() {

    }

    private fun setClickable() {

        binding.layoutPrescription.setOnClickListener {
            dismiss()
            listener.onAttachDocumentOptionClick(Constants.PRESCRIPTION)
        }

        binding.layoutLabReport.setOnClickListener {
            dismiss()
            listener.onAttachDocumentOptionClick(Constants.LAB_REPORT)
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


    interface OnAttachDocumentOptionClickListener {
        fun onAttachDocumentOptionClick(docTypecode: String)
    }

}