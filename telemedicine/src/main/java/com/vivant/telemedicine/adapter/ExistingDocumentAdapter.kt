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
import com.vivant.telemedicine.common.Helper
import com.vivant.telemedicine.common.Utilities
import com.vivant.telemedicine.databinding.ItemExistingDocumentBinding
import com.vivant.telemedicine.model.ListDocumentModel

class ExistingDocumentAdapter( val context: Context,
                               val listener : OnHealthRecordClickListener) : RecyclerView.Adapter<ExistingDocumentAdapter.ExistingDocumentViewHolder>() {

    private var selectedPos = -1
    private var healthRecordsList: MutableList<ListDocumentModel.HealthRelatedDocument> = mutableListOf()

    override fun getItemCount(): Int = healthRecordsList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExistingDocumentViewHolder =
        ExistingDocumentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_existing_document,parent,false))

    @SuppressLint("RecyclerView")
    override fun onBindViewHolder(holder: ExistingDocumentAdapter.ExistingDocumentViewHolder, position: Int) {
        try {
            val record = healthRecordsList[position]

            holder.txtDate.text = DateHelper.formatDateValue(DateHelper.DATEFORMAT_DDMMMYYYY_NEW,record.recordDate.split("T").toTypedArray()[0])
            holder.txtRecordName.text = record.title
            if ( !Utilities.isNullOrEmpty(record.documentTypeCode) ) {
                holder.txtRecordType.text = Helper.getDocumentDescFromCode(record.documentTypeCode,context)
            } else {
                holder.txtRecordType.text = ""
            }

            if (selectedPos == position) {
                holder.layoutRecord.background = ContextCompat.getDrawable(context,R.drawable.btn_fill_dark_green)
            } else {
                holder.layoutRecord.background = ContextCompat.getDrawable(context,R.drawable.btn_dark_green)
            }

            holder.layoutRecord.setOnClickListener {
                listener.onHealthRecordClick(position,"",record)
                notifyItemChanged(selectedPos)
                selectedPos = position
                notifyItemChanged(selectedPos)
            }
        } catch ( e : Exception ) {
            e.printStackTrace()
        }
    }

    fun updateList( list: MutableList<ListDocumentModel.HealthRelatedDocument> ) {
        Utilities.printData("HealthRecordsList",list,true)
        healthRecordsList.clear()
        healthRecordsList.addAll(list)
        notifyDataSetChanged()
    }

    interface OnHealthRecordClickListener {
        fun onHealthRecordClick(position: Int,action:String,rec:ListDocumentModel.HealthRelatedDocument)
    }

    inner class ExistingDocumentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemExistingDocumentBinding.bind(view)
        val layoutRecord = binding.layoutRecord
        val txtDate = binding.txtDate
        val txtRecordName = binding.txtRecordName
        val txtRecordType = binding.txtRecordType
    }

}