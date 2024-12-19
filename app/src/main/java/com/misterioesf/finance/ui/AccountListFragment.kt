package com.misterioesf.finance.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.misterioesf.finance.R
import com.misterioesf.finance.data.dao.entity.Account
import com.misterioesf.finance.di.AccountListAdapterFactory
import com.misterioesf.finance.domain.model.StatefulData
import com.misterioesf.finance.ui.adapter.AccountListAdapter
import com.misterioesf.finance.viewModel.AccountListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AccountListFragment : Fragment() {
    private val accountListViewModel: AccountListViewModel by viewModels()

    @Inject
    lateinit var accountListAdapterFactory: AccountListAdapterFactory
    lateinit var adapter: AccountListAdapter
    lateinit var accountListRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                accountListViewModel.allAccounts.collect {
                    renderAccountList(it)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter =
            accountListAdapterFactory.create(R.layout.account_list_item) {
                navigateToAccountFragment(it)
            }
        accountListRecyclerView = view.findViewById(R.id.account_list)
        accountListRecyclerView.adapter = adapter
        accountListRecyclerView.layoutManager = LinearLayoutManager(context)

        setHasOptionsMenu(true)
    }

    private fun renderAccountList(state: StatefulData<List<Account>>) {
        when (state) {
            is StatefulData.LoadingState -> {}
            is StatefulData.SaveState -> {}
            is StatefulData.SuccessState -> {
                adapter.updateData(state.data)
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

    private fun navigateToAccountFragment(account: Account?) {
        var action: NavDirections?

        if (account == null) {
            action = AccountListFragmentDirections.actionAllAccountsFragmentToAccountHomeFragment()
        } else {
            action = AccountListFragmentDirections.actionAllAccountsFragmentToAccountInfoFragment(
                account
            )
        }

        requireView().findNavController().navigate(action!!)
    }
}