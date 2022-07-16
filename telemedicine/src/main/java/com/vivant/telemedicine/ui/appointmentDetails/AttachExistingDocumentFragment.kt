package com.vivant.telemedicine.ui.appointmentDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.vivant.telemedicine.R
import com.vivant.telemedicine.adapter.ExistingDocumentAdapter
import com.vivant.telemedicine.base.BaseFragment
import com.vivant.telemedicine.base.BaseViewModel
import com.vivant.telemedicine.common.AppointmentDetailsSingleton
import com.vivant.telemedicine.common.Constants
import com.vivant.telemedicine.common.DateHelper
import com.vivant.telemedicine.common.Utilities
import com.vivant.telemedicine.databinding.FragmentAttachExistingDocumentBinding
import com.vivant.telemedicine.model.ListAppointmentsModel
import com.vivant.telemedicine.model.ListDocumentModel
import com.vivant.telemedicine.viewmodel.AppointmentDetailsViewModel
import com.vivant.telemedicine.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AttachExistingDocumentFragment : BaseFragment(),ExistingDocumentAdapter.OnHealthRecordClickListener {

    private var _binding: FragmentAttachExistingDocumentBinding? = null
    private val binding get() = _binding!!

    val viewModel: AppointmentDetailsViewModel by viewModel()
    val homeViewModel: HomeViewModel by viewModel()

    private var from = ""
    private var date = ""
    private val appointment = AppointmentDetailsSingleton.instance!!.appointment
    private var record = ListDocumentModel.HealthRelatedDocument()
    private var existingDocumentAdapter: ExistingDocumentAdapter? = null
    private var animation: LayoutAnimationController? = null

    override fun getViewModel(): BaseViewModel = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            requireArguments().let {
                from = it.getString(Constants.FROM, "")!!
                Utilities.printLogError("from----->$from")
            }
            // callback to Handle back button event
            val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.action_attachExisttingDocumentFragment_to_attachDocumentFragment)
                }
            }
            requireActivity().onBackPressedDispatcher.addCallback(this, callback)

        } catch ( e : Exception ) {
            e.printStackTrace()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAttachExistingDocumentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        try {
            initialise()
            setClickable()
            registerObserver()
        } catch ( e : Exception ) {
            e.printStackTrace()
        }
        return root
    }

    private fun initialise() {
        animation = AnimationUtils.loadLayoutAnimation(requireContext(),R.anim.layout_animation_slide_from_bottom)
        existingDocumentAdapter = ExistingDocumentAdapter(requireContext(),this)
        //binding.rvHealthRecords.layoutAnimation = animation
        binding.rvHealthRecords.adapter = existingDocumentAdapter

        startShimmer()
        homeViewModel.callListDocumentApi()
    }

    private fun registerObserver() {

        homeViewModel.listDocument.observe(viewLifecycleOwner) {
            if (it != null) {
                val documents = it.healthRelatedDocument
                if ( documents.toString() != "" && !documents.isNullOrEmpty() ) {
                    updateList(documents)
                } else {
                    binding.rvHealthRecords.visibility = View.GONE
                    binding.layoutNoData.visibility = View.VISIBLE
                }
                stopShimmer()
            }
        }

        viewModel.shareDocuments.observe(viewLifecycleOwner) {
            if (it != null) {
                if ( !it.documentSharingRecord.documentList.isNullOrEmpty()
                    && !Utilities.isNullOrEmptyOrZero(it.documentSharingRecord.documentList[0].logID.toString()) ) {
                    date = DateHelper.currentDateAsStringyyyyMMdd + "T" + DateHelper.currentTimeAs_hh_mm_ss
                    appointment.sharedDocuments.add(ListAppointmentsModel.SharedDocument(
                        appointmentID = appointment.iD,
                        documentID = record.documentID,
                        documentTypeCode = record.documentTypeCode,
                        //relatedTo = record.documentType.description,
                        fileName = record.fileName,
                        createdDate = date,
                        sharedDate = date
                    ))
                    Utilities.toastMessageShort(requireContext(),resources.getString(R.string.RECORD_SHARED_SUCCESS))
                    //findNavController().navigate(R.id.action_attachExisttingDocumentFragment_to_appointmentDetailsFragment)
                    when(from) {
                        Constants.ATTACH_DOCUMENT -> {
                            (activity as AppointmentDetailsActivity).routeToHomeScreen()
                        }
                        else -> {
                            findNavController().navigate(R.id.action_attachExisttingDocumentFragment_to_appointmentDetailsFragment)
                        }
                    }
                }
            }
        }

    }

    private fun setClickable() {

        binding.btnNext.setOnClickListener {
            if ( !Utilities.isNullOrEmptyOrZero(record.documentID.toString()) ) {
                viewModel.callShareDocumentsApi(appointment.iD,appointment.doctorID,appointment.firstName,
                    record.documentID,record.fileName,record.documentTypeCode)
            } else {
                Utilities.toastMessageShort(requireContext(),resources.getString(R.string.PLEASE_SELECT_DOCUMENT))
            }
        }

    }

    private fun updateList(list: MutableList<ListDocumentModel.HealthRelatedDocument>) {
        if ( !list.isNullOrEmpty() ) {
            binding.rvHealthRecords.visibility = View.VISIBLE
            binding.layoutNoData.visibility = View.GONE
            //binding.rvHealthRecords.layoutAnimation = animation
            existingDocumentAdapter!!.updateList(list)
            binding.rvHealthRecords.scheduleLayoutAnimation()
        } else {
            binding.rvHealthRecords.visibility = View.GONE
            binding.layoutNoData.visibility = View.VISIBLE
        }
    }

    private fun startShimmer() {
        binding.layoutHealthRecordsShimmer.startShimmer()
        binding.layoutHealthRecordsShimmer.visibility = View.VISIBLE
    }

    private fun stopShimmer() {
        binding.layoutHealthRecordsShimmer.stopShimmer()
        binding.layoutHealthRecordsShimmer.visibility = View.GONE
    }

    override fun onHealthRecordClick(position: Int, action: String, rec: ListDocumentModel.HealthRelatedDocument) {
        Utilities.printLogError("action--->$action")
        Utilities.printData("Record",rec,true)
        record = rec
    }

}