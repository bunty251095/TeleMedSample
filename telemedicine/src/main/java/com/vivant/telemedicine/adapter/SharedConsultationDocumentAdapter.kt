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
import com.vivant.telemedicine.model.ListConsultationModel

class SharedConsultationDocumentAdapter(val context: Context,
                            val listener : OnConsultationHealthRecordClickListener) : RecyclerView.Adapter<SharedConsultationDocumentAdapter.SharedConsultationDocumentViewHolder>() {

    //private var selectedPos = -1
    private var healthRecordsList: MutableList<ListConsultationModel.SharedDocument> = mutableListOf()

    override fun getItemCount(): Int = healthRecordsList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SharedConsultationDocumentViewHolder =
        SharedConsultationDocumentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_shared_document,parent,false))

    @SuppressLint("RecyclerView")
    override fun onBindViewHolder(holder: SharedConsultationDocumentAdapter.SharedConsultationDocumentViewHolder, position: Int) {
        try {
            val record = healthRecordsList[position]

            holder.txtDate.text = DateHelper.formatDateValue(DateHelper.DATEFORMAT_DDMMMYYYY_NEW,record.sharedDate.split("T").toTypedArray()[0])
            holder.txtRecordName.text = record.fileName
            holder.txtRecordType.text = Helper.getDocumentDescFromCode(record.documentTypeCode,context)

            holder.imgRemove.setOnClickListener {
                listener.onConsultationHealthRecordClick(position,"",record)
                //notifyItemChanged(selectedPos)
                //notifyItemChanged(selectedPos)
            }
        } catch ( e : Exception ) {
            e.printStackTrace()
        }
    }

    fun updateList( list: MutableList<ListConsultationModel.SharedDocument> ) {
        Utilities.printData("HealthRecordsList",list,true)
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
            Utilities.printLogError("HealthRecordsListCount--->${healthRecordsList.size}")
        }
    }

    interface OnConsultationHealthRecordClickListener {
        fun onConsultationHealthRecordClick(position: Int,action:String,rec: ListConsultationModel.SharedDocument)
    }

    inner class SharedConsultationDocumentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemSharedDocumentBinding.bind(view)
        val layoutRecord = binding.layoutRecord
        val imgRemove = binding.imgRemove
        val txtDate = binding.txtDate
        val txtRecordName = binding.txtRecordName
        val txtRecordType = binding.txtRecordType
    }

}
