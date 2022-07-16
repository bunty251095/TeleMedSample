package com.vivant.telemedicine.ui.appointmentDetails

import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.vivant.telemedicine.R
import com.vivant.telemedicine.adapter.ChooseTimeSlotAdapter
import com.vivant.telemedicine.base.BaseFragment
import com.vivant.telemedicine.base.BaseViewModel
import com.vivant.telemedicine.common.*
import com.vivant.telemedicine.databinding.FragmentBookFollowupBinding
import com.vivant.telemedicine.model.BookAppointmentModel
import com.vivant.telemedicine.model.GetDoctorSlotsModel
import com.vivant.telemedicine.viewmodel.ConsultAndScheduleViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class BookFollowupFragment : BaseFragment(), ChooseTimeSlotAdapter.OnTimeSlotClickListener {

    private var _binding: FragmentBookFollowupBinding? = null
    private val binding get() = _binding!!

    val consultAndScheduleViewModel: ConsultAndScheduleViewModel by viewModel()
    private val consultAndScheduleSingleton = ConsultAndScheduleSingleton.instance!!
    //private val appointmentDetailsSingleton = AppointmentDetailsSingleton.instance!!
    val consultation = AppointmentDetailsSingleton.instance!!.consultation

    private var from = ""
    private var totalPrice = 0.0
    private var walletAmount = 0.0
    private var updatedBalance = 0.0
    private var supplierId = ""

    private var audioCharge = ""
    private var videoCharge = ""
    private var chatCharge = ""

    private val currentDate = DateHelper.currentDateAsStringyyyyMMdd
    private var selectedDate = DateHelper.currentDateAsStringyyyyMMdd
    private var mode = ""
    private var timeSlot = ""
    private var duration = ""
    private val cal = Calendar.getInstance()
    private val df1 = SimpleDateFormat(DateHelper.SERVER_DATE_YYYYMMDD, Locale.ENGLISH)

    private val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
    private val currentTime = dateFormat.parse(dateFormat.format(Date()))
    private lateinit var slotTime : Date
    private var differenceInMinutes = 0

    private val morningSlotList: MutableList<GetDoctorSlotsModel.TimeSlot> = mutableListOf()
    private val afternoonSlotList: MutableList<GetDoctorSlotsModel.TimeSlot> = mutableListOf()
    private val eveningSlotList: MutableList<GetDoctorSlotsModel.TimeSlot> = mutableListOf()

    private var chooseMorningTimeSlotAdapter: ChooseTimeSlotAdapter? = null
    private var chooseAfternoonTimeSlotAdapter: ChooseTimeSlotAdapter? = null
    private var chooseEveningTimeSlotAdapter: ChooseTimeSlotAdapter? = null
    private var appointment = BookAppointmentModel.AppointmentDetails()

    override fun getViewModel(): BaseViewModel = consultAndScheduleViewModel

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
                        Constants.BOOK_FOLLOW_UP -> {
                            //(activity as AppointmentDetailsActivity).routeToHomeScreen()
                            requireActivity().finish()
                        }
                        else -> {
                            findNavController().navigate(R.id.action_bookFollowupFragment_to_appointmentDetailsFragment)
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
        _binding = FragmentBookFollowupBinding.inflate(inflater, container, false)
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
        mode = consultation.appointmentMode
        consultAndScheduleViewModel.callGetRequestDetailsApi(consultation.doctorID,consultation.appointmentDate)

        binding.txtDate.text = DateHelper.currentDateAsStringddMMMyyyyNew

        chooseMorningTimeSlotAdapter = ChooseTimeSlotAdapter(requireContext(),Constants.MORNING,this)
        binding.rvMorningSlots.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
        binding.rvMorningSlots.adapter = chooseMorningTimeSlotAdapter

        chooseAfternoonTimeSlotAdapter = ChooseTimeSlotAdapter(requireContext(),Constants.AFTERNOON,this)
        binding.rvAfternoonSlots.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
        binding.rvAfternoonSlots.adapter = chooseAfternoonTimeSlotAdapter

        chooseEveningTimeSlotAdapter = ChooseTimeSlotAdapter(requireContext(),Constants.EVENING,this)
        binding.rvEveningSlots.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
        binding.rvEveningSlots.adapter = chooseEveningTimeSlotAdapter
        resetAllSlots()
    }

    private fun setClickable() {

        binding.layoutDate.setOnClickListener {
            showDatePicker()
        }

        binding.btnAudio.setOnClickListener {
            selectAudio()
        }

        binding.btnVideo.setOnClickListener {
            selectVideo()
        }

        binding.btnChat.setOnClickListener {
            selectChat()
        }

        binding.btnConfirm.setOnClickListener {
            if ( !Utilities.isNullOrEmpty(timeSlot) ) {
                consultAndScheduleSingleton.doctorId = consultation.doctorID.toInt()
                consultAndScheduleSingleton.supplierId = supplierId.toInt()
                consultAndScheduleSingleton.appointmentDate = selectedDate
                consultAndScheduleSingleton.appointmentTime = timeSlot
                consultAndScheduleSingleton.appointmentDuration = duration
                consultAndScheduleSingleton.consultationMode = mode
                consultAndScheduleSingleton.totalPrice = totalPrice.toString()
                consultAndScheduleViewModel.callBookAppointmentApi(Constants.BOOK_FOLLOW_UP,walletAmount.toString())
            } else {
                Utilities.toastMessageShort(requireContext(),resources.getString(R.string.PLEASE_SELECT_APPOINTMENT_TIME))
            }
        }

        binding.btnCancel.setOnClickListener {
            when(from) {
                Constants.BOOK_FOLLOW_UP -> {
                    //(activity as AppointmentDetailsActivity).routeToHomeScreen()
                    requireActivity().finish()
                }
                else -> {
                    findNavController().navigate(R.id.action_bookFollowupFragment_to_appointmentDetailsFragment)
                }
            }
        }

    }

    private fun registerObserver() {

        consultAndScheduleViewModel.getRequestDetails.observe(viewLifecycleOwner) {
            if (it != null) {
                val doctor = it.requestDetails
                supplierId = doctor.supplierDetails.supplierID
                Utilities.printLogError("supplierId--->$supplierId")
                if ( doctor.consultationModeDetails.any { it.consultationMode == "A" } ) {
                    audioCharge = doctor.consultationModeDetails.filter { it.consultationMode == "A" }[0].consultationCharges
                    binding.btnAudio.visibility = View.VISIBLE
                } else {
                    binding.btnAudio.visibility = View.GONE
                }

                if ( doctor.consultationModeDetails.any { it.consultationMode == "V" } ) {
                    videoCharge = doctor.consultationModeDetails.filter { it.consultationMode == "V" }[0].consultationCharges
                    binding.btnVideo.visibility = View.VISIBLE
                } else {
                    binding.btnVideo.visibility = View.GONE
                }

                if ( doctor.consultationModeDetails.any { it.consultationMode == "C" } ) {
                    chatCharge = doctor.consultationModeDetails.filter { it.consultationMode == "C" }[0].consultationCharges
                    binding.btnChat.visibility = View.VISIBLE
                } else {
                    binding.btnChat.visibility = View.GONE
                }

                if ( !Utilities.isNullOrEmpty(mode) ) {
                    when(mode) {
                        Constants.AUDIO_CALL -> {
                            if ( !Utilities.isNullOrEmpty(audioCharge) ) { selectAudio() }
                        }
                        Constants.VIDEO_CALL -> {
                            if ( !Utilities.isNullOrEmpty(videoCharge) ) { selectVideo() }
                        }
                        Constants.TEXT_CHAT -> {
                            if ( !Utilities.isNullOrEmpty(chatCharge) ) { selectChat() }
                        }
                    }
                }

                if ( doctor.profileImage.toString() != "" ) {
                    if ( !Utilities.isNullOrEmpty(doctor.profileImage.fileBytes) ) {
                        val abc = Base64.decode(doctor.profileImage.fileBytes, Base64.DEFAULT)
                        binding.imgDoctor.setImageBitmap(BitmapFactory.decodeByteArray(abc,0,abc.size) )
                    } else {
                        binding.imgDoctor.setImageResource(R.drawable.img_my_profile)
                    }
                }
                if ( !Utilities.isNullOrEmpty(doctor.name) ) {
                    binding.txtDoctorName.text = "${resources.getString(R.string.DR)} ${doctor.name}"
                } else {
                    binding.txtDoctorName.text = ""
                }
                if ( !Utilities.isNullOrEmpty(doctor.specialization) ) {
                    binding.txtDoctorSpeciality.text = doctor.specialization
                    binding.txtDoctorSpeciality.visibility = View.VISIBLE
                } else {
                    binding.txtDoctorSpeciality.visibility = View.GONE
                }
                if ( !Utilities.isNullOrEmpty(doctor.experience) ) {
                    binding.txtDoctorExperience.text = "${doctor.experience} ${resources.getString(R.string.YRS)}"
                    binding.view2.visibility = View.VISIBLE
                    binding.txtDoctorExperience.visibility = View.VISIBLE
                } else {
                    binding.txtDoctorExperience.text = ""
                    binding.view2.visibility = View.GONE
                    binding.txtDoctorExperience.visibility = View.GONE
                }
                binding.txtDoctorGender.text = Helper.getGenderDisplayValue(doctor.gender,requireContext())
                consultAndScheduleViewModel.callGetWalletApi()
            }
        }

        consultAndScheduleViewModel.getWallet.observe(viewLifecycleOwner) {
            if (it != null) {
                Utilities.printData("getWallet",it,true)
                if ( !it.report.isNullOrEmpty() ) {
                    //totalPrice = consultAndScheduleSingleton.totalPrice.toDouble()
                    //totalPrice = consultation.appointmentDetails.finalPrice.toDouble()
                    when(mode) {
                        Constants.AUDIO_CALL -> totalPrice = audioCharge.toDouble()
                        Constants.VIDEO_CALL -> totalPrice = videoCharge.toDouble()
                        Constants.TEXT_CHAT -> totalPrice = chatCharge.toDouble()
                    }
                    walletAmount = it.report[0].walletAmount.toDouble()
                    //walletAmount = 10.0
                    consultAndScheduleSingleton.walletAmount = walletAmount.toString()

                    binding.txtTotal.text = "RM $totalPrice"
                    binding.txtCompanyCover.text = "RM $walletAmount"
                    calculatePrice()
                }
                consultAndScheduleViewModel.callGetDoctorSlotsApi(
                    consultation.doctorID.toInt(),
                    consultation.supplierID,
                    selectedDate, mode)
            }
        }

        consultAndScheduleViewModel.getDoctorSlots.observe(viewLifecycleOwner) {
            if (it != null) {
                morningSlotList.clear()
                afternoonSlotList.clear()
                eveningSlotList.clear()
                val doctorSessionSlots = it.doctorSessionSlots
                Utilities.printLogError("currentTime--->$currentTime")
                if ( doctorSessionSlots.toString() != "" && !doctorSessionSlots.isNullOrEmpty() ) {
                    for ( i in  doctorSessionSlots ) {
                        when {
                            i.header.equals("Morning",ignoreCase = true) -> {
                                if ( currentDate == selectedDate ) {
                                    for ( timeSlot in i.timeSlot ) {
                                        slotTime = dateFormat.parse(timeSlot.slot)
                                        differenceInMinutes = ((currentTime!!.time - slotTime.time) / (60 * 1000)).toInt()
                                        if ( slotTime.after(currentTime) && differenceInMinutes <= -15  ) {
                                            morningSlotList.add(timeSlot)
                                        }
                                    }
                                } else {
                                    morningSlotList.addAll(i.timeSlot)
                                }
                            }
                            i.header.equals("Afternoon",ignoreCase = true) -> {
                                if ( currentDate == selectedDate ) {
                                    for ( timeSlot in i.timeSlot ) {
                                        slotTime = dateFormat.parse(timeSlot.slot)
                                        differenceInMinutes = ((currentTime!!.time - slotTime.time) / (60 * 1000)).toInt()
                                        if ( slotTime.after(currentTime) && differenceInMinutes <= -15  ) {
                                            afternoonSlotList.add(timeSlot)
                                        }
                                    }
                                } else {
                                    afternoonSlotList.addAll(i.timeSlot)
                                }
                            }
                            i.header.equals("Evening",ignoreCase = true) -> {
                                if ( currentDate == selectedDate ) {
                                    for ( timeSlot in i.timeSlot ) {
                                        slotTime = dateFormat.parse(timeSlot.slot)
                                        differenceInMinutes = ((currentTime!!.time - slotTime.time) / (60 * 1000)).toInt()
                                        if ( slotTime.after(currentTime) && differenceInMinutes <= -15  ) {
                                            eveningSlotList.add(timeSlot)
                                        }
                                    }
                                } else {
                                    eveningSlotList.addAll(i.timeSlot)
                                }
                            }
                        }
                    }

                    if ( morningSlotList.isNotEmpty() ) {
                        chooseMorningTimeSlotAdapter!!.updateList(morningSlotList)
                        binding.txtMorningSlotCount.text = "(${morningSlotList.size} ${resources.getString(R.string.SLOTS)})"
                        binding.layoutMorningSlots.visibility = View.VISIBLE
                        binding.layoutNoSlots.visibility = View.GONE
                        //binding.btnNext.visibility = View.VISIBLE
                    } else {
                        binding.layoutMorningSlots.visibility = View.GONE
                    }

                    if ( afternoonSlotList.isNotEmpty() ) {
                        chooseAfternoonTimeSlotAdapter!!.updateList(afternoonSlotList)
                        binding.txtAfternoonSlotCount.text = "(${afternoonSlotList.size} ${resources.getString(R.string.SLOTS)})"
                        binding.layoutAfternoonSlots.visibility = View.VISIBLE
                        binding.layoutNoSlots.visibility = View.GONE
                        //binding.btnNext.visibility = View.VISIBLE
                    } else {
                        binding.layoutAfternoonSlots.visibility = View.GONE
                    }

                    if ( eveningSlotList.isNotEmpty() ) {
                        chooseEveningTimeSlotAdapter!!.updateList(eveningSlotList)
                        binding.txtEveningSlotCount.text = "(${eveningSlotList.size} ${resources.getString(R.string.SLOTS)})"
                        binding.layoutEveningSlots.visibility = View.VISIBLE
                        binding.layoutNoSlots.visibility = View.GONE
                        //binding.btnNext.visibility = View.VISIBLE
                    } else {
                        binding.layoutEveningSlots.visibility = View.GONE
                    }

                    if ( morningSlotList.isEmpty()
                        && afternoonSlotList.isEmpty()
                        && eveningSlotList.isEmpty() ) {
                        binding.layoutMorningSlots.visibility = View.GONE
                        binding.layoutAfternoonSlots.visibility = View.GONE
                        binding.layoutEveningSlots.visibility = View.GONE
                        binding.txtFollowupDetails.visibility = View.GONE
                        //binding.btnNext.visibility = View.GONE
                        binding.layoutNoSlots.visibility = View.VISIBLE
                    }

                } else {
                    binding.layoutMorningSlots.visibility = View.GONE
                    binding.layoutAfternoonSlots.visibility = View.GONE
                    binding.layoutEveningSlots.visibility = View.GONE
                    binding.txtFollowupDetails.visibility = View.GONE
                    //binding.btnNext.visibility = View.GONE
                    binding.layoutNoSlots.visibility = View.VISIBLE
                }
            }
        }

        consultAndScheduleViewModel.bookAppointment.observe(viewLifecycleOwner) {
            if (it != null) {
                appointment = it.response.appointmentDetails
                Utilities.printLogError("AppointmentId----->${appointment.id}")
                if ( !Utilities.isNullOrEmptyOrZero(it.response.appointmentDetails.id.toString()) ) {
                    consultAndScheduleViewModel.callGenerateOnBookingApi(it.response.appointmentDetails.id)
                }
            }
        }

        consultAndScheduleViewModel.generateOnBooking.observe(viewLifecycleOwner) {
            if (it != null) {
                Utilities.printLogError("OrderID----->${it.orderDetails.orderID}")
                if ( !Utilities.isNullOrEmptyOrZero(it.orderDetails.orderID.toString()) ) {
                    //consultAndScheduleViewModel.callUpdateWalletApi(600.0)
                    consultAndScheduleViewModel.callUpdateWalletApi(updatedBalance)
                }
            }
        }

        consultAndScheduleViewModel.updateWallet.observe(viewLifecycleOwner) {
            if (it != null) {
                if ( it.isSuccess.equals(Constants.TRUE,ignoreCase = true) ) {
                    openAnotherActivity(destination = NavigationConstants.BOOKING, clearTop = true) {
                        putString(Constants.FROM,Constants.SCHEDULE_APPOINTMENT)
                    }
                    //openAnotherActivity(destination = NavigationConstants.HOME, clearTop = true)
                }
            }
        }

    }

    private fun calculatePrice() {
        if ( walletAmount >= totalPrice ) {
            binding.txtSelfPay.text = "RM 0"
            updatedBalance = walletAmount - totalPrice
            consultAndScheduleSingleton.selfPay = "0"
        } else {
            binding.txtSelfPay.text = "RM ${Utilities.roundOffPrecision(totalPrice - walletAmount,2)}"
            updatedBalance = 0.0
            consultAndScheduleSingleton.selfPay = (totalPrice - walletAmount).toString()
        }
    }

    private fun showDatePicker() {
        try {
            cal.time = df1.parse(selectedDate)!!
            DialogHelper().showDatePickerDialog(resources.getString(R.string.SELECT_DATE),
                requireContext(),cal, Calendar.getInstance(),null,

                object : DialogHelper.DateListener {

                    override fun onDateSet(date: String, year: String, month: String, dayOfMonth: String) {
                        selectedDate = DateHelper.convertDateSourceToDestination(date, DateHelper.DISPLAY_DATE_DDMMMYYYY, DateHelper.SERVER_DATE_YYYYMMDD)
                        Utilities.printLogError("SelectedDate--->$selectedDate")
                        //val date = year + "-" + (month + 1) + "-" + dayOfMonth
                        binding.txtDate.text = DateHelper.formatDateValue(DateHelper.DATEFORMAT_DDMMMYYYY_NEW, date)
                        consultAndScheduleViewModel.callGetDoctorSlotsApi(
                            consultation.doctorID.toInt(),
                            consultation.supplierID,
                            selectedDate,mode)
                        resetAllSlots()
                    }
                })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun resetAllSlots() {
        //binding.lblPrefferedSlots.visibility = View.GONE
        binding.layoutMorningSlots.visibility = View.GONE
        binding.layoutAfternoonSlots.visibility = View.GONE
        binding.layoutEveningSlots.visibility = View.GONE
        binding.txtFollowupDetails.visibility = View.GONE
        //binding.btnConfirm.visibility = View.INVISIBLE
        timeSlot = ""
        chooseMorningTimeSlotAdapter!!.resetList()
        chooseAfternoonTimeSlotAdapter!!.resetList()
        chooseEveningTimeSlotAdapter!!.resetList()
    }

    private fun selectAudio() {
        mode = Constants.AUDIO_CALL
        totalPrice = audioCharge.toDouble()
        binding.txtTotal.text = "RM $totalPrice"
        calculatePrice()
        binding.btnAudio.background = ContextCompat.getDrawable(requireContext(),R.drawable.btn_fill_selected)
        binding.btnVideo.background = ContextCompat.getDrawable(requireContext(),R.drawable.btn_border_normal)
        binding.btnChat.background = ContextCompat.getDrawable(requireContext(),R.drawable.btn_border_normal)
        ImageViewCompat.setImageTintList(binding.imgAudio, ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.white)))
        ImageViewCompat.setImageTintList(binding.imgVideo, ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.colorPrimary)))
        ImageViewCompat.setImageTintList(binding.imgChat, ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.colorPrimary)))
        binding.txtAudio.setTextColor( ContextCompat.getColor(requireContext(),R.color.white) )
        binding.txtVideo.setTextColor( ContextCompat.getColor(requireContext(),R.color.textViewColor) )
        binding.txtChat.setTextColor( ContextCompat.getColor(requireContext(),R.color.textViewColor) )
    }

    private fun selectVideo() {
        mode = Constants.VIDEO_CALL
        totalPrice = videoCharge.toDouble()
        binding.txtTotal.text = "RM $totalPrice"
        calculatePrice()
        binding.btnVideo.background = ContextCompat.getDrawable(requireContext(),R.drawable.btn_fill_selected)
        binding.btnAudio.background = ContextCompat.getDrawable(requireContext(),R.drawable.btn_border_normal)
        binding.btnChat.background = ContextCompat.getDrawable(requireContext(),R.drawable.btn_border_normal)
        ImageViewCompat.setImageTintList(binding.imgVideo, ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.white)))
        ImageViewCompat.setImageTintList(binding.imgAudio, ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.colorPrimary)))
        ImageViewCompat.setImageTintList(binding.imgChat, ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.colorPrimary)))
        binding.txtVideo.setTextColor( ContextCompat.getColor(requireContext(),R.color.white) )
        binding.txtAudio.setTextColor( ContextCompat.getColor(requireContext(),R.color.textViewColor) )
        binding.txtChat.setTextColor( ContextCompat.getColor(requireContext(),R.color.textViewColor) )
    }

    private fun selectChat() {
        mode = Constants.TEXT_CHAT
        totalPrice = chatCharge.toDouble()
        binding.txtTotal.text = "RM $totalPrice"
        calculatePrice()
        binding.btnChat.background = ContextCompat.getDrawable(requireContext(),R.drawable.btn_fill_selected)
        binding.btnAudio.background = ContextCompat.getDrawable(requireContext(),R.drawable.btn_border_normal)
        binding.btnVideo.background = ContextCompat.getDrawable(requireContext(),R.drawable.btn_border_normal)
        ImageViewCompat.setImageTintList(binding.imgChat, ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.white)))
        ImageViewCompat.setImageTintList(binding.imgAudio, ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.colorPrimary)))
        ImageViewCompat.setImageTintList(binding.imgVideo, ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.colorPrimary)))
        binding.txtChat.setTextColor( ContextCompat.getColor(requireContext(),R.color.white) )
        binding.txtAudio.setTextColor( ContextCompat.getColor(requireContext(),R.color.textViewColor) )
        binding.txtVideo.setTextColor( ContextCompat.getColor(requireContext(),R.color.textViewColor) )
    }

    override fun onTimeSlotSelection(type: String, slot: GetDoctorSlotsModel.TimeSlot) {
        timeSlot = slot.slot
        duration = slot.duration
        Utilities.printLogError("timeSlotType--->$type")
        Utilities.printLogError("timeSlot--->$timeSlot")
        binding.txtFollowupDetails.text = "Follow Up Appointment for ${binding.txtDate.text} at ${DateHelper.getTimeIn12HrFormatAmOrPm(timeSlot)}"
        binding.txtFollowupDetails.visibility = View.VISIBLE
        when(type) {
            Constants.MORNING -> {
                chooseAfternoonTimeSlotAdapter!!.resetList()
                chooseEveningTimeSlotAdapter!!.resetList()
            }
            Constants.AFTERNOON -> {
                chooseMorningTimeSlotAdapter!!.resetList()
                chooseEveningTimeSlotAdapter!!.resetList()
            }
            Constants.EVENING -> {
                chooseMorningTimeSlotAdapter!!.resetList()
                chooseAfternoonTimeSlotAdapter!!.resetList()
            }
        }
    }

}

