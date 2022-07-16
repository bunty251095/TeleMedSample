package com.vivant.telemedicine.ui.appointmentDetails

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.vivant.telemedicine.R
import com.vivant.telemedicine.adapter.*
import com.vivant.telemedicine.base.BaseFragment
import com.vivant.telemedicine.base.BaseViewModel
import com.vivant.telemedicine.common.*
import com.vivant.telemedicine.databinding.FragmentAppointmentDetailsBinding
import com.vivant.telemedicine.extension.showDialog
import com.vivant.telemedicine.model.*
import com.vivant.telemedicine.viewmodel.AppointmentDetailsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class AppointmentDetailsFragment : BaseFragment(),
    AppointmentOptionsBottomSheet.OnAppointmentOptionClickListener,
    CancelAppointmentDialog.OnCancelListener,
    SharedDocumentAdapter.OnHealthRecordClickListener,ReceivedDocumentAdapter.OnReceivedRecordClickListener,
    DefaultNotificationDialog.OnDialogValueListener,
    SharedConsultationDocumentAdapter.OnConsultationHealthRecordClickListener,
    FeedbackDialog.OnSubmitListener,OrderMedicineDialog.OnOrderConfirmListener {

    private var _binding: FragmentAppointmentDetailsBinding? = null
    private val binding get() = _binding!!

    val viewModel: AppointmentDetailsViewModel by viewModel()

    private var from = ""
    private var isHistory = false
    private var isFeedback = false
    private var itemPosition = -1
    private var selectedRecord = ListAppointmentsModel.SharedDocument()
    private var selectedConsultationRecord = ListConsultationModel.SharedDocument()

    private var isOrderMedicine = false

    private var showHide = false
    private var showHideSharedProblems = false
    private var showHideReceivedDocuments = false
    private var showHideSymptoms = false
    private var showHideMedication = false
    private var showHideAdvice= false
    private var showHideInvestigation = false

    private var orderMedicineDialog: OrderMedicineDialog? = null
    private var stateList: MutableList<AutocompleteTextViewModel> = mutableListOf()

    private val fileUtils = FileUtils
    private var recordInSession = RecordInSession()
    //private var extension = ""
    val appointment = AppointmentDetailsSingleton.instance!!.appointment
    val consultation = AppointmentDetailsSingleton.instance!!.consultation

    private lateinit var date :Array<String>

    private var cancelAppointmentDialog: CancelAppointmentDialog? = null
    private var sharedDocumentAdapter: SharedDocumentAdapter? = null
    private var sharedConsultationDocumentAdapter: SharedConsultationDocumentAdapter? = null
    private var receivedDocumentAdapter: ReceivedDocumentAdapter? = null

    private var symptomsAdapter: SymptomsAdapter? = null
    private var medicationsAdapter: MedicationsAdapter? = null
    private var adviceAdapter: AdviceAdapter? = null
    private var investigationsAdapter: InvestigationsAdapter? = null

    private var animation: LayoutAnimationController? = null

    private val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
    private val currentTime = dateFormat.parse(dateFormat.format(Date()))
    private lateinit var slotTime : Date
    private var differenceInMinutes = 0
    private val currentDate = DateHelper.currentDateAsStringyyyyMMdd

    override fun getViewModel(): BaseViewModel = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            requireArguments().let {
                from = it.getString(Constants.FROM, "")!!
                Utilities.printLogError("from----->$from")
            }
            when(from) {
                Constants.RESCHEDULE -> {
                    val bundle = Bundle()
                    bundle.putString(Constants.FROM,from)
                    findNavController().navigate(R.id.action_appointmentDetailsFragment_rescheduleFragment,bundle)
                }
                Constants.ATTACH_DOCUMENT -> {
                    val bundle = Bundle()
                    bundle.putString(Constants.FROM,from)
                    findNavController().navigate(R.id.action_appointmentDetailsFragment_attachDocumentFragment,bundle)
                }
                Constants.BOOK_FOLLOW_UP -> {
                    val bundle = Bundle()
                    bundle.putString(Constants.FROM,from)
                    findNavController().navigate(R.id.action_appointmentDetailsFragment_bookFollowupFragment,bundle)
                }
            }
            // callback to Handle back button event
            val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    when(from) {
                        Constants.UPCOMING,Constants.PAST,Constants.CONSULTATION -> {
                            requireActivity().finish()
                        }
                        else -> {
                            (activity as AppointmentDetailsActivity).routeToHomeScreen()
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
        _binding = FragmentAppointmentDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        try {
            animation = AnimationUtils.loadLayoutAnimation(requireContext(),R.anim.layout_animation_slide_from_bottom)
            when(from) {
                Constants.DASHBOARD,Constants.UPCOMING-> {
                    isHistory = false
                    binding.btnAttachDocument.visibility = View.VISIBLE
                    binding.btnMode.visibility = View.VISIBLE
                    binding.lblOrderMedicine.visibility = View.GONE
                    binding.btnPrescription.visibility = View.GONE
                    binding.btnBookFollowup.visibility = View.GONE
                    initialiseAppointmentDetails()
                }
                Constants.PAST-> {
                    isHistory = true
                    isFeedback = true
                    binding.btnAttachDocument.visibility = View.GONE
                    binding.btnMode.visibility = View.GONE
                    binding.lblOrderMedicine.visibility = View.GONE
                    binding.btnPrescription.visibility = View.GONE
                    binding.btnBookFollowup.visibility = View.GONE
                    initialiseAppointmentDetails()
                }
                Constants.CONSULTATION-> {
                    isHistory = true
                    isFeedback = consultation.isFeedbackAdded
                    binding.btnAttachDocument.visibility = View.GONE
                    binding.btnMode.visibility = View.GONE
                    binding.lblOrderMedicine.visibility = View.VISIBLE
                    binding.btnPrescription.visibility = View.VISIBLE
                    binding.btnBookFollowup.visibility = View.VISIBLE
                    initialiseConsultationDetails()
                }
            }
            setClickable()
            registerObserver()
        } catch ( e : Exception ) {
            e.printStackTrace()
        }
        return root
    }

    @SuppressLint("SetTextI18n")
    private fun initialiseAppointmentDetails() {
        binding.layoutRecord.visibility = View.GONE
        binding.layoutProblems.visibility = View.GONE
        binding.layoutReceivedDocuments.visibility = View.GONE
        binding.layoutSymptoms.visibility = View.GONE
        binding.layoutMedication.visibility = View.GONE
        binding.layoutAdvice.visibility = View.GONE
        binding.layoutInvestigation.visibility = View.GONE

        if ( appointment.profileImage.toString() != "" ) {
            if ( !Utilities.isNullOrEmpty(appointment.profileImage.fileBytes) ) {
                val abc = Base64.decode(appointment.profileImage.fileBytes, Base64.DEFAULT)
                binding.imgDoctor.setImageBitmap(BitmapFactory.decodeByteArray(abc,0,abc.size) )
            } else {
                binding.imgDoctor.setImageResource(R.drawable.img_my_profile)
            }
        }

        if ( !Utilities.isNullOrEmpty(appointment.appointmentMode) ) {
            binding.imgAppointmentMode.setImageResource(Helper.getModeImageHistory(appointment.appointmentMode))
            binding.imgAppointmentMode.visibility = View.VISIBLE
            binding.imgMode.setImageResource(Helper.getModeImage(appointment.appointmentMode))
            binding.txtMode.text = Helper.getDisplayMode(appointment.appointmentMode,requireContext())
        } else {
            binding.imgAppointmentMode.visibility = View.INVISIBLE
        }

        binding.txtDoctorName.text = "${resources.getString(R.string.DR)} ${appointment.firstName}"
        binding.txtDoctorSpeciality.text = appointment.specialization
        binding.txtDoctorGender.text = Helper.getGenderDisplayValue(appointment.doctorGender,requireContext())

        if ( !Utilities.isNullOrEmpty(appointment.yearsOfPractice) ) {
            binding.txtDoctorExperience.text = "${appointment.yearsOfPractice} ${resources.getString(R.string.YRS)}"
            //binding.view1.visibility = View.VISIBLE
            binding.txtDoctorExperience.visibility = View.VISIBLE
        } else {
            ///binding.view1.visibility = View.GONE
            binding.txtDoctorExperience.visibility = View.GONE
        }

        binding.txtPatientName.text = appointment.bookedForFirstName
        if ( appointment.bookingFor.equals("Self",ignoreCase = true) ) {
            binding.txtPatientName.text = viewModel.firstName
        }
        binding.txtAppointmentDate.text = "${DateHelper.formatDateValue(DateHelper.DATEFORMAT_DDMMMYYYY_NEW,appointment.appointmentDate)} at ${DateHelper.getTimeIn12HrFormatAmOrPm(appointment.appointmentStartTime!!)}"
        binding.txtConsultationMode.text = Helper.getDisplayMode(appointment.appointmentMode,requireContext())
        binding.txtConsultationFees.text = "RM ${appointment.consultationFees}"

        sharedDocumentAdapter = SharedDocumentAdapter(requireContext(),this)
        binding.rvHealthRecords.adapter = sharedDocumentAdapter
        sharedDocumentAdapter!!.updateList(appointment.sharedDocuments)

        enableOrDisableMode()
        refreshAppointmentRecordView()

        if ( appointment.problemStatement != null && appointment.problemStatement != "" ) {
            binding.txtMedicalProblem.text = Html.fromHtml( "<a><B><font color='#000000'>" + resources.getString(R.string.CURRENT_MEDICAL_PROBLEM) + "</font></B></a>" + " :\n " + appointment.problemStatement )
        } else {
            binding.txtMedicalProblem.text = Html.fromHtml( "<a><B><font color='#000000'>" + resources.getString(R.string.CURRENT_MEDICAL_PROBLEM) + "</font></B></a>" + " :\n " + resources.getString(R.string.NOT_MENTIONED) )
        }

        if ( appointment.ongoingMedication != null && appointment.ongoingMedication != "" ) {
            binding.txtCurrentMedication.text = Html.fromHtml( "<a><B><font color='#000000'>" + resources.getString(R.string.CURRENT_MEDICATION) + "</font></B></a>" + " :\n " + appointment.ongoingMedication )
        } else {
            binding.txtCurrentMedication.text = Html.fromHtml( "<a><B><font color='#000000'>" + resources.getString(R.string.CURRENT_MEDICATION) + "</font></B></a>" + " :\n " + resources.getString(R.string.NOT_MENTIONED) )
        }

        when(appointment.appointmentStatus) {
            "CAN" -> {
                binding.lblAttachDocuments.visibility = View.GONE
                binding.imgShowHide.visibility = View.GONE
                binding.layoutRecord.visibility = View.GONE
                binding.btnAttachDocument.visibility = View.GONE
                binding.btnMode.visibility = View.GONE
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initialiseConsultationDetails() {
        binding.layoutRecord.visibility = View.GONE
        binding.layoutProblems.visibility = View.GONE
        binding.rvReceivedDocuments.visibility = View.GONE
        binding.rvSymptoms.visibility = View.GONE
        binding.rvMedication.visibility = View.GONE
        binding.rvAdvice.visibility = View.GONE
        binding.rvInvestigation.visibility = View.GONE

        date = consultation.consultationDate.split(" ").toTypedArray()

        if ( consultation.doctorDetails.profileImage.toString() != "" ) {
            if ( !Utilities.isNullOrEmpty(consultation.doctorDetails.profileImage.fileBytes) ) {
                val abc = Base64.decode(consultation.doctorDetails.profileImage.fileBytes, Base64.DEFAULT)
                binding.imgDoctor.setImageBitmap(BitmapFactory.decodeByteArray(abc,0,abc.size) )
            } else {
                binding.imgDoctor.setImageResource(R.drawable.img_my_profile)
            }
        }

        if ( !Utilities.isNullOrEmpty(consultation.appointmentMode) ) {
            binding.imgAppointmentMode.setImageResource(Helper.getModeImageHistory(consultation.appointmentMode))
            binding.imgAppointmentMode.visibility = View.VISIBLE
            binding.imgMode.setImageResource(Helper.getModeImage(consultation.appointmentMode))
            binding.txtMode.text = Helper.getDisplayMode(consultation.appointmentMode,requireContext())
        } else {
            binding.imgAppointmentMode.visibility = View.INVISIBLE
        }

        if ( !Utilities.isNullOrEmpty(consultation.appointmentDetails.doctorName) ) {
            binding.txtDoctorName.text = "${resources.getString(R.string.DR)} ${consultation.appointmentDetails.doctorName}"
            binding.txtDoctorName.visibility = View.VISIBLE
        } else {
            binding.txtDoctorName.text = ""
            binding.txtDoctorName.visibility = View.GONE
        }

        if ( !Utilities.isNullOrEmpty(consultation.doctorDetails.specialization) ) {
            binding.txtDoctorSpeciality.text = consultation.doctorDetails.specialization
            binding.txtDoctorSpeciality.visibility = View.VISIBLE
        } else {
            binding.txtDoctorSpeciality.visibility = View.VISIBLE
        }

        if ( !Utilities.isNullOrEmpty(consultation.doctorDetails.experience) ) {
            binding.txtDoctorExperience.text = "${consultation.doctorDetails.experience} ${resources.getString(R.string.YRS)}"
            binding.view2.visibility = View.VISIBLE
            binding.txtDoctorExperience.visibility = View.VISIBLE
        } else {
            binding.txtDoctorExperience.text = ""
            binding.view2.visibility = View.GONE
            binding.txtDoctorExperience.visibility = View.GONE
        }

        binding.txtDoctorGender.text = Helper.getGenderDisplayValue(consultation.doctorDetails.gender,requireContext())

        binding.txtPatientName.text = consultation.bookedForFirstName
        //binding.txtAppointmentDate.text = "${DateHelper.formatDateValue(DateHelper.DATEFORMAT_DDMMMYYYY_NEW,appointment.appointmentDate)} at ${DateHelper.getTimeIn12HrFormatAmOrPm(appointment.appointmentStartTime)}"
        binding.txtAppointmentDate.text = "${DateHelper.convertDateSourceToDestination(date[0],DateHelper.DISPLAY_DATE_MMDDYYYY,DateHelper.DATEFORMAT_DDMMMYYYY_NEW)} at ${DateHelper.getTimeIn12HrFormatAmOrPm(date[1])}"
        //binding.txtAppointmentDate.text = consultation.appointmentDate
        binding.txtConsultationMode.text = Helper.getDisplayMode(consultation.appointmentMode,requireContext())
        binding.txtConsultationFees.text = "RM ${consultation.appointmentDetails.finalPrice}"

        sharedConsultationDocumentAdapter = SharedConsultationDocumentAdapter(requireContext(),this)
        binding.rvHealthRecords.adapter = sharedConsultationDocumentAdapter
        sharedConsultationDocumentAdapter!!.updateList(consultation.sharedDocuments)

        refreshConsultationRecordView()

        if ( consultation.appointmentDetails.problemStatement != null && consultation.appointmentDetails.problemStatement != "" ) {
            binding.txtMedicalProblem.text = Html.fromHtml( "<a><B><font color='#000000'>" + resources.getString(R.string.CURRENT_MEDICAL_PROBLEM) + "</font></B></a>" + " :\n " + consultation.appointmentDetails.problemStatement )
        } else {
            binding.txtMedicalProblem.text = Html.fromHtml( "<a><B><font color='#000000'>" + resources.getString(R.string.CURRENT_MEDICAL_PROBLEM) + "</font></B></a>" + " :\n " + resources.getString(R.string.NOT_MENTIONED) )
        }

        if ( consultation.appointmentDetails.ongoingMedication != null && consultation.appointmentDetails.ongoingMedication != "" ) {
            binding.txtCurrentMedication.text = Html.fromHtml( "<a><B><font color='#000000'>" + resources.getString(R.string.CURRENT_MEDICATION) + "</font></B></a>" + " :\n " + consultation.appointmentDetails.ongoingMedication )
        } else {
            binding.txtCurrentMedication.text = Html.fromHtml( "<a><B><font color='#000000'>" + resources.getString(R.string.CURRENT_MEDICATION) + "</font></B></a>" + " :\n " + resources.getString(R.string.NOT_MENTIONED) )
        }

        if ( consultation.attachments != null ) {
            binding.layoutReceivedDocuments.visibility = View.VISIBLE
            binding.rvReceivedDocuments.visibility = View.GONE
            receivedDocumentAdapter = ReceivedDocumentAdapter(requireContext(),this)
            binding.rvReceivedDocuments.adapter = receivedDocumentAdapter
            receivedDocumentAdapter!!.updateList(consultation.attachments)
        } else {
            binding.layoutReceivedDocuments.visibility = View.GONE
        }

        if ( consultation.symptoms != null ) {
            binding.layoutSymptoms.visibility = View.VISIBLE
            binding.rvSymptoms.visibility = View.GONE
            symptomsAdapter = SymptomsAdapter(requireContext())
            binding.rvSymptoms.adapter = symptomsAdapter
            symptomsAdapter!!.updateList(consultation.symptoms)
        } else {
            binding.layoutSymptoms.visibility = View.GONE
        }

        if ( consultation.medicines != null ) {
            binding.layoutMedication.visibility = View.VISIBLE
            binding.rvMedication.visibility = View.GONE
            medicationsAdapter = MedicationsAdapter(requireContext())
            binding.rvMedication.adapter = medicationsAdapter
            medicationsAdapter!!.updateList(consultation.medicines)
        } else {
            binding.layoutMedication.visibility = View.GONE
        }

        if ( consultation.advice != null ) {
            binding.layoutAdvice.visibility = View.VISIBLE
            binding.rvAdvice.visibility = View.GONE
            adviceAdapter = AdviceAdapter(requireContext())
            binding.rvAdvice.adapter = adviceAdapter
            adviceAdapter!!.updateList(consultation.advice)
        } else {
            binding.layoutAdvice.visibility = View.GONE
        }

        if ( consultation.investigation != null ) {
            binding.layoutInvestigation.visibility = View.VISIBLE
            binding.rvInvestigation.visibility = View.GONE
            investigationsAdapter = InvestigationsAdapter(requireContext())
            binding.rvInvestigation.adapter = investigationsAdapter
            investigationsAdapter!!.updateList(consultation.investigation)
        } else {
            binding.layoutInvestigation.visibility = View.GONE
        }

        if ( consultation.appointmentDetails != null ) {
            if ( consultation.appointmentDetails.isOrderMedicine ) {
                isOrderMedicine = true
                binding.lblOrderMedicine.text = resources.getString(R.string.MEDICINE_ORDER_PLACED)
                binding.lblOrderMedicine.setTextColor( ContextCompat.getColor(requireContext(),R.color.dark_green) )
                binding.lblOrderMedicine.background = ContextCompat.getDrawable(requireContext(),R.drawable.bg_transparant)
            } else {
                isOrderMedicine = false
                viewModel.callPatientStateListApi()
                binding.lblOrderMedicine.text = resources.getString(R.string.ORDER_MEDICINES_FROM_E_FARMA)
                binding.lblOrderMedicine.setTextColor( ContextCompat.getColor(requireContext(),R.color.colorPrimary) )
                binding.lblOrderMedicine.background = ContextCompat.getDrawable(requireContext(),R.drawable.btn_border_selected)
            }
        }

    }

    private fun setClickable() {

        binding.imgOption.setOnClickListener {
            val bottomSheet = AppointmentOptionsBottomSheet( this,appointment.appointmentStatus,isHistory,isFeedback)
            bottomSheet.show(childFragmentManager,AppointmentOptionsBottomSheet.TAG)
        }

        binding.imgShowHide.setOnClickListener {
            showHide = !showHide
            if ( showHide ) {
                binding.layoutRecord.visibility = View.VISIBLE
                binding.imgShowHide.setImageResource(R.drawable.img_up)
            } else {
                binding.layoutRecord.visibility = View.GONE
                binding.imgShowHide.setImageResource(R.drawable.img_down)
            }
        }

        binding.imgShowHideSharedProblems.setOnClickListener {
            showHideSharedProblems = !showHideSharedProblems
            if ( showHideSharedProblems ) {
                binding.layoutProblems.visibility = View.VISIBLE
                binding.imgShowHideSharedProblems.setImageResource(R.drawable.img_up)
            } else {
                binding.layoutProblems.visibility = View.GONE
                binding.imgShowHideSharedProblems.setImageResource(R.drawable.img_down)
            }
        }

        binding.imgShowHideReceivedDocuments.setOnClickListener {
            showHideReceivedDocuments = !showHideReceivedDocuments
            if ( showHideReceivedDocuments ) {
                binding.rvReceivedDocuments.visibility = View.VISIBLE
                binding.imgShowHideReceivedDocuments.setImageResource(R.drawable.img_up)
            } else {
                binding.rvReceivedDocuments.visibility = View.GONE
                binding.imgShowHideReceivedDocuments.setImageResource(R.drawable.img_down)
            }
        }

        binding.imgShowHideSymptoms.setOnClickListener {
            showHideSymptoms = !showHideSymptoms
            if ( showHideSymptoms ) {
                binding.rvSymptoms.visibility = View.VISIBLE
                binding.imgShowHideSymptoms.setImageResource(R.drawable.img_up)
            } else {
                binding.rvSymptoms.visibility = View.GONE
                binding.imgShowHideSymptoms.setImageResource(R.drawable.img_down)
            }
        }

        binding.imgShowHideMedication.setOnClickListener {
            showHideMedication = !showHideMedication
            if ( showHideMedication ) {
                Utilities.printData("medicines",consultation.medicines!!,true)
                binding.rvMedication.visibility = View.VISIBLE
                binding.imgShowHideMedication.setImageResource(R.drawable.img_up)
            } else {
                binding.rvMedication.visibility = View.GONE
                binding.imgShowHideMedication.setImageResource(R.drawable.img_down)
            }
        }

        binding.imgShowHideAdvice.setOnClickListener {
            showHideAdvice = !showHideAdvice
            if ( showHideAdvice ) {
                binding.rvAdvice.visibility = View.VISIBLE
                binding.imgShowHideAdvice.setImageResource(R.drawable.img_up)
            } else {
                binding.rvAdvice.visibility = View.GONE
                binding.imgShowHideAdvice.setImageResource(R.drawable.img_down)
            }
        }

        binding.imgShowHideInvestigation.setOnClickListener {
            showHideInvestigation = !showHideInvestigation
            if ( showHideInvestigation ) {
                binding.rvInvestigation.visibility = View.VISIBLE
                binding.imgShowHideInvestigation.setImageResource(R.drawable.img_up)
            } else {
                binding.rvInvestigation.visibility = View.GONE
                binding.imgShowHideInvestigation.setImageResource(R.drawable.img_down)
            }
        }

        binding.btnAttachDocument.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(Constants.FROM,from)
            findNavController().navigate(R.id.action_appointmentDetailsFragment_attachDocumentFragment,bundle)
        }

        binding.btnMode.setOnClickListener {
            openAnotherActivity(destination = NavigationConstants.JITSI) {
                putString(Constants.MODE,appointment.appointmentMode)
                putString(Constants.ROOM_NAME,appointment.roomName)
                //putString(Constants.MODE,Constants.VIDEO_CALL)
                //putString(Constants.ROOM_NAME,"testJitsimy1")
            }
        }

        binding.btnPrescription.setOnClickListener {
            viewModel.callDownloadPrescriptionApi(consultation.appointmentID,consultation.iD,consultation.doctorID)
        }

        binding.btnBookFollowup.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(Constants.FROM,from)
            findNavController().navigate(R.id.action_appointmentDetailsFragment_bookFollowupFragment,bundle)
        }

        binding.lblOrderMedicine.setOnClickListener {
            if ( !isOrderMedicine ) {
                showOrderMedicineDialog()
            }
        }

    }

    private fun registerObserver() {

        viewModel.cancelAppointment.observe(viewLifecycleOwner) {
            if (it != null) {
                if ( it.iStatusUpdated.equals(Constants.TRUE,ignoreCase = true) ) {
                    Utilities.toastMessageShort(requireContext(),resources.getString(R.string.APPOINTMENT_CANCELLED_SUCCESSFULLY))
                    (activity as AppointmentDetailsActivity).routeToHomeScreen()
                }
            }
        }

        viewModel.removeSharedDocument.observe(viewLifecycleOwner) {
            if (it != null) {
                if ( it.document.equals(Constants.TRUE,ignoreCase = true) ) {
                    Utilities.toastMessageShort(requireContext(),resources.getString(R.string.RECORD_REMOVED_SUCCESS))
                    binding.rvHealthRecords.layoutAnimation = animation
                    when(from) {
                        Constants.CONSULTATION-> {
                            sharedConsultationDocumentAdapter!!.removeItem(itemPosition)
                            binding.rvHealthRecords.scheduleLayoutAnimation()
                            itemPosition = -1
                            consultation.sharedDocuments.remove(selectedConsultationRecord)
                            refreshConsultationRecordView()
                        }
                        else -> {
                            sharedDocumentAdapter!!.removeItem(itemPosition)
                            binding.rvHealthRecords.scheduleLayoutAnimation()
                            itemPosition = -1
                            appointment.sharedDocuments.remove(selectedRecord)
                            refreshAppointmentRecordView()
                        }
                    }
                }
            }
        }

        viewModel.downloadInvoice.observe(viewLifecycleOwner) {
            if (it != null) {
                saveDownloadedInvoice(it)
            }
        }

        viewModel.downloadPrescription.observe(viewLifecycleOwner) {
            if (it != null) {
                savePrescription(it)
            }
        }

        viewModel.getConsultationHealthDocument.observe(viewLifecycleOwner) {
            if (it != null) {
                saveReceivedDocument(it.healthRelatedDocument)
            }
        }

        viewModel.saveFeedback.observe(viewLifecycleOwner) {
            if (it != null) {
                if ( it.isProcessed.equals(Constants.TRUE,ignoreCase = true) ) {
                    isFeedback = true
                    Utilities.toastMessageShort(requireContext(),resources.getString(R.string.FEEDBACK_SHARED_SUCCESSFULLY))
                }
            }
        }

        viewModel.patientStateList.observe(viewLifecycleOwner) {
            if (it != null) {
                stateList.clear()
                //stateList.addAll(it.stateList!!)
                for ( i in 0 until  it.stateList!!.size ) {
                    stateList.add( AutocompleteTextViewModel( id = i.toString() , name = it.stateList[i].stateName!! ) )
                }
                Utilities.printData("patientStateList",stateList,true)
            }
        }

        viewModel.sendEpharmaDetails.observe(viewLifecycleOwner) {
            if (it != null) {
                //savePrescription(it)
                if ( it.success.equals(Constants.TRUE,ignoreCase = false) ) {
                    isOrderMedicine = true
                    consultation.appointmentDetails.isOrderMedicine = true
                    binding.lblOrderMedicine.text = resources.getString(R.string.MEDICINE_ORDER_PLACED)
                    binding.lblOrderMedicine.setTextColor( ContextCompat.getColor(requireContext(),R.color.dark_green) )
                }
            }
        }

    }

    private fun enableOrDisableMode() {
        slotTime = dateFormat.parse(appointment.appointmentStartTime)!!
        differenceInMinutes = ((currentTime!!.time - slotTime.time) / (60 * 1000)).toInt()
        if ( currentDate == appointment.appointmentDate && differenceInMinutes >= -15  ) {
            binding.btnMode.background = ContextCompat.getDrawable(requireContext(),R.drawable.btn_fill_selected)
            binding.btnMode.isEnabled = true
        } else {
            binding.btnMode.background = ContextCompat.getDrawable(requireContext(),R.drawable.btn_fill_disabled_light_blue)
            binding.btnMode.isEnabled = false
        }
    }

    private fun showUpdateStatusDialog() {
        cancelAppointmentDialog = CancelAppointmentDialog(requireContext(),this)
        cancelAppointmentDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        cancelAppointmentDialog!!.show()
    }

    override fun onAppointmentOptionClick(action: String) {
        Utilities.printLogError("Action--->$action")
        when(action) {
            Constants.RESCHEDULE-> {
                findNavController().navigate(R.id.action_appointmentDetailsFragment_rescheduleFragment)
            }
            Constants.CANCEL-> {
                showUpdateStatusDialog()
            }
            Constants.VIEW_INVOICE-> {
                try {
                    when(from) {
                        Constants.CONSULTATION-> {
                            viewModel.callDownloadInvoiceApi(consultation.appointmentID.toInt(),consultation.orderID,consultation.doctorID.toInt(),consultation.supplierID)
                        }
                        else -> {
                            viewModel.callDownloadInvoiceApi(appointment.iD,appointment.orderID,appointment.doctorID,appointment.supplierID)
                        }
                    }
                } catch ( e : Exception ) {
                    e.printStackTrace()
                }
            }
            Constants.SAVE_FEEDBACK-> {
                FeedbackDialog(requireContext(),this).show()
            }
        }
    }

    override fun onCancelClickListener(reason: String) {
        viewModel.callCancelAppointmentApi(appointment.iD,reason)
    }

    private fun saveDownloadedInvoice( invoice: DownloadInvoiceResponse) {
        try {
            val byteArray = invoice.fileData
            val name = fileUtils.generateUniqueFileName( invoice.fileName ,".pdf" )
            recordInSession = createRecordInSession(name,name,Utilities.getAppFolderLocation(requireContext()),"PDF")
            val decodedString = Base64.decode(byteArray, Base64.DEFAULT)
            if (decodedString != null) {
                val saveRecord = fileUtils.saveByteArrayToExternalStorage(requireContext(),decodedString,name)
                if ( saveRecord != null ) {
                    Helper.viewRecord(requireContext(),recordInSession)
                }
            }
        } catch ( e : Exception) {
            e.printStackTrace()
        }
    }

    private fun savePrescription( invoice: DownloadPrescriptionResponse) {
        try {
            val byteArray = invoice.fileData
            val name = fileUtils.generateUniqueFileName( consultation.bookedForFirstName + "_" ,".pdf" )
            recordInSession = createRecordInSession(name,name,Utilities.getAppFolderLocation(requireContext()),"PDF")
            val decodedString = Base64.decode(byteArray, Base64.DEFAULT)
            if (decodedString != null) {
                val saveRecord = fileUtils.saveByteArrayToExternalStorage(requireContext(),decodedString,name)
                if ( saveRecord != null ) {
                    Helper.viewRecord(requireContext(),recordInSession)
                }
            }
        } catch ( e : Exception) {
            e.printStackTrace()
        }
    }

    private fun saveReceivedDocument( invoice: GetConsultationHealthDocumentModel.HealthRelatedDocument) {
        try {
            val byteArray = invoice.fileBytes
            //val name = fileUtils.generateUniqueFileName( invoice.fileName ,".pdf" )
            val name = fileUtils.generateUniqueFileName( "Received-Rec",invoice.fileName )
            recordInSession = createRecordInSession(name,name,Utilities.getAppFolderLocation(requireContext()),Utilities.findTypeOfDocument(invoice.fileName))
            val decodedString = Base64.decode(byteArray, Base64.DEFAULT)
            if (decodedString != null) {
                val saveRecord = fileUtils.saveByteArrayToExternalStorage(requireContext(),decodedString,name)
                if ( saveRecord != null ) {
                    Helper.viewRecord(requireContext(),recordInSession)
                }
            }
        } catch ( e : Exception) {
            e.printStackTrace()
        }
    }

/*    private fun saveDownloadedRecord( document: GetInvoiceModel.DoctorSign) {
        try {
            extension = fileUtils.getFileExt(document.fileName)
            val byteArray = document.fileBytes
            val name = fileUtils.generateUniqueFileName( document.fileName.split(".")[0] ,".$extension" )
            recordInSession = createRecordInSession(document.fileName,name,Utilities.getAppFolderLocation(requireContext()),Utilities.getDocumentTypeFromExt(extension))
            val decodedString = Base64.decode(byteArray, Base64.DEFAULT)
            if (decodedString != null) {
                val saveRecord = fileUtils.saveByteArrayToExternalStorage(requireContext(),decodedString,name)
                if ( saveRecord != null ) {
                    Helper.viewRecord(requireContext(),recordInSession)
                }
            }
        } catch ( e : Exception) {
            e.printStackTrace()
        }
    }*/

    private fun createRecordInSession( OriginalFileName:String,Name:String,Path:String,ImageType:String ) : RecordInSession {
        //val id = (0..100000).random().toString()
        return RecordInSession(
            Name = Name ,
            Id = "0" ,
            OriginalFileName = OriginalFileName ,
            Path = Path ,
            Type = ImageType)
    }

    override fun onHealthRecordClick(position: Int, action: String, rec: ListAppointmentsModel.SharedDocument) {
        selectedRecord = rec
        itemPosition = position
        showDialog(
            listener = this,
            title = this.resources.getString(R.string.DELETE_RECORD),
            message = this.resources.getString(R.string.MSG_DELETE_SHARED_CONFIRMATION) + " ${rec.fileName}",
            leftText = this.resources.getString(R.string.NO),
            rightText = this.resources.getString(R.string.YES))
    }

    override fun onConsultationHealthRecordClick(position: Int, action: String, rec: ListConsultationModel.SharedDocument) {
        selectedConsultationRecord = rec
        itemPosition = position
        showDialog(
            listener = this,
            title = this.resources.getString(R.string.DELETE_RECORD),
            message = this.resources.getString(R.string.MSG_DELETE_SHARED_CONFIRMATION) + " ${rec.fileName}",
            leftText = this.resources.getString(R.string.NO),
            rightText = this.resources.getString(R.string.YES))
    }

    override fun onReceivedRecordClick(position: Int, action: String, rec: ListConsultationModel.Attachments) {
        Utilities.printLogError("Action--->$action")
        Utilities.printData("ReceivedRecord",rec,true)
        viewModel.callGetConsultationHealthDocumentApi(rec.healthDocumentID)
    }

    override fun onDialogClickListener(isButtonLeft: Boolean, isButtonRight: Boolean) {
        if ( isButtonRight ) {
            when(from) {
                Constants.CONSULTATION-> {
                    viewModel.callRemoveSharedDocumentApi(selectedConsultationRecord.documentID)
                }
                else -> {
                    viewModel.callRemoveSharedDocumentApi(selectedRecord.documentID)
                }
            }
        }
        if ( isButtonLeft ) {
            itemPosition = -1
        }
    }

    private fun refreshAppointmentRecordView() {
        if ( !appointment.sharedDocuments.isNullOrEmpty() ) {
            binding.rvHealthRecords.visibility = View.VISIBLE
            binding.lblNoAttachments.visibility = View.GONE
        } else {
            binding.rvHealthRecords.visibility = View.GONE
            binding.lblNoAttachments.visibility = View.VISIBLE
        }
    }

    private fun refreshConsultationRecordView() {
        if ( !consultation.sharedDocuments.isNullOrEmpty() ) {
            binding.rvHealthRecords.visibility = View.VISIBLE
            binding.lblNoAttachments.visibility = View.GONE
        } else {
            binding.rvHealthRecords.visibility = View.GONE
            binding.lblNoAttachments.visibility = View.VISIBLE
        }
    }

    private fun showOrderMedicineDialog() {
        orderMedicineDialog = OrderMedicineDialog(requireContext(),this,stateList)
        orderMedicineDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        orderMedicineDialog!!.show()
    }

    override fun onSubmitClickListener(rating: Int, comment: String) {
        Utilities.printLogError("rating--->$rating")
        Utilities.printLogError("comment--->$comment")
        //viewModel.callSaveFeedbackApi(consultation.appointmentID,rating,comment)
    }

    override fun onOrderConfirmClickListener(address: String, city: String, state: String, postCode: String) {
        Utilities.printLogError("address--->$address")
        Utilities.printLogError("city--->$address")
        Utilities.printLogError("state--->$address")
        Utilities.printLogError("postCode--->$address")
        viewModel.callSendEpharmaDetailsApi(consultation,address,city,state,postCode)
    }

}