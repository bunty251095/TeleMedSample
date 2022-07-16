package com.vivant.telemedicine.ui.consultAndSchedule

import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.vivant.telemedicine.R
import com.vivant.telemedicine.adapter.ChooseStepAdapter
import com.vivant.telemedicine.base.BaseActivity
import com.vivant.telemedicine.base.BaseViewModel
import com.vivant.telemedicine.common.NavigationConstants
import com.vivant.telemedicine.common.openAnotherActivity
import com.vivant.telemedicine.common.*
import com.vivant.telemedicine.databinding.ActivityConsultAndScheduleBinding
import com.vivant.telemedicine.viewmodel.ConsultAndScheduleViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConsultAndScheduleActivity : BaseActivity(),ChooseStepAdapter.OnStepClickListener {

    private lateinit var binding: ActivityConsultAndScheduleBinding
    private lateinit var navController: NavController

    var from = ""
    private val consultAndScheduleSingleton = ConsultAndScheduleSingleton.instance!!
    private var chooseStepAdapter: ChooseStepAdapter? = null

    val viewModel: ConsultAndScheduleViewModel by viewModel()

    override fun getViewModel(): BaseViewModel = viewModel

    override fun onCreateEvent(savedInstanceState: Bundle?) {
        try {
            binding = ActivityConsultAndScheduleBinding.inflate(layoutInflater)
            setContentView(binding.root)
            configureStepsAdapter()
            initialise()
        } catch ( e : Exception ) {
            e.printStackTrace()
        }
    }

    private fun initialise() {
        supportActionBar!!.hide()
        //setSupportActionBar(binding.toolBarView.toolbarCommon)

        // Setting up a back button
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_consult_and_schedule) as NavHostFragment
        navController = navHostFragment.navController
        setupActionBarWithNavController(navController)
        //setupActionBarWithNavController(findNavController(R.id.nav_host_fragment_consult_and_schedule))

        navController.setGraph(R.navigation.nav_graph_consult_and_schedule)

        navController.addOnDestinationChangedListener { controller, destination, _ ->

            binding.toolBarView.toolbarTitle.text = when (destination.id) {
                R.id.chooseModeFragment -> resources.getString(R.string.TITLE_CHOOSE_MODE)
                R.id.chooseSpecializationFragment -> resources.getString(R.string.TITLE_CHOOSE_SPECIALIZATION)
                R.id.chooseDoctorFragment -> resources.getString(R.string.TITLE_CHOOSE_DOCTOR)
                R.id.chooseSlotFragment -> resources.getString(R.string.TITLE_CHOOSE_SLOT)
                R.id.describeSymptomsFragment -> resources.getString(R.string.TITLE_DESCRIBE_SYMPTOMS)
                R.id.confirmAppointmentFragment -> resources.getString(R.string.TITLE_CONFIRM_APPOINTMENT)
                else -> resources.getString(R.string.TELE_CONSULTATION)
            }

        }

        binding.toolBarView.toolbarCommon.setNavigationIcon(R.drawable.ic_back)

        binding.toolBarView.toolbarCommon.setNavigationOnClickListener {
            onBackPressed()
        }

    }

    private fun configureStepsAdapter() {
        try {
            chooseStepAdapter = ChooseStepAdapter(this,this)
            binding.rvChooseStep.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            binding.rvChooseStep.adapter = chooseStepAdapter

            if (intent.hasExtra(Constants.FROM)) {
                from = intent.getStringExtra(Constants.FROM)!!
                Utilities.printLogError("from--->$from")
                when (from) {
                    Constants.CONSULT_NOW -> {
                        chooseStepAdapter!!.updateList(Helper.getConsultNowStepList(this))
                    }
                    Constants.SCHEDULE_APPOINTMENT -> {
                        chooseStepAdapter!!.updateList(Helper.getScheduleAppointmentStepList(this))
                    }
                }
            }
        } catch ( e : Exception ) {
            e.printStackTrace()
        }
    }

    fun setStepPositionScheduleAppointment(step: Int, isReverse : Boolean = false) {
        when(step) {
            Constants.STEP_ONE -> {
                chooseStepAdapter!!.updatePosition(0)
            }
            Constants.STEP_TWO -> {
                chooseStepAdapter!!.updatePosition(1)
            }
            Constants.STEP_THREE -> {
                chooseStepAdapter!!.updatePosition(2)
                if ( isReverse ) {
                    binding.rvChooseStep.invalidate()
                    configureStepsAdapter()
                    chooseStepAdapter!!.updatePosition(2)
                }
            }
            Constants.STEP_FOUR -> {
                chooseStepAdapter!!.updatePosition(3)
                binding.rvChooseStep.layoutManager!!.scrollToPosition(5)
            }
            Constants.STEP_FIVE -> {
                chooseStepAdapter!!.updatePosition(4)
            }
            Constants.STEP_SIX -> {
                chooseStepAdapter!!.updatePosition(5)
            }
        }
    }

    fun setStepPositionConsultNow(step: Int, isReverse : Boolean = false) {
        when(step) {
            Constants.STEP_ONE -> {
                chooseStepAdapter!!.updatePosition(0)
                if ( isReverse ) {
                    binding.rvChooseStep.invalidate()
                    configureStepsAdapter()
                    chooseStepAdapter!!.updatePosition(0)
                }
            }
            Constants.STEP_TWO -> {
                chooseStepAdapter!!.updatePosition(1)
/*                if ( isReverse ) {
                    binding.rvChooseStep.invalidate()
                    configureStepsAdapter()
                    chooseStepAdapter!!.updatePosition(1)
                }*/
            }
            Constants.STEP_THREE -> {
                chooseStepAdapter!!.updatePosition(2)
            }
            Constants.STEP_FOUR -> {
                chooseStepAdapter!!.updatePosition(3)
                binding.rvChooseStep.layoutManager!!.scrollToPosition(3)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_consult_and_schedule)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

/*    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }*/

/*    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_consult_and_schedule)
        //return super.onSupportNavigateUp()
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }*/

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

    fun routeToHomeScreen() {
        consultAndScheduleSingleton.clearData()
        openAnotherActivity(destination = NavigationConstants.HOME, clearTop = true)
    }

    override fun onStepSelection(position: Int, tag: String) {
        Utilities.printLogError("position--->$position")
        when(position) {
            1 -> {
                //it.findNavController().navigate(R.id.action_chooseModeFragment_chooseSpecializationFragment)
            }
        }
    }

}