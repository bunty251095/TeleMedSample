package com.vivant.telemedicine.ui.appointmentDetails

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vivant.telemedicine.R
import com.vivant.telemedicine.common.DateHelper
import com.vivant.telemedicine.common.DialogHelper
import com.vivant.telemedicine.common.Helper
import com.vivant.telemedicine.common.Utilities
import com.vivant.telemedicine.databinding.BottomSheetUploadNewDocumentBinding
import com.vivant.telemedicine.model.RecordInSession
import java.text.SimpleDateFormat
import java.util.*

class UploadNewDocumentBottomSheet(val recordInSession : RecordInSession,var listener: OnSaveRecordClickListener) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetUploadNewDocumentBinding
    private var selectedDate = DateHelper.currentDateAsStringyyyyMMdd
    private val cal = Calendar.getInstance()
    private val df1 = SimpleDateFormat(DateHelper.SERVER_DATE_YYYYMMDD, Locale.ENGLISH)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = BottomSheetUploadNewDocumentBinding.inflate(inflater, container, false)
        initialise()
        setClickable()
        return binding.root
    }

    private fun initialise() {

        val filePath = recordInSession.Path
        val fileName = recordInSession.Name
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
                    binding.imgSelectedDoc.setImageResource(R.drawable.icon_file_unknown)
                } else {
                    binding.imgSelectedDoc.setImageBitmap(bitmap)
                    binding.imgSelectedDoc.setOnClickListener {
                        //Helper.viewImage(context,recordInSession)
                        Utilities.showFullImageWithBitmap(bitmap,requireContext(),true)
                    }
                }
            }
        } else if (type.equals("PDF", ignoreCase = true)) {
            binding.imgSelectedDoc.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.img_pdf))
            binding.imgSelectedDoc.setOnClickListener {
                Helper.viewRecord(requireContext(),recordInSession)
            }
        }

    }

    private fun setClickable() {

        binding.txtDate.text = DateHelper.currentDateAsStringddMMMyyyyNew

        binding.imgCancelDoc.setOnClickListener {
            dismiss()
            Utilities.deleteFileFromLocalSystem(recordInSession.Path+"/"+recordInSession.Name)
        }

        binding.cardDate.setOnClickListener {
            showDatePicker()
        }

        binding.btnSave.setOnClickListener {
            dismiss()
            listener.onSaveRecordClick(selectedDate)
        }

    }

    private fun showDatePicker() {
        try {
            cal.time = df1.parse(selectedDate)!!
            DialogHelper().showDatePickerDialog(resources.getString(R.string.SELECT_DATE),
                requireContext(),cal, Calendar.getInstance(),null,

                object : DialogHelper.DateListener {

                    override fun onDateSet(date: String, year: String, month: String, dayOfMonth: String) {
                        selectedDate = DateHelper.convertDateSourceToDestination(date, DateHelper.DISPLAY_DATE_DDMMMYYYY, DateHelper.SERVER_DATE_YYYYMMDD)
                        Utilities.printLogError("SelectedDate--->$selectedDate")
                        //val date = year + "-" + (month + 1) + "-" + dayOfMonth
                        binding.txtDate.text = DateHelper.formatDateValue(DateHelper.DATEFORMAT_DDMMMYYYY_NEW, date)
                    }
                })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getTheme(): Int {
        //return super.getTheme();
        return R.style.BottomSheetDialog
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //return super.onCreateDialog(savedInstanceState);
        return BottomSheetDialog(requireContext(), theme)
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }


    interface OnSaveRecordClickListener {
        fun onSaveRecordClick(selectedDate: String)
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        Utilities.deleteFileFromLocalSystem(recordInSession.Path+"/"+recordInSession.Name)
    }

}