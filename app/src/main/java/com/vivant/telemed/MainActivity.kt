package com.vivant.telemed

import android.content.Intent
import android.os.Bundle
import com.vivant.telemed.databinding.ActivityMainBinding
import com.vivant.telemedicine.base.BaseActivity
import com.vivant.telemedicine.base.BaseViewModel
import com.vivant.telemedicine.common.Constants
import com.vivant.telemedicine.common.NavigationConstants
import com.vivant.telemedicine.common.Utilities
import com.vivant.telemedicine.common.openAnotherActivity
import com.vivant.telemedicine.ui.SplashActivity
import com.vivant.telemedicine.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    private val keyur = "{\"NM\":\"Keyur Tailor\",\"DOB\":\"1989-10-19\",\"G\":\"Male\",\"PN\":\"7984689045\",\"EA\":\"keyur.tailor@vivant.me\",\"PC\":\"PMCARE\",\"MN\":\"VIV123-KEY\",\"IC\":\"VIV123-KEY\",\"LID\":\"VIV123-KEY\",\"EID\":\"VIV123-KEY\",\"SRC\":\"TELEMED\",\"PCD\":\"PMCARE\",\"CN\":\"PMCARE\",\"LOC\":\"IN\",\"BU\":\"001\",\"EN\":\"IT\",\"DEPT\":\"IT\",\"DEVICEID\":\"\",\"LAUNCHTYPE\":\"DASHBOARD\",\"FAMILY\":[{\"FMID\":\"1\",\"FMNA\":\"Keyur Tailor\",\"FMDOB\":\"1989-10-19\",\"FMG\":\"Male\",\"FMRE\":\"Self\"}],\"PAYMENT\":[{\"FMID\":\"1\",\"CRD\":\"1000\"}]}"

    private val redzuan = "{\"NM\":\"MUHAMMAD REDZUAN BIN ISHAK\",\"DOB\":\"1988-04-20\",\"G\":\"Male\",\"PN\":\"0176678469\",\"EA\":\"redzuan@pmcare.com.my\",\"PC\":\"PMCARE\",\"MN\":\"PMC867-I\",\"IC\":\"PMC867-I\",\"LID\":\"PMC867-I\",\"EID\":\"PMC867-I\",\"SRC\":\"TELEMED\",\"PCD\":\"PMCARE\",\"CN\":\"PMCARE\",\"LOC\":\"IN\",\"BU\":\"001\",\"EN\":\"IT\",\"DEPT\":\"IT\",\"DEVICEID\":\"\",\"LAUNCHTYPE\":\"DASHBOARD\",\"FAMILY\":[{\"FMID\":\"1\",\"FMNA\":\"MUHAMMAD REDZUAN BIN ISHAK\",\"FMDOB\":\"1988-04-20\",\"FMG\":\"Male\",\"FMRE\":\"Self\"}],\"PAYMENT\":[{\"FMID\":\"1\",\"CRD\":\"1000\"}]}"

    private val ashish = "{\"NM\":\"Ashish Rathi\",\"DOB\":\"1994-11-01\",\"G\":\"Male\",\"PN\":\"7875996336\",\"EA\":\"ashish.rathi@vivant.me\",\"PC\":\"PMCARE\",\"MN\":\"VIV123-KEY\",\"IC\":\"VIV123-KEY\",\"LID\":\"VIV123-KEY\",\"EID\":\"VIV123-KEY\",\"SRC\":\"TELEMED\",\"PCD\":\"PMCARE\",\"CN\":\"PMCARE\",\"LOC\":\"IN\",\"BU\":\"001\",\"EN\":\"IT\",\"DEPT\":\"IT\",\"DEVICEID\":\"\",\"LAUNCHTYPE\":\"DASHBOARD\",\"FAMILY\":[{\"FMID\":\"1\",\"FMNA\":\"Keyur Tailor\",\"FMDOB\":\"1989-10-19\",\"FMG\":\"Male\",\"FMRE\":\"Self\"}],\"PAYMENT\":[{\"FMID\":\"1\",\"CRD\":\"1000\"}]}"

    //private val amarpreet = "{\"NM\":\"Amarpreet Bhatia\",\"DOB\":\"1993-01-09\",\"G\":\"Male\",\"PN\":\"9899454506\",\"EA\":\"amarpreet.bhatia@vivant.me\",\"PC\":\"PMCARE\",\"MN\":\"VIV123-KEY\",\"IC\":\"VIV123-KEY\",\"LID\":\"VIV123-KEY\",\"EID\":\"VIV123-KEY\",\"SRC\":\"TELEMED\",\"PCD\":\"PMCARE\",\"CN\":\"PMCARE\",\"LOC\":\"IN\",\"BU\":\"001\",\"EN\":\"IT\",\"DEPT\":\"IT\",\"DEVICEID\":\"\",\"LAUNCHTYPE\":\"DASHBOARD\",\"FAMILY\":[{\"FMID\":\"1\",\"FMNA\":\"Keyur Tailor\",\"FMDOB\":\"1989-10-19\",\"FMG\":\"Male\",\"FMRE\":\"Self\"}],\"PAYMENT\":[{\"FMID\":\"1\",\"CRD\":\"1000\"}]}"

    private val tanvi = "{\"NM\":\"Tanvi soni\",\"DOB\":\"1992-07-30\",\"G\":\"Male\",\"PN\":\"9403187676\",\"EA\":\"tanvi.soni@vivant.me\",\"TNCIsAccepted\":\"false\",\"PC\":\"PMCARE\",\"MN\":\"VIV123-KEY\",\"IC\":\"VIV123-KEY\",\"LID\":\"VIV123-KEY\",\"EID\":\"VIV123-KEY\",\"SRC\":\"TELEMED\",\"PCD\":\"PMCARE\",\"CN\":\"PMCARE\",\"LOC\":\"IN\",\"BU\":\"001\",\"EN\":\"IT\",\"DEPT\":\"IT\",\"DEVICEID\":\"\",\"LAUNCHTYPE\":\"DASHBOARD\",\"FAMILY\":[{\"FMID\":\"1\",\"FMNA\":\"Keyur Tailor\",\"FMDOB\":\"1989-10-19\",\"FMG\":\"Male\",\"FMRE\":\"Self\"}],\"PAYMENT\":[{\"FMID\":\"1\",\"CRD\":\"1000\"}]}"

    private val shreyash = "{\"NM\":\"Shreyash Chauhan\",\"DOB\":\"1990-01-01\",\"G\":\"Male\",\"PN\":\"9993572482\",\"EA\":\"shreyash.chauhan@vivant.me\",\"PC\":\"PMCARE\",\"MN\":\"VIV123-KEY\",\"IC\":\"VIV123-KEY\",\"LID\":\"VIV123-KEY\",\"EID\":\"VIV123-KEY\",\"SRC\":\"TELEMED\",\"PCD\":\"PMCARE\",\"CN\":\"PMCARE\",\"LOC\":\"IN\",\"BU\":\"001\",\"EN\":\"IT\",\"DEPT\":\"IT\",\"DEVICEID\":\"\",\"LAUNCHTYPE\":\"DASHBOARD\",\"FAMILY\":[{\"FMID\":\"1\",\"FMNA\":\"Keyur Tailor\",\"FMDOB\":\"1989-10-19\",\"FMG\":\"Male\",\"FMRE\":\"Self\"}],\"PAYMENT\":[{\"FMID\":\"1\",\"CRD\":\"1000\"}]}"

    private val vinay = "{\"NM\":\"Vinay Kumar\",\"DOB\":\"1984-07-30\",\"G\":\"Male\",\"PN\":\"9109098098\",\"EA\":\"vinay.kumar@vivant.me\",\"PC\":\"PMCARE\",\"MN\":\"VIV123-KEY\",\"IC\":\"VIV123-KEY\",\"LID\":\"VIV123-KEY\",\"EID\":\"VIV123-KEY\",\"SRC\":\"TELEMED\",\"PCD\":\"PMCARE\",\"CN\":\"PMCARE\",\"LOC\":\"IN\",\"BU\":\"001\",\"EN\":\"IT\",\"DEPT\":\"IT\",\"DEVICEID\":\"\",\"LAUNCHTYPE\":\"DASHBOARD\",\"FAMILY\":[{\"FMID\":\"1\",\"FMNA\":\"Keyur Tailor\",\"FMDOB\":\"1989-10-19\",\"FMG\":\"Male\",\"FMRE\":\"Self\"}],\"PAYMENT\":[{\"FMID\":\"1\",\"CRD\":\"1000\"}]}"

    private val viewModel: HomeViewModel by viewModel()

    override fun getViewModel(): BaseViewModel = viewModel

    override fun onCreateEvent(savedInstanceState: Bundle?) {
        try {
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
            //supportActionBar!!.hide()
            initialise()
            setClickable()
        } catch ( e : Exception ) {
            e.printStackTrace()
        }
    }

    private fun initialise() {
        if( !Utilities.isNullOrEmpty(viewModel.token) ) {
            proceedToApp()
        }
    }

    private fun setClickable() {

        binding.btnLogin1.setOnClickListener {
            openAnotherActivity(destination = NavigationConstants.SPLASH_SCREEN) {
                putString(Constants.DATA, keyur)
            }
        }

        binding.btnLogin2.setOnClickListener {
            openAnotherActivity(destination = NavigationConstants.SPLASH_SCREEN) {
                putString(Constants.DATA, redzuan)
            }
        }

        binding.btnLogin3.setOnClickListener {
            openAnotherActivity(destination = NavigationConstants.SPLASH_SCREEN) {
                putString(Constants.DATA, ashish)
            }
        }

        binding.btnLogin4.setOnClickListener {
            openAnotherActivity(destination = NavigationConstants.SPLASH_SCREEN) {
                putString(Constants.DATA, tanvi)
            }
        }

    }

    private fun proceedToApp() {
        //startActivity(Intent(this, SplashActivity::class.java))
        openAnotherActivity(destination = NavigationConstants.SPLASH_SCREEN) {
            putString(Constants.DATA, Constants.LOGIN)
        }
        finish()
    }

}