package com.vivant.telemedicine.ui

import com.vivant.telemedicine.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import com.vivant.telemedicine.adapter.NotificationAdapter
import com.vivant.telemedicine.base.BaseFragment
import com.vivant.telemedicine.base.BaseViewModel
import com.vivant.telemedicine.common.Constants
import com.vivant.telemedicine.common.DefaultNotificationDialog
import com.vivant.telemedicine.common.Utilities
import com.vivant.telemedicine.databinding.FragmentNotificationBinding
import com.vivant.telemedicine.extension.showDialog
import com.vivant.telemedicine.model.ListNotificationsModel
import com.vivant.telemedicine.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotificationFragment(val listener : OnNotificationCountRefreshListener) : BaseFragment() ,
    NotificationAdapter.OnCancelClickListener,
    DefaultNotificationDialog.OnDialogValueListener {

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    val viewModel: HomeViewModel by viewModel()

    private var isClearCall = false
    private var itemPosition = -1
    private var notificationAdapter: NotificationAdapter? = null
    private var animation: LayoutAnimationController? = null
    private val notificationsList: MutableList<ListNotificationsModel.Notification> = mutableListOf()

    override fun getViewModel(): BaseViewModel = viewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        val root: View = binding.root
        try {
            initialise()
            registerObserver()
        } catch ( e : Exception ) {
            e.printStackTrace()
        }
        return root
    }

    private fun initialise() {
        animation = AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_slide_from_bottom)
        notificationAdapter = NotificationAdapter(requireContext(),this)
        //binding.rvNotifications.layoutAnimation = animation
        binding.rvNotifications.adapter = notificationAdapter

        binding.txtClearAll.visibility = View.INVISIBLE
        startShimmer()
        viewModel.callListNotificationsApi()

        binding.txtClearAll.setOnClickListener {
            showDialog(
                listener = this,
                title = this.resources.getString(R.string.CLEAR_ALL),
                message = this.resources.getString(R.string.CLEAR_ALL_DESC),
                leftText = this.resources.getString(R.string.NO),
                rightText = this.resources.getString(R.string.YES))
        }
    }

    private fun registerObserver() {

        viewModel.listNotifications.observe(viewLifecycleOwner) {
            if (it != null) {
                val doctors = it.notificationList
                if ( doctors.toString() != "" && !doctors.isNullOrEmpty() ) {
                    updateList(doctors)
                    listener.onNotificationCountRefresh(doctors.size)
                } else {
                    binding.rvNotifications.visibility = View.GONE
                    binding.txtClearAll.visibility = View.INVISIBLE
                    binding.layoutNoData.visibility = View.VISIBLE
                    listener.onNotificationCountRefresh(0)
                }
                stopShimmer()
            }
        }

        viewModel.updateNotification.observe(viewLifecycleOwner) {
            if (it != null) {
                if ( it.success.equals(Constants.TRUE,ignoreCase = true) ) {
                    if ( isClearCall ) {
                        listener.onNotificationCountRefresh(0)
                        updateList(notificationsList)
                    } else {
                        binding.rvNotifications.layoutAnimation = animation
                        notificationAdapter!!.removeItem(itemPosition)
                        binding.rvNotifications.scheduleLayoutAnimation()
                        Utilities.toastMessageShort(requireContext(),resources.getString(R.string.NOTIFICATION_CANCELED_SUCCESS))
                        listener.onNotificationCountRefresh(notificationAdapter!!.getNotificationCount())
                        itemPosition = -1
                    }
                }
            }
        }

    }

    private fun updateList(notificationsList: MutableList<ListNotificationsModel.Notification>) {
        if ( !notificationsList.isNullOrEmpty() ) {
            binding.rvNotifications.visibility = View.VISIBLE
            binding.txtClearAll.visibility = View.VISIBLE
            binding.layoutNoData.visibility = View.GONE
            //binding.rvNotifications.layoutAnimation = animation
            notificationAdapter!!.updateList(notificationsList)
            binding.rvNotifications.scheduleLayoutAnimation()
        } else {
            binding.rvNotifications.visibility = View.GONE
            binding.txtClearAll.visibility = View.INVISIBLE
            binding.layoutNoData.visibility = View.VISIBLE
        }
    }

    override fun onCancelClick(position: Int, notification: ListNotificationsModel.Notification) {
        itemPosition = position
        Utilities.printLogError("notificationID--->${notification.notificationID}")
        Utilities.printLogError("position--->$position")
        if ( position == 0 ) {
            isClearCall = true
        }
        viewModel.callUpdateNotificationApi(notification.notificationID,false)
    }

    private fun startShimmer() {
        binding.layoutNotificationsShimmer.startShimmer()
        binding.layoutNotificationsShimmer.visibility = View.VISIBLE
    }

    private fun stopShimmer() {
        binding.layoutNotificationsShimmer.stopShimmer()
        binding.layoutNotificationsShimmer.visibility = View.GONE
    }

    interface OnNotificationCountRefreshListener {
        fun onNotificationCountRefresh(count: Int)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDialogClickListener(isButtonLeft: Boolean, isButtonRight: Boolean) {
        if ( isButtonRight ) {
            viewModel.callUpdateNotificationApi(0,true)
            isClearCall = true
        }
    }

}