package com.vivant.telemedicine.ui.consultAndSchedule

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.vivant.telemedicine.R
import com.vivant.telemedicine.adapter.ChooseTimeSlotAdapter
import com.vivant.telemedicine.base.BaseFragment
import com.vivant.telemedicine.base.BaseViewModel
import com.vivant.telemedicine.common.*
import com.vivant.telemedicine.databinding.FragmentChooseSlotBinding
import com.vivant.telemedicine.model.GetDoctorSlotsModel.TimeSlot
import com.vivant.telemedicine.viewmodel.ConsultAndScheduleViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class ChooseSlotFragment : BaseFragment() , ChooseTimeSlotAdapter.OnTimeSlotClickListener {

    private var _binding: FragmentChooseSlotBinding? = null
    private val binding get() = _binding!!

    private val consultAndScheduleSingleton = ConsultAndScheduleSingleton.instance!!
    private var chooseMorningTimeSlotAdapter: ChooseTimeSlotAdapter? = null
    private var chooseAfternoonTimeSlotAdapter: ChooseTimeSlotAdapter? = null
    private var chooseEveningTimeSlotAdapter: ChooseTimeSlotAdapter? = null

    private val currentDate = DateHelper.currentDateAsStringyyyyMMdd
    private var selectedDate = DateHelper.currentDateAsStringyyyyMMdd
    private var timeSlot = ""
    private var duration = ""
    private val cal = Calendar.getInstance()
    private val df1 = SimpleDateFormat(DateHelper.SERVER_DATE_YYYYMMDD, Locale.ENGLISH)

    val viewModel: ConsultAndScheduleViewModel by viewModel()

    private val morningSlotList: MutableList<TimeSlot> = mutableListOf()
    private val afternoonSlotList: MutableList<TimeSlot> = mutableListOf()
    private val eveningSlotList: MutableList<TimeSlot> = mutableListOf()

    private val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
    private val currentTime = dateFormat.parse(dateFormat.format(Date()))
    private lateinit var slotTime : Date
    private var differenceInMinutes = 0

    override fun getViewModel(): BaseViewModel = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            // callback to Handle back button event
            val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    (activity as ConsultAndScheduleActivity).setStepPositionScheduleAppointment(Constants.STEP_THREE,true)
                    findNavController().navigate(R.id.action_chooseSlotFragment_to_chooseDoctorFragment)
                }
            }
            requireActivity().onBackPressedDispatcher.addCallback(this, callback)

        } catch ( e : Exception ) {
            e.printStackTrace()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentChooseSlotBinding.inflate(inflater, container, false)
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

    @SuppressLint("SetTextI18n")
    private fun initialise() {

        if ( !Utilities.isNullOrEmpty(consultAndScheduleSingleton.doctorImage) ) {
            val abc = Base64.decode(consultAndScheduleSingleton.doctorImage, Base64.DEFAULT)
            binding.imgDoctor.setImageBitmap(BitmapFactory.decodeByteArray(abc,0,abc.size) )
        } else {
            binding.imgDoctor.setImageResource(R.drawable.img_my_profile)
        }

        binding.txtDoctorName.text = "${resources.getString(R.string.DR)} ${consultAndScheduleSingleton.doctorName}"
        binding.txtDoctorSpeciality.text = consultAndScheduleSingleton.doctorSpeciality
        binding.txtDoctorGender.text = Helper.getGenderDisplayValue(consultAndScheduleSingleton.doctorGender,requireContext())

        if ( !Utilities.isNullOrEmpty(consultAndScheduleSingleton.doctorExperince) ) {
            binding.txtDoctorExperience.text = "${consultAndScheduleSingleton.doctorExperince} ${resources.getString(R.string.YRS)}"
            //binding.view1.visibility = View.VISIBLE
            binding.txtDoctorExperience.visibility = View.VISIBLE
        } else {
            //binding.view1.visibility = View.GONE
            binding.txtDoctorExperience.visibility = View.GONE
        }

        binding.txtDate.text = DateHelper.currentDateAsStringddMMMyyyyNew

        chooseMorningTimeSlotAdapter = ChooseTimeSlotAdapter(requireContext(),Constants.MORNING,this)
        binding.rvMorningSlots.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.rvMorningSlots.adapter = chooseMorningTimeSlotAdapter

        chooseAfternoonTimeSlotAdapter = ChooseTimeSlotAdapter(requireContext(),Constants.AFTERNOON,this)
        binding.rvAfternoonSlots.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.rvAfternoonSlots.adapter = chooseAfternoonTimeSlotAdapter

        chooseEveningTimeSlotAdapter = ChooseTimeSlotAdapter(requireContext(),Constants.EVENING,this)
        binding.rvEveningSlots.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.rvEveningSlots.adapter = chooseEveningTimeSlotAdapter

        resetAllSlots()
        viewModel.callGetDoctorSlotsApi(
            consultAndScheduleSingleton.doctorId,
            consultAndScheduleSingleton.supplierId,
            DateHelper.currentDateAsStringyyyyMMdd,
            consultAndScheduleSingleton.consultationMode)
    }

    private fun setClickable() {

        binding.layoutDate.setOnClickListener {
            showDatePicker()
        }

        binding.btnNext.setOnClickListener {
            if ( !Utilities.isNullOrEmpty(timeSlot) ) {
                (activity as ConsultAndScheduleActivity).setStepPositionScheduleAppointment(Constants.STEP_FIVE)
                consultAndScheduleSingleton.appointmentDate = selectedDate
                consultAndScheduleSingleton.appointmentTime = timeSlot
                consultAndScheduleSingleton.appointmentDuration = duration
                it.findNavController().navigate(R.id.action_chooseSlotFragment_describeSymptomsFragment)
            } else {
                Utilities.toastMessageShort(requireContext(),resources.getString(R.string.PLEASE_SELECT_APPOINTMENT_TIME))
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
                        binding.btnNext.visibility = View.GONE
                        binding.layoutNoSlots.visibility = View.VISIBLE
                    }

                } else {
                    binding.layoutMorningSlots.visibility = View.GONE
                    binding.layoutAfternoonSlots.visibility = View.GONE
                    binding.layoutEveningSlots.visibility = View.GONE
                    binding.btnNext.visibility = View.GONE
                    binding.layoutNoSlots.visibility = View.VISIBLE
                }
            }
        }

    }

    private fun showDatePicker() {
        try {
            cal.time = df1.parse(selectedDate)!!
            DialogHelper().showDatePickerDialog(resources.getString(R.string.SELECT_DATE),
                requireContext(),cal,Calendar.getInstance(),null,

                object : DialogHelper.DateListener {

                    override fun onDateSet(date: String, year: String, month: String, dayOfMonth: String) {
                        selectedDate = DateHelper.convertDateSourceToDestination(date, DateHelper.DISPLAY_DATE_DDMMMYYYY, DateHelper.SERVER_DATE_YYYYMMDD)
                        Utilities.printLogError("SelectedDate--->$selectedDate")
                        //val date = year + "-" + (month + 1) + "-" + dayOfMonth
                        binding.txtDate.text = DateHelper.formatDateValue(DateHelper.DATEFORMAT_DDMMMYYYY_NEW, date)
                        viewModel.callGetDoctorSlotsApi(
                            consultAndScheduleSingleton.doctorId,
                            consultAndScheduleSingleton.supplierId,
                            selectedDate,
                            consultAndScheduleSingleton.consultationMode
                        )
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
        binding.btnNext.visibility = View.GONE
        timeSlot = ""
        chooseMorningTimeSlotAdapter!!.resetList()
        chooseAfternoonTimeSlotAdapter!!.resetList()
        chooseEveningTimeSlotAdapter!!.resetList()
    }

    override fun onTimeSlotSelection(type: String, slot: TimeSlot) {
        timeSlot = slot.slot
        duration = slot.duration
        Utilities.printLogError("timeSlotType--->$type")
        Utilities.printLogError("timeSlot--->$timeSlot")
        binding.btnNext.visibility = View.VISIBLE
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