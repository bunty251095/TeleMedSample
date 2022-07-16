package com.vivant.telemedicine.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.vivant.telemedicine.R
import com.vivant.telemedicine.common.Constants
import com.vivant.telemedicine.common.DateHelper
import com.vivant.telemedicine.common.Helper
import com.vivant.telemedicine.common.Utilities
import com.vivant.telemedicine.databinding.ItemConsultationBinding
import com.vivant.telemedicine.model.ListAppointmentsModel
import com.vivant.telemedicine.model.ListConsultationModel
import java.util.*

class ConsultationAdapter( val context: Context,
                        val listener: OnConsultationClickListener) : RecyclerView.Adapter<ConsultationAdapter.ConsultationHolder>() {

    private var appointmentsList: MutableList<ListConsultationModel.Consultation> = mutableListOf()
    private lateinit var appointmentDate :Date
    private lateinit var date :Array<String>

    override fun getItemCount(): Int = appointmentsList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsultationHolder =
        ConsultationHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_consultation,parent,false))

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ConsultationAdapter.ConsultationHolder, position: Int) {
        try {
            val appointment = appointmentsList[position]
            //appointmentDate = DateHelper.convertStringDateToDate(appointment.appointmentDate,"yyyy-MM-dd")
            date = appointment.consultationDate.split(" ").toTypedArray()

            if ( appointment.isRefundDone != null && appointment.isRefundDone.equals(Constants.TRUE,ignoreCase = true) ) {
                holder.imgRefund.visibility = View.VISIBLE
                holder.txtRefund.visibility = View.VISIBLE
            } else {
                holder.imgRefund.visibility = View.GONE
                holder.txtRefund.visibility = View.GONE
            }

            if ( !Utilities.isNullOrEmpty(appointment.doctorDetails.profileImage.toString()) ) {
                if ( !Utilities.isNullOrEmpty(appointment.doctorDetails.profileImage.fileBytes) ) {
                    val abc = Base64.decode(appointment.doctorDetails.profileImage.fileBytes, Base64.DEFAULT)
                    holder.imgDoctor.setImageBitmap(BitmapFactory.decodeByteArray(abc,0,abc.size) )
                } else {
                    holder.imgDoctor.setImageResource(R.drawable.img_my_profile)
                }
            }

            if ( !Utilities.isNullOrEmpty(appointment.appointmentMode) ) {
                holder.imgMode.setImageResource(Helper.getModeImageHistory(appointment.appointmentMode))
                holder.imgMode.visibility = View.VISIBLE
            } else {
                holder.imgMode.visibility = View.INVISIBLE
            }

            //holder.txtDateTime.text = "${DateHelper.formatDateValue(DateHelper.DATEFORMAT_DDMMMYYYY_NEW,date[0])}\n${date[1]}"
            holder.txtDateTime.text = "${DateHelper.convertDateSourceToDestination(date[0],DateHelper.DISPLAY_DATE_MMDDYYYY,DateHelper.DATEFORMAT_DDMMMYYYY_NEW)}\n${DateHelper.getTimeIn12HrFormatAmOrPm(date[1])}"
            if ( !Utilities.isNullOrEmpty(appointment.appointmentDetails.doctorName) ) {
                holder.txtDoctorName.text = "${context.resources.getString(R.string.DR)} ${appointment.appointmentDetails.doctorName}"
                holder.txtDoctorName.visibility = View.VISIBLE
            } else {
                holder.txtDoctorName.text = ""
                holder.txtDoctorName.visibility = View.GONE
            }

            holder.txtCategory.text = Helper.getDisplayCategory(appointment.appointmentCategory,context)

            if ( !Utilities.isNullOrEmpty(appointment.doctorDetails.specialization) ) {
                holder.txtDoctorSpeciality.text = appointment.doctorDetails.specialization
                holder.txtDoctorSpeciality.visibility = View.VISIBLE
            } else {
                holder.txtDoctorSpeciality.visibility = View.VISIBLE
            }

            if ( !Utilities.isNullOrEmpty(appointment.doctorDetails.experience) ) {
                holder.txtDoctorExp.text = "${appointment.doctorDetails.experience} ${context.resources.getString(R.string.YRS)}"
                holder.txtDoctorExp.visibility = View.VISIBLE
            } else {
                holder.txtDoctorExp.text = ""
                holder.txtDoctorExp.visibility = View.GONE
            }

            if ( !Utilities.isNullOrEmptyOrZero(appointment.appointmentDetails.finalPrice) ) {
                holder.txtDoctorFees.text = "RM ${appointment.appointmentDetails.finalPrice}"
            } else {
                holder.txtDoctorFees.text = "RM --"
            }

            //Utilities.printLogError("AppointmentStatus--->${appointment.appointmentStatus}")
            when {
                appointment.appointmentStatus == "COM" -> {
                    holder.txtAppointmentStatus.text = context.resources.getText(R.string.COMPLETED)
                    holder.txtAppointmentStatus.setTextColor(ContextCompat.getColor(context,R.color.dark_green))
                }
                appointment.appointmentStatus == "CONFIRM" -> {
                    holder.txtAppointmentStatus.text = context.resources.getText(R.string.CONFIRMED)
                    holder.txtAppointmentStatus.setTextColor(ContextCompat.getColor(context,R.color.dark_green))
                }
/*                appointment.appointmentStatus == "CONFIRM" && appointmentDate.before(Date()) -> {
                    holder.txtAppointmentStatus.text = context.resources.getText(R.string.UNATTENDED)
                    holder.txtAppointmentStatus.setTextColor(ContextCompat.getColor(context,R.color.dark_gray))
                }
                appointment.appointmentStatus == "PENDING" && appointmentDate.before(Date()) -> {
                    holder.txtAppointmentStatus.text = context.resources.getText(R.string.UNATTENDED)
                    holder.txtAppointmentStatus.setTextColor(ContextCompat.getColor(context,R.color.dark_gray))
                }*/
                appointment.appointmentStatus == "PENDING" -> {
                    holder.txtAppointmentStatus.text = context.resources.getText(R.string.NOT_CONFIRMED)
                    holder.txtAppointmentStatus.setTextColor(ContextCompat.getColor(context,R.color.state_error))
                }
                appointment.appointmentStatus == "RES" -> {
                    holder.txtAppointmentStatus.text = context.resources.getText(R.string.NOT_CONFIRMED)
                    holder.txtAppointmentStatus.setTextColor(ContextCompat.getColor(context,R.color.state_error))
                }
                appointment.appointmentStatus == "CAN" -> {
                    holder.txtAppointmentStatus.text = context.resources.getText(R.string.CANCELLED)
                    holder.txtAppointmentStatus.setTextColor(ContextCompat.getColor(context,R.color.state_error))
                }
                appointment.appointmentStatus == "ACT" -> {
                    holder.txtAppointmentStatus.text = context.resources.getText(R.string.NOT_COMPLETED)
                    holder.txtAppointmentStatus.setTextColor(ContextCompat.getColor(context,R.color.state_error))
                }
                appointment.appointmentStatus == "NOTACCEPTED" -> {
                    holder.txtAppointmentStatus.text = ""
                }
                else -> {
                    holder.txtAppointmentStatus.text = appointment.appointmentStatus
                    //holder.txtAppointmentStatus.text = ""
                }
            }

            holder.layoutUpcomingAppointments.setOnClickListener {
                listener.onConsultationSelection(position,Constants.CLICK,appointment)
            }

            holder.imgOption.setOnClickListener {
                listener.onConsultationSelection(position,Constants.OPTION,appointment)
            }

            holder.btnPrescription.setOnClickListener {
                listener.onConsultationSelection(position,Constants.PRESCRIPTION,appointment)
            }

            holder.btnBookFollowup.setOnClickListener {
                listener.onConsultationSelection(position,Constants.BOOK_FOLLOW_UP,appointment)
            }

        } catch ( e : Exception ) {
            e.printStackTrace()
        }
    }

    fun updateList( list: MutableList<ListConsultationModel.Consultation> ) {
        appointmentsList.clear()
        appointmentsList.addAll(list)
        notifyDataSetChanged()
    }

    interface OnConsultationClickListener {
        fun onConsultationSelection(position: Int,action:String,con:ListConsultationModel.Consultation)
    }

    inner class ConsultationHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemConsultationBinding.bind(view)
        val layoutUpcomingAppointments = binding.layoutUpcomingAppointments
        val imgDoctor = binding.imgDoctor
        val txtDoctorName = binding.txtDoctorName
        val txtCategory = binding.txtCategory
        val txtDateTime = binding.txtDateTime
        val txtDoctorSpeciality = binding.txtDoctorSpeciality
        val txtDoctorExp = binding.txtDoctorExp
        val txtDoctorGender = binding.txtDoctorGender
        val txtDoctorFees = binding.txtDoctorFees
        val txtAppointmentStatus = binding.txtAppointmentStatus
        val imgRefund = binding.imgRefund
        val txtRefund = binding.txtRefund

        val btnPrescription = binding.btnPrescription
        val btnBookFollowup = binding.btnBookFollowup
        val imgMode = binding.imgMode
        val imgOption = binding.imgOption
    }

}