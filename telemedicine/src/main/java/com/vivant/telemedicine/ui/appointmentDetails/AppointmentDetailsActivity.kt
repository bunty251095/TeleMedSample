package com.vivant.telemedicine.ui.appointmentDetails

import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.vivant.telemedicine.R
import com.vivant.telemedicine.base.BaseActivity
import com.vivant.telemedicine.base.BaseViewModel
import com.vivant.telemedicine.common.*
import com.vivant.telemedicine.databinding.ActivityAppointmentDetailsBinding
import com.vivant.telemedicine.viewmodel.AppointmentDetailsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AppointmentDetailsActivity : BaseActivity() {

    private lateinit var binding: ActivityAppointmentDetailsBinding
    private lateinit var navController: NavController

    private val appointmentDetailsSingleton = AppointmentDetailsSingleton.instance!!

    val viewModel: AppointmentDetailsViewModel by viewModel()

    override fun getViewModel(): BaseViewModel = viewModel

    override fun onCreateEvent(savedInstanceState: Bundle?) {
        try {
            binding = ActivityAppointmentDetailsBinding.inflate(layoutInflater)
            setContentView(binding.root)
            initialise()
        } catch ( e : Exception ) {
            e.printStackTrace()
        }
    }
    private fun initialise() {
        supportActionBar!!.hide()

        // Setting up a back button
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_appointment_details) as NavHostFragment
        navController = navHostFragment.navController
        setupActionBarWithNavController(navController)
        //setupActionBarWithNavController(findNavController(R.id.nav_host_fragment_consult_and_schedule))

        val bundle = Bundle()
        if (intent.hasExtra(Constants.FROM) && !Utilities.isNullOrEmpty(intent.getStringExtra(Constants.FROM))) {
            when (intent.getStringExtra(Constants.FROM)) {
                Constants.DASHBOARD -> bundle.putString(Constants.FROM,Constants.DASHBOARD)
                Constants.UPCOMING -> bundle.putString(Constants.FROM,Constants.UPCOMING)
                Constants.CONSULTATION -> bundle.putString(Constants.FROM,Constants.CONSULTATION)
                Constants.PAST -> bundle.putString(Constants.FROM,Constants.PAST)
                Constants.RESCHEDULE -> bundle.putString(Constants.FROM,Constants.RESCHEDULE)
                Constants.ATTACH_DOCUMENT -> bundle.putString(Constants.FROM,Constants.ATTACH_DOCUMENT)
                Constants.BOOK_FOLLOW_UP -> bundle.putString(Constants.FROM,Constants.BOOK_FOLLOW_UP)
            }
        }

        navController.setGraph(R.navigation.nav_graph_appointment_details,bundle)

        navController.addOnDestinationChangedListener { controller, destination, _ ->

            binding.toolBarView.toolbarTitle.text = when (destination.id) {
                R.id.appointmentDetailsFragment -> resources.getString(R.string.TITLE_APPOINTMENT_DETAILS)
                R.id.rescheduleFragment -> resources.getString(R.string.TITLE_RESCHEDULE)
                R.id.attachDocumentFragment -> resources.getString(R.string.TITLE_ATTACH_DOCUMENT)
                R.id.attachExisttingDocumentFragment -> resources.getString(R.string.TITLE_ATTACH_DOCUMENT)
                R.id.bookFollowupFragment -> resources.getString(R.string.TITLE_FOLLOWUP)
                else -> resources.getString(R.string.TELE_CONSULTATION)
            }

        }

        binding.toolBarView.toolbarCommon.setNavigationIcon(R.drawable.ic_back)

        binding.toolBarView.toolbarCommon.setNavigationOnClickListener {
            onBackPressed()
        }

    }

    fun routeToHomeScreen() {
        appointmentDetailsSingleton.clearData()
        openAnotherActivity(destination = NavigationConstants.HOME, clearTop = true)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_appointment_details)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // API 5+ solution
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}