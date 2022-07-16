package com.vivant.telemedicine.common

import com.vivant.telemedicine.R
import android.app.Activity
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import timber.log.Timber
import java.io.File
import java.math.BigDecimal

object Utilities {

    private val gson: Gson = GsonBuilder().create()
    private val prettyGson: Gson = GsonBuilder().setPrettyPrinting().create()

    fun toastMessageLong(context: Context?, message: String) {
        try {
            if (context != null && !isNullOrEmpty(message)) {
                val toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
                //toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun toastMessageShort(context: Context?, message: String) {
        try {
            if (context != null && !isNullOrEmpty(message)) {
                val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
                //toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun printLog(message: String) {
        Timber.i(message)
    }

    fun printLogError(message: String) {
        Timber.e(message)
    }

    fun printData(tag: String, list: Any, toPretty: Boolean = true) {
        try {
            var jsonArrayString: String = if (toPretty) {
                prettyGson.toJson(list)
            } else {
                gson.toJson(list)
            }
            jsonArrayString = jsonArrayString.replace("\\\"", "\"")
                .replace("\\r\\n", "")
                .replace("\\\\r\\\\n", "")
                .replace("\\\\\"", "\"")
                .replace("\"{", "{")
                .replace("\"[", "[")
                .replace("}\"", "}")
                .replace("]\"", "]")
            printLog("$tag---> $jsonArrayString")
        } catch ( e : Exception ) {
            e.printStackTrace()
        }
    }

    fun isNullOrEmpty(data: String?): Boolean {
        var result = false
        try {
            result = data == null || data == ""
                    || data.equals("null", ignoreCase = true)
                    || data == "." || data.trim { it <= ' ' }.isEmpty()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return result
    }

    fun isNullOrEmptyOrZero(data: String?): Boolean {
        var result = false
        try {
            result = data == null || data == "" || data.equals(
                "null",
                ignoreCase = true
            ) || data == "." || data == "0" || data == "0.0"
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return result
    }

    fun hideKeyboard(activity: Activity) {
        try {
            val view = activity.findViewById<View>(android.R.id.content)
            if (view != null) {
                val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun hideKeyboard(view: View, context: Context) {
        try {
            val view = view.findViewById<View>(android.R.id.content)
            if (view != null) {
                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun hideSoftInput(activity: Activity) {
        var view = activity.currentFocus
        if (view == null) view = View(activity)
        val imm = activity
            .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showKeyboard(view: View, context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT
            /* WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE*/)
    }

    fun getFileExt(fileName: String): String {
        var extension = ""
        if( !isNullOrEmpty(fileName) ) {
            val index = fileName.lastIndexOf(".")
            if (index == -1) {
                return ""
            }
            val strFileEXT = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length)
            extension = strFileEXT.trim { it <= ' ' }
        }
        printLogError("Extension : $extension")
        return extension
    }

    fun findTypeOfDocument(fileName: String): String {
        val extension = getFileExt(fileName)
        printLogError("Extension : $extension")
        var documentType = "Unknown"
        // before uploading verifing it's extension
        if (extension.equals("JPEG", ignoreCase = true)
            || extension.equals("jpg", ignoreCase = true)
            || extension.equals("PNG", ignoreCase = true)
            || extension.equals("png", ignoreCase = true)) {
            documentType = "IMAGE"
        } else if (extension.equals("PDF", ignoreCase = true)
            || extension.equals("pdf", ignoreCase = true)) {
            documentType = "PDF"
        } else if (extension.equals("DOC", ignoreCase = true)
            || extension.equals("doc", ignoreCase = true)
            || extension.equals("docx", ignoreCase = true)
            || extension.equals("DOCX", ignoreCase = true)) {
            documentType = "DOC"
        }
        return documentType
    }

    fun getDocumentTypeFromExt(extension:String) : String {
        var documentType = ""
        if (extension.equals("JPEG", ignoreCase = true) ||
            extension.equals("jpg", ignoreCase = true) ||
            extension.equals("PNG", ignoreCase = true) ||
            extension.equals("png", ignoreCase = true)) {
            documentType = "IMAGE"
        } else if (extension.equals("PDF", ignoreCase = true) || extension.equals("pdf", ignoreCase = true)) {
            documentType = "PDF"
        } else if (extension.equals("txt", ignoreCase = true) ||
            extension.equals("doc", ignoreCase = true) ||
            extension.equals("docx", ignoreCase = true)) {
            documentType = "DOC"
        }
        return documentType
    }

    fun isAcceptableDocumentType(extension:String) : Boolean {
        var isAcceptable = false
        var documentType = ""
        //val extension1 = RealPathUtil.getFileExt(filePath)
        printLogError("Extension : $extension")

        if (extension.equals("JPEG", ignoreCase = true) ||
            extension.equals("jpg", ignoreCase = true) ||
            extension.equals("PNG", ignoreCase = true) ||
            extension.equals("png", ignoreCase = true)) {
            documentType = "IMAGE"
        } else if (extension.equals("PDF", ignoreCase = true) || extension.equals("pdf", ignoreCase = true)) {
            documentType = "PDF"
        }
/*        else if (extension.equals("txt", ignoreCase = true) ||
            extension.equals("doc", ignoreCase = true) ||
            extension.equals("docx", ignoreCase = true)) {
            documentType = "DOC"
        }*/
        printLogError("documentType : $documentType")
        if ( !isNullOrEmpty(documentType) ) {
            isAcceptable = true
        }
        return isAcceptable
    }

    fun deleteFileFromLocalSystem(Path: String) {
        val file = File(Path)
        if ( file.exists() ) {
            val isDeleted = file.delete()
            printLogError("isDeleted--->$isDeleted")
        } else {
            printLogError("File not exist")
        }
    }

    fun deleteFile(file: File) {
        if ( file.exists() ) {
            val isDeleted = file.delete()
            printLogError("isDeleted--->$isDeleted")
        } else {
            printLogError("File not exist")
        }
    }

    fun roundOffPrecision(value: Double, precision: Int): Double {
        val scale = Math.pow(10.0, precision.toDouble()).toInt()
        return Math.round(value * scale).toDouble() / scale
    }

    fun round(d: Double, decimalPlace: Int): Float {
        try {
            var bd = BigDecimal(java.lang.Double.toString(d))
            bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP)
            return bd.toFloat()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return d.toFloat()
    }

    fun getAppFolderLocation(context: Context) : String {
        var appFolderLocation = Constants.primaryStorage + "/" + context.resources.getString(R.string.app_name)
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val lmn = context.externalMediaDirs
                for ( i in lmn ) {
                    if ( i.absolutePath.contains(context.packageName) ) {
                        printLogError(i.absolutePath)
                        appFolderLocation = i.absolutePath + "/" + context.resources.getString(R.string.app_name)
                        break
                    }
                }
            }
            val dir = File(appFolderLocation)
            if (!dir.exists()) {
                val directoryCreated = dir.mkdirs()
                printLogError("DirectoryCreated--->$directoryCreated")
                printLogError("DirectoryName--->$dir")
            } else {
                printLogError("DirectoryAlreadyExist")
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return appFolderLocation
    }

    fun getAppVersion(context: Context): Int {
        return try {
            val packageInfo: PackageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            throw RuntimeException("Could not get package name: $e")
        }
    }

    fun getVersionName(context: Context): String {
        return try {
            val packageInfo: PackageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            throw RuntimeException("Could not get package name: $e")
        }
    }

    fun showFullImageWithBitmap(bitmap: Bitmap, context:Context, isImage:Boolean) {
        try {
            val  dialog = DialogFullScreenView(context,isImage,"",bitmap)
            dialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showFullImageWithImgUrl(imgUrl:String,context:Context,isImage:Boolean) {
        try {
            val  dialog = DialogFullScreenView(context,isImage,imgUrl,null)
            dialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun resetPreferences( preferenceUtils: PreferenceUtils ) {
        preferenceUtils.storeBooleanPreference(PreferenceConstants.IS_LOGIN, false)
        preferenceUtils.storePreference(PreferenceConstants.TOKEN,"")
        preferenceUtils.storePreference(PreferenceConstants.PERSONID,"")
        preferenceUtils.storePreference(PreferenceConstants.ACCOUNTID,"")
        preferenceUtils.storePreference(PreferenceConstants.CUSTOMERID,"")
        preferenceUtils.storePreference(PreferenceConstants.FIRSTNAME,"")
        preferenceUtils.storePreference(PreferenceConstants.EMAIL,"")
        preferenceUtils.storePreference(PreferenceConstants.PHONE,"")
        preferenceUtils.storePreference(PreferenceConstants.DOB,"")
        preferenceUtils.storePreference(PreferenceConstants.GENDER,"")
        //preferenceUtils.storePreference(PreferenceConstants.FCM_REGISTRATION_ID,"")
    }

}