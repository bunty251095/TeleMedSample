package com.vivant.telemedicine.base

import com.vivant.telemedicine.R
import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.animation.AnimationUtils
import com.vivant.telemedicine.databinding.CustumProgressBarBinding

class CustomProgressBar(context: Context) : Dialog(context, R.style.TransparentProgressDialog) {

    private var binding: CustumProgressBarBinding

    init {
        binding = CustumProgressBarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val wlmp = window!!.attributes
        wlmp.gravity = Gravity.CENTER_HORIZONTAL
        window!!.attributes = wlmp
        setTitle(null)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        //setOnCancelListener(null)
    }

    override fun show() {
        super.show()
        val anim = AnimationUtils.loadAnimation(context, R.anim.rotate_forward)
        binding.imgCustomProgress.animation = anim
        binding.imgCustomProgress.startAnimation(anim)

    }

}
