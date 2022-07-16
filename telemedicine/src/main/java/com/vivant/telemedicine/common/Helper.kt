package com.vivant.telemedicine.common

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import com.vivant.telemedicine.R
import com.vivant.telemedicine.model.ChooseStepModel
import com.vivant.telemedicine.model.RecordInSession
import java.io.File
import java.util.*

object Helper {

    private val fileUtils = FileUtils

    fun getDisplayCategory( cat : String , context: Context ) : String {
        var category = ""
        when( cat ) {
            "TELE" -> category = context.resources.getString(R.string.SCHEDULE_APPOINTMENT2)
            "CON" -> category = context.resources.getString(R.string.CONSULT_NOW2)
        }
        return category
    }

    fun getGenderDisplayValue( gen : String , context: Context ) : String {
        var gender = ""
        when( gen ) {
            "M","Male" -> gender = context.resources.getString(R.string.MALE)
            "F","Female" -> gender = context.resources.getString(R.string.FEMALE)
        }
        return gender
    }

    fun getGenderValueApi( gen : String ) : String {
        var gender = ""
        when( gen ) {
            "Male" -> gender = "M"
            "Female" -> gender = "F"
        }
        return gender
    }

    fun getModeForApi( consultationMode : String ) : String {
        var mode = ""
        when( consultationMode ) {
            "VIDEO" -> mode = "V"
            "AUDIO" -> mode = "A"
            "CHAT" -> mode = "C"
        }
        return mode
    }

    fun getDisplayMode( consultationMode : String , context: Context ) : String {
        var mode = ""
        when( consultationMode ) {
            "VIDEO" -> mode = context.resources.getString(R.string.VIDEO_CALL)
            "AUDIO" -> mode = context.resources.getString(R.string.AUDIO_CALL)
            "CHAT" -> mode = context.resources.getString(R.string.TEXT_CHAT)
        }
        return mode
    }

    fun getModeImage( consultationMode : String ) : Int {
        var mode = 0
        when( consultationMode ) {
            "VIDEO" -> mode = R.drawable.img_btn_video_call
            "AUDIO" -> mode = R.drawable.img_btn_audio_call
            "CHAT" -> mode = R.drawable.img_btn_text_chat
        }
        return mode
    }

    fun getModeImageHistory( consultationMode : String ) : Int {
        var mode = 0
        when( consultationMode ) {
            "VIDEO" -> mode = R.drawable.img_video_call
            "AUDIO" -> mode = R.drawable.img_audio_call
            "CHAT" -> mode = R.drawable.img_text_chat
        }
        return mode
    }

    fun getDocumentTypeDescForApi( code : String ) : String {
        var desc = ""
        when( code ) {
            "PRE" -> desc = "Prescription"
            "LAB" -> desc = "Lab Report"
        }
        return desc
    }

    fun getRatingText( rating : Int,context: Context ) : String {
        var mode = ""
        when( rating ) {
            1 -> mode = context.resources.getString(R.string.BAD)
            2 -> mode = context.resources.getString(R.string.IMPROVEMENT_NEEDED)
            3 -> mode = context.resources.getString(R.string.FAIR)
            4 -> mode = context.resources.getString(R.string.GOOD)
            5 -> mode = context.resources.getString(R.string.EXCELLENT)
        }
        return mode
    }

/*    fun getModePriceConsultNow( consultationMode : String ) : String {
        var mode = ""
        when( consultationMode ) {
            "VIDEO" -> mode = "20"
            "AUDIO" -> mode = "10"
            "CHAT" -> mode = "5"
        }
        return mode
    }*/

    fun getWeekdayCodeForApi( date : String ) : Int {
        var weekDayCode = 0
        val dayName = DateHelper.convertDateToDayName(date,"yyyy-MM-dd")
        Utilities.printLogError("dayName--->$dayName")
        when(dayName) {
            "Sun" -> weekDayCode = 0
            "Mon" -> weekDayCode = 1
            "Tue" -> weekDayCode = 2
            "Wed" -> weekDayCode = 3
            "Thu" -> weekDayCode = 4
            "Fri" -> weekDayCode = 5
            "Sat" -> weekDayCode = 6
        }
        return weekDayCode
    }

    fun getDocumentDescFromCode( code : String, context: Context ) : String {
        var recordDesc = ""
        when( code ) {
            "PRE" -> recordDesc = context.resources.getString(R.string.PRESCRIPTION)
            "LAB" -> recordDesc = context.resources.getString(R.string.LAB_REPORT)
            "HRAREPORT" -> recordDesc = context.resources.getString(R.string.HRA_REPORT)
        }
        return recordDesc
    }

