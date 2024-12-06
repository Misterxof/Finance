package com.misterioesf.finance.ui.dialog

import androidx.fragment.app.DialogFragment
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class DatePickerDialogFragment: DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val  calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(requireContext(), this, year, month, day)
    }
    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
        val resultDate = GregorianCalendar(year, month, day).time
        val result = Bundle()

        result.putSerializable(ARG_DATE, resultDate)

        parentFragmentManager.setFragmentResult(REQUEST_DATE, result)
    }

    companion object {
        const val NAME = "DatePickerDialog"
        const val REQUEST_DATE = "REQUEST_DATE"
        const val ARG_DATE = "DATE"
    }
}