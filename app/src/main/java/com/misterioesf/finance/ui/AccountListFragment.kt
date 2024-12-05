package com.misterioesf.finance.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.misterioesf.finance.R
import com.misterioesf.finance.dao.entity.Account
import com.misterioesf.finance.ui.adapter.AccountListAdapter
import com.misterioesf.finance.viewModel.AccountListViewModel
import kotlinx.coroutines.launch

class AccountListFragment : Fragment() {
    lateinit var adapter: AccountListAdapter
    lateinit var accountListViewModel: AccountListViewModel
    lateinit var accountListRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account_list, container, false)
        adapter =
            AccountListAdapter(emptyList(), R.layout.account_list_item) {
                navigateToAccountFragment(
                    it
                )
            }
        accountListRecyclerView = view.findViewById(R.id.account_list)
        accountListRecyclerView.adapter = adapter
        accountListRecyclerView.layoutManager = LinearLayoutManager(context)

        accountListViewModel = ViewModelProvider(this)[AccountListViewModel::class.java]

        setHasOptionsMenu(true)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        accountListViewModel.getAccountList().observe(viewLifecycleOwner) { accounts ->
            accounts?.let {
                this@AccountListFragment.lifecycleScope.launch {
                    accounts.forEach {
                        it.sum = accountListViewModel.setAccountAmount(it.id)
                    }

                    updateUI(accounts)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.add_trasfer_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_new_transfer_item -> {
                navigateToAccountFragment(null)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun updateUI(accountList: List<Account>) {
        adapter =
            AccountListAdapter(accountList, R.layout.account_list_item) {
                navigateToAccountFragment(
                    it
                )
            }
        accountListRecyclerView.adapter = adapter
    }

    private fun navigateToAccountFragment(account: Account?) {
        val action = AccountListFragmentDirections.actionAllAccountsFragmentToAccountHomeFragment(
            account
        )
        requireView().findNavController().navigate(action)
    }
}