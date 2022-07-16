package com.vivant.telemedicine.adapter

import com.vivant.telemedicine.R
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.vivant.telemedicine.common.ConsultAndScheduleSingleton
import com.vivant.telemedicine.common.Helper
import com.vivant.telemedicine.common.Utilities
import com.vivant.telemedicine.databinding.ItemUploadRecordBinding
import com.vivant.telemedicine.model.RecordInSession

class UploadRecordAdapter(val type : String,
                          val context: Context,
                          val listener: OnRecordsUpdateListener) : RecyclerView.Adapter<UploadRecordAdapter.UploadRecordViewHolder>() {

    private val consultAndScheduleSingleton = ConsultAndScheduleSingleton.instance!!
    internal var uploadRecordList: MutableList<RecordInSession> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UploadRecordViewHolder =
        UploadRecordViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_upload_record, parent, false))

    override fun getItemCount(): Int = uploadRecordList.size

    override fun onBindViewHolder(holder: UploadRecordViewHolder, position: Int) {
        val recordInSession = uploadRecordList[position]
        holder.bindTo(recordInSession)
    }

    fun updateList(list: MutableList<RecordInSession>) {
        Utilities.printData("UploadDataList", list)
        this.uploadRecordList.clear()
        this.uploadRecordList.addAll(list)
        this.notifyDataSetChanged()
    }

    fun insertItem(item: RecordInSession, position: Int) {
        Utilities.printLogError("Inserted_Item_At--->$position")
        this.uploadRecordList.add(item)
        consultAndScheduleSingleton.addRecordInSession(item)
        this.notifyItemInserted(position)
    }

    private fun removeItem(position: Int,item:RecordInSession) {
        Utilities.printLogError("Removed_Item_At--->$position")
        this.uploadRecordList.removeAt(position)
        consultAndScheduleSingleton.removeRecordInSession(item)
        this.notifyItemRemoved(position)
        this.notifyDataSetChanged()
    }

    interface OnRecordsUpdateListener {
        fun onRecordsUpdated(size: Int)
    }

    inner class UploadRecordViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemUploadRecordBinding.bind(view)
        private val imgCancelDoc = binding.imgCancelDoc
        private val imgSelectedDoc = binding.imgSelectedDoc
        private val txtRecordName = binding.txtRecordName

        fun bindTo(recordInSession: RecordInSession) {
            imgCancelDoc.tag = position
            txtRecordName.text = recordInSession.OriginalFileName
            if ( recordInSession != null ) {
                val filePath = recordInSession.Path
                val fileName = recordInSession.OriginalFileName
                val type = recordInSession.Type
                val completeFilePath = "$filePath/$fileName"
                if (type.equals("IMAGE", ignoreCase = true)) {
                    if ( !completeFilePath.equals("", ignoreCase = true)) {

                        var bitmap: Bitmap? = null
                        try {
                            bitmap = BitmapFactory.decodeFile(completeFilePath)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        if (bitmap == null) {
                            imgSelectedDoc.setImageResource(R.drawable.icon_file_unknown)
                        } else {
                            imgSelectedDoc.setImageBitmap(bitmap)
                            imgSelectedDoc.setOnClickListener {
                                //Helper.viewImage(context,recordInSession)
                                Utilities.showFullImageWithBitmap(bitmap,context,true)
                            }
                        }
                    }
                } else if (type.equals("PDF", ignoreCase = true)) {
                    imgSelectedDoc.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.img_pdf))
                    imgSelectedDoc.setOnClickListener {
                        Helper.viewRecord(context,recordInSession)
                    }
                }
            }

            imgCancelDoc.setOnClickListener {
                try {
                    if (uploadRecordList.size != 0 && uploadRecordList.size >= position) {
                        removeItem(position,recordInSession)
                        Utilities.deleteFileFromLocalSystem(recordInSession.Path+"/"+recordInSession.Name)
                    }
                    listener.onRecordsUpdated(uploadRecordList.size)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

}