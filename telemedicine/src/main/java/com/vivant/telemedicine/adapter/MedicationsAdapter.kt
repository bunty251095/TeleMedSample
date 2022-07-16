package com.vivant.telemedicine.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vivant.telemedicine.R
import com.vivant.telemedicine.databinding.ItemSubPointBinding
import com.vivant.telemedicine.model.ListConsultationModel

class MedicationsAdapter(val context: Context ) : RecyclerView.Adapter<MedicationsAdapter.MedicationsViewHolder>() {

    private var notificationsList: MutableList<ListConsultationModel.Medicine> = mutableListOf()

    override fun getItemCount(): Int = notificationsList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicationsViewHolder =
        MedicationsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_sub_point,parent,false))

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MedicationsViewHolder, position: Int) {
        val notification = notificationsList[position]
        //holder.txtPoint.text = notification.medicinesDetail.toString()
        holder.txtPoint.text = "${notification.drugName} for ${notification.duration} ${notification.durationIn} ${notification.instruction}"
    }

    fun updateList( list: MutableList<ListConsultationModel.Medicine> ) {
        //Utilities.printLogError("NotificationsListCount--->${list.size}")
        notificationsList.clear()
        notificationsList.addAll(list)
        notifyDataSetChanged()
    }

    inner class MedicationsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemSubPointBinding.bind(view)
        val txtPoint = binding.txtPoint
    }

}
