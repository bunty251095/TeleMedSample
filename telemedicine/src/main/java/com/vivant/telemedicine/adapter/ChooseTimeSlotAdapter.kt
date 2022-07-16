package com.vivant.telemedicine.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.vivant.telemedicine.R
import com.vivant.telemedicine.common.DateHelper
import com.vivant.telemedicine.databinding.ItemTimeSlotBinding
import com.vivant.telemedicine.model.GetDoctorSlotsModel

class ChooseTimeSlotAdapter(val context: Context,
                            val type:String,
                            private val listener: OnTimeSlotClickListener) : RecyclerView.Adapter<ChooseTimeSlotAdapter.ChooseTimeSlotViewHolder>() {

    private var selectedPos = -1
    private var timeSlotList: MutableList<GetDoctorSlotsModel.TimeSlot> = mutableListOf()

    override fun getItemCount(): Int = timeSlotList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ChooseTimeSlotViewHolder =
        ChooseTimeSlotViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_time_slot,parent,false))

    @SuppressLint("RecyclerView")
    override fun onBindViewHolder(holder: ChooseTimeSlotAdapter.ChooseTimeSlotViewHolder, position: Int) {
        val timeSlot = timeSlotList[position]

        //holder.txtTime.text = timeSlot.slot
        holder.txtTime.text = DateHelper.getTimeIn12HrFormatAmOrPm(timeSlot.slot)

        if (selectedPos == position) {
            holder.layoutTimeSlot.background = ContextCompat.getDrawable(context,R.drawable.btn_fill_selected)
            holder.txtTime.setTextColor(ContextCompat.getColor(context,R.color.white))
        } else {
            holder.layoutTimeSlot.background = ContextCompat.getDrawable(context,R.drawable.btn_fill_disabled)
            holder.txtTime.setTextColor(ContextCompat.getColor(context,R.color.textViewColor))
        }

        holder.itemView.setOnClickListener {
            listener.onTimeSlotSelection(type,timeSlot)
            notifyItemChanged(selectedPos)
            selectedPos = position
            notifyItemChanged(selectedPos)
        }
    }

    fun updateList( list: MutableList<GetDoctorSlotsModel.TimeSlot> ) {
        //Utilities.printData("TimeSlot",list,true)
        timeSlotList.clear()
        timeSlotList.addAll(list)
        notifyDataSetChanged()
    }

    fun clearAdapter() {
        timeSlotList.clear()
        notifyDataSetChanged()
    }

    fun resetList() {
        selectedPos = -1
        notifyDataSetChanged()
    }

    interface OnTimeSlotClickListener {
        fun onTimeSlotSelection(type: String, slot: GetDoctorSlotsModel.TimeSlot)
    }

    inner class ChooseTimeSlotViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemTimeSlotBinding.bind(view)
        val layoutTimeSlot = binding.layoutTimeSlot
        val txtTime = binding.txtTime
    }

}