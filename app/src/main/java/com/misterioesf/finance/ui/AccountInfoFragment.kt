package com.misterioesf.finance.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.misterioesf.finance.R
import com.misterioesf.finance.dao.entity.Transfer
import com.misterioesf.finance.data.entity.Segment
import com.misterioesf.finance.di.TransfersListAdapterFactory
import com.misterioesf.finance.ui.adapter.TransfersListAdapter
import com.misterioesf.finance.viewModel.AccountInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AccountInfoFragment : Fragment() {
    private val accountInfoViewModel: AccountInfoViewModel by viewModels()

    private lateinit var transfersListAdapter: TransfersListAdapter
    private lateinit var transfersListRecyclerView: RecyclerView
    private lateinit var circleDiagramView: AccountInfoCircleDiagram
    private lateinit var colorImageView: ImageView
    private lateinit var accountInfoSpinner: Spinner
    lateinit var deleteAccountImageButton: ImageButton
    private val args: AccountInfoFragmentArgs by navArgs()

    @Inject
    lateinit var transfersListAdapterFactory: TransfersListAdapterFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account_info, container, false)
        transfersListAdapter = transfersListAdapterFactory.create(emptyList()) {}
        circleDiagramView = view.findViewById(R.id.circle_Diagram)
        transfersListRecyclerView = view.findViewById(R.id.info_account_list_rv)
        colorImageView = view.findViewById(R.id.account_info_color_image_view)
        accountInfoSpinner = view.findViewById(R.id.account_info_spinner)
        deleteAccountImageButton = view.findViewById(R.id.delete_account_img_button)

        accountInfoSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                updateUI(accountInfoViewModel.getTransfersList(p2))
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}

        }

        transfersListRecyclerView.layoutManager = LinearLayoutManager(context)
        transfersListRecyclerView.adapter = transfersListAdapter

        accountInfoViewModel.setAccount(args.account!!)
        accountInfoViewModel.setAccountId(args.account!!.id)

        deleteAccountImageButton.setOnClickListener {
            AlertDialog.Builder(this.context)
                .setTitle(R.string.delete_account_title)
                .setMessage(R.string.delete_account_description)
                .setPositiveButton(R.string.yes) { _, _ ->
                    accountInfoViewModel.getAccount()
                        ?.let { accountInfoViewModel.deleteAccount(it) }
                    requireView().findNavController().navigateUp()
                }
                .setNegativeButton(R.string.no, null).show()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        accountInfoViewModel.getTransfersById(accountInfoViewModel.getAccountId()).observe(
            viewLifecycleOwner
        ) { transfers ->
            transfers?.let {
                accountInfoViewModel.transfersList = transfers
                updateUI(transfers)
            }
        }
    }

    private fun updateUI(transfers: List<Transfer>) {
        transfersListAdapter = transfersListAdapterFactory.create(transfers) {}
        transfersListRecyclerView.adapter = transfersListAdapter

        circleDiagramView.setMap(listToTree(transfers))
    }

    private fun listToTree(transfers: List<Transfer>): TreeMap<String, Segment> {
        val tree = TreeMap<String, Segment>()

        transfers?.let {
            it.forEach { transfer ->
                val name =
                    if (transfer.description.isEmpty()) transfer.sum.toString() else transfer.description
                tree[name] = transfer
            }
        }

        return tree
    }
}