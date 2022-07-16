package com.vivant.telemedicine.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vivant.telemedicine.R
import com.vivant.telemedicine.common.Constants
import com.vivant.telemedicine.common.Helper
import com.vivant.telemedicine.databinding.ItemReceivedDocumentBinding
import com.vivant.telemedicine.model.ListConsultationModel

class ReceivedDocumentAdapter(val context: Context,
                            val listener : OnReceivedRecordClickListener) : RecyclerView.Adapter<ReceivedDocumentAdapter.ReceivedDocumentViewHolder>() {

    //private var selectedPos = -1
    private var healthRecordsList: MutableList<ListConsultationModel.Attachments> = mutableListOf()

    override fun getItemCount(): Int = healthRecordsList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceivedDocumentViewHolder =
        ReceivedDocumentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_received_document,parent,false))

    @SuppressLint("RecyclerView")
    override fun onBindViewHolder(holder: ReceivedDocumentAdapter.ReceivedDocumentViewHolder, position: Int) {
        try {
            val record = healthRecordsList[position]
            //holder.txtRecordType.text = Helper.getDocumentDescFromCode(record.documentTypeCode,context)
            holder.txtRecordType.text = record.relatedTo
            holder.txtRecordName.text = record.fileName

/*            holder.layoutRecord.setOnClickListener {
                listener.onReceivedRecordClick(position,Constants.CLICK,record)
            }*/

            holder.imgDownload.setOnClickListener {
                listener.onReceivedRecordClick(position,Constants.DOWNLOAD,record)
            }
        } catch ( e : Exception ) {
            e.printStackTrace()
        }
    }

    fun updateList( list: MutableList<ListConsultationModel.Attachments> ) {
        //Utilities.printData("HealthRecordsList",list,true)
        healthRecordsList.clear()
        healthRecordsList.addAll(list)
        healthRecordsList.sortByDescending { it.consultationDate }
        notifyDataSetChanged()
    }

    interface OnReceivedRecordClickListener {
        fun onReceivedRecordClick(position: Int,action:String,rec: ListConsultationModel.Attachments)
    }

    inner class ReceivedDocumentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemReceivedDocumentBinding.bind(view)
        val layoutRecord = binding.layoutRecord
        val txtRecordType = binding.txtRecordType
        val txtRecordName = binding.txtRecordName
        val imgDownload = binding.imgDownload
    }

}
