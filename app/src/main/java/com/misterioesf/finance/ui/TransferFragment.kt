package com.misterioesf.finance.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import androidx.lifecycle.ViewModelProvider
import com.misterioesf.finance.Currencies
import com.misterioesf.finance.R
import com.misterioesf.finance.dao.entity.Transfer
import com.misterioesf.finance.ui.dialog.DatePickerDialogFragment
import com.misterioesf.finance.viewModel.TransferViewModel
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "TRANSFER"

/**
 * A simple [Fragment] subclass.
 * Use the [TransferFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TransferFragment private constructor(val accountId: Int, val currency: Currencies, val back: () -> Unit) : Fragment() {
    private lateinit var date: Date
    private var transfer: Transfer? = null
    private lateinit var viewModel: TransferViewModel

    lateinit var datePickerFragment: DatePickerDialogFragment
    lateinit var amountEditText: EditText
    lateinit var descriptionEditText: EditText
    lateinit var pickDateButton: Button
    lateinit var confirmButton: Button
    lateinit var currencySpinner: Spinner
    lateinit var isBillCheckBox: CheckBox
    lateinit var deleteTransferImageButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            transfer = it.getSerializable(ARG_PARAM1) as Transfer?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_transfer, container, false)
        viewModel = ViewModelProvider(this)[TransferViewModel::class.java]
        amountEditText = view.findViewById(R.id.amount_edit_text)
        descriptionEditText = view.findViewById(R.id.description_edit_text)
        pickDateButton = view.findViewById(R.id.pick_date_button)
        confirmButton = view.findViewById(R.id.confirm_button)
        currencySpinner = view.findViewById(R.id.currency_spinner)
        isBillCheckBox = view.findViewById(R.id.isBill_check_box)
        deleteTransferImageButton = view.findViewById(R.id.delete_transfer_img_button)

        currencySpinner.setSelection(Currencies.getCurrencyByName(currency.name))
        currencySpinner.isEnabled = false

        if (transfer != null) {
            transfer?.let {
                amountEditText.setText(it.sum.toString())
                descriptionEditText.setText(it.description)
                isBillCheckBox.isChecked = it.isBill
                date = it.date
                pickDateButton.text = SimpleDateFormat("dd-MM-yyyy").format(it.date)
            }
        } else {
            deleteTransferImageButton.visibility = View.GONE
            deleteTransferImageButton.isEnabled = false
            date = Date()
            pickDateButton.text = SimpleDateFormat("dd-MM-yyyy").format(date)
        }

        datePickerFragment = DatePickerDialogFragment()

        pickDateButton.setOnClickListener {
            datePickerFragment.apply {
                this@TransferFragment.parentFragmentManager.setFragmentResultListener(
                    DatePickerDialogFragment.REQUEST_DATE, this
                ) { key, bundle ->
                    key.let {
                        when (it) {
                            DatePickerDialogFragment.REQUEST_DATE -> {
                                val newDate =
                                    bundle.getSerializable(DatePickerDialogFragment.ARG_DATE) as Date
                                newDate.let {
                                    pickDateButton.text =
                                        SimpleDateFormat("dd-MM-yyyy").format(newDate)
                                    date = newDate
                                }
                            }
                        }
                    }
                }
                show(this@TransferFragment.parentFragmentManager, DatePickerDialogFragment.NAME)
            }
        }

        confirmButton.setOnClickListener { addNewTransfer() }

        deleteTransferImageButton.setOnClickListener {
            AlertDialog.Builder(this.context)
                .setTitle(R.string.delete_transfer_title)
                .setMessage(R.string.delete_transfer_description)
                .setPositiveButton(R.string.yes) { _, _ ->
                    transfer?.let { viewModel.deleteTransfer(it) }
                    back()
                }
                .setNegativeButton(R.string.no, null).show()
        }

        return view
    }

    fun addNewTransfer() {
        val transfer = Transfer(
            sum = amountEditText.text.toString().toDouble(),
            description = descriptionEditText.text.toString(),
            currency = currency.name,
            isBill = isBillCheckBox.isChecked,
            date = date,
            accountId = accountId
        )

        viewModel.addNewTransfer(transfer)
        back()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param transfer Parameter 1.
         * @return A new instance of fragment TransferFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(accountId: Int, currency: Currencies, transfer: Transfer?, back: () -> Unit) =
            TransferFragment(accountId, currency, back).apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, transfer)
                }
            }
    }
}