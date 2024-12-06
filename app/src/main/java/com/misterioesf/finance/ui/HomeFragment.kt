package com.misterioesf.finance.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.misterioesf.finance.R
import com.misterioesf.finance.dao.entity.Account
import com.misterioesf.finance.di.AccountListAdapterFactory
import com.misterioesf.finance.ui.adapter.AccountListAdapter
import com.misterioesf.finance.viewModel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var diagramView: CircleDiagramView
    private lateinit var courseTextView: TextView
    private lateinit var eurCourseTextView: TextView
    private lateinit var homeAccountListRecyclerView: RecyclerView
    private lateinit var adapter: AccountListAdapter
    private lateinit var sharedPreferences: SharedPreferences

    private val homeViewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var accountListAdapterFactory: AccountListAdapterFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences("SETTINGS", Context.MODE_PRIVATE)

        startCollectingUSDCourse()
        startCollectingEURCourse()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        diagramView = view.findViewById(R.id.circleDiagram)
        courseTextView = view.findViewById(R.id.usd_course_text_view)
        eurCourseTextView = view.findViewById(R.id.eur_course_text_view)
        adapter = accountListAdapterFactory.create(
                emptyList(),
                R.layout.account_list_item_colored
            ) { navigateToAccountFragment(it) }
        homeAccountListRecyclerView = view.findViewById(R.id.home_account_list_rv)
        homeAccountListRecyclerView.adapter = adapter
        homeAccountListRecyclerView.layoutManager = LinearLayoutManager(context)

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

    private fun startCollectingUSDCourse() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.usdCourse.collect() {
                    if (it != null) {
                        sharedPreferences?.edit()?.putFloat("USD", it.rate)?.apply()
                        setTotalUSDAmount(it.rate)
                    } else {
                        val rate = sharedPreferences?.getFloat("USD", 1f)
                        rate?.let { setTotalUSDAmount(it) }
                    }
                }
            }
        }
    }

    private fun startCollectingEURCourse() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.eurCourse.collect() {
                    if (it != null) {
                        sharedPreferences?.edit()?.putFloat("EUR", it.rate)?.apply()
                        setTotalEUAmount(it.rate)
                    } else {
                        val rate = sharedPreferences?.getFloat("EUR", 1f)
                        rate?.let { setTotalEUAmount(it) }
                    }
                }
            }
        }
    }

    private fun updateUI(accountList: List<Account>) {
        adapter =
            accountListAdapterFactory.create(
                accountList,
                R.layout.account_list_item_colored
            ) { navigateToAccountFragment(it) }
        homeAccountListRecyclerView.adapter = adapter
    }

    private fun setTotalUSDAmount(rate: Float) {
        courseTextView.text = getString(R.string.course, rate)

        homeViewModel.setUsdAmount(rate)
    }

    private suspend fun setTotalEUAmount(rate: Float) {
        eurCourseTextView.text = getString(R.string.course_eur, rate)

        withContext(Dispatchers.IO) {
            homeViewModel.setEurAmount(rate)
        }

        diagramView.setTotalAmount(homeViewModel.getUsdTotalAmount(), homeViewModel.getEurTotalAmount(), homeViewModel.getBynTotalAmount())
    }

    private fun navigateToAccountFragment(account: Account?) {
        val action = HomeFragmentDirections.actionHomeFragmentToAccountHomeFragment(
            account
        )
        requireView().findNavController().navigate(action)
    }
}