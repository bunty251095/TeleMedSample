package com.vivant.telemedicine.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import com.vivant.telemedicine.base.BaseActivity
import com.vivant.telemedicine.base.BaseViewModel
import com.vivant.telemedicine.common.Constants
import com.vivant.telemedicine.common.Utilities
import com.vivant.telemedicine.databinding.ActivitySplashBinding
import com.vivant.telemedicine.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashBinding

    private val viewModel: HomeViewModel by viewModel()

    private var data = ""

    override fun getViewModel(): BaseViewModel = viewModel

    override fun onCreateEvent(savedInstanceState: Bundle?) {
        try {
            binding = ActivitySplashBinding.inflate(layoutInflater)
            setContentView(binding.root)
            //supportActionBar!!.hide()

            if (intent.hasExtra(Constants.DATA)) {
                data = intent.getStringExtra(Constants.DATA)!!
                Utilities.printLogError("Data--->$data")
            }

            initialise()
        } catch ( e : Exception ) {
            e.printStackTrace()
        }
    }

    private fun initialise() {
        val animation = AnimationUtils.loadAnimation(this, com.vivant.telemedicine.R.anim.rotate_forward)
        binding.imgLoader.startAnimation(animation)
        binding.imgLoader.visibility = View.VISIBLE

        when(data) {
            Constants.LOGIN -> {
                proceedToApp()
            }
            else -> {
                if ( !Utilities.isNullOrEmpty(data) ) {
                    viewModel.callLoginSSO(data)
                } else {
                    Utilities.toastMessageShort(this,"User Credentials not found")
                }
            }
        }

        viewModel.user.observe(this) {
            if (it != null) {
                //binding.imgLoader.clearAnimation()
                //binding.imgLoader.visibility = View.GONE
                proceedToApp()
            }
        }
    }

    private fun proceedToApp() {
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, TeleMedHomeActivity::class.java))
            //openAnotherActivity(destination = NavigationConstants.HOME, clearTop = true)
        }, 2000)
    }

}