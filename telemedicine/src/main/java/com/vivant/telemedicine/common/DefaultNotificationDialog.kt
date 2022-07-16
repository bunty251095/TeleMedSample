package com.vivant.telemedicine.common

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Html
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import com.vivant.telemedicine.R
import com.vivant.telemedicine.databinding.DefaultDialogBinding


class DefaultNotificationDialog(context: Context?,
                                private val onDialogValueListener: OnDialogValueListener,
                                data: DialogData) : Dialog(context!!), View.OnClickListener {

    private var dialogData: DialogData? = null
    private var mContext: Context? = null
    private var animation: Animation? = null
    private lateinit var binding: DefaultDialogBinding

    init {
        //Utilities.printData("dialogData", data)
        this.dialogData = data
        this.mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DefaultDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        this.setCancelable(false)
        this.setCanceledOnTouchOutside(false)

        binding.imgCloseInput.setOnClickListener(this)
        binding.btnLeftSide.setOnClickListener(this)
        binding.btnRightSide.setOnClickListener(this)

        binding.btnLeftSide.text = dialogData!!.btnLeftName
        binding.btnRightSide.text = dialogData!!.btnRightName
        binding.txtDialogTitle.text = dialogData!!.title
        //txt_dialog_description.text = dialogData!!.message
        binding.txtDialogDescription.text = Html.fromHtml(dialogData!!.message)

        if ( dialogData!!.showImage && dialogData!!.imgId != 0 ) {
            binding.imgToDisplay.visibility = View.VISIBLE
            binding.imgToDisplay.setImageResource(dialogData!!.imgId)
        } else {
            binding.imgToDisplay.visibility = View.GONE
        }

        if ( dialogData!!.animate ) {
            animation = AnimationUtils.loadAnimation(context,R.anim.rotate_forward)
            ImageViewCompat.setImageTintList(binding.imgToDisplay,ColorStateList.valueOf(ContextCompat.getColor(context,R.color.colorPrimary)))
            binding.imgToDisplay.animation = animation
            binding.imgToDisplay.startAnimation(animation)
        }

        if ( Utilities.isNullOrEmpty(dialogData!!.title) ) {
            binding.txtDialogTitle.visibility = View.GONE
        } else {
            binding.txtDialogTitle.visibility = View.VISIBLE
        }

        if (!dialogData!!.showLeftButton) {
            binding.btnLeftSide.visibility = View.GONE
        } else {
            binding.btnLeftSide.visibility = View.VISIBLE
        }
        if (!dialogData!!.showRightButton) {
            binding.btnRightSide.visibility = View.GONE
        } else {
            binding.btnRightSide.visibility = View.VISIBLE
        }

        if (dialogData!!.showDismiss) {
            binding.imgCloseInput.visibility = View.VISIBLE
        } else {
            binding.imgCloseInput.visibility = View.INVISIBLE
        }

        if ( dialogData!!.hasErrorBtn ) {
            binding.btnRightSide.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(mContext!!,R.color.state_error))
        }

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_left_side -> {
                onDialogValueListener.onDialogClickListener(isButtonLeft = true, isButtonRight = false)
                dismiss()
            }
            R.id.btn_right_side -> {
                onDialogValueListener.onDialogClickListener(isButtonLeft = false, isButtonRight = true)
                dismiss()
            }
            R.id.img_close_input -> {
                onDialogValueListener.onDialogClickListener(isButtonLeft = false, isButtonRight = false)
                dismiss()
            }
        }
    }

    class DialogData {
        var title = "Title"
        var message = "Message"
        var btnLeftName = "Cancel"
        var btnRightName = "Ok"
        var showLeftButton = true
        var showRightButton = true
        var showDismiss = true
        var hasErrorBtn = false
        var showImage = false
        var animate = false
        var imgId = 0
    }

    interface OnDialogValueListener {
        fun onDialogClickListener(isButtonLeft: Boolean, isButtonRight: Boolean)
    }

}