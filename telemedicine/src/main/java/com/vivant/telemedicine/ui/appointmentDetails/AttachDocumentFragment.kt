package com.vivant.telemedicine.ui.appointmentDetails

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.vivant.telemedicine.R
import com.vivant.telemedicine.base.BaseFragment
import com.vivant.telemedicine.base.BaseViewModel
import com.vivant.telemedicine.common.*
import com.vivant.telemedicine.databinding.FragmentAttachDocumentBinding
import com.vivant.telemedicine.model.ListAppointmentsModel
import com.vivant.telemedicine.model.RecordInSession
import com.vivant.telemedicine.viewmodel.AppointmentDetailsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileInputStream

class AttachDocumentFragment : BaseFragment(),
    AttachDocumentOptionsBottomSheet.OnAttachDocumentOptionClickListener,
    UploadNewDocumentBottomSheet.OnSaveRecordClickListener {

    private var _binding: FragmentAttachDocumentBinding? = null
    private val binding get() = _binding!!

    val viewModel: AppointmentDetailsViewModel by viewModel()

    private var from = ""
    private var date = ""
    private var code = ""
    private var documentId = 0
    private var rsData = RecordInSession()
    private val appointment = AppointmentDetailsSingleton.instance!!.appointment
    private val permissionUtil = PermissionUtil
    private val mimeTypes = arrayOf("image/*", "application/pdf")
    private val fileUtils = FileUtils
    private val permissionListener = object : PermissionUtil.AppPermissionListener {
        override fun isPermissionGranted(isGranted: Boolean) {
            Utilities.printLogError("$isGranted")
            if ( isGranted ) {
                showFileChooser()
            }
        }
    }

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
                    when(from) {
                        Constants.ATTACH_DOCUMENT -> {
                            //(activity as AppointmentDetailsActivity).routeToHomeScreen()
                            requireActivity().finish()
                        }
                        else -> {
                            findNavController().navigate(R.id.action_attachDocumentFragment_to_appointmentDetailsFragment)
                        }
                    }
                }
            }
            requireActivity().onBackPressedDispatcher.addCallback(this, callback)

        } catch ( e : Exception ) {
            e.printStackTrace()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAttachDocumentBinding.inflate(inflater, container, false)
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

    }

    private fun setClickable() {

        binding.layoutUploadExistingDocument.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(Constants.FROM,from)
            findNavController().navigate(R.id.action_attachDocumentFragment_attachExisttingDocumentFragment,bundle)
        }

        binding.layoutUploadNewDocument.setOnClickListener {
            openBottomSheet()
        }

    }

    private fun registerObserver() {

        viewModel.saveDocuments.observe(viewLifecycleOwner) {
            if (it != null) {
                val document = it.healthDocuments[0]
                if ( !Utilities.isNullOrEmptyOrZero(document.documentID.toString()) ) {
                    documentId = document.documentID
                    viewModel.callShareDocumentsApi(appointment.iD,appointment.doctorID,appointment.firstName,
                        documentId,document.fileName,document.documentTypeCode)
                }
            }
        }

        viewModel.shareDocuments.observe(viewLifecycleOwner) {
            if (it != null) {
                if ( !it.documentSharingRecord.documentList.isNullOrEmpty()
                    && !Utilities.isNullOrEmptyOrZero(it.documentSharingRecord.documentList[0].logID.toString()) ) {
                    appointment.sharedDocuments.add(
                        ListAppointmentsModel.SharedDocument(
                        appointmentID = appointment.iD,
                        documentID = documentId,
                        documentTypeCode = code,
                        fileName = rsData.Name,
                        createdDate = date,
                        sharedDate = date
                    ))
                    Utilities.toastMessageShort(requireContext(),resources.getString(R.string.RECORD_SHARED_SUCCESS))
                    findNavController().navigate(R.id.action_attachDocumentFragment_to_appointmentDetailsFragment)
                }
            }
        }

    }

    private fun openBottomSheet() {
        val bottomSheet = AttachDocumentOptionsBottomSheet( this )
        bottomSheet.show(childFragmentManager,AttachDocumentOptionsBottomSheet.TAG)
    }

    private fun uploadNewDocumentBottomSheet(record : RecordInSession) {
        val bottomSheet = UploadNewDocumentBottomSheet( record,this)
        bottomSheet.show(childFragmentManager,UploadNewDocumentBottomSheet.TAG)
    }

    override fun onAttachDocumentOptionClick(docTypecode: String) {
        code = docTypecode
        Utilities.printLogError("Code--->$code")
        val permissionResult = permissionUtil.checkStoragePermission(permissionListener,requireContext())
        if (permissionResult) {
            showFileChooser()
        }
    }

    override fun onSaveRecordClick(selectedDate: String) {
        Utilities.printLogError("SelectedDate--->$selectedDate")
        date = selectedDate + "T" + DateHelper.currentTimeAs_hh_mm_ss
        var encodedFile =""
        val file = File(rsData.Path,rsData.OriginalFileName)
        if (file.exists()) {
            try {
                val bytesFile = ByteArray(file.length().toInt())
                requireContext().contentResolver.openFileDescriptor(Uri.fromFile(file), "r")?.use { parcelFileDescriptor ->
                    FileInputStream(parcelFileDescriptor.fileDescriptor).use { inStream ->
                        inStream.read(bytesFile)
                        encodedFile = Base64.encodeToString(bytesFile, Base64.DEFAULT)
                    }
                }
            } catch (e: Exception) {
                Utilities.printLogError("Error retrieving document to upload")
                e.printStackTrace()
            }
        } else {
            Utilities.printLogError("${rsData.OriginalFileName} : File not found")
        }
        viewModel.callSaveDocumentsApi(appointment.iD,appointment.doctorID,appointment.firstName,
            code,date,rsData.Name,encodedFile)
    }

    private fun showFileChooser()  {
        val chooserIntent = Intent(Intent.ACTION_GET_CONTENT)
        chooserIntent.type = "image/*|application/pdf"
        chooserIntent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes)
        startActivityForResult(Intent.createChooser(chooserIntent,resources.getString(R.string.SELECT_A_FILE)),Constants.FILE_SELECT_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        try {
            Utilities.printLogError("requestCode-> $requestCode")
            Utilities.printLogError("resultCode-> $resultCode")
            Utilities.printLogError("data-> $data")

            if (resultCode == Activity.RESULT_OK && data != null) {
                when (requestCode) {
                    Constants.FILE_SELECT_CODE -> {
                        try {
                            val uriFile = data.data
                            val filePath = fileUtils.getFilePath(requireContext(), uriFile!!)!!
                            val fileSize = fileUtils.calculateFileSize(filePath,"MB")
                            if (fileSize <= 5.0) {
                                val extension = fileUtils.getFileExt(filePath)
                                val origFileName = filePath.substring(filePath.lastIndexOf("/") + 1)
                                Utilities.printLogError("Extension : $extension")
                                if ( Utilities.isAcceptableDocumentType(extension) ) {
                                    //val fileName = fileUtils.generateUniqueFileName(resources.getString(R.string.app_name) + "_REC", filePath)
                                    Utilities.printLogError("File Path---> $filePath")
                                    val saveFile = fileUtils.saveRecordToExternalStorage(requireContext(),filePath,uriFile,origFileName)
                                    val mainDirectoryPath = Utilities.getAppFolderLocation(requireContext())
                                    if ( saveFile != null ) {
                                        rsData = createRecordInSession(origFileName,origFileName,mainDirectoryPath,Utilities.getDocumentTypeFromExt(extension),Uri.fromFile(saveFile))
                                        //rsData = createRecordInSession(origFileName,fileName,mainDirectoryPath,Utilities.getDocumentTypeFromExt(extension),Uri.fromFile(saveFile))
                                        Utilities.printData("Record",rsData,true)
                                        uploadNewDocumentBottomSheet(rsData)
                                    }
                                } else {
                                    Utilities.toastMessageLong(requireContext(), extension + " " + resources.getString(R.string.ERROR_FILES_NOT_ACCEPTED))
                                }
                            } else {
                                Utilities.toastMessageLong(requireContext(),resources.getString(R.string.ERROR_FILE_SIZE_LESS_THEN_5MB))
                            }
                        } catch ( e : Exception) {
                            Utilities.toastMessageShort(requireContext(),resources.getString(R.string.ERROR_UNABLE_TO_READ_FILE))
                            e.printStackTrace()
                        }
                    }

                }
            }
        } catch ( e : Exception) {
            e.printStackTrace()
        }
    }

    private fun createRecordInSession( OriginalFileName  :String ,Name: String , Path: String, ImageType: String,recordUri : Uri ) : RecordInSession {
        //val id = (0..100000).random().toString()
        return RecordInSession(
            Name = Name ,
            Id = "0" ,
            OriginalFileName = OriginalFileName ,
            Path = Path ,
            Type = ImageType ,
            FileUri = recordUri.toString(),
            Code = "" )
    }

}