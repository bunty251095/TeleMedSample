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
import com.vivant.telemedicine.databinding.ItemUpcomingAppointmentsBinding
import com.vivant.telemedicine.model.ListAppointmentsModel
import java.text.SimpleDateFormat
import java.util.*

class UpcomingAppointmentsAdapter( val context: Context,
                                   val listener: OnUpcomingAppointmentClickListener,
                                   val showOption: Boolean) : RecyclerView.Adapter<UpcomingAppointmentsAdapter.UpcomingAppointmentsViewHolder>() {

    private var appointmentsList: MutableList<ListAppointmentsModel.UpcomingAppointment> = mutableListOf()
    private val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
    private val currentTime = dateFormat.parse(dateFormat.format(Date()))
    private lateinit var slotTime : Date
    private var differenceInMinutes = 0
    private val currentDate = DateHelper.currentDateAsStringyyyyMMdd
    private lateinit var appointmentDate :Date

    override fun getItemCount(): Int = appointmentsList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingAppointmentsViewHolder =
        UpcomingAppointmentsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_upcoming_appointments,parent,false))

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: UpcomingAppointmentsAdapter.UpcomingAppointmentsViewHolder, position: Int) {
        try {
            val appointment = appointmentsList[position]
            appointmentDate = DateHelper.convertStringDateToDate(appointment.appointmentDate,"yyyy-MM-dd")

            if ( showOption ) {
                holder.imgOption.visibility = View.VISIBLE
            } else {
                holder.imgOption.visibility = View.GONE
            }

            holder.txtDateTime.text = "${DateHelper.formatDateValue(DateHelper.DATEFORMAT_DDMMMYYYY_NEW,appointment.appointmentDate)}\n${DateHelper.getTimeIn12HrFormatAmOrPm(appointment.appointmentStartTime!!)}"
            if ( !Utilities.isNullOrEmpty(appointment.firstName) ) {
                holder.txtDoctorName.text = "${context.resources.getString(R.string.DR)} ${appointment.firstName}"
                holder.txtDoctorName.visibility = View.VISIBLE
            } else {
                holder.txtDoctorName.text = ""
                holder.txtDoctorName.visibility = View.GONE
            }

            holder.txtCategory.text = Helper.getDisplayCategory(appointment.appointmentCategory,context)

            holder.txtDoctorSpeciality.text = appointment.specialization

            if ( !Utilities.isNullOrEmpty(appointment.yearsOfPractice) ) {
                holder.txtDoctorExp.text = "${appointment.yearsOfPractice} ${context.resources.getString(R.string.YRS)}"
                holder.txtDoctorExp.visibility = View.VISIBLE
            } else {
                holder.txtDoctorExp.text = ""
                holder.txtDoctorExp.visibility = View.GONE
            }

            if ( !Utilities.isNullOrEmpty(appointment.profileImage.toString()) ) {
                if ( !Utilities.isNullOrEmpty(appointment.profileImage.fileBytes) ) {
                    val abc = Base64.decode(appointment.profileImage.fileBytes, Base64.DEFAULT)
                    holder.imgDoctor.setImageBitmap(BitmapFactory.decodeByteArray(abc,0,abc.size) )
                } else {
                    holder.imgDoctor.setImageResource(R.drawable.img_my_profile)
                }
            }

            holder.txtDoctorFees.text = "RM ${appointment.consultationFees}"

            //Utilities.printLogError("AppointmentStatus--->${appointment.appointmentStatus}")
            when (appointment.appointmentStatus) {
                "PENDING" -> {
                    holder.txtAppointmentStatus.text = context.resources.getText(R.string.NOT_CONFIRMED)
                    holder.txtAppointmentStatus.setTextColor(ContextCompat.getColor(context,R.color.state_error))
                }
                "CONFIRM" -> {
                    holder.txtAppointmentStatus.text = context.resources.getText(R.string.CONFIRMED)
                    holder.txtAppointmentStatus.setTextColor(ContextCompat.getColor(context,R.color.dark_green))
                }
                "RES" -> {
                    holder.txtAppointmentStatus.text = context.resources.getText(R.string.NOT_CONFIRMED)
                    holder.txtAppointmentStatus.setTextColor(ContextCompat.getColor(context,R.color.state_error))
                }
                "CAN" -> {
                    holder.txtAppointmentStatus.text = context.resources.getText(R.string.CANCELLED)
                    holder.txtAppointmentStatus.setTextColor(ContextCompat.getColor(context,R.color.state_error))
                    holder.btnAttachDocument.visibility = View.INVISIBLE
                    holder.btnMode.visibility = View.INVISIBLE
                    holder.imgOption.visibility = View.GONE
                }
                "ACT" -> {
                    holder.txtAppointmentStatus.text = ""
                }
                "NOTACCEPTED" -> {
                    holder.txtAppointmentStatus.text = ""
                    holder.btnAttachDocument.visibility = View.INVISIBLE
                    holder.btnMode.visibility = View.INVISIBLE
                    holder.imgOption.visibility = View.GONE
                }
                else -> {
                    holder.txtAppointmentStatus.text = appointment.appointmentStatus
                    //holder.txtAppointmentStatus.text = ""
                }
            }

            holder.txtMode.text = Helper.getDisplayMode(appointment.appointmentMode,context)
            holder.imgMode.setImageResource(Helper.getModeImage(appointment.appointmentMode))

            slotTime = dateFormat.parse(appointment.appointmentStartTime)!!
            //slotTime = dateFormat.parse("14:45:00")
            differenceInMinutes = ((currentTime!!.time - slotTime.time) / (60 * 1000)).toInt()
            //Utilities.printLogError("differenceInMinutes--->$differenceInMinutes")
            //Utilities.printLogError("${appointment.appointmentStartTime}")

/*            if ( currentDate == appointment.appointmentDate && differenceInMinutes >= -15  ) {
                holder.btnMode.background = ContextCompat.getDrawable(context,R.drawable.btn_fill_selected)
                holder.btnMode.isEnabled = true
            } else {
                holder.btnMode.background = ContextCompat.getDrawable(context,R.drawable.btn_fill_disabled_light_blue)
                holder.btnMode.isEnabled = false
            }*/

            holder.layoutUpcomingAppointments.setOnClickListener {
                listener.onUpcomingAppointmentSelection(position,Constants.CLICK,appointment)
            }

            holder.imgOption.setOnClickListener {
                listener.onUpcomingAppointmentSelection(position,Constants.OPTION,appointment)
            }

            holder.btnAttachDocument.setOnClickListener {
                listener.onUpcomingAppointmentSelection(position,Constants.ATTACH_DOCUMENT,appointment)
            }

            holder.btnMode.setOnClickListener {
                listener.onUpcomingAppointmentSelection(position,Constants.MODE,appointment)
            }
        } catch ( e : Exception ) {
            e.printStackTrace()
        }
    }

    fun updateList( list: MutableList<ListAppointmentsModel.UpcomingAppointment> ) {
        //Utilities.printData("AppointmentsList",list,true)
/*        for ( i in 0 until  list.size ) {
            Utilities.printLogError("$i)--->${list[i].appointmentStatus},${list[i].sessionStartTime}")
        }*/
        appointmentsList.clear()
        appointmentsList.addAll(list)
        notifyDataSetChanged()
    }

    interface OnUpcomingAppointmentClickListener {
        fun onUpcomingAppointmentSelection(position: Int,action:String,appt:ListAppointmentsModel.UpcomingAppointment)
    }

    inner class UpcomingAppointmentsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemUpcomingAppointmentsBinding.bind(view)
        val imgOption = binding.imgOption
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

        val btnAttachDocument = binding.btnAttachDocument
        val btnMode = binding.btnMode
        val imgMode = binding.imgMode
        val txtMode = binding.txtMode
    }

}
/*                appointment.appointmentStatus == "PENDING" && appointmentDate.before(Date()) -> {
    holder.txtAppointmentStatus.text = context.resources.getText(R.string.UNATTENDED)
    holder.txtAppointmentStatus.setTextColor(ContextCompat.getColor(context,R.color.dark_gray))
    holder.btnMode.visibility = View.INVISIBLE
    holder.imgOption.visibility = View.GONE
}*/

/*                appointment.appointmentStatus == "CONFIRM" && appointmentDate.before(Date()) -> {
                    holder.txtAppointmentStatus.text = context.resources.getText(R.string.UNATTENDED)
                    holder.txtAppointmentStatus.setTextColor(ContextCompat.getColor(context,R.color.dark_gray))
                    holder.btnMode.visibility = View.INVISIBLE
                    holder.imgOption.visibility = View.GONE
                }*/