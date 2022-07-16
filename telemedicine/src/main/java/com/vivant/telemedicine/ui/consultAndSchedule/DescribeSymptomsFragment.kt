package com.vivant.telemedicine.ui.consultAndSchedule

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.vivant.telemedicine.R
import com.vivant.telemedicine.adapter.UploadRecordAdapter
import com.vivant.telemedicine.base.BaseFragment
import com.vivant.telemedicine.base.BaseViewModel
import com.vivant.telemedicine.common.*
import com.vivant.telemedicine.databinding.FragmentDescribeSymptomsBinding
import com.vivant.telemedicine.model.RecordInSession
import com.vivant.telemedicine.ui.appointmentDetails.AttachDocumentOptionsBottomSheet
import com.vivant.telemedicine.viewmodel.ConsultAndScheduleViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DescribeSymptomsFragment : BaseFragment(),UploadRecordAdapter.OnRecordsUpdateListener,AttachDocumentOptionsBottomSheet.OnAttachDocumentOptionClickListener {

    private var _binding: FragmentDescribeSymptomsBinding? = null
    private val binding get() = _binding!!

    val viewModel: ConsultAndScheduleViewModel by viewModel()

    private var uploadRecordAdapter: UploadRecordAdapter? = null

    var from = ""
    var code = "PRE"
    private val consultAndScheduleSingleton = ConsultAndScheduleSingleton.instance!!
    private val permissionUtil = PermissionUtil
    private val fileUtils = FileUtils
    private val mimeTypes = arrayOf("image/*", "application/pdf")

    private val permissionListener = object : PermissionUtil.AppPermissionListener {
        override fun isPermissionGranted(isGranted: Boolean) {
            Utilities.printLogError("from----->$from")
            if ( isGranted ) {
                //showFileChooser()
                openBottomSheet()
            }
        }
    }

    override fun getViewModel(): BaseViewModel = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            // callback to Handle back button event
            val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    when(from) {
                        Constants.CONSULT_NOW -> {
                            (activity as ConsultAndScheduleActivity).setStepPositionConsultNow(Constants.STEP_TWO)
                            findNavController().navigate(R.id.action_describeSymptomsFragment_to_chooseSpecializationFragment)
                        }
                        Constants.SCHEDULE_APPOINTMENT -> {
                            (activity as ConsultAndScheduleActivity).setStepPositionScheduleAppointment(Constants.STEP_FOUR)
                            findNavController().navigate(R.id.action_describeSymptomsFragment_to_chooseSlotFragment)
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
        _binding = FragmentDescribeSymptomsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        try {
            from = (activity as ConsultAndScheduleActivity).from
            initialise()
        } catch ( e : Exception ) {
            e.printStackTrace()
        }
        return root
    }

    private fun initialise() {
        val recordsList = consultAndScheduleSingleton.recordsList
        Utilities.printData("RecordsList",recordsList,true)
        uploadRecordAdapter = UploadRecordAdapter("1",requireContext(),this)
        binding.rvUploadRecords.layoutManager = GridLayoutManager(requireContext(),3)
        binding.rvUploadRecords.adapter = uploadRecordAdapter

        if ( recordsList.isNotEmpty() ) {
            uploadRecordAdapter!!.updateList(recordsList)
            setListVisibility(true)
        }

        binding.layoutRecords.setOnClickListener {
            val permissionResult = permissionUtil.checkStoragePermission(permissionListener,requireContext())
            if (permissionResult) {
               // showFileChooser()
                openBottomSheet()
            }
        }

        binding.layoutAdd.setOnClickListener {
            val permissionResult = permissionUtil.checkStoragePermission(permissionListener,requireContext())
            if (permissionResult) {
                if ( uploadRecordAdapter!!.uploadRecordList.size < 3 ) {
                    //showFileChooser()
                    openBottomSheet()
                } else {
                    Utilities.toastMessageShort(requireContext(),resources.getString(R.string.ONLY_3_ATTACHMENTS_CAN_BE_ADDED))
                }
            }
        }

        binding.btnNext.setOnClickListener {

            if ( !Utilities.isNullOrEmpty(binding.edtCurrentMedicalProblem.text.toString()) ) {
                consultAndScheduleSingleton.currentMedicalHistory = binding.edtCurrentMedicalProblem.text.toString()
            } else {
                consultAndScheduleSingleton.currentMedicalHistory = resources.getString(R.string.NOT_MENTIONED)
            }

            if ( !Utilities.isNullOrEmpty(binding.edtCurrentMedication.text.toString()) ) {
                consultAndScheduleSingleton.currentMedication = binding.edtCurrentMedication.text.toString()
            } else {
                consultAndScheduleSingleton.currentMedication = resources.getString(R.string.NOT_MENTIONED)
            }

            when(from) {
                Constants.CONSULT_NOW -> {
                    (activity as ConsultAndScheduleActivity).setStepPositionConsultNow(Constants.STEP_FOUR)
                }
                Constants.SCHEDULE_APPOINTMENT -> {
                    (activity as ConsultAndScheduleActivity).setStepPositionScheduleAppointment(Constants.STEP_SIX)
                }
            }
            it.findNavController().navigate(R.id.action_describeSymptomsFragment_confirmAppointmentFragment)
        }

    }

    private fun addRecordToList(item: RecordInSession) {
        val list = uploadRecordAdapter!!.uploadRecordList
        uploadRecordAdapter!!.insertItem(item, list.size)
        //viewModel.saveRecordsInSession(item)
        setListVisibility(true)
    }

    private fun setListVisibility(needToShow: Boolean) {
        if ( needToShow ) {
            binding.rvUploadRecords.visibility = View.VISIBLE
            binding.layoutNoData.visibility = View.GONE

        } else {
            binding.rvUploadRecords.visibility = View.GONE
            binding.layoutNoData.visibility = View.VISIBLE
        }
    }

    private fun showFileChooser()  {
        val chooserIntent = Intent(Intent.ACTION_GET_CONTENT)
        chooserIntent.type = "image/*|application/pdf"
        chooserIntent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes)
        startActivityForResult(Intent.createChooser(chooserIntent,resources.getString(R.string.SELECT_A_FILE)),Constants.FILE_SELECT_CODE)
    }

    private fun showImageChooser() {
        val pickIntent = Intent(Intent.ACTION_PICK)
        pickIntent.type = "image/*"
        startActivityForResult(pickIntent,Constants.GALLERY_SELECT_CODE)
    }

    private fun openBottomSheet() {
        val bottomSheet = AttachDocumentOptionsBottomSheet( this )
        bottomSheet.show(childFragmentManager, AttachDocumentOptionsBottomSheet.TAG)
    }

    private fun createRecordInSession( OriginalFileName  :String ,Name: String , Path: String, ImageType: String,recordUri : Uri ) :RecordInSession {
        //val id = (0..100000).random().toString()
        return RecordInSession(
            Name = Name ,
            Id = "0" ,
            OriginalFileName = OriginalFileName ,
            Path = Path ,
            Type = ImageType ,
            FileUri = recordUri.toString(),
            Code = code )
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
                                Utilities.printLogError("Extension : $extension")
                                if ( Utilities.isAcceptableDocumentType(extension) ) {
                                    //val fileName = fileUtils.generateUniqueFileName(Configuration.strAppIdentifier + "_REC", filePath)
                                    val fileName = fileUtils.generateUniqueFileName(resources.getString(R.string.app_name) + "_REC", filePath)
                                    val origFileName = filePath.substring(filePath.lastIndexOf("/") + 1)
                                    Utilities.printLogError("File Path---> $filePath")
                                    val saveFile = fileUtils.saveRecordToExternalStorage(requireContext(),filePath,uriFile,origFileName)
                                    val mainDirectoryPath = Utilities.getAppFolderLocation(requireContext())
                                    if ( saveFile != null ) {
                                        val rsData = createRecordInSession( origFileName,fileName,mainDirectoryPath,Utilities.getDocumentTypeFromExt(extension), Uri.fromFile(saveFile))
                                        //Utilities.printData("Record",rsData,true)
                                        addRecordToList(rsData)
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

                    Constants.GALLERY_SELECT_CODE -> {
                        try {
                            val uriImage = data.data
                            val imagePath = fileUtils.getFilePath(requireContext(), uriImage!!)!!
                            val fileSize = fileUtils.calculateFileSize(imagePath,"MB")
                            if (fileSize <= 5.0) {
                                val extension1 = fileUtils.getFileExt(imagePath)
                                Utilities.printLogError("Extension : $extension1")
                                if ( Utilities.isAcceptableDocumentType(extension1) ) {
                                    val fileName = fileUtils.generateUniqueFileName(Configuration.strAppIdentifier + "_REC", imagePath)
                                    val origFileName = imagePath.substring(imagePath.lastIndexOf("/") + 1)
                                    Utilities.printLogError("File Path---> $imagePath")
                                    val saveImage = fileUtils.saveRecordToExternalStorage(requireContext(),imagePath,uriImage,origFileName)
                                    val mainDirectoryPath = Utilities.getAppFolderLocation(requireContext())
                                    if ( saveImage != null ) {
                                        val rsData = createRecordInSession(origFileName,fileName,mainDirectoryPath,Utilities.getDocumentTypeFromExt(extension1),Uri.fromFile(saveImage))
                                        //Utilities.printData("Record",rsData,true)
                                        addRecordToList(rsData)
                                    }
                                } else {
                                    Utilities.toastMessageLong(context, extension1 + " " + resources.getString(R.string.ERROR_FILES_NOT_ACCEPTED))
                                }
                            } else {
                                Utilities.toastMessageLong(context,resources.getString(R.string.ERROR_FILE_SIZE_LESS_THEN_5MB))
                            }
                        } catch (e: Exception) {
                            Utilities.toastMessageShort(context,resources.getString(R.string.ERROR_UNABLE_TO_READ_FILE))
                            e.printStackTrace()
                        }
                    }

                }
            }
        } catch ( e : Exception) {
            e.printStackTrace()
        }
    }

    override fun onRecordsUpdated(size: Int) {
        if (size > 0) {
            setListVisibility(true)
        } else {
            setListVisibility(false)
        }
    }

    override fun onAttachDocumentOptionClick(docTypecode: String) {
        code = docTypecode
        Utilities.printLogError("Code--->$code")
        showFileChooser()
    }

}