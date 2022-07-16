package com.vivant.telemedicine.ui.appointmentDetails

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import com.vivant.telemedicine.R
import com.vivant.telemedicine.adapter.AutocompleteTextViewAdapter
import com.vivant.telemedicine.common.Utilities
import com.vivant.telemedicine.databinding.DialogOrderMedicineBinding
import com.vivant.telemedicine.model.AutocompleteTextViewModel
import java.util.*
import java.util.stream.Collectors

class OrderMedicineDialog(private val mcontext: Context,
                          private val listener: OnOrderConfirmListener,
                          private val stateList: MutableList<AutocompleteTextViewModel>) : Dialog(mcontext)  {

    private lateinit var binding: DialogOrderMedicineBinding

    internal var isSelectedMedicine = false
    private lateinit var autocompleteTextViewAdapter: AutocompleteTextViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DialogOrderMedicineBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        this.setCancelable(false)
        this.setCanceledOnTouchOutside(false)

        binding.imgClose.setOnClickListener {
            dismiss()
        }

        binding.btnLeftSide.setOnClickListener {
            dismiss()
        }

        binding.btnRightSide.setOnClickListener {
            if ( Utilities.isNullOrEmpty(binding.edtAddress.text.toString()) ) {
                Utilities.toastMessageShort(context,context.resources.getString(R.string.PLEASE_ENTER_ADDRESS))
            } else if ( Utilities.isNullOrEmpty(binding.edtCity.text.toString()) ) {
                Utilities.toastMessageShort(context,context.resources.getString(R.string.PLEASE_ENTER_CITY))
            } else if ( Utilities.isNullOrEmpty(binding.edtState.text.toString()) ) {
                Utilities.toastMessageShort(context,context.resources.getString(R.string.PLEASE_ENTER_STATE))
            } else if ( Utilities.isNullOrEmpty(binding.edtPostCode.text.toString()) ) {
                Utilities.toastMessageShort(context,context.resources.getString(R.string.PLEASE_ENTER_POST_CODE))
            } else {
                dismiss()
                listener.onOrderConfirmClickListener(binding.edtAddress.text.toString(),
                    binding.edtCity.text.toString(),
                    binding.edtState.text.toString(),
                    binding.edtPostCode.text.toString())
            }
        }

        autocompleteTextViewAdapter = AutocompleteTextViewAdapter(mcontext,ArrayList())
        binding.edtState.setAdapter(autocompleteTextViewAdapter)

        val onItemClickListener = AdapterView.OnItemClickListener { adapterView, _, i, _ ->
            isSelectedMedicine = true
            val medicineModel = adapterView.getItemAtPosition(i) as AutocompleteTextViewModel
            binding.edtState.setText(medicineModel.name)
            //drugId = medicineModel.iD
            Utilities.hideKeyboard(binding.edtState,mcontext)
        }
        binding.edtState.onItemClickListener = onItemClickListener

        binding.edtState.setOnFocusChangeListener { _: View?, hasFocus: Boolean ->
            if (hasFocus && binding.edtState.text.toString() == "") {
                filterSpinnerList(stateList, "")
            }
        }

        binding.edtState.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun afterTextChanged(editable: Editable) {
                try {
                    if (isSelectedMedicine) {
                        isSelectedMedicine = false
                    } else {
                        if (!binding.edtState.isPerformingCompletion) {
                            if (editable.toString().length >= 2) {
                                filterSpinnerList(stateList,editable.toString())
                            }
                        }
                    }
/*                    if (!editable.toString().equals("", ignoreCase = true)) {
                        binding.tilEdtState.error = null
                        binding.tilSpecialization.isErrorEnabled = false
                    }*/
/*                    if (editable.toString().length <= 1) {
                        binding.edtSpecialization.dismissDropDown()
                    }*/
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })

    }

    fun filterSpinnerList(specialities: MutableList<AutocompleteTextViewModel>, txt: String) {
        if (!txt.equals("", ignoreCase = true)) {
            var filteredList: List<AutocompleteTextViewModel> = ArrayList()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                filteredList = specialities.stream()
                    .filter { speciality: AutocompleteTextViewModel ->
                        speciality.name.toLowerCase(Locale.ROOT)
                            .contains(txt.toLowerCase(Locale.ROOT))
                    }.collect(Collectors.toList())
            }
            autocompleteTextViewAdapter.updateData(filteredList)
            if (filteredList.isNotEmpty()) {
                Utilities.hideKeyboard(binding.edtState,mcontext)
            }
            binding.edtState.showDropDown()
        } else {
            autocompleteTextViewAdapter.updateData(specialities)
            if (specialities.size > 0) {
                Utilities.hideKeyboard(binding.edtState,mcontext)
            }
            //binding.edtState.showDropDown()
        }
    }

    interface OnOrderConfirmListener {
        fun onOrderConfirmClickListener( address: String,city: String,state: String,postCode: String)
    }

}
