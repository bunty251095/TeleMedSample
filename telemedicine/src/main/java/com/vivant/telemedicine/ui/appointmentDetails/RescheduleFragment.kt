package com.vivant.telemedicine.ui.appointmentDetails

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.vivant.telemedicine.R
import com.vivant.telemedicine.adapter.ChooseTimeSlotAdapter
import com.vivant.telemedicine.base.BaseFragment
import com.vivant.telemedicine.base.BaseViewModel
import com.vivant.telemedicine.common.*
import com.vivant.telemedicine.databinding.FragmentRescheduleBinding
import com.vivant.telemedicine.model.GetDoctorSlotsModel
import com.vivant.telemedicine.viewmodel.AppointmentDetailsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class RescheduleFragment : BaseFragment() , ChooseTimeSlotAdapter.OnTimeSlotClickListener {

    private var _binding: FragmentRescheduleBinding? = null
    private val binding get() = _binding!!

    val viewModel: AppointmentDetailsViewModel by viewModel()

    private var chooseMorningTimeSlotAdapter: ChooseTimeSlotAdapter? = null
    private var chooseAfternoonTimeSlotAdapter: ChooseTimeSlotAdapter? = null
    private var chooseEveningTimeSlotAdapter: ChooseTimeSlotAdapter? = null

    private var from = ""
    val appointment = AppointmentDetailsSingleton.instance!!.appointment
    private val currentDate = DateHelper.currentDateAsStringyyyyMMdd
    private var selectedDate = DateHelper.currentDateAsStringyyyyMMdd
    private var timeSlotType = ""
    private var timeSlot = ""
    private var duration = ""
    private val cal = Calendar.getInstance()
    private val df1 = SimpleDateFormat(DateHelper.SERVER_DATE_YYYYMMDD, Locale.ENGLISH)

    private val morningSlotList: MutableList<GetDoctorSlotsModel.TimeSlot> = mutableListOf()
    private val afternoonSlotList: MutableList<GetDoctorSlotsModel.TimeSlot> = mutableListOf()
    private val eveningSlotList: MutableList<GetDoctorSlotsModel.TimeSlot> = mutableListOf()

    private val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
    private val currentTime = dateFormat.parse(dateFormat.format(Date()))
    private lateinit var slotTime : Date
    private var differenceInMinutes = 0

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
                        Constants.RESCHEDULE -> {
                            //(activity as AppointmentDetailsActivity).routeToHomeScreen()
                            requireActivity().finish()
                        }
                        else -> {
                            findNavController().navigate(R.id.action_rescheduleFragment_to_appointmentDetailsFragment)
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
        _binding = FragmentRescheduleBinding.inflate(inflater, container, false)
        val root: View = binding.root
        try {
            initialise()
            registerObserver()
            setClickable()
        } catch ( e : Exception ) {
            e.printStackTrace()
        }
        return root
    }

    private fun initialise() {
        resetAllSlots()

        if ( appointment.profileImage.toString() != "" ) {
            val abc = Base64.decode(appointment.profileImage.fileBytes, Base64.DEFAULT)
            binding.imgDoctor.setImageBitmap(BitmapFactory.decodeByteArray(abc,0,abc.size) )
        }

        binding.txtDoctorName.text = "${resources.getString(R.string.DR)} ${appointment.firstName}"
        binding.txtDoctorSpeciality.text = appointment.specialization
        binding.txtDoctorGender.text = Helper.getGenderDisplayValue(appointment.doctorGender,requireContext())

        if ( !Utilities.isNullOrEmpty(appointment.yearsOfPractice) ) {
            binding.txtDoctorAge.text = "${appointment.yearsOfPractice} ${resources.getString(R.string.YRS)}"
            binding.view1.visibility = View.VISIBLE
            binding.txtDoctorAge.visibility = View.VISIBLE
        } else {
            binding.view1.visibility = View.GONE
            binding.txtDoctorAge.visibility = View.GONE
        }

        binding.txtDate.text = DateHelper.currentDateAsStringddMMMyyyyNew

        chooseMorningTimeSlotAdapter = ChooseTimeSlotAdapter(requireContext(), Constants.MORNING,this)
        binding.rvMorningSlots.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
        binding.rvMorningSlots.adapter = chooseMorningTimeSlotAdapter

        chooseAfternoonTimeSlotAdapter = ChooseTimeSlotAdapter(requireContext(), Constants.AFTERNOON,this)
        binding.rvAfternoonSlots.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
        binding.rvAfternoonSlots.adapter = chooseAfternoonTimeSlotAdapter

        chooseEveningTimeSlotAdapter = ChooseTimeSlotAdapter(requireContext(), Constants.EVENING,this)
        binding.rvEveningSlots.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
        binding.rvEveningSlots.adapter = chooseEveningTimeSlotAdapter

        viewModel.callGetDoctorSlotsApi(DateHelper.currentDateAsStringyyyyMMdd,appointment.appointmentMode,appointment.doctorID,appointment.supplierID)

/*        val morningSlotList: MutableList<TimeSlotModel> = mutableListOf()
        morningSlotList.add(TimeSlotModel("07:00:00","07:00 AM"))
        morningSlotList.add(TimeSlotModel("07:15:00","07:15 AM"))
        morningSlotList.add(TimeSlotModel("07:30:00","07:30 AM"))
        morningSlotList.add(TimeSlotModel("07:45:00","07:45 AM"))
        morningSlotList.add(TimeSlotModel("08:00:00","08:00 AM"))
        morningSlotList.add(TimeSlotModel("08:15:00","08:15 AM"))
        morningSlotList.add(TimeSlotModel("08:30:00","08:30 AM"))
        chooseMorningTimeSlotAdapter!!.updateList(morningSlotList)

        val afternoonSlotList: MutableList<TimeSlotModel> = mutableListOf()
        afternoonSlotList.add(TimeSlotModel("12:00:00","12:00 PM"))
        afternoonSlotList.add(TimeSlotModel("12:15:00","12:15 PM"))
        afternoonSlotList.add(TimeSlotModel("12:30:00","12:30 PM"))
        afternoonSlotList.add(TimeSlotModel("12:45:00","12:45 PM"))
        afternoonSlotList.add(TimeSlotModel("13:00:00","01:00 PM"))
        chooseAfternoonTimeSlotAdapter!!.updateList(afternoonSlotList)

        val eveningSlotList: MutableList<TimeSlotModel> = mutableListOf()
        eveningSlotList.add(TimeSlotModel("16:00:00","04:00 PM"))
        eveningSlotList.add(TimeSlotModel("16:15:00","04:15 PM"))
        eveningSlotList.add(TimeSlotModel("16:30:00","04:30 PM"))
        eveningSlotList.add(TimeSlotModel("16:45:00","04:45 PM"))
        eveningSlotList.add(TimeSlotModel("17:00:00","05:00 PM"))
        chooseEveningTimeSlotAdapter!!.updateList(eveningSlotList)

        if ( morningSlotList.isNotEmpty() ) {
            binding.txtMorningSlotCount.text = "(${morningSlotList.size} ${resources.getString(R.string.SLOTS)})"
        }
        if ( morningSlotList.isNotEmpty() ) {
            binding.txtAfternoonSlotCount.text = "(${afternoonSlotList.size} ${resources.getString(R.string.SLOTS)})"
        }
        if ( morningSlotList.isNotEmpty() ) {
            binding.txtEveningSlotCount.text = "(${eveningSlotList.size} ${resources.getString(R.string.SLOTS)})"
        }*/

    }

    private fun setClickable() {

        binding.layoutDate.setOnClickListener {
            showDatePicker()
        }

        binding.btnReschedule.setOnClickListener {
            if ( Utilities.isNullOrEmpty(timeSlot) ) {
                Utilities.toastMessageShort(requireContext(),resources.getString(R.string.PLEASE_SELECT_APPOINTMENT_TIME))
            } else if ( appointment.appointmentStartTime == timeSlot ) {
                Utilities.toastMessageShort(requireContext(),resources.getString(R.string.PLEASE_SELECT_DIFFERENT_APPOINTMENT_TIME))
            } else if ( Utilities.isNullOrEmpty(binding.edtRescheduleReason.text.toString()) ) {
                Utilities.toastMessageShort(requireContext(),resources.getString(R.string.PLEASE_ENTER_REASON_RESCHEDULE))
            } else {
                viewModel.callRescheduleAppointmentApi(
                    appointment.iD,
                    selectedDate,
                    timeSlot, duration.toInt(),
                    appointment.appointmentMode,
                    binding.edtRescheduleReason.text.toString())
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun registerObserver() {

        viewModel.getDoctorSlots.observe(viewLifecycleOwner) {
            if (it != null) {
                morningSlotList.clear()
                afternoonSlotList.clear()
                eveningSlotList.clear()
                val doctorSessionSlots = it.doctorSessionSlots

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
                        binding.tilEdtRescheduleReason.visibility = View.VISIBLE
                        binding.btnReschedule.visibility = View.VISIBLE
                    } else {
                        binding.layoutMorningSlots.visibility = View.GONE
                    }

                    if ( afternoonSlotList.isNotEmpty() ) {
                        chooseAfternoonTimeSlotAdapter!!.updateList(afternoonSlotList)
                        binding.txtAfternoonSlotCount.text = "(${afternoonSlotList.size} ${resources.getString(R.string.SLOTS)})"
                        binding.layoutAfternoonSlots.visibility = View.VISIBLE
                        binding.layoutNoSlots.visibility = View.GONE
                        binding.tilEdtRescheduleReason.visibility = View.VISIBLE
                        binding.btnReschedule.visibility = View.VISIBLE
                    } else {
                        binding.layoutAfternoonSlots.visibility = View.GONE
                    }

                    if ( eveningSlotList.isNotEmpty() ) {
                        chooseEveningTimeSlotAdapter!!.updateList(eveningSlotList)
                        binding.txtEveningSlotCount.text = "(${eveningSlotList.size} ${resources.getString(R.string.SLOTS)})"
                        binding.layoutEveningSlots.visibility = View.VISIBLE
                        binding.layoutNoSlots.visibility = View.GONE
                        binding.tilEdtRescheduleReason.visibility = View.VISIBLE
                        binding.btnReschedule.visibility = View.VISIBLE
                    } else {
                        binding.layoutEveningSlots.visibility = View.GONE
                    }

                    if ( morningSlotList.isEmpty()
                        && afternoonSlotList.isEmpty()
                        && eveningSlotList.isEmpty() ) {
                        binding.layoutMorningSlots.visibility = View.GONE
                        binding.layoutAfternoonSlots.visibility = View.GONE
                        binding.layoutEveningSlots.visibility = View.GONE
                        binding.tilEdtRescheduleReason.visibility = View.GONE
                        binding.btnReschedule.visibility = View.GONE
                        binding.layoutNoSlots.visibility = View.VISIBLE
                    }

                } else {
                    binding.layoutMorningSlots.visibility = View.GONE
                    binding.layoutAfternoonSlots.visibility = View.GONE
                    binding.layoutEveningSlots.visibility = View.GONE
                    binding.tilEdtRescheduleReason.visibility = View.GONE
                    binding.btnReschedule.visibility = View.GONE
                    binding.layoutNoSlots.visibility = View.VISIBLE
                }
            }
        }

        viewModel.rescheduleAppointment.observe(viewLifecycleOwner) {
            if (it != null) {
                if ( it.iStatusUpdated.equals(Constants.TRUE,ignoreCase = true) ) {
                    Utilities.toastMessageShort(requireContext(),resources.getString(R.string.APPOINTMENT_RESCHEDULED_SUCCESSFULLY))
                    (activity as AppointmentDetailsActivity).routeToHomeScreen()
                }
            }
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
                        viewModel.callGetDoctorSlotsApi(selectedDate,appointment.appointmentMode,appointment.doctorID,appointment.supplierID)
                        resetAllSlots()
                    }
                })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun resetAllSlots() {
        binding.layoutMorningSlots.visibility = View.GONE
        binding.layoutAfternoonSlots.visibility = View.GONE
        binding.layoutEveningSlots.visibility = View.GONE
        binding.tilEdtRescheduleReason.visibility = View.GONE
        binding.btnReschedule.visibility = View.INVISIBLE

/*        chooseMorningTimeSlotAdapter!!.clearAdapter()
        chooseAfternoonTimeSlotAdapter!!.clearAdapter()
        chooseEveningTimeSlotAdapter!!.clearAdapter()

        binding.txtMorningSlotCount.text = "(-- Slots)"
        binding.txtAfternoonSlotCount.text = "(-- Slots)"
        binding.txtEveningSlotCount.text = "(-- Slots)"*/
    }

    override fun onTimeSlotSelection(type: String, slot: GetDoctorSlotsModel.TimeSlot) {
        timeSlotType = type
        timeSlot = slot.slot
        duration = slot.duration
        Utilities.printLogError("timeSlotType--->$timeSlotType")
        Utilities.printLogError("timeSlot--->$timeSlot")
        Utilities.printLogError("duration--->$duration")
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