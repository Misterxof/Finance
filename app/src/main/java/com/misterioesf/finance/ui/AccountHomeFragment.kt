package com.misterioesf.finance.ui

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.misterioesf.finance.Currencies
import com.misterioesf.finance.R
import com.misterioesf.finance.dao.entity.Account
import com.misterioesf.finance.viewModel.AccountHomeViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ACCOUNT = "ACCOUNT"

/**
 * A simple [Fragment] subclass.
 * Use the [AccountHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountHomeFragment constructor(val back: () -> Unit) : Fragment() {
    // TODO: Rename and change types of parameters
    private var account: Account? = null
    lateinit var accountHomeViewModel: AccountHomeViewModel
    lateinit var accountName: EditText
    lateinit var accountAmount: TextView
    lateinit var accountSpinner: Spinner
    lateinit var addAccountConfirmButton: Button
    lateinit var deleteAccountImageButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            account = it.getSerializable(ACCOUNT) as Account?
        }
    }

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

        if (account == null) {
            deleteAccountImageButton.isEnabled = false
            deleteAccountImageButton.visibility = View.GONE
        } else {
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
                    back()
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
        back()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param account Parameter 1.
         * @return A new instance of fragment AccountHomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(account: Account?, back: () -> Unit) =
            AccountHomeFragment(back).apply {
                arguments = Bundle().apply {
                    putSerializable(ACCOUNT, account)
                }
            }
    }
}