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
import com.vivant.telemedicine.databinding.ItemMyHistoryBinding
import com.vivant.telemedicine.model.ListAppointmentsModel
import java.util.*

class MyHistoryAdapter( val context: Context,
                        val listener: OnPastAppointmentClickListener) : RecyclerView.Adapter<MyHistoryAdapter.MyHistoryViewHolder>() {

    private var appointmentsList: MutableList<ListAppointmentsModel.UpcomingAppointment> = mutableListOf()
    private lateinit var appointmentDate :Date

    override fun getItemCount(): Int = appointmentsList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHistoryViewHolder =
        MyHistoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_my_history,parent,false))

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyHistoryAdapter.MyHistoryViewHolder, position: Int) {
        try {
            val appointment = appointmentsList[position]
            appointmentDate = DateHelper.convertStringDateToDate(appointment.appointmentDate,"yyyy-MM-dd")

            if ( appointment.isRefundDone != null && appointment.isRefundDone.toString().equals(Constants.TRUE,ignoreCase = true) ) {
                holder.imgRefund.visibility = View.VISIBLE
                holder.txtRefund.visibility = View.VISIBLE
            } else {
                holder.imgRefund.visibility = View.GONE
                holder.txtRefund.visibility = View.GONE
            }

            if ( !Utilities.isNullOrEmpty(appointment.profileImage.toString()) ) {
                if ( !Utilities.isNullOrEmpty(appointment.profileImage.fileBytes) ) {
                    val abc = Base64.decode(appointment.profileImage.fileBytes, Base64.DEFAULT)
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

            if ( appointment.appointmentStartTime != null ) {
                holder.txtDateTime.text = "${DateHelper.formatDateValue(DateHelper.DATEFORMAT_DDMMMYYYY_NEW,appointment.appointmentDate)}\n${DateHelper.getTimeIn12HrFormatAmOrPm(appointment.appointmentStartTime)}"
            } else {
                holder.txtDateTime.text = "${DateHelper.formatDateValue(DateHelper.DATEFORMAT_DDMMMYYYY_NEW,appointment.appointmentDate)}"
            }
            if ( !Utilities.isNullOrEmpty(appointment.firstName) ) {
                holder.txtDoctorName.text = "${context.resources.getString(R.string.DR)} ${appointment.firstName}"
                holder.txtDoctorName.visibility = View.VISIBLE
            } else {
                holder.txtDoctorName.text = ""
                holder.txtDoctorName.visibility = View.GONE
            }

            holder.txtCategory.text = Helper.getDisplayCategory(appointment.appointmentCategory,context)

            if ( !Utilities.isNullOrEmpty(appointment.specialization) ) {
                holder.txtDoctorSpeciality.text = appointment.specialization
                holder.txtDoctorSpeciality.visibility = View.VISIBLE
            } else {
                holder.txtDoctorSpeciality.visibility = View.VISIBLE
            }

            if ( !Utilities.isNullOrEmpty(appointment.yearsOfPractice) ) {
                holder.txtDoctorExp.text = "${appointment.yearsOfPractice} ${context.resources.getString(R.string.YRS)}"
                holder.txtDoctorExp.visibility = View.VISIBLE
            } else {
                holder.txtDoctorExp.text = ""
                holder.txtDoctorExp.visibility = View.GONE
            }

            if ( !Utilities.isNullOrEmptyOrZero(appointment.consultationFees.toString()) ) {
                holder.txtDoctorFees.text = "RM ${appointment.consultationFees}"
            } else {
                holder.txtDoctorFees.text = "RM --"
            }

            if ( appointment.doctorID != null && appointment.doctorID != 0 ) {
                holder.btnViewInvoice.visibility = View.VISIBLE
            } else {
                holder.btnViewInvoice.visibility = View.INVISIBLE
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
                appointment.appointmentStatus == "CONFIRM" && appointmentDate.before(Date()) -> {
                    holder.txtAppointmentStatus.text = context.resources.getText(R.string.UNATTENDED)
                    holder.txtAppointmentStatus.setTextColor(ContextCompat.getColor(context,R.color.dark_gray))
                }
                appointment.appointmentStatus == "PENDING" && appointmentDate.before(Date()) -> {
                    holder.txtAppointmentStatus.text = context.resources.getText(R.string.UNATTENDED)
                    holder.txtAppointmentStatus.setTextColor(ContextCompat.getColor(context,R.color.dark_gray))
                }
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
                appointment.appointmentStatus == "INPROGRESS" -> {
                    holder.txtAppointmentStatus.text = ""
                }
                else -> {
                    holder.txtAppointmentStatus.text = appointment.appointmentStatus
                    //holder.txtAppointmentStatus.text = ""
                }
            }

            holder.layoutMyHistory.setOnClickListener {
                listener.onPastAppointmentSelection(position,Constants.CLICK,appointment)
            }

            holder.btnViewInvoice.setOnClickListener {
                listener.onPastAppointmentSelection(position,Constants.VIEW_INVOICE,appointment)
            }

        } catch ( e : Exception ) {
            e.printStackTrace()
        }
    }

    fun updateList( list: MutableList<ListAppointmentsModel.UpcomingAppointment> ) {
        appointmentsList.clear()
        appointmentsList.addAll(list)
        notifyDataSetChanged()
    }

    interface OnPastAppointmentClickListener {
        fun onPastAppointmentSelection(position: Int,action:String,appt:ListAppointmentsModel.UpcomingAppointment)
    }

    inner class MyHistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemMyHistoryBinding.bind(view)
        val imgOption = binding.imgOption
        val layoutMyHistory = binding.layoutMyHistory
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

        val btnViewInvoice = binding.btnViewInvoice
        val imgMode = binding.imgMode
        //val txtMode = binding.txtMode
    }

}
