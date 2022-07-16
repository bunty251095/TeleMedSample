package com.vivant.telemedicine.common

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.navigation.ActivityNavigator
import com.vivant.telemedicine.R
import com.vivant.telemedicine.common.Event
import com.google.android.material.snackbar.Snackbar

//private val appColorHelper = AppColorHelper.instance!!

/**
 * Transforms static java function Snackbar.make() to an extension function on View.
 */
fun Fragment.showSnackbar(snackbarText: String, timeLength: Int) {
    activity?.let {
        Snackbar.make(it.findViewById<View>(android.R.id.content), snackbarText, timeLength).show()
    }
}

fun Fragment.showToast(toastText: String, timeLength: Int) {
    activity?.let {
        val toast = Toast.makeText(context, toastText, timeLength)
        toast.show()
    }
}

/**
 * Triggers a snackbar message when the value contained by snackbarTaskMessageLiveEvent is modified.
 */
fun Fragment.setupSnackbar(
    lifecycleOwner: LifecycleOwner,
    snackbarEvent: LiveData<Event<Int>>,
    timeLength: Int) {
    snackbarEvent.observe(lifecycleOwner) { event ->
        event.getContentIfNotHandled()?.let { res ->
            context?.let { showSnackbar(it.getString(res), timeLength) }
        }
    }
}

fun Activity.openAnotherActivity(destination:String,clearTop:Boolean = false,extras: Bundle.() -> Unit = {}) {
    val intent = Intent()
    intent.component = ComponentName(NavigationConstants.APPID,destination)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    if ( clearTop ) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
    }
    intent.putExtras(Bundle().apply(extras))
    startActivity(intent)
    if ( clearTop ) {
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)
    } else {
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
    }
}

fun Fragment.openAnotherActivity(destination:String,clearTop:Boolean = false,extras: Bundle.() -> Unit = {}) {
    val intent = Intent()
    intent.component = ComponentName(NavigationConstants.APPID,destination)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    if ( clearTop ) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
    }
    intent.putExtras(Bundle().apply(extras))
    startActivity(intent)
    if ( clearTop ) {
        requireActivity().overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)
    } else {
        requireActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
    }
}

/*fun Activity.openAnotherActivity(destination:String,extras:Bundle = Bundle(),clearTop:Boolean = false) {
    val intent = Intent()
    intent.component = ComponentName(NavigationConstants.APPID,destination)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    if ( clearTop ) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
    }
    intent.putExtras(extras)
    startActivity(intent)
    if ( clearTop ) {
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)
    } else {
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
    }
}

fun Fragment.openAnotherActivity(destination:String,extras: Bundle = Bundle(),clearTop:Boolean = false) {
    val intent = Intent()
    intent.component = ComponentName(NavigationConstants.APPID,destination)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    if ( clearTop ) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
    }
    intent.putExtras(extras)
    startActivity(intent)
    if ( clearTop ) {
        requireActivity().overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)
    } else {
        requireActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
    }
}*/

fun Fragment.setupSnackbarMessenger(
    lifecycleOwner: LifecycleOwner,
    snackbarEvent: LiveData<Event<String>>,
    timeLength: Int) {
    snackbarEvent.observe(lifecycleOwner) { event ->
        event.getContentIfNotHandled()?.let { data ->
            context?.let { showSnackbar(data, timeLength) }
        }
    }
}

fun Fragment.setupToast(
    lifecycleOwner: LifecycleOwner,
    toastEvent: LiveData<Event<String>>,
    timeLength: Int) {
    toastEvent.observe(lifecycleOwner) { event ->
        event.getContentIfNotHandled()?.let { data ->
            context?.let { showToast(data, timeLength) }
        }
    }
}

fun Fragment.setupToastError(
    lifecycleOwner: LifecycleOwner,
    toastEvent: LiveData<Event<Int>>,
    timeLength: Int) {
    toastEvent.observe(lifecycleOwner) { event ->
        event.getContentIfNotHandled()?.let { data ->
            context?.let { showToast(it.getString(data), timeLength) }
        }
    }

}