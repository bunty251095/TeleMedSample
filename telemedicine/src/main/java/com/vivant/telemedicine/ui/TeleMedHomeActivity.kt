package com.vivant.telemedicine.ui

import com.vivant.telemedicine.R
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vivant.telemedicine.base.BaseActivity
import com.vivant.telemedicine.base.BaseViewModel
import com.vivant.telemedicine.common.Constants
import com.vivant.telemedicine.common.NavigationConstants
import com.vivant.telemedicine.common.Utilities
import com.vivant.telemedicine.common.openAnotherActivity
import com.vivant.telemedicine.databinding.ActivityTeleMedHomeBinding
import com.vivant.telemedicine.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TeleMedHomeActivity : BaseActivity() {

    //private lateinit var navView: NavigationView
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityTeleMedHomeBinding

    val viewModel: HomeViewModel by viewModel()

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigation: BottomNavigationView
    private lateinit var fm: FragmentManager
    private var selectedTab = 0
    private var versionName = ""

    override fun getViewModel(): BaseViewModel = viewModel

    override fun onCreateEvent(savedInstanceState: Bundle?) {
        try {
            binding = ActivityTeleMedHomeBinding.inflate(layoutInflater)
            setContentView(binding.root)
            selectedTab = 0
            setupToolbar()
            initialise()
            //configureDrawerLayout()
            setClickable()
        } catch ( e : Exception ) {
            e.printStackTrace()
        }
    }

    private fun initialise() {
        drawerLayout = binding.drawerLayout

        Utilities.printLogError("name--->${viewModel.name}")
        Utilities.printLogError("email--->${viewModel.email}")
        binding.txtUsername.text = viewModel.name
        binding.txtUserEmail.text = viewModel.email

        versionName = Utilities.getVersionName(this)
        if (!Utilities.isNullOrEmpty(versionName)) {
            var versionText = "${resources.getString(R.string.VERSION)} - ${Constants.VERSION_NAME}"
            if (Constants.environment.equals("UAT", ignoreCase = true)) {
                versionText = "$versionText (UAT)"
            }
            binding.txtVersionName.text = versionText
        }

        //val navView = binding.navView
        //val navController = findNavController(R.id.nav_host_fragment_content_tele_med_home)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        //appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_appointments,R.id.nav_my_history,R.id.nav_health_records),drawerLayout)
        //setupActionBarWithNavController(navController,appBarConfiguration)
        //navView.setupWithNavController(navController)

        //NavigationUI.setupWithNavController(binding.toolBarView.toolbar,navController,appBarConfiguration)
/*            navController.addOnDestinationChangedListener { controller, destination, arguments ->
                if (destination.id == R.id.nav_home){
                    binding.toolBarView.toolbar.setNavigationIcon(R.drawable.img_back)
                }
            }*/
    }

    private fun setClickable() {

        binding.layoutMyHistory.setOnClickListener {
            onBackPressed()
            navigation.selectedItemId = R.id.menu_my_history
        }

        binding.layoutAppointment.setOnClickListener {
            try {
                onBackPressed()
                replaceContainer("4")
                //it.findNavController().navigate(R.id.action_mainHomeFragment_fragmentAppointments)
            } catch ( e : Exception ) {
                e.printStackTrace()
            }
        }

        binding.layoutHealthRecords.setOnClickListener {
            try {
                onBackPressed()
                replaceContainer("5")
                //it.findNavController().navigate(R.id.action_mainHomeFragment_fragmentHealthRecords)
                //findNavController(R.id.layout_health_records).navigate(R.id.action_mainHomeFragment_fragmentHealthRecords)
            } catch ( e : Exception ) {
                e.printStackTrace()
            }
        }

        binding.layoutLogout.setOnClickListener {
            logout()
        }

        binding.toolBarView.imgSettings.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END)
            } else {
                drawerLayout.openDrawer(GravityCompat.END)
            }
        }

    }

    private fun logout() {
        viewModel.logoutAndResetPreferences()
        finishAffinity()
    }

/*    private fun logout() {
        viewModel.logoutAndResetPreferences()
        openAnotherActivity(destination = NavigationConstants.MAIN, clearTop = true) {}
        //finishAffinity()
        //onBackPressed()
    }*/

    private fun setupToolbar() {
        setSupportActionBar(binding.toolBarView.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back)

        binding.toolBarView.toolbar.navigationIcon?.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
            ContextCompat.getColor(this,R.color.textViewColor), BlendModeCompat.SRC_ATOP)

        binding.toolBarView.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    fun setToolbarInfo(tabPosition: Int,showSettingsIcon: Boolean, title: String="") {
        selectedTab = tabPosition
        if ( !Utilities.isNullOrEmpty(title) ) {
            binding.toolBarView.txtTitle.text = title
        } else {
            binding.toolBarView.txtTitle.text = ""
        }

        if( showSettingsIcon ) {
            binding.toolBarView.imgSettings.visibility = View.VISIBLE
        } else {
            binding.toolBarView.imgSettings.visibility = View.GONE
        }

    }

    fun setBottomNavigationView(navView: BottomNavigationView,cfm: FragmentManager) {
        navigation = navView
        fm = cfm
    }

    private fun replaceContainer(tag : String) {
        when(tag) {
            "1" -> {
                fm.beginTransaction().replace(R.id.main_container,HomeScreenFragment(),tag).commit()
                setToolbarInfo(0,true,"")
                navigation.visibility = View.VISIBLE
            }
            "4" -> {
                fm.beginTransaction().replace(R.id.main_container,AppointmentsFragment(),tag).commit()
                setToolbarInfo(3,false,resources.getString(R.string.TITLE_MY_APPOINTMENTS))
                navigation.visibility = View.GONE
            }
            "5" -> {
                fm.beginTransaction().replace(R.id.main_container,HealthRecordsFragment(),tag).commit()
                setToolbarInfo(4,false,resources.getString(R.string.TITLE_HEALTH_RECORDS))
                navigation.visibility = View.GONE
            }
        }
    }

    override fun onBackPressed() {
        Utilities.printLogError("selectedTab--->$selectedTab")
        when(selectedTab) {
            0 -> {
                val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
                if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    drawerLayout.closeDrawer(GravityCompat.END)
                } else {
                    //finishAffinity()
                    logout()
                }
            }
            3,4 -> {
                replaceContainer("1")
            }
            else -> {
                navigation.selectedItemId = R.id.menu_home
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_tele_med_home)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    /*    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.tele_med_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_settings) {
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END)
            } else {
                drawerLayout.openDrawer(GravityCompat.END)
            }
        }
        return true
    }*/

/*    private fun configureDrawerLayout() {

        val toggle = ActionBarDrawerToggle(this,drawerLayout,binding.toolBarView.toolbar,
            R.string.NAVIGATION_DRAWER_OPEN,R.string.NAVIGATION_DRAWER_CLOSE)

        toggle.isDrawerIndicatorEnabled = false

        toggle.toolbarNavigationClickListener = View.OnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END)
            } else {
                drawerLayout.openDrawer(GravityCompat.END)
            }
        }

        toggle.syncState()
    }*/


/*    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment_content_tele_med_home).navigateUp()
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
    }*/

/*    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }*/

    /*     binding.appBarTeleMedHome.fab.setOnClickListener { view ->
           Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
        }*/
}