/*        if ( consultation.doctorDetails.profileImage.toString() != "" ) {
            if ( !Utilities.isNullOrEmpty(consultation.doctorDetails.profileImage.fileBytes) ) {
                val abc = Base64.decode(consultation.doctorDetails.profileImage.fileBytes, Base64.DEFAULT)
                binding.imgDoctor.setImageBitmap(BitmapFactory.decodeByteArray(abc,0,abc.size) )
            } else {
                binding.imgDoctor.setImageResource(R.drawable.img_my_profile)
            }
        }

        if ( !Utilities.isNullOrEmpty(consultation.appointmentDetails.doctorName) ) {
            binding.txtDoctorName.text = "${resources.getString(R.string.DR)} ${consultation.appointmentDetails.doctorName}"
        } else {
            binding.txtDoctorName.text = ""
        }

        if ( !Utilities.isNullOrEmpty(consultation.doctorDetails.specialization) ) {
            binding.txtDoctorSpeciality.text = consultation.doctorDetails.specialization
            binding.txtDoctorSpeciality.visibility = View.VISIBLE
        } else {
            binding.txtDoctorSpeciality.visibility = View.GONE
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

        binding.txtDoctorGender.text = Helper.getGenderDisplayValue(consultation.doctorDetails.gender,requireContext())*/