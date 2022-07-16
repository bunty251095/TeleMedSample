package com.vivant.telemedicine.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vivant.telemedicine.R
import com.vivant.telemedicine.base.BaseFragment
import com.vivant.telemedicine.base.BaseViewModel
import com.vivant.telemedicine.common.Utilities
import com.vivant.telemedicine.databinding.FragmentHomeMainBinding
import com.vivant.telemedicine.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentHomeMain : BaseFragment(),NotificationFragment.OnNotificationCountRefreshListener {

    private var _binding: FragmentHomeMainBinding? = null
    private lateinit var fm: FragmentManager

    var badgeDrawable: BadgeDrawable? = null

    private val binding get() = _binding!!

    val viewModel: HomeViewModel by viewModel()

    override fun getViewModel(): BaseViewModel = viewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeMainBinding.inflate(inflater, container, false)
        initialise()
        registerObserver()
        return binding.root
    }

    private fun initialise() {
        fm = childFragmentManager
        binding.bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        binding.bottomNavigation.setOnNavigationItemReselectedListener { }
        fm.beginTransaction().replace(R.id.main_container, HomeScreenFragment(), "1").commit()
        (activity as TeleMedHomeActivity).setBottomNavigationView(binding.bottomNavigation,fm)

        viewModel.callListNotificationsApi()
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    fm.beginTransaction().replace(R.id.main_container,HomeScreenFragment(),"1").commit()
                    (activity as TeleMedHomeActivity).setToolbarInfo(0,true,"")
                    viewModel.callListNotificationsApi()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.menu_my_history -> {
                    fm.beginTransaction().replace(R.id.main_container,MyHistoryFragment(),"2").commit()
                    (activity as TeleMedHomeActivity).setToolbarInfo(1,false,resources.getString(R.string.TITLE_MY_HISTORY))
                    return@OnNavigationItemSelectedListener true
                }
                R.id.menu_notification -> {
                    fm.beginTransaction().replace(R.id.main_container,NotificationFragment(this),"3").commit()
                    (activity as TeleMedHomeActivity).setToolbarInfo(2,false,resources.getString(R.string.NOTIFICATION))
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    private fun registerObserver() {

        viewModel.listNotifications.observe(viewLifecycleOwner) {
            if (it != null) {
                val list = it.notificationList
                if ( list.toString() != "" && !list.isNullOrEmpty() ) {
                    Utilities.printLogError("NotificationCount--->${list.size}")
                    createBadge(list.size)
                } else {
                    removeBadge()
                }
            }
        }

    }

    private fun createBadge(count:Int) {
        badgeDrawable = binding.bottomNavigation.getOrCreateBadge(R.id.menu_notification)
        badgeDrawable!!.backgroundColor = ContextCompat.getColor(requireContext(),R.color.secondary_pink)
        badgeDrawable!!.badgeTextColor = ContextCompat.getColor(requireContext(),R.color.white)
        badgeDrawable!!.number = count
        badgeDrawable!!.isVisible = true
    }
    private fun updateBadge(count:Int) {
        if ( count > 0 ) {
            badgeDrawable = binding.bottomNavigation.getOrCreateBadge(R.id.menu_notification)
            badgeDrawable!!.number = count
            badgeDrawable!!.isVisible = true
        } else {
            removeBadge()
        }
    }

    private fun removeBadge() {
        badgeDrawable = binding.bottomNavigation.getOrCreateBadge(R.id.menu_notification)
        badgeDrawable!!.clearNumber()
        badgeDrawable!!.isVisible = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onNotificationCountRefresh(count: Int) {
        Utilities.printLogError("NotificationRefreshCount--->$count")
        updateBadge(count)
    }

}