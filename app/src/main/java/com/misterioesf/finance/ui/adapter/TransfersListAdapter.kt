package com.misterioesf.finance.ui.adapter

import android.graphics.Color
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.misterioesf.finance.R
import com.misterioesf.finance.dao.entity.Transfer

class TransfersListAdapter(
    var transfersList: List<Transfer>,
    val onTransferClick: (transfer: Transfer) -> Unit
) : RecyclerView.Adapter<TransfersListAdapter.TransferHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransferHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.transfer_list_item, parent, false)
        return TransferHolder(view, onTransferClick)
    }

    override fun getItemCount(): Int {
        return transfersList.size
    }

    override fun onBindViewHolder(holder: TransferHolder, position: Int) {
        val transfer = transfersList[position]
        holder.bind(transfer)
    }

    inner class TransferHolder(view: View, val onTransferClick: (transfer: Transfer) -> Unit) :
        RecyclerView.ViewHolder(view), View.OnClickListener {
        val amountTextView: TextView = itemView.findViewById(R.id.amount_text_view)
        val dateTextView: TextView = itemView.findViewById(R.id.date_text_view)
        val descriptionTextView: TextView = itemView.findViewById(R.id.description_text_view)
        private lateinit var transfer: Transfer

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(transfer: Transfer) {
            val dateString: String =
                DateFormat.format("dd.MM.yyyy", transfer.date).toString()
            this.transfer = transfer
            amountTextView.text = "${transfer.sum} ${transfer.currency}"
            descriptionTextView.text = transfer.description
            dateTextView.text = dateString

            if (transfer.isBill) amountTextView.setTextColor(Color.RED)
        }

        override fun onClick(p0: View?) {
            onTransferClick(transfer)
        }
    }
}