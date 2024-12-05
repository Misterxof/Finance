package com.misterioesf.finance.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.misterioesf.finance.ColorSetter
import com.misterioesf.finance.R
import com.misterioesf.finance.dao.entity.Account
import com.misterioesf.finance.ui.adapter.AccountListAdapter
import com.misterioesf.finance.viewModel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment private constructor(): Fragment() {

    private lateinit var diagramView: CircleDiagramView
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var courseTextView: TextView
    private lateinit var homeAccountListRecyclerView: RecyclerView
    private lateinit var adapter: AccountListAdapter
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences("SETTINGS", Context.MODE_PRIVATE)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                homeViewModel.currencyCourse.collect() {
                    if (it != null) {
                        sharedPreferences?.edit()?.putFloat("USD", it.rate)?.apply()
                        setTotalAmount(it.rate)
                    } else {
                        val rate = sharedPreferences?.getFloat("USD", 1f)
                        rate?.let { setTotalAmount(it) }
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        diagramView = view.findViewById(R.id.circleDiagram)
        courseTextView = view.findViewById(R.id.course_text_view)
        adapter =
            AccountListAdapter(emptyList(), R.layout.account_list_item_colored) {  }
        homeAccountListRecyclerView = view.findViewById(R.id.home_account_list_rv)
        homeAccountListRecyclerView.adapter = adapter
        homeAccountListRecyclerView.layoutManager = LinearLayoutManager(context)

        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            val treeMap = homeViewModel.getTreeMap()
            val accountList = mutableListOf<Account>()
            treeMap.forEach { (k, v) ->
                accountList.add(v)
            }
            updateUI(accountList)
        }
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            diagramView.setMap(homeViewModel.getAmountMap())
        }
    }

    fun updateUI(accountList: List<Account>) {
        adapter =
            AccountListAdapter(accountList, R.layout.account_list_item_colored) {  }
        homeAccountListRecyclerView.adapter = adapter
    }

    suspend fun setTotalAmount(rate: Float) {
        courseTextView.text = getString(R.string.course, rate)

        withContext(Dispatchers.IO) {
            homeViewModel.setAmount(rate)
        }

        diagramView.setTotalAmount(homeViewModel.getTotalAmount())
    }

    companion object {
        fun newInstance() = HomeFragment()
    }
}