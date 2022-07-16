package com.vivant.telemedicine.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vivant.telemedicine.R
import com.vivant.telemedicine.common.Constants
import com.vivant.telemedicine.common.DateHelper
import com.vivant.telemedicine.common.Helper
import com.vivant.telemedicine.common.Utilities
import com.vivant.telemedicine.databinding.ItemHealthRecordsBinding
import com.vivant.telemedicine.model.ListDocumentModel

class HealthRecordsAdapter( val context: Context,
                            val listener : OnHealthRecordClickListener) : RecyclerView.Adapter<HealthRecordsAdapter.HealthRecordsViewHolder>() {


    private var healthRecordsList: MutableList<ListDocumentModel.HealthRelatedDocument> = mutableListOf()

    override fun getItemCount(): Int = healthRecordsList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HealthRecordsViewHolder =
        HealthRecordsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_health_records,parent,false))

    override fun onBindViewHolder(holder: HealthRecordsAdapter.HealthRecordsViewHolder, position: Int) {
        try {
            val record = healthRecordsList[position]

            if ( !Utilities.isNullOrEmpty(record.documentTypeCode) ) {
                when(record.documentTypeCode) {
                    "PRE" -> holder.imgRecord.setImageResource(R.drawable.img_prescription)
                    "HRAREPORT","LAB" -> holder.imgRecord.setImageResource(R.drawable.img_lab_report)
                }
                holder.txtRecordType.text = Helper.getDocumentDescFromCode(record.documentTypeCode,context)
            } else {
                holder.imgRecord.setImageResource(R.drawable.icon_file_unknown)
                holder.txtRecordType.text = "---"
            }

            holder.txtRecordName.text = record.title
            holder.txtDate.text = DateHelper.formatDateValue(DateHelper.DATEFORMAT_DDMMMYYYY_NEW,record.recordDate.split("T").toTypedArray()[0])

            holder.imgDelete.setOnClickListener {
                listener.onHealthRecordClick(position,Constants.DELETE,record)
            }

            holder.btnViewRecord.setOnClickListener {
                listener.onHealthRecordClick(position,Constants.VIEW,record)
            }
        } catch ( e : Exception ) {
            e.printStackTrace()
        }
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

    fun updateList( list: MutableList<ListDocumentModel.HealthRelatedDocument> ) {
        //Utilities.printData("HealthRecordsList",list,true)
        healthRecordsList.clear()
        healthRecordsList.addAll(list)
        notifyDataSetChanged()
    }

    interface OnHealthRecordClickListener {
        fun onHealthRecordClick(position: Int,action:String,rec:ListDocumentModel.HealthRelatedDocument)
    }

    inner class HealthRecordsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemHealthRecordsBinding.bind(view)
        val imgDelete = binding.imgDelete
        val imgRecord = binding.imgRecord
        val btnViewRecord = binding.btnViewRecord
        val txtRecordName = binding.txtRecordName
        val txtRecordType = binding.txtRecordType
        val txtDate = binding.txtDate
    }

}