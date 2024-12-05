package com.misterioesf.finance.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.misterioesf.finance.Currencies
import com.misterioesf.finance.R
import com.misterioesf.finance.dao.entity.Account
import com.misterioesf.finance.viewModel.AccountHomeViewModel

class AccountHomeFragment : Fragment() {
    private val args: AccountHomeFragmentArgs by navArgs()
    private var account: Account? = null

    lateinit var accountHomeViewModel: AccountHomeViewModel
    lateinit var accountName: EditText
    lateinit var accountAmount: TextView
    lateinit var accountSpinner: Spinner
    lateinit var addAccountConfirmButton: Button
    lateinit var deleteAccountImageButton: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_account_home, container, false)
        accountName = view.findViewById(R.id.account_name_edit_text)
        accountAmount = view.findViewById(R.id.account_amount_text_view)
        accountSpinner = view.findViewById(R.id.account_currency_spinner)
        addAccountConfirmButton = view.findViewById(R.id.add_new_account_button)
        deleteAccountImageButton = view.findViewById(R.id.delete_account_img_button)

        account = args.account

        if (account == null) {
            deleteAccountImageButton.isEnabled = false
            deleteAccountImageButton.visibility = View.GONE
        } else {
            accountSpinner.isEnabled = false
            accountSpinner.visibility = View.GONE
            addAccountConfirmButton.isEnabled = false
            addAccountConfirmButton.visibility = View.GONE
            account?.let {
                accountName.setText(it.name)
                accountAmount.text = it.sum.toString()
                accountSpinner.setSelection(Currencies.getCurrencyByName(it.currency))
            }
        }

        addAccountConfirmButton.setOnClickListener {
            addNewAccount()
        }

        accountHomeViewModel = ViewModelProvider(this)[AccountHomeViewModel::class.java]

        deleteAccountImageButton.setOnClickListener {
            AlertDialog.Builder(this.context)
                .setTitle(R.string.delete_account_title)
                .setMessage(R.string.delete_account_description)
                .setPositiveButton(R.string.yes) { _, _ ->
                    account?.let { accountHomeViewModel.deleteAccount(it) }
                    requireView().findNavController().navigateUp()
                }
                .setNegativeButton(R.string.no, null).show()
        }

        return view
    }

    private fun addNewAccount() {
        val newAccount = Account(
            name = accountName.text.toString(),
            sum = 0.0,
            currency = accountSpinner.selectedItem.toString()
        )

        accountHomeViewModel.addNewAccount(newAccount)
        requireView().findNavController().navigateUp()
    }
}