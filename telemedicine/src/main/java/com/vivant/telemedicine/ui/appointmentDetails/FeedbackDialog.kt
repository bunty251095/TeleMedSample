package com.vivant.telemedicine.ui.appointmentDetails

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.RatingBar
import com.vivant.telemedicine.R
import com.vivant.telemedicine.common.Helper
import com.vivant.telemedicine.common.Utilities
import com.vivant.telemedicine.databinding.DialogFeedbackBinding

class FeedbackDialog(private val mContext: Context,
                     private val listener: OnSubmitListener) : Dialog(mContext),RatingBar.OnRatingBarChangeListener  {

    private lateinit var binding: DialogFeedbackBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DialogFeedbackBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        this.setCancelable(false)
        this.setCanceledOnTouchOutside(false)
        setClickable()
    }

    private fun setClickable() {

        binding.txtRating.visibility = View.GONE
        binding.userRating.onRatingBarChangeListener = this

        binding.imgClose.setOnClickListener {
            dismiss()
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnSubmit.setOnClickListener {
            if ( Utilities.isNullOrEmptyOrZero(binding.userRating.rating.toString()) ) {
                Utilities.toastMessageShort(mContext,mContext.resources.getString(R.string.PLEASE_SELECT_RATING))
            } else if ( Utilities.isNullOrEmpty(binding.edtComment.text.toString()) ) {
                Utilities.toastMessageShort(mContext,mContext.resources.getString(R.string.PLEASE_WRITE_COMMENT))
            } else {
                dismiss()
                listener.onSubmitClickListener(binding.userRating.rating.toInt(),binding.edtComment.text.toString())
            }
        }

    }

    override fun onRatingChanged(p0: RatingBar?, p1: Float, p2: Boolean) {
        binding.txtRating.text = Helper.getRatingText(p1.toInt(),mContext)
        binding.txtRating.visibility = View.VISIBLE
    }

    interface OnSubmitListener {
        fun onSubmitClickListener(rating:Int,comment:String)
    }

}
