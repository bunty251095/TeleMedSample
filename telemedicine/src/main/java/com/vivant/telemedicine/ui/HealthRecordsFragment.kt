package com.vivant.telemedicine.ui

import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import com.vivant.telemedicine.R
import com.vivant.telemedicine.adapter.HealthRecordsAdapter
import com.vivant.telemedicine.adapter.NotificationAdapter
import com.vivant.telemedicine.base.BaseFragment
import com.vivant.telemedicine.base.BaseViewModel
import com.vivant.telemedicine.common.*
import com.vivant.telemedicine.databinding.FragmentHealthRecordsBinding
import com.vivant.telemedicine.extension.showDialog
import com.vivant.telemedicine.model.*
import com.vivant.telemedicine.viewmodel.HomeViewModel
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class HealthRecordsFragment : BaseFragment(),HealthRecordsAdapter.OnHealthRecordClickListener,DefaultNotificationDialog.OnDialogValueListener {

    private var _binding: FragmentHealthRecordsBinding? = null
    private val binding get() = _binding!!

    val viewModel: HomeViewModel by viewModel()

    private val fileUtils = FileUtils
    private var path = ""
    private var extension = ""
    private var dialogClickType = ""
    private var itemPosition = -1
    private var record = ListDocumentModel.HealthRelatedDocument()
    private var recordInSession = RecordInSession()
    private var healthRecordsAdapter: HealthRecordsAdapter? = null
    private var animation: LayoutAnimationController? = null

    override fun getViewModel(): BaseViewModel = viewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHealthRecordsBinding.inflate(inflater, container, false)
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
        path = Utilities.getAppFolderLocation(requireContext())
        animation = AnimationUtils.loadLayoutAnimation(requireContext(),R.anim.layout_animation_slide_from_bottom)
        healthRecordsAdapter = HealthRecordsAdapter(requireContext(),this)
        //binding.rvHealthRecords.layoutAnimation = animation
        binding.rvHealthRecords.adapter = healthRecordsAdapter

        startShimmer()
        viewModel.callListDocumentApi()

        //val list: MutableList<NotificationModel> = mutableListOf()
        //updateList(list)
    }

    private fun registerObserver() {

        viewModel.listDocument.observe(viewLifecycleOwner) {
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

        viewModel.downloadDocument.observe(viewLifecycleOwner) {
            if (it != null) {
                saveDownloadedRecord(it.healthRelatedDocument)
            }
        }

        viewModel.deleteDocument.observe(viewLifecycleOwner) {
            if (it != null) {
                val isProcessed = it.isProcessed
                if (isProcessed.equals(Constants.TRUE, ignoreCase = true)) {
                    binding.rvHealthRecords.layoutAnimation = animation
                    healthRecordsAdapter!!.removeItem(itemPosition)
                    binding.rvHealthRecords.scheduleLayoutAnimation()
                    Utilities.toastMessageShort(requireContext(),resources.getString(R.string.MSG_RECORD_DELETED))
                    itemPosition = -1
                }
            }
        }

    }

    private fun updateList(list: MutableList<ListDocumentModel.HealthRelatedDocument>) {
        if ( !list.isNullOrEmpty() ) {
            binding.rvHealthRecords.visibility = View.VISIBLE
            binding.layoutNoData.visibility = View.GONE
            //binding.rvHealthRecords.layoutAnimation = animation
            healthRecordsAdapter!!.updateList(list)
            binding.rvHealthRecords.scheduleLayoutAnimation()
        } else {
            binding.rvHealthRecords.visibility = View.GONE
            binding.layoutNoData.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onHealthRecordClick(position: Int,action:String,rec:ListDocumentModel.HealthRelatedDocument) {
        try {
            Utilities.printLogError("action--->$action")
            Utilities.printData("Record",rec,true)
            record = rec
            dialogClickType = action
            when(action) {
                Constants.DELETE -> {
                    itemPosition = position
                    showDialog(
                        listener = this,
                        title = this.resources.getString(R.string.DELETE_RECORD),
                        message = this.resources.getString(R.string.MSG_DELETE_SHARED_CONFIRMATION) + " ${rec.fileName}",
                        leftText = this.resources.getString(R.string.NO),
                        rightText = this.resources.getString(R.string.YES))
                }
                Constants.VIEW -> {
                    //extension = fileUtils.getFileExt(rec.title).toUpperCase()
                    // recordInSession = createRecordInSession(rec.title,rec.title,Utilities.getAppFolderLocation(requireContext()),Utilities.getDocumentTypeFromExt(extension))
                    extension = fileUtils.getFileExt(rec.title).toUpperCase()
                    recordInSession = createRecordInSession(rec.title,rec.title,Utilities.getAppFolderLocation(requireContext()),Utilities.getDocumentTypeFromExt(extension))
                    if ( !Utilities.isNullOrEmpty(rec.title) && File(path,rec.title).exists()) {
                        Helper.viewRecord(requireContext(),recordInSession)
                    } else {
                        viewModel.callDownloadDocumentApi(rec.id)
                        //viewModel.callDownloadDocumentApi(rec.documentID)
                    }
                }
            }
        } catch ( e : Exception) {
            e.printStackTrace()
        }
    }

    override fun onDialogClickListener(isButtonLeft: Boolean, isButtonRight: Boolean) {
        if ( isButtonRight ) {
            //viewModel.callDeleteDocumentApi(record.documentID)
            viewModel.callDeleteDocumentApi(record.id)
        }
        if ( isButtonLeft ) {
            itemPosition = -1
        }
    }

    private fun saveDownloadedRecord( document: DownloadRecordModel.HealthRelatedDocument ) {
        try {
            //extension = fileUtils.getFileExt(document.title).toUpperCase()
            //recordInSession = createRecordInSession(document.title,document.title,Utilities.getAppFolderLocation(requireContext()),Utilities.getDocumentTypeFromExt(extension))
            extension = fileUtils.getFileExt(document.title).toUpperCase()
            recordInSession = createRecordInSession(document.title,document.title,Utilities.getAppFolderLocation(requireContext()),Utilities.getDocumentTypeFromExt(extension))
            val byteArray = document.fileBytes
            val decodedString = Base64.decode(byteArray, Base64.DEFAULT)
            if (decodedString != null) {
                val saveRecord = fileUtils.saveByteArrayToExternalStorage(requireContext(),decodedString,document.title)
                if ( saveRecord != null ) {
                    Helper.viewRecord(requireContext(),recordInSession)
                }
            }
        } catch ( e : Exception) {
            e.printStackTrace()
        }
    }

    private fun createRecordInSession( OriginalFileName:String,Name:String,Path:String,ImageType:String ) : RecordInSession {
        //val id = (0..100000).random().toString()
        return RecordInSession(
            Name = Name ,
            Id = "0" ,
            OriginalFileName = OriginalFileName ,
            Path = Path ,
            Type = ImageType)
    }

    private fun startShimmer() {
        binding.layoutHealthRecordsShimmer.startShimmer()
        binding.layoutHealthRecordsShimmer.visibility = View.VISIBLE
    }

    private fun stopShimmer() {
        binding.layoutHealthRecordsShimmer.stopShimmer()
        binding.layoutHealthRecordsShimmer.visibility = View.GONE
    }

}