package com.misterioesf.finance.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.misterioesf.finance.Currencies
import com.misterioesf.finance.R
import com.misterioesf.finance.dao.entity.Account
import com.misterioesf.finance.dao.entity.Transfer
import com.misterioesf.finance.ui.adapter.TransfersListAdapter
import com.misterioesf.finance.viewModel.TransfersListViewModel
import kotlinx.coroutines.launch

private const val ACCOUNT_ID = "ACCOUNT_ID"

class TransfersListFragment: Fragment() {

    private lateinit var viewModel: TransfersListViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TransfersListAdapter
    private lateinit var accountSpinner: Spinner
    private var addNewObjectCallback: AddNewObjectCallback? = null
    private var accountsList: List<Account>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[TransfersListViewModel::class.java]

        addNewObjectCallback = context as AddNewObjectCallback

        val view = inflater.inflate(R.layout.fragment_transfers__list, container, false)
        adapter = TransfersListAdapter(emptyList()) {
            addNewObjectCallback?.onTransferSelected(
                viewModel.getAccountId(),
                viewModel.getCurrency(),
                it
            )
        }
        recyclerView = view.findViewById(R.id.transfers_recycle_view) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        accountSpinner = view.findViewById(R.id.accounts_spinner)
        accountSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                updateCurrentList()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        setHasOptionsMenu(true)

        return view
    }

    override fun onResume() {
        super.onResume()
        updateSpinnerAdapter()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.add_trasfer_menu, menu)
        if (viewModel.getAccountId() == -1) setHasOptionsMenu(false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_new_transfer_item -> {
                addNewObjectCallback?.onTransferSelected(viewModel.getAccountId(), viewModel.getCurrency(),null)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDetach() {
        super.onDetach()
        addNewObjectCallback = null
    }

    private fun updateSpinnerAdapter() {
        this.lifecycleScope.launch() {
            accountsList = viewModel.getAllAccounts()   // waiting for getting all accounts

            accountsList?.let {
                // create and update new spinner names, filled with accounts names
                val spinnerArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                    this@TransfersListFragment.requireContext(),
                    android.R.layout.simple_spinner_item,
                    it.map { item -> item.name }.toTypedArray()
                )
                accountSpinner.adapter = spinnerArrayAdapter

                if (viewModel.getAccountId() == -1) updateAccountId()
                else accountSpinner.setSelection(viewModel.getAccountId() - 1)
            }
            updateCurrentList()
        }
    }

    private fun updateCurrentList() {
        if (viewModel.getAccountId() == -1) {
            viewModel.transfers.observe(
                viewLifecycleOwner
            ) { transfers -> transfers?.let { updateUI(transfers) } }
        } else {
            updateAccountId()
            setHasOptionsMenu(true)
            viewModel.getTransfersById(viewModel.getAccountId()).observe(
                viewLifecycleOwner
            ) { transfers -> transfers?.let { updateUI(transfers) } }
        }
    }

    private fun getAccountIdByName(name: String): Int? {
        if (!accountsList.isNullOrEmpty()) {
            accountsList!!.forEach {
                if (it.name == name) {
                    viewModel.setCurrency(Currencies.valueOf(it.currency))
                    return it.id
                }
            }
        }

        return null
    }

    private fun updateUI(transfers: List<Transfer>) {
        adapter = TransfersListAdapter(transfers) {
            addNewObjectCallback?.onTransferSelected(
                it.accountId,
                viewModel.getCurrency(),
                it
            )
        }
        recyclerView.adapter = adapter
    }

    private fun updateAccountId() {
        val accId = getAccountIdByName(accountSpinner.selectedItem.toString()) ?: -1
        viewModel.setAccountId(accId)
    }

    companion object {
        fun newInstance() = TransfersListFragment()
    }
}