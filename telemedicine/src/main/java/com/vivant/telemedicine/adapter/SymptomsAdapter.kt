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

class SymptomsAdapter(val context: Context ) : RecyclerView.Adapter<SymptomsAdapter.SymptomsViewHolder>() {

    private var notificationsList: MutableList<ListConsultationModel.Symptom> = mutableListOf()

    override fun getItemCount(): Int = notificationsList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SymptomsViewHolder =
        SymptomsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_sub_point,parent,false))

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SymptomsViewHolder, position: Int) {
        val notification = notificationsList[position]
        holder.txtPoint.text = notification.symptom
    }

    fun updateList( list: MutableList<ListConsultationModel.Symptom> ) {
        //Utilities.printLogError("NotificationsListCount--->${list.size}")
        notificationsList.clear()
        notificationsList.addAll(list)
        notifyDataSetChanged()
    }

    inner class SymptomsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemSubPointBinding.bind(view)
        val txtPoint = binding.txtPoint
    }

}
