package com.misterioesf.finance.ui

import android.os.Bundle
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
import com.misterioesf.finance.data.dao.entity.Account
import com.misterioesf.finance.di.AccountListAdapterFactory
import com.misterioesf.finance.domain.model.Segment
import com.misterioesf.finance.domain.model.StatefulData
import com.misterioesf.finance.ui.adapter.AccountListAdapter
import com.misterioesf.finance.viewModel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var diagramView: HomeCircleDiagram
    private lateinit var courseTextView: TextView
    private lateinit var eurCourseTextView: TextView
    private lateinit var homeAccountListRecyclerView: RecyclerView
    private lateinit var adapter: AccountListAdapter

    private val homeViewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var accountListAdapterFactory: AccountListAdapterFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.allAccounts.collect {
                    renderAccountList(it)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.allCourses.collect {
                    renderCourses(it)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.totalAmounts.collect {
                    renderTotalAmounts(it)
                }
            }
        }
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

    private fun renderCourses(state: StatefulData<Pair<Float, Float>>) {
        val usdText = getString(R.string.course, state.data!!.first)
        val eurText = getString(R.string.course_eur, state.data!!.second)

        when (state) {
            is StatefulData.LoadingState -> {
                setCoursesTextView(usdText, eurText)
            }
            is StatefulData.SaveState -> {
                setCoursesTextView(usdText, eurText)
            }
            is StatefulData.SuccessState -> {
                setCoursesTextView(usdText, eurText)
            }
        }
    }

    private fun renderTotalAmounts(state: StatefulData<HashMap<String, Any>>) {
        when (state) {
            is StatefulData.LoadingState -> {
                diagramView.setTotalAmount(1.0, 1.0, 1.0)
                diagramView.setMap(TreeMap<String, Segment>())
            }
            is StatefulData.SaveState -> {}
            is StatefulData.SuccessState -> {
                val treeMap = state.data?.get("AccountTreeMap")
                val totalList = state.data?.get("Totals") as List<Float>
                diagramView.setTotalAmount(
                    totalList[0].toDouble(),
                    totalList[1].toDouble(),
                    totalList[2].toDouble()
                )
                diagramView.setMap(treeMap as TreeMap<String, Segment>)
            }
        }
    }

    private fun setCoursesTextView(usdText: String, eurText: String) {
        courseTextView.text = usdText
        eurCourseTextView.text = eurText
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        diagramView = view.findViewById(R.id.circleDiagram)
        courseTextView = view.findViewById(R.id.usd_course_text_view)
        eurCourseTextView = view.findViewById(R.id.eur_course_text_view)
        adapter = accountListAdapterFactory.create(
            R.layout.account_list_item_colored
        ) { navigateToAccountFragment(it) }
        homeAccountListRecyclerView = view.findViewById(R.id.home_account_list_rv)
        homeAccountListRecyclerView.adapter = adapter
        homeAccountListRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun navigateToAccountFragment(account: Account?) {
        val action = HomeFragmentDirections.actionHomeFragmentToAccountInfoFragment(
            account
        )
        requireView().findNavController().navigate(action)
    }
}