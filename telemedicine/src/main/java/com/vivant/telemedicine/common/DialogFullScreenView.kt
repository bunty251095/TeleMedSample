package com.vivant.telemedicine.common

import com.vivant.telemedicine.R
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.github.chrisbanes.photoview.PhotoViewAttacher
import com.squareup.picasso.Picasso
import com.vivant.telemedicine.databinding.DialogImageFullViewCommonBinding

class DialogFullScreenView(context:Context,
                           val isImage:Boolean,
                           val imgUrl:String,
                           val imgBitmap:Bitmap?) : Dialog(context,R.style.TransparentProgressDialog) {

    private lateinit var binding: DialogImageFullViewCommonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DialogImageFullViewCommonBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        if ( !Utilities.isNullOrEmpty(imgUrl) ) {

            binding.expandedImage.visibility = View.VISIBLE
            binding.layoutImgBitmap.visibility = View.GONE
            if ( isImage ) {
                Picasso.get()
                    .load(imgUrl)
                    .placeholder(R.drawable.img_placeholder)
                    .resize(15000,12000)
                    .onlyScaleDown()
                    .error(R.drawable.img_placeholder)
                    .into(binding.expandedImage)
                PhotoViewAttacher(binding.expandedImage)
            } else {
                binding.expandedImage.setImageResource(R.drawable.img_placeholder)
            }
        } else {

            binding.layoutImgBitmap.visibility = View.VISIBLE
            binding.expandedImage.visibility = View.GONE
            if ( imgBitmap != null ) {
                setCanceledOnTouchOutside(false)
                binding.expandedBitmapImage.setImageBitmap(imgBitmap)
            }
        }

        binding.imgCloseImg.setOnClickListener {
            dismiss()
        }

        binding.expandedBitmapImage.setOnViewDragListener { dx, dy ->

        }

    }

}