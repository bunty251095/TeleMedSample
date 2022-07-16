package com.vivant.telemedicine.ui.appointmentDetails

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import com.vivant.telemedicine.R
import com.vivant.telemedicine.common.Utilities
import com.vivant.telemedicine.databinding.DialogCancelAppointmentBinding

class CancelAppointmentDialog(private val mcontext: Context,
                              private val listener: OnCancelListener) : Dialog(mcontext)  {

    private lateinit var binding: DialogCancelAppointmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DialogCancelAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        this.setCancelable(false)
        this.setCanceledOnTouchOutside(false)

        binding.imgClose.setOnClickListener {
            dismiss()
        }

        binding.btnLeftSide.setOnClickListener {
            dismiss()
        }

        binding.btnRightSide.setOnClickListener {
            if ( !Utilities.isNullOrEmpty(binding.edtReason.text.toString()) ) {
                dismiss()
                listener.onCancelClickListener(binding.edtReason.text.toString())
            } else {
                Utilities.toastMessageShort(context,context.resources.getString(R.string.PLEASE_ENTER_REASON_CANCELLATION))
            }
        }
    }

    interface OnCancelListener {
        fun onCancelClickListener(reason: String)
    }

}