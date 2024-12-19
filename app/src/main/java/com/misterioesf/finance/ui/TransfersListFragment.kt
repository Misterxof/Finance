package com.misterioesf.finance.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.misterioesf.finance.R
import com.misterioesf.finance.data.dao.entity.Account
import com.misterioesf.finance.data.dao.entity.Transfer
import com.misterioesf.finance.di.TransfersListAdapterFactory
import com.misterioesf.finance.domain.model.StatefulData
import com.misterioesf.finance.domain.model.StatefulData.LoadingState
import com.misterioesf.finance.domain.model.StatefulData.SuccessState
import com.misterioesf.finance.ui.adapter.TransfersListAdapter
import com.misterioesf.finance.viewModel.TransfersListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TransfersListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TransfersListAdapter
    private lateinit var accountSpinner: Spinner
    private lateinit var menu: Menu

    private val viewModel: TransfersListViewModel by viewModels()

    private var isVisible = true
    private var accountsList: List<Account>? = null

    @Inject
    lateinit var transfersListAdapterFactory: TransfersListAdapterFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.transfersListUiState.collect {
                    renderState(it)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.allAccounts.collect {
                    updateSpinnerAdapter(it.data!!)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_transfers__list, container, false)
        adapter = transfersListAdapterFactory.create() {
            navigateToTransferFragment(it)
        }
        recyclerView = view.findViewById(R.id.transfers_recycle_view) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        accountSpinner = view.findViewById(R.id.accounts_spinner)
        accountSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                viewModel.getNewAccountById(p2)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        setHasOptionsMenu(true)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.add_trasfer_menu, menu)
        this.menu = menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_new_transfer_item -> {
                navigateToTransferFragment(null)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateSpinnerAdapter(list: List<Account>) {
        list?.let {
            // create and update new spinner names, filled with accounts names
            val spinnerArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                this@TransfersListFragment.requireContext(),
                android.R.layout.simple_spinner_item,
                it.map { item -> item.name }.toTypedArray()
            )
            accountSpinner.adapter = spinnerArrayAdapter

            accountSpinner.setSelection(
                viewModel.getAccountIndexById()
            )
        }
    }

    //  FIXME updateMenu
    private fun renderState(state: StatefulData<List<Transfer>>) {
        when (state) {
            is LoadingState -> {
                recyclerView.visibility = View.GONE
                adapter.updateData(emptyList())
                isVisible = false
               // updateMenu()
            }
            is SuccessState -> {
                isVisible = true
                //updateMenu()
                recyclerView.visibility = View.VISIBLE
                adapter.updateData(state.data)
            }
            else -> {}
        }
    }

    private fun updateMenu() {
        if (!isVisible) {
            menu.getItem(0).isVisible = false
            menu.getItem(0).isEnabled = false
        } else {
            menu.getItem(0).isVisible = true
            menu.getItem(0).isEnabled = true
        }
    }

    private fun navigateToTransferFragment(transfer: Transfer?) {
        val action = TransfersListFragmentDirections.actionAllTransfersFragmentToTransferFragment(
            transfer,
            viewModel.getAccountId(),
            viewModel.getCurrency(),
        )
        requireView().findNavController().navigate(action)
    }
}