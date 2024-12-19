package com.misterioesf.finance.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.misterioesf.finance.R
import com.misterioesf.finance.data.dao.entity.Account
import com.misterioesf.finance.domain.model.Currencies
import com.misterioesf.finance.viewModel.AccountHomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountHomeFragment : Fragment() {
    private var account: Account? = null
    private val accountHomeViewModel: AccountHomeViewModel by viewModels()

    lateinit var accountName: EditText
    lateinit var accountAmount: TextView
    lateinit var accountSpinner: Spinner
    lateinit var addAccountConfirmButton: Button

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

        if (account != null) {
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