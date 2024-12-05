package com.misterioesf.finance.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.misterioesf.finance.R
import com.misterioesf.finance.dao.entity.Account
import com.misterioesf.finance.ui.adapter.AccountListAdapter
import com.misterioesf.finance.viewModel.AccountListViewModel
import kotlinx.coroutines.launch


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AccountListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountListFragment constructor() : Fragment() {
    lateinit var adapter: AccountListAdapter
    lateinit var accountListViewModel: AccountListViewModel
    lateinit var accountListRecyclerView: RecyclerView
    private var addNewObjectCallback: AddNewObjectCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account_list, container, false)
        adapter =
            AccountListAdapter(emptyList(), R.layout.account_list_item) { addNewObjectCallback?.onAccountSelected(account = it) }
        accountListRecyclerView = view.findViewById(R.id.account_list)
        accountListRecyclerView.adapter = adapter
        accountListRecyclerView.layoutManager = LinearLayoutManager(context)

        accountListViewModel = ViewModelProvider(this)[AccountListViewModel::class.java]
        addNewObjectCallback = context as AddNewObjectCallback
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
                addNewObjectCallback?.onAccountSelected(null)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDetach() {
        super.onDetach()
        addNewObjectCallback = null
    }

    fun updateUI(accountList: List<Account>) {
        adapter =
            AccountListAdapter(accountList, R.layout.account_list_item) { addNewObjectCallback?.onAccountSelected(account = it) }
        accountListRecyclerView.adapter = adapter
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment AccountListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            AccountListFragment()
    }
}