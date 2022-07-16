package com.vivant.telemedicine.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.vivant.telemedicine.R
import com.vivant.telemedicine.common.Constants
import com.vivant.telemedicine.common.Helper
import com.vivant.telemedicine.common.Utilities
import com.vivant.telemedicine.databinding.ItemChooseDoctorBinding
import com.vivant.telemedicine.model.ListSearchedDoctorsModel

class ChooseDoctorAdapter(val context: Context,val listener: OnDoctorClickListener,val mode:String) : RecyclerView.Adapter<ChooseDoctorAdapter.ChooseDoctorViewHolder>() {

    val gson = GsonBuilder().setPrettyPrinting().create()
    private var doctorsList: MutableList<ListSearchedDoctorsModel.DoctorDetail> = mutableListOf()

    override fun getItemCount(): Int = doctorsList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseDoctorViewHolder =
        ChooseDoctorViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_choose_doctor,parent,false))

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ChooseDoctorAdapter.ChooseDoctorViewHolder, position: Int) {
        try {
            val doctor = doctorsList[position]

            holder.txtDoctorName.text = doctor.firstName
            holder.txtDoctorSpeciality.text = doctor.speciality

            if ( !Utilities.isNullOrEmpty(doctor.yearsOfPractice) ) {
                holder.txtDoctorAge.text = "${doctor.yearsOfPractice} ${context.resources.getString(R.string.YRS)}"
                holder.txtDoctorAge.visibility = View.VISIBLE
            } else {
                holder.txtDoctorAge.visibility = View.GONE
            }

            holder.txtDoctorGender.text = Helper.getGenderDisplayValue(doctor.gender,context)
            when( mode ) {
                Constants.VIDEO_CALL -> holder.txtDoctorCode.text = "RM ${doctor.videoPrice}"
                Constants.AUDIO_CALL -> holder.txtDoctorCode.text = "RM ${doctor.audioPrice}"
                Constants.TEXT_CHAT -> holder.txtDoctorCode.text = "RM ${doctor.chatPrice}"
            }

            if ( !Utilities.isNullOrEmpty(doctor.profileImage.toString()) ) {
                if ( !Utilities.isNullOrEmpty(doctor.profileImage.fileBytes) ) {
                    val abc = Base64.decode(doctor.profileImage.fileBytes, Base64.DEFAULT)
                    holder.imgDoctor.setImageBitmap(BitmapFactory.decodeByteArray(abc,0,abc.size) )
                } else {
                    holder.imgDoctor.setImageResource(R.drawable.img_my_profile)
                }
            }

/*            when(doctor.gender) {
                "M" -> holder.imgDoctor.setImageResource(R.drawable.img_specialist)
                "F" -> holder.imgDoctor.setImageResource(R.drawable.img_general_physician)
            }*/

            holder.itemView.setOnClickListener {
                listener.onDoctorSelection(position, doctor)
            }
        } catch ( e : Exception ) {
            e.printStackTrace()
        }
    }

    fun updateList( list: MutableList<ListSearchedDoctorsModel.DoctorDetail> ) {
        //Utilities.printData("DoctorList",list,true)
        doctorsList.clear()
        doctorsList.addAll(list)
        notifyDataSetChanged()
    }

    interface OnDoctorClickListener {
        fun onDoctorSelection(position: Int, doctor: ListSearchedDoctorsModel.DoctorDetail)
    }

    inner class ChooseDoctorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemChooseDoctorBinding.bind(view)
        val imgDoctor = binding.imgDoctor
        val txtDoctorName = binding.txtDoctorName
        val txtDoctorSpeciality = binding.txtDoctorSpeciality
        val txtDoctorAge = binding.txtDoctorAge
        val txtDoctorGender = binding.txtDoctorGender
        val txtDoctorCode = binding.txtDoctorCode
    }

}