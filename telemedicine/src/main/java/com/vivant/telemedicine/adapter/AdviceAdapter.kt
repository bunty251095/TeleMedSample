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

class AdviceAdapter(val context: Context ) : RecyclerView.Adapter<AdviceAdapter.AdviceViewHolder>() {

    private var notificationsList: MutableList<ListConsultationModel.Advice> = mutableListOf()

    override fun getItemCount(): Int = notificationsList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdviceViewHolder =
        AdviceViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_sub_point,parent,false))

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AdviceViewHolder, position: Int) {
        val notification = notificationsList[position]
        holder.txtPoint.text = notification.advice
    }

    fun updateList( list: MutableList<ListConsultationModel.Advice> ) {
        //Utilities.printLogError("NotificationsListCount--->${list.size}")
        notificationsList.clear()
        notificationsList.addAll(list)
        notifyDataSetChanged()
    }

    inner class AdviceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemSubPointBinding.bind(view)
        val txtPoint = binding.txtPoint
    }

}

/*
class AdviceAdapter {
}*/