    fun getScheduleAppointmentStepList(context: Context): MutableList<ChooseStepModel> {

        val stepsList: MutableList<ChooseStepModel> = mutableListOf()
        stepsList.add(ChooseStepModel(R.drawable.img_choose_mode,R.drawable.img_choose_mode_disabled,context.getString(R.string.STEP_CHOOSE_MODE),true))
        stepsList.add(ChooseStepModel(R.drawable.img_choose_specialization,R.drawable.img_choose_specialization_disabled,context.getString(R.string.STEP_CHOOSE_SPECIALIZATION),false))
        stepsList.add(ChooseStepModel(R.drawable.img_choose_doctor,R.drawable.img_choose_doctor_disabled,context.getString(R.string.STEP_CHOOSE_DOCTOR),false))
        stepsList.add(ChooseStepModel(R.drawable.img_choose_slots,R.drawable.img_choose_slots,context.getString(R.string.STEP_CHOOSE_SLOT),false))
        stepsList.add(ChooseStepModel(R.drawable.img_describe_symptoms,R.drawable.img_describe_symptoms,context.getString(R.string.STEP_DESCRIBE_SYMPTOMS),false))
        stepsList.add(ChooseStepModel(R.drawable.img_confirm_appointment,R.drawable.img_confirm_appointment,context.getString(R.string.STEP_CONFIRM_APPOINTMENT),false))

        return stepsList
    }

    fun getConsultNowStepList(context: Context): MutableList<ChooseStepModel> {

        val stepsList: MutableList<ChooseStepModel> = mutableListOf()
        stepsList.add(ChooseStepModel(R.drawable.img_choose_mode,R.drawable.img_choose_mode_disabled,context.getString(R.string.STEP_CHOOSE_MODE),true))
        stepsList.add(ChooseStepModel(R.drawable.img_choose_specialization,R.drawable.img_choose_specialization_disabled,context.getString(R.string.STEP_CHOOSE_SPECIALIZATION),false))
        stepsList.add(ChooseStepModel(R.drawable.img_describe_symptoms2,R.drawable.img_describe_symptoms2_disabled,context.getString(R.string.STEP_DESCRIBE_SYMPTOMS),false))
        stepsList.add(ChooseStepModel(R.drawable.img_confirm_appointment2,R.drawable.img_confirm_appointment2_disabled,context.getString(R.string.STEP_CONFIRM_APPOINTMENT),false))

        return stepsList
    }

    fun viewImage(context: Context,recordData : RecordInSession) {
        try {
            val recordName = recordData.Name
            val recordPath = recordData.Path
            val file = File(recordPath , recordName)
            if (file.exists()) {
                val type = "image/*"
                val intent = Intent(Intent.ACTION_VIEW)
                val uri = Uri.fromFile(file)
                intent.setDataAndType(uri, type)
                //intent.setDataAndType(FileProvider.getUriForFile(this, getPackageName().toString() + ".provider", file), type)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
                try {
                    context.startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Utilities.toastMessageShort(context,context.resources.getString(R.string.ERROR_UNABLE_TO_OPEN_FILE))
                }
            } else {
                Utilities.toastMessageShort(context,context.resources.getString(R.string.ERROR_FILE_DOES_NOT_EXIST))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun viewRecord(context: Context,recordData : RecordInSession) {
        //val localResource = LocaleHelper.getLocalizedResources(context, Locale(LocaleHelper.getLanguage(context)!!))!!
        val recordName = recordData.Name
        val recordPath = recordData.Path
        val recordType = recordData.Type
        val type: String
        when {
            recordType.equals("IMAGE", ignoreCase = true) -> {
                type = "image/*"
            }
            recordType.equals("DOC", ignoreCase = true) -> {
                type = "application/msword"
            }
            recordType.equals("PDF", ignoreCase = true) -> {
                type = "application/pdf"
            }
            fileUtils.getFileExt(recordName).equals("txt", ignoreCase = true) -> {
                type = "text/*"
            }
            else -> {
                type = "application/pdf"
            }
        }
        if (!type.equals("", ignoreCase = true)) {
            val file = File(recordPath , recordName)
            //val file = DocumentFile.fromTreeUri(context, recordData.FileUri.toUri())!!
            if (file.exists()) {
                openDownloadedFile(context,file,type )
            }
        }
    }

    private fun openDownloadedFile(context: Context,file : File, type :String) {
        //val localResource = LocaleHelper.getLocalizedResources(context, Locale(LocaleHelper.getLanguage(context)!!))!!
        try {
            val uri = FileProvider.getUriForFile(context,context.packageName + ".provider",file)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(uri,type)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
            //val openIntent = Intent.createChooser(intent,"Open using")
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
            Utilities.toastMessageShort(context,context.resources.getString(R.string.ERROR_NO_APPLICATION_TO_VIEW_PDF))
        } catch (e: Exception) {
            e.printStackTrace()
            Utilities.toastMessageShort(context,context.resources.getString(R.string.ERROR_UNABLE_TO_OPEN_FILE))
        }
    }

}