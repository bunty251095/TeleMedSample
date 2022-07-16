package com.vivant.telemedicine.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vivant.telemedicine.R
import com.vivant.telemedicine.common.DateHelper
import com.vivant.telemedicine.common.Helper
import com.vivant.telemedicine.common.Utilities
import com.vivant.telemedicine.databinding.ItemSharedDocumentBinding
import com.vivant.telemedicine.model.ListAppointmentsModel

class SharedDocumentAdapter(val context: Context,
                              val listener : OnHealthRecordClickListener) : RecyclerView.Adapter<SharedDocumentAdapter.SharedDocumentViewHolder>() {

    //private var selectedPos = -1
    private var healthRecordsList: MutableList<ListAppointmentsModel.SharedDocument> = mutableListOf()

    override fun getItemCount(): Int = healthRecordsList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SharedDocumentViewHolder =
        SharedDocumentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_shared_document,parent,false))

    @SuppressLint("RecyclerView")
    override fun onBindViewHolder(holder: SharedDocumentAdapter.SharedDocumentViewHolder, position: Int) {
        try {
            val record = healthRecordsList[position]

            holder.txtDate.text = DateHelper.formatDateValue(DateHelper.DATEFORMAT_DDMMMYYYY_NEW,record.sharedDate.split("T").toTypedArray()[0])
            holder.txtRecordName.text = record.fileName
            holder.txtRecordType.text = Helper.getDocumentDescFromCode(record.documentTypeCode,context)

            holder.imgRemove.setOnClickListener {
                listener.onHealthRecordClick(position,"",record)
                //notifyItemChanged(selectedPos)
                //notifyItemChanged(selectedPos)
            }
        } catch ( e : Exception ) {
            e.printStackTrace()
        }
    }

    fun updateList( list: MutableList<ListAppointmentsModel.SharedDocument> ) {
        //Utilities.printData("HealthRecordsList",list,true)
        healthRecordsList.clear()
        healthRecordsList.addAll(list)
        healthRecordsList.sortByDescending { it.sharedDate }
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        if ( position != -1 ) {
            Utilities.printLogError("Removed_Item_At--->$position")
            this.healthRecordsList.removeAt(position)
            this.notifyItemRemoved(position)
            this.notifyDataSetChanged()
            Utilities.printLogError("NotificationsListCount--->${healthRecordsList.size}")
        }
    }

    interface OnHealthRecordClickListener {
        fun onHealthRecordClick(position: Int,action:String,rec: ListAppointmentsModel.SharedDocument)
    }

    inner class SharedDocumentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemSharedDocumentBinding.bind(view)
        val layoutRecord = binding.layoutRecord
        val imgRemove = binding.imgRemove
        val txtDate = binding.txtDate
        val txtRecordName = binding.txtRecordName
        val txtRecordType = binding.txtRecordType
    }

}
