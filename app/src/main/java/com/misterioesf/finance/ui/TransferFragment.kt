package com.misterioesf.finance.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.misterioesf.finance.data.entity.Currencies
import com.misterioesf.finance.R
import com.misterioesf.finance.dao.entity.Transfer
import com.misterioesf.finance.ui.dialog.DatePickerDialogFragment
import com.misterioesf.finance.viewModel.TransferViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "TRANSFER"

@AndroidEntryPoint
class TransferFragment : Fragment() {
    private lateinit var currency: Currencies
    private lateinit var date: Date
    private lateinit var datePickerFragment: DatePickerDialogFragment

    private val viewModel: TransferViewModel by viewModels()
    private val args: TransferFragmentArgs by navArgs()
    private var transfer: Transfer? = null
    private var accountId: Int = -1

    lateinit var amountEditText: EditText
    lateinit var descriptionEditText: EditText
    lateinit var pickDateButton: Button
    lateinit var confirmButton: Button
    lateinit var currencySpinner: Spinner
    lateinit var isBillCheckBox: CheckBox
    lateinit var deleteTransferImageButton: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_transfer, container, false)
        amountEditText = view.findViewById(R.id.amount_edit_text)
        descriptionEditText = view.findViewById(R.id.description_edit_text)
        pickDateButton = view.findViewById(R.id.pick_date_button)
        confirmButton = view.findViewById(R.id.confirm_button)
        currencySpinner = view.findViewById(R.id.currency_spinner)
        isBillCheckBox = view.findViewById(R.id.isBill_check_box)
        deleteTransferImageButton = view.findViewById(R.id.delete_transfer_img_button)

        // Get the arguments
        transfer = args.transfer
        accountId = args.accountId
        currency = args.currency

        currencySpinner.setSelection(Currencies.getCurrencyByName(currency.name))
        currencySpinner.isEnabled = false

        if (transfer != null) {
            confirmButton.visibility = View.GONE
            confirmButton.isEnabled = false
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
                    this.findNavController().navigateUp()
                }
                .setNegativeButton(R.string.no, null).show()
        }

        return view
    }

    private fun addNewTransfer() {
        val transfer = Transfer(
            sum = amountEditText.text.toString().toDouble(),
            description = descriptionEditText.text.toString(),
            currency = currency.name,
            isBill = isBillCheckBox.isChecked,
            date = date,
            accountId = accountId
        )

        viewModel.addNewTransfer(transfer)
        this.findNavController().navigateUp()
    }
